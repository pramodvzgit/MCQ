import { query } from '../config/database';
import { Exam, ExamDto, Question, QuestionDto, Option, ExamQuestion, ExamAttempt, AttemptDto } from '../models/types';

export class ExamRepository {
  async getAllExams(subjectId?: string): Promise<ExamDto[]> {
    let sql = `
      SELECT e.id, e.title, e.description, e.duration_minutes, e.total_questions, e.passing_percentage,
             s.id as subject_id, s.name as subject_name
      FROM exams e
      JOIN subjects s ON e.subject_id = s.id
      WHERE e.is_active = true
    `;
    const params: any[] = [];

    if (subjectId) {
      sql += ' AND e.subject_id = $1';
      params.push(subjectId);
    }

    sql += ' ORDER BY e.created_at DESC';

    const result = await query(sql, params);
    return result.rows.map((row: any) => ({
      id: row.id,
      title: row.title,
      description: row.description,
      subject: {
        id: row.subject_id,
        name: row.subject_name,
      },
      duration_minutes: row.duration_minutes,
      total_questions: row.total_questions,
      passing_percentage: parseFloat(row.passing_percentage),
    }));
  }

  async getExamById(examId: string): Promise<ExamDto | null> {
    const sql = `
      SELECT e.id, e.title, e.description, e.duration_minutes, e.total_questions, e.passing_percentage,
             s.id as subject_id, s.name as subject_name
      FROM exams e
      JOIN subjects s ON e.subject_id = s.id
      WHERE e.id = $1 AND e.is_active = true
    `;

    const result = await query(sql, [examId]);
    if (result.rows.length === 0) return null;

    const row = result.rows[0];
    return {
      id: row.id,
      title: row.title,
      description: row.description,
      subject: {
        id: row.subject_id,
        name: row.subject_name,
      },
      duration_minutes: row.duration_minutes,
      total_questions: row.total_questions,
      passing_percentage: parseFloat(row.passing_percentage),
    };
  }

  async getExamQuestions(examId: string, includeCorrectAnswers: boolean = false): Promise<QuestionDto[]> {
    const selectFields = includeCorrectAnswers
      ? 'o.id, o.option_text, o.option_order, o.is_correct'
      : 'o.id, o.option_text, o.option_order';

    const sql = `
      SELECT q.id, q.question_text, q.explanation, q.difficulty, q.marks, q.negative_marks,
             ${selectFields}
      FROM exam_questions eq
      JOIN questions q ON eq.question_id = q.id
      LEFT JOIN options o ON q.id = o.question_id
      WHERE eq.exam_id = $1 AND q.is_active = true
      ORDER BY eq.question_order, o.option_order
    `;

    const result = await query(sql, [examId]);

    // Group options by question
    const questionsMap = new Map<string, QuestionDto>();

    for (const row of result.rows) {
      if (!questionsMap.has(row.id)) {
        questionsMap.set(row.id, {
          id: row.id,
          question_text: row.question_text,
          explanation: row.explanation,
          difficulty: row.difficulty,
          marks: parseFloat(row.marks),
          negative_marks: parseFloat(row.negative_marks),
          options: [],
        });
      }

      if (row.id) {
        const question = questionsMap.get(row.id)!;
        question.options.push({
          id: row.id,
          option_text: row.option_text,
          option_order: row.option_order,
          is_correct: includeCorrectAnswers ? row.is_correct : undefined,
        });
      }
    }

    return Array.from(questionsMap.values());
  }

  async createAttempt(userId: string, examId: string): Promise<string> {
    const sql = `
      INSERT INTO exam_attempts (user_id, exam_id, status)
      VALUES ($1, $2, 'in_progress')
      RETURNING id
    `;

    const result = await query(sql, [userId, examId]);
    return result.rows[0].id;
  }

  async getAttemptById(attemptId: string): Promise<ExamAttempt | null> {
    const sql = `
      SELECT * FROM exam_attempts
      WHERE id = $1
    `;

    const result = await query(sql, [attemptId]);
    if (result.rows.length === 0) return null;

    const row = result.rows[0];
    return {
      id: row.id,
      user_id: row.user_id,
      exam_id: row.exam_id,
      started_at: row.started_at,
      submitted_at: row.submitted_at,
      score: row.score ? parseFloat(row.score) : null,
      percentage: row.percentage ? parseFloat(row.percentage) : null,
      correct_answers: row.correct_answers,
      incorrect_answers: row.incorrect_answers,
      unanswered_questions: row.unanswered_questions,
      status: row.status,
    };
  }

  async submitAttempt(
    attemptId: string,
    answers: { question_id: string; selected_option_id: string }[]
  ): Promise<void> {
    const client = await query('BEGIN');

    try {
      // Get the attempt to calculate score
      const attemptResult = await query(
        'SELECT * FROM exam_attempts WHERE id = $1',
        [attemptId]
      );

      if (attemptResult.rows.length === 0) {
        throw new Error('Attempt not found');
      }

      const attempt = attemptResult.rows[0];
      const examId = attempt.exam_id;

      // Get questions and correct answers for this exam
      const questionsResult = await query(
        `
        SELECT q.id, q.marks, q.negative_marks, o.id as correct_option_id
        FROM exam_questions eq
        JOIN questions q ON eq.question_id = q.id
        LEFT JOIN options o ON q.id = o.question_id AND o.is_correct = true
        WHERE eq.exam_id = $1
        `,
        [examId]
      );

      const correctAnswers = new Map<string, { marks: number; negative_marks: number; correct_option_id: string }>();
      for (const row of questionsResult.rows) {
        correctAnswers.set(row.id, {
          marks: parseFloat(row.marks),
          negative_marks: parseFloat(row.negative_marks),
          correct_option_id: row.correct_option_id,
        });
      }

      let totalScore = 0;
      let correctCount = 0;
      let incorrectCount = 0;
      let unansweredCount = 0;

      // Process each answer
      for (const answer of answers) {
        const questionData = correctAnswers.get(answer.question_id);
        if (!questionData) continue;

        const isCorrect = answer.selected_option_id === questionData.correct_option_id;
        let marksAwarded = 0;

        if (isCorrect) {
          marksAwarded = questionData.marks;
          correctCount++;
        } else {
          marksAwarded = -questionData.negative_marks;
          incorrectCount++;
        }

        totalScore += marksAwarded;

        // Insert or update attempt answer
        await query(
          `
          INSERT INTO attempt_answers (attempt_id, question_id, selected_option_id, is_correct, marks_awarded)
          VALUES ($1, $2, $3, $4, $5)
          ON CONFLICT (attempt_id, question_id) 
          DO UPDATE SET 
            selected_option_id = EXCLUDED.selected_option_id,
            is_correct = EXCLUDED.is_correct,
            marks_awarded = EXCLUDED.marks_awarded,
            answered_at = CURRENT_TIMESTAMP
          `,
          [attemptId, answer.question_id, answer.selected_option_id, isCorrect, marksAwarded]
        );
      }

      // Count unanswered
      unansweredCount = correctAnswers.size - answers.length;

      // Calculate percentage
      const totalPossibleMarks = Array.from(correctAnswers.values()).reduce((sum, q) => sum + q.marks, 0);
      const percentage = totalPossibleMarks > 0 ? (totalScore / totalPossibleMarks) * 100 : 0;

      // Update attempt
      await query(
        `
        UPDATE exam_attempts
        SET submitted_at = CURRENT_TIMESTAMP,
            score = $1,
            percentage = $2,
            correct_answers = $3,
            incorrect_answers = $4,
            unanswered_questions = $5,
            status = 'submitted'
        WHERE id = $6
        `,
        [totalScore, percentage, correctCount, incorrectCount, unansweredCount, attemptId]
      );

      await query('COMMIT');
    } catch (error) {
      await query('ROLLBACK');
      throw error;
    }
  }

  async getUserAttempts(userId: string, examId?: string): Promise<AttemptDto[]> {
    let sql = `
      SELECT ea.id, ea.started_at, ea.submitted_at, ea.score, ea.percentage,
             ea.correct_answers, ea.incorrect_answers, ea.unanswered_questions, ea.status,
             e.id as exam_id, e.title, e.description, e.duration_minutes, e.total_questions, e.passing_percentage,
             s.id as subject_id, s.name as subject_name
      FROM exam_attempts ea
      JOIN exams e ON ea.exam_id = e.id
      JOIN subjects s ON e.subject_id = s.id
      WHERE ea.user_id = $1
    `;
    const params: any[] = [userId];

    if (examId) {
      sql += ' AND ea.exam_id = $2';
      params.push(examId);
    }

    sql += ' ORDER BY ea.started_at DESC';

    const result = await query(sql, params);

    return result.rows.map((row: any) => ({
      id: row.id,
      exam: {
        id: row.exam_id,
        title: row.title,
        description: row.description,
        subject: {
          id: row.subject_id,
          name: row.subject_name,
        },
        duration_minutes: row.duration_minutes,
        total_questions: row.total_questions,
        passing_percentage: parseFloat(row.passing_percentage),
      },
      started_at: row.started_at,
      submitted_at: row.submitted_at,
      score: row.score ? parseFloat(row.score) : null,
      percentage: row.percentage ? parseFloat(row.percentage) : null,
      correct_answers: row.correct_answers,
      incorrect_answers: row.incorrect_answers,
      unanswered_questions: row.unanswered_questions,
      status: row.status,
    }));
  }

  async getAttemptReview(attemptId: string): Promise<any[]> {
    const sql = `
      SELECT q.id, q.question_text, q.explanation, q.difficulty, q.marks, q.negative_marks,
             o.id as option_id, o.option_text, o.option_order, o.is_correct,
             aa.selected_option_id, aa.is_correct as user_is_correct, aa.marks_awarded
      FROM attempt_answers aa
      JOIN questions q ON aa.question_id = q.id
      LEFT JOIN options o ON q.id = o.question_id
      WHERE aa.attempt_id = $1
      ORDER BY q.id, o.option_order
    `;

    const result = await query(sql, [attemptId]);

    // Group by question
    const questionsMap = new Map<string, any>();

    for (const row of result.rows) {
      if (!questionsMap.has(row.id)) {
        questionsMap.set(row.id, {
          id: row.id,
          question_text: row.question_text,
          explanation: row.explanation,
          difficulty: row.difficulty,
          marks: parseFloat(row.marks),
          negative_marks: parseFloat(row.negative_marks),
          options: [],
          user_answer: {
            option_id: row.selected_option_id,
            is_correct: row.user_is_correct,
            marks_awarded: row.marks_awarded ? parseFloat(row.marks_awarded) : null,
          },
        });
      }

      const question = questionsMap.get(row.id)!;
      question.options.push({
        id: row.option_id,
        option_text: row.option_text,
        option_order: row.option_order,
        is_correct: row.is_correct,
      });
    }

    return Array.from(questionsMap.values());
  }
}
