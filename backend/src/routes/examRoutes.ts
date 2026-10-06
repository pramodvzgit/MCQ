import { Router } from 'express';
import { ExamController } from '../controllers/examController';
import { authenticateToken } from '../middleware/auth';

const router = Router();
const examController = new ExamController();

// Public routes
router.get('/exams', examController.getExams);
router.get('/exams/:id', examController.getExamById);

// Protected routes
router.post('/exams/:id/start', authenticateToken, examController.startExam);
router.post('/attempts/:id/submit', authenticateToken, examController.submitExam);
router.get('/attempts/:id/review', authenticateToken, examController.getAttemptReview);
router.get('/attempts', authenticateToken, examController.getUserAttempts);

export default router;
