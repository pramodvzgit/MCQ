-- SQL Query to Verify Sample Questions in Database
-- Run this after seeding to verify the data

-- 1. Check Subjects
SELECT '--- SUBJECTS ---' as section;
SELECT id, name, description FROM subjects;

-- 2. Check Categories
SELECT '--- CATEGORIES ---' as section;
SELECT c.id, s.name as subject, c.name as category
FROM categories c
JOIN subjects s ON c.subject_id = s.id;

-- 3. Check Exams
SELECT '--- EXAMS ---' as section;
SELECT e.id, e.title, s.name as subject, e.duration_minutes, e.total_questions, e.passing_percentage
FROM exams e
JOIN subjects s ON e.subject_id = s.id;

-- 4. Check Questions Count by Category
SELECT '--- QUESTIONS BY CATEGORY ---' as section;
SELECT c.name as category, COUNT(q.id) as question_count
FROM questions q
JOIN categories c ON q.category_id = c.id
GROUP BY c.name;

-- 5. Sample Questions from Each Category
SELECT '--- SAMPLE QUESTIONS ---' as section;

-- Algebra Questions
SELECT 'ALGEBRA QUESTIONS:' as category;
SELECT q.id, q.question_text, q.difficulty, q.marks
FROM questions q
JOIN categories c ON q.category_id = c.id
WHERE c.name = 'Algebra'
LIMIT 3;

-- Calculus Questions
SELECT 'CALCULUS QUESTIONS:' as category;
SELECT q.id, q.question_text, q.difficulty, q.marks
FROM questions q
JOIN categories c ON q.category_id = c.id
WHERE c.name = 'Calculus'
LIMIT 3;

-- Physics Questions
SELECT 'PHYSICS QUESTIONS:' as category;
SELECT q.id, q.question_text, q.difficulty, q.marks
FROM questions q
JOIN categories c ON q.category_id = c.id
WHERE c.name = 'Physics'
LIMIT 3;

-- 6. Check Options for a Sample Question
SELECT '--- SAMPLE OPTIONS ---' as section;
SELECT o.option_order, o.option_text, o.is_correct
FROM options o
WHERE o.question_id = (
  SELECT id FROM questions LIMIT 1
)
ORDER BY o.option_order;

-- 7. Check Exam-Question Mapping
SELECT '--- EXAM QUESTIONS MAPPING ---' as section;
SELECT e.title as exam, eq.question_order, q.question_text
FROM exam_questions eq
JOIN exams e ON eq.exam_id = e.id
JOIN questions q ON eq.question_id = q.id
WHERE e.title = 'Algebra Basics'
ORDER BY eq.question_order;

-- 8. Check Test User
SELECT '--- TEST USER ---' as section;
SELECT id, name, email FROM users WHERE email = 'test@example.com';

-- 9. Summary Statistics
SELECT '--- SUMMARY ---' as section;
SELECT 'Total Subjects' as metric, COUNT(*) as count FROM subjects
UNION ALL
SELECT 'Total Categories', COUNT(*) FROM categories
UNION ALL
SELECT 'Total Exams', COUNT(*) FROM exams
UNION ALL
SELECT 'Total Questions', COUNT(*) FROM questions
UNION ALL
SELECT 'Total Options', COUNT(*) FROM options
UNION ALL
SELECT 'Total Users', COUNT(*) FROM users;
