import { query } from '../config/database';
import { v4 as uuidv4 } from 'uuid';
import bcrypt from 'bcrypt';

async function seed() {
  console.log('Starting database seeding...');

  try {
    // Create test user
    const hashedPassword = await bcrypt.hash('password123', 10);
    const userResult = await query(
      `INSERT INTO users (name, email, password_hash) 
       VALUES ($1, $2, $3) 
       ON CONFLICT (email) DO NOTHING 
       RETURNING id`,
      ['Test User', 'test@example.com', hashedPassword]
    );
    const userId = userResult.rows[0]?.id;

    // Create subjects
    const mathSubject = await query(
      `INSERT INTO subjects (name, description) 
       VALUES ($1, $2) 
       ON CONFLICT DO NOTHING 
       RETURNING id`,
      ['Mathematics', 'Study of numbers, quantities, shapes, and patterns']
    );
    const mathSubjectId = mathSubject.rows[0]?.id || '00000000-0000-0000-0000-000000000001';

    const scienceSubject = await query(
      `INSERT INTO subjects (name, description) 
       VALUES ($1, $2) 
       ON CONFLICT DO NOTHING 
       RETURNING id`,
      ['Science', 'Study of the natural world through observation and experiment']
    );
    const scienceSubjectId = scienceSubject.rows[0]?.id || '00000000-0000-0000-0000-000000000002';

    // Create categories
    const algebraCategory = await query(
      `INSERT INTO categories (subject_id, name, description) 
       VALUES ($1, $2, $3) 
       ON CONFLICT DO NOTHING 
       RETURNING id`,
      [mathSubjectId, 'Algebra', 'Mathematical symbols and rules for manipulating them']
    );
    const algebraCategoryId = algebraCategory.rows[0]?.id;

    const calculusCategory = await query(
      `INSERT INTO categories (subject_id, name, description) 
       VALUES ($1, $2, $3) 
       ON CONFLICT DO NOTHING 
       RETURNING id`,
      [mathSubjectId, 'Calculus', 'Mathematical study of continuous change']
    );
    const calculusCategoryId = calculusCategory.rows[0]?.id;

    const physicsCategory = await query(
      `INSERT INTO categories (subject_id, name, description) 
       VALUES ($1, $2, $3) 
       ON CONFLICT DO NOTHING 
       RETURNING id`,
      [scienceSubjectId, 'Physics', 'Study of matter, energy, and their interactions']
    );
    const physicsCategoryId = physicsCategory.rows[0]?.id;

    // Create questions
    const questions = [
      // Algebra questions
      {
        category_id: algebraCategoryId,
        question_text: 'What is the value of x in the equation 2x + 5 = 15?',
        explanation: 'Subtract 5 from both sides: 2x = 10, then divide by 2: x = 5',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'x = 5', correct: true },
          { text: 'x = 10', correct: false },
          { text: 'x = 7.5', correct: false },
          { text: 'x = 20', correct: false },
        ]
      },
      {
        category_id: algebraCategoryId,
        question_text: 'Simplify: 3(x + 2) - 2x',
        explanation: 'Distribute: 3x + 6 - 2x = x + 6',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'x + 6', correct: true },
          { text: '5x + 6', correct: false },
          { text: 'x + 2', correct: false },
          { text: '3x + 4', correct: false },
        ]
      },
      {
        category_id: algebraCategoryId,
        question_text: 'What is the slope of the line y = 3x + 7?',
        explanation: 'In the form y = mx + b, m is the slope. Here, m = 3',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: '3', correct: true },
          { text: '7', correct: false },
          { text: '-3', correct: false },
          { text: '0', correct: false },
        ]
      },
      {
        category_id: algebraCategoryId,
        question_text: 'Solve: x² - 9 = 0',
        explanation: 'Factor: (x + 3)(x - 3) = 0, so x = 3 or x = -3',
        difficulty: 'medium',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'x = 3 or x = -3', correct: true },
          { text: 'x = 9', correct: false },
          { text: 'x = 0', correct: false },
          { text: 'x = 3 only', correct: false },
        ]
      },
      {
        category_id: algebraCategoryId,
        question_text: 'What is the solution to the system: y = 2x + 1 and y = -x + 7?',
        explanation: 'Set equal: 2x + 1 = -x + 7, so 3x = 6, x = 2. Then y = 2(2) + 1 = 5',
        difficulty: 'medium',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: '(2, 5)', correct: true },
          { text: '(5, 2)', correct: false },
          { text: '(1, 3)', correct: false },
          { text: '(3, 7)', correct: false },
        ]
      },
      {
        category_id: algebraCategoryId,
        question_text: 'Factor completely: x² - 5x + 6',
        explanation: 'Find two numbers that multiply to 6 and add to -5: -2 and -3. Result: (x - 2)(x - 3)',
        difficulty: 'medium',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: '(x - 2)(x - 3)', correct: true },
          { text: '(x + 2)(x + 3)', correct: false },
          { text: '(x - 1)(x - 6)', correct: false },
          { text: '(x + 1)(x + 6)', correct: false },
        ]
      },
      {
        category_id: algebraCategoryId,
        question_text: 'What is the quadratic formula?',
        explanation: 'The quadratic formula is x = (-b ± √(b² - 4ac)) / (2a)',
        difficulty: 'hard',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'x = (-b ± √(b² - 4ac)) / (2a)', correct: true },
          { text: 'x = (b ± √(b² - 4ac)) / (2a)', correct: false },
          { text: 'x = (-b ± √(b² + 4ac)) / (2a)', correct: false },
          { text: 'x = (-b ± √(b² - 4ac)) / a', correct: false },
        ]
      },
      {
        category_id: algebraCategoryId,
        question_text: 'If f(x) = 2x² - 3x + 1, what is f(2)?',
        explanation: 'Substitute x = 2: f(2) = 2(4) - 3(2) + 1 = 8 - 6 + 1 = 3',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: '3', correct: true },
          { text: '7', correct: false },
          { text: '1', correct: false },
          { text: '5', correct: false },
        ]
      },

      // Calculus questions
      {
        category_id: calculusCategoryId,
        question_text: 'What is the derivative of f(x) = x³?',
        explanation: 'Using the power rule: d/dx(x^n) = nx^(n-1). So d/dx(x³) = 3x²',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: '3x²', correct: true },
          { text: 'x²', correct: false },
          { text: '3x', correct: false },
          { text: 'x³', correct: false },
        ]
      },
      {
        category_id: calculusCategoryId,
        question_text: 'What is the integral of 2x dx?',
        explanation: 'The integral of x^n is x^(n+1)/(n+1). So ∫2x dx = 2(x²/2) = x² + C',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'x² + C', correct: true },
          { text: '2x² + C', correct: false },
          { text: 'x + C', correct: false },
          { text: '2x + C', correct: false },
        ]
      },
      {
        category_id: calculusCategoryId,
        question_text: 'What is the limit of (x² - 1)/(x - 1) as x approaches 1?',
        explanation: 'Factor numerator: (x-1)(x+1)/(x-1) = x+1. As x→1, limit = 2',
        difficulty: 'medium',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: '2', correct: true },
          { text: '0', correct: false },
          { text: '1', correct: false },
          { text: 'undefined', correct: false },
        ]
      },
      {
        category_id: calculusCategoryId,
        question_text: 'What is the derivative of sin(x)?',
        explanation: 'The derivative of sin(x) is cos(x)',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'cos(x)', correct: true },
          { text: '-cos(x)', correct: false },
          { text: 'sin(x)', correct: false },
          { text: '-sin(x)', correct: false },
        ]
      },
      {
        category_id: calculusCategoryId,
        question_text: 'Find the derivative of f(x) = e^x',
        explanation: 'The derivative of e^x is e^x (it is its own derivative)',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'e^x', correct: true },
          { text: 'xe^(x-1)', correct: false },
          { text: 'ln(x)', correct: false },
          { text: '1/x', correct: false },
        ]
      },
      {
        category_id: calculusCategoryId,
        question_text: 'What is the integral of 1/x dx?',
        explanation: 'The integral of 1/x is ln|x| + C',
        difficulty: 'medium',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'ln|x| + C', correct: true },
          { text: 'x + C', correct: false },
          { text: '1 + C', correct: false },
          { text: '-1/x² + C', correct: false },
        ]
      },
      {
        category_id: calculusCategoryId,
        question_text: 'Using the chain rule, what is the derivative of f(x) = sin(2x)?',
        explanation: 'Chain rule: f\'(x) = cos(2x) × 2 = 2cos(2x)',
        difficulty: 'medium',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: '2cos(2x)', correct: true },
          { text: 'cos(2x)', correct: false },
          { text: '2sin(2x)', correct: false },
          { text: '-2cos(2x)', correct: false },
        ]
      },

      // Physics questions
      {
        category_id: physicsCategoryId,
        question_text: 'What is the SI unit of force?',
        explanation: 'The SI unit of force is the Newton (N)',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'Newton (N)', correct: true },
          { text: 'Joule (J)', correct: false },
          { text: 'Watt (W)', correct: false },
          { text: 'Pascal (Pa)', correct: false },
        ]
      },
      {
        category_id: physicsCategoryId,
        question_text: 'What is Newton\'s Second Law of Motion?',
        explanation: 'Newton\'s Second Law states that Force = mass × acceleration (F = ma)',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'F = ma', correct: true },
          { text: 'F = mv', correct: false },
          { text: 'F = m/a', correct: false },
          { text: 'F = a/m', correct: false },
        ]
      },
      {
        category_id: physicsCategoryId,
        question_text: 'What is the speed of light in vacuum?',
        explanation: 'The speed of light in vacuum is approximately 3 × 10^8 m/s',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: '3 × 10^8 m/s', correct: true },
          { text: '3 × 10^6 m/s', correct: false },
          { text: '3 × 10^10 m/s', correct: false },
          { text: '3 × 10^5 m/s', correct: false },
        ]
      },
      {
        category_id: physicsCategoryId,
        question_text: 'What is kinetic energy?',
        explanation: 'Kinetic energy = (1/2)mv², where m is mass and v is velocity',
        difficulty: 'medium',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: '(1/2)mv²', correct: true },
          { text: 'mv²', correct: false },
          { text: 'mgh', correct: false },
          { text: 'mg', correct: false },
        ]
      },
      {
        category_id: physicsCategoryId,
        question_text: 'What is the formula for gravitational potential energy?',
        explanation: 'Gravitational potential energy = mgh, where m is mass, g is gravitational acceleration, and h is height',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'mgh', correct: true },
          { text: '(1/2)mv²', correct: false },
          { text: 'mg', correct: false },
          { text: 'mv', correct: false },
        ]
      },
      {
        category_id: physicsCategoryId,
        question_text: 'What is Ohm\'s Law?',
        explanation: 'Ohm\'s Law states that V = IR, where V is voltage, I is current, and R is resistance',
        difficulty: 'easy',
        marks: 1.0,
        negative_marks: 0.25,
        options: [
          { text: 'V = IR', correct: true },
          { text: 'I = VR', correct: false },
          { text: 'R = VI', correct: false },
          { text: 'V = I/R', correct: false },
        ]
      },
    ];

    const questionIds: string[] = [];

    for (const q of questions) {
      const questionResult = await query(
        `INSERT INTO questions (category_id, question_text, explanation, difficulty, marks, negative_marks) 
         VALUES ($1, $2, $3, $4, $5, $6) 
         RETURNING id`,
        [q.category_id, q.question_text, q.explanation, q.difficulty, q.marks, q.negative_marks]
      );
      const questionId = questionResult.rows[0].id;
      questionIds.push(questionId);

      for (let i = 0; i < q.options.length; i++) {
        await query(
          `INSERT INTO options (question_id, option_text, option_order, is_correct) 
           VALUES ($1, $2, $3, $4)`,
          [questionId, q.options[i].text, i + 1, q.options[i].correct]
        );
      }
    }

    // Create exams
    const algebraExam = await query(
      `INSERT INTO exams (title, description, subject_id, duration_minutes, total_questions, passing_percentage) 
       VALUES ($1, $2, $3, $4, $5, $6) 
       RETURNING id`,
      ['Algebra Basics', 'Test your algebra skills with basic equations and expressions', mathSubjectId, 30, 8, 70.0]
    );
    const algebraExamId = algebraExam.rows[0].id;

    const calculusExam = await query(
      `INSERT INTO exams (title, description, subject_id, duration_minutes, total_questions, passing_percentage) 
       VALUES ($1, $2, $3, $4, $5, $6) 
       RETURNING id`,
      ['Calculus Fundamentals', 'Introduction to derivatives and integrals', mathSubjectId, 45, 7, 60.0]
    );
    const calculusExamId = calculusExam.rows[0].id;

    const physicsExam = await query(
      `INSERT INTO exams (title, description, subject_id, duration_minutes, total_questions, passing_percentage) 
       VALUES ($1, $2, $3, $4, $5, $6) 
       RETURNING id`,
      ['Physics Basics', 'Fundamental concepts in physics', scienceSubjectId, 30, 6, 65.0]
    );
    const physicsExamId = physicsExam.rows[0].id;

    // Add questions to exams
    // Algebra exam: first 8 questions (algebra questions)
    for (let i = 0; i < 8; i++) {
      await query(
        `INSERT INTO exam_questions (exam_id, question_id, question_order, marks) 
         VALUES ($1, $2, $3, $4)`,
        [algebraExamId, questionIds[i], i + 1, 1.0]
      );
    }

    // Calculus exam: next 7 questions (calculus questions)
    for (let i = 8; i < 15; i++) {
      await query(
        `INSERT INTO exam_questions (exam_id, question_id, question_order, marks) 
         VALUES ($1, $2, $3, $4)`,
        [calculusExamId, questionIds[i], i - 7, 1.0]
      );
    }

    // Physics exam: last 6 questions (physics questions)
    for (let i = 15; i < 21; i++) {
      await query(
        `INSERT INTO exam_questions (exam_id, question_id, question_order, marks) 
         VALUES ($1, $2, $3, $4)`,
        [physicsExamId, questionIds[i], i - 14, 1.0]
      );
    }

    console.log('Database seeding completed successfully!');
    console.log('Test user: test@example.com / password123');
  } catch (error) {
    console.error('Seeding failed:', error);
    process.exit(1);
  }
}

seed();
