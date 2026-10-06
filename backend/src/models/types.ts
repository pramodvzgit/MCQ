export interface User {
  id: string;
  name: string;
  email: string;
  password_hash: string;
  created_at: Date;
  updated_at: Date;
}

export interface Subject {
  id: string;
  name: string;
  description: string | null;
  is_active: boolean;
  created_at: Date;
  updated_at: Date;
}

export interface Category {
  id: string;
  subject_id: string;
  name: string;
  description: string | null;
  is_active: boolean;
  created_at: Date;
  updated_at: Date;
}

export interface Exam {
  id: string;
  title: string;
  description: string | null;
  subject_id: string;
  duration_minutes: number;
  total_questions: number;
  passing_percentage: number;
  is_active: boolean;
  created_at: Date;
  updated_at: Date;
}

export interface Question {
  id: string;
  category_id: string;
  question_text: string;
  explanation: string | null;
  difficulty: string;
  marks: number;
  negative_marks: number;
  is_active: boolean;
  created_at: Date;
  updated_at: Date;
}

export interface Option {
  id: string;
  question_id: string;
  option_text: string;
  option_order: number;
  is_correct: boolean;
  created_at: Date;
  updated_at: Date;
}

export interface ExamQuestion {
  id: string;
  exam_id: string;
  question_id: string;
  question_order: number;
  marks: number;
}

export interface ExamAttempt {
  id: string;
  user_id: string;
  exam_id: string;
  started_at: Date;
  submitted_at: Date | null;
  score: number | null;
  percentage: number | null;
  correct_answers: number;
  incorrect_answers: number;
  unanswered_questions: number;
  status: 'in_progress' | 'submitted' | 'timed_out';
}

export interface AttemptAnswer {
  id: string;
  attempt_id: string;
  question_id: string;
  selected_option_id: string | null;
  is_correct: boolean | null;
  marks_awarded: number | null;
  answered_at: Date;
}

// DTOs for API responses
export interface ExamDto {
  id: string;
  title: string;
  description: string | null;
  subject: {
    id: string;
    name: string;
  };
  duration_minutes: number;
  total_questions: number;
  passing_percentage: number;
}

export interface QuestionDto {
  id: string;
  question_text: string;
  explanation: string | null;
  difficulty: string;
  marks: number;
  negative_marks: number;
  options: OptionDto[];
}

export interface OptionDto {
  id: string;
  option_text: string;
  option_order: number;
  is_correct?: boolean; // Only included after submission
}

export interface QuestionWithAnswerDto extends QuestionDto {
  user_answer?: {
    option_id: string | null;
    is_correct: boolean | null;
    marks_awarded: number | null;
  };
}

export interface AttemptDto {
  id: string;
  exam: ExamDto;
  started_at: Date;
  submitted_at: Date | null;
  score: number | null;
  percentage: number | null;
  correct_answers: number;
  incorrect_answers: number;
  unanswered_questions: number;
  status: string;
}

export interface StartExamResponse {
  attempt_id: string;
  exam: ExamDto;
  questions: QuestionDto[];
  duration_minutes: number;
}

export interface SubmitExamRequest {
  answers: {
    question_id: string;
    selected_option_id: string;
  }[];
}

export interface SubmitExamResponse {
  attempt: AttemptDto;
  questions: QuestionWithAnswerDto[];
  passed: boolean;
}
