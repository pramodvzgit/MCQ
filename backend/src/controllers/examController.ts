import { Request, Response } from 'express';
import { ExamRepository } from '../repositories/examRepository';
import { AuthRequest } from '../middleware/auth';

export class ExamController {
  private examRepository: ExamRepository;

  constructor() {
    this.examRepository = new ExamRepository();
  }

  getExams = async (req: Request, res: Response) => {
    try {
      const { subject_id } = req.query;
      const exams = await this.examRepository.getAllExams(subject_id as string);
      res.json(exams);
    } catch (error) {
      console.error('Error fetching exams:', error);
      res.status(500).json({ error: 'Failed to fetch exams' });
    }
  };

  getExamById = async (req: Request, res: Response) => {
    try {
      const { id } = req.params;
      const exam = await this.examRepository.getExamById(id);

      if (!exam) {
        return res.status(404).json({ error: 'Exam not found' });
      }

      res.json(exam);
    } catch (error) {
      console.error('Error fetching exam:', error);
      res.status(500).json({ error: 'Failed to fetch exam' });
    }
  };

  startExam = async (req: AuthRequest, res: Response) => {
    try {
      const { id } = req.params;
      const userId = req.userId!;

      // Verify exam exists
      const exam = await this.examRepository.getExamById(id);
      if (!exam) {
        return res.status(404).json({ error: 'Exam not found' });
      }

      // Create attempt
      const attemptId = await this.examRepository.createAttempt(userId, id);

      // Get questions (without correct answers)
      const questions = await this.examRepository.getExamQuestions(id, false);

      res.json({
        attempt_id: attemptId,
        exam,
        questions,
        duration_minutes: exam.duration_minutes,
      });
    } catch (error) {
      console.error('Error starting exam:', error);
      res.status(500).json({ error: 'Failed to start exam' });
    }
  };

  submitExam = async (req: AuthRequest, res: Response) => {
    try {
      const { id } = req.params;
      const { answers } = req.body;

      if (!Array.isArray(answers)) {
        return res.status(400).json({ error: 'Invalid answers format' });
      }

      // Submit attempt
      await this.examRepository.submitAttempt(id, answers);

      // Get attempt details
      const attempt = await this.examRepository.getAttemptById(id);
      if (!attempt) {
        return res.status(404).json({ error: 'Attempt not found' });
      }

      // Get exam details
      const exam = await this.examRepository.getExamById(attempt.exam_id);
      if (!exam) {
        return res.status(404).json({ error: 'Exam not found' });
      }

      // Get questions with correct answers for review
      const questions = await this.examRepository.getExamQuestions(attempt.exam_id, true);

      // Get user's answers
      const attemptReview = await this.examRepository.getAttemptReview(id);

      // Merge questions with user answers
      const questionsWithAnswers = questions.map((q) => {
        const review = attemptReview.find((r) => r.id === q.id);
        return {
          ...q,
          user_answer: review?.user_answer,
        };
      });

      const passed = attempt.percentage !== null && attempt.percentage >= exam.passing_percentage;

      res.json({
        attempt: {
          id: attempt.id,
          exam,
          started_at: attempt.started_at,
          submitted_at: attempt.submitted_at,
          score: attempt.score,
          percentage: attempt.percentage,
          correct_answers: attempt.correct_answers,
          incorrect_answers: attempt.incorrect_answers,
          unanswered_questions: attempt.unanswered_questions,
          status: attempt.status,
        },
        questions: questionsWithAnswers,
        passed,
      });
    } catch (error) {
      console.error('Error submitting exam:', error);
      res.status(500).json({ error: 'Failed to submit exam' });
    }
  };

  getAttemptReview = async (req: AuthRequest, res: Response) => {
    try {
      const { id } = req.params;

      const attempt = await this.examRepository.getAttemptById(id);
      if (!attempt) {
        return res.status(404).json({ error: 'Attempt not found' });
      }

      const exam = await this.examRepository.getExamById(attempt.exam_id);
      if (!exam) {
        return res.status(404).json({ error: 'Exam not found' });
      }

      const questions = await this.examRepository.getAttemptReview(id);

      res.json({
        attempt: {
          id: attempt.id,
          exam,
          started_at: attempt.started_at,
          submitted_at: attempt.submitted_at,
          score: attempt.score,
          percentage: attempt.percentage,
          correct_answers: attempt.correct_answers,
          incorrect_answers: attempt.incorrect_answers,
          unanswered_questions: attempt.unanswered_questions,
          status: attempt.status,
        },
        questions,
      });
    } catch (error) {
      console.error('Error fetching review:', error);
      res.status(500).json({ error: 'Failed to fetch review' });
    }
  };

  getUserAttempts = async (req: AuthRequest, res: Response) => {
    try {
      const userId = req.userId!;
      const { exam_id } = req.query;

      const attempts = await this.examRepository.getUserAttempts(userId, exam_id as string);
      res.json(attempts);
    } catch (error) {
      console.error('Error fetching attempts:', error);
      res.status(500).json({ error: 'Failed to fetch attempts' });
    }
  };
}
