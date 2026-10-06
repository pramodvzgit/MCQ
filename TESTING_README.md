# Testing Resources Created

I've created several testing resources to help you test the MCQ Exam App with sample questions:

## Files Created

### 1. `backend/test-setup.sh`
**Purpose**: Automatically creates the `.env` file for local testing

**Usage**:
```bash
cd backend
./test-setup.sh
```

This creates a `.env` file with default database credentials. You can edit it if needed:
- `DB_USER` - Your PostgreSQL username
- `DB_PASSWORD` - Your PostgreSQL password
- `DB_NAME` - Database name (default: mcq_exam_db)

### 2. `backend/test-api.sh`
**Purpose**: Automated API testing script

**Usage**:
```bash
cd backend
# Ensure server is running first
npm run dev

# In another terminal
./test-api.sh
```

This script tests:
1. Health check endpoint
2. User registration
3. User login
4. Get all exams
5. Get specific exam
6. Start exam (with authentication)
7. Submit exam
8. Get attempt review
9. Get user history

### 3. `backend/test-questions.sql`
**Purpose**: SQL queries to verify sample questions in the database

**Usage**:
```bash
# After seeding the database
psql -U postgres mcq_exam_db -f test-questions.sql
```

This displays:
- All subjects
- All categories
- All exams
- Question counts by category
- Sample questions from each category
- Sample options
- Exam-question mappings
- Test user
- Summary statistics

### 4. `TESTING_GUIDE.md`
**Purpose**: Comprehensive testing documentation

**Contents**:
- Backend testing setup (database, migrations, seeding)
- Manual API testing with curl commands
- Android testing setup
- Unit and instrumented test execution
- Manual UI testing steps
- Expected test results
- Troubleshooting guide
- Sample question verification
- Security testing (verifying correct answers are hidden)
- Performance testing suggestions

## Quick Testing Checklist

### Backend Testing

```bash
# 1. Install dependencies
cd backend
npm install

# 2. Setup environment
./test-setup.sh

# 3. Create database
createdb mcq_exam_db

# 4. Run migrations
npm run migrate

# 5. Seed data
npm run seed

# 6. Start server
npm run dev

# 7. Run API tests (in another terminal)
./test-api.sh

# 8. Verify questions in database
psql -U postgres mcq_exam_db -f test-questions.sql
```

### Android Testing

```bash
# 1. Open project in Android Studio
# 2. Configure API URL in NetworkModule.kt
# 3. Sync Gradle
# 4. Run unit tests
./gradlew test

# 5. Run on emulator/device
# 6. Test exam flow manually
```

## Sample Questions Included

The seed data includes 21 sample questions across 3 subjects:

### Mathematics - Algebra (8 questions)
- Basic equations
- Expressions simplification
- Slope and lines
- Quadratic equations
- Systems of equations
- Factoring
- Quadratic formula
- Function evaluation

### Mathematics - Calculus (7 questions)
- Derivatives
- Integrals
- Limits
- Trigonometric derivatives
- Exponential derivatives
- Logarithmic integrals
- Chain rule

### Science - Physics (6 questions)
- Units of measurement
- Newton's laws
- Speed of light
- Kinetic energy
- Potential energy
- Ohm's law

Each question has:
- 4 multiple choice options
- Exactly 1 correct answer
- Difficulty level (easy/medium/hard)
- Marks (typically 1.0)
- Negative marking (typically 0.25)
- Explanation

## Expected Test Results

### After Seeding Database

**Subjects**: 2 (Mathematics, Science)
**Categories**: 3 (Algebra, Calculus, Physics)
**Exams**: 3
- Algebra Basics (8 questions, 30 min, 70% pass)
- Calculus Fundamentals (7 questions, 45 min, 60% pass)
- Physics Basics (6 questions, 30 min, 65% pass)
**Questions**: 21 total
**Options**: 84 total (4 per question)
**Users**: 1 (test@example.com / password123)

### API Testing

When you run `./test-api.sh`, you should see:
- ✅ Health check returns status "ok"
- ✅ Registration creates user and returns JWT token
- ✅ Login returns JWT token
- ✅ Get exams returns array of 3 exams
- ✅ Get specific exam returns exam details
- ✅ Start exam returns attempt ID and questions (without correct answers)
- ✅ Submit exam returns score and questions (with correct answers)
- ✅ Get review returns detailed answers
- ✅ Get history returns user's attempts

### Android UI Testing

When you run the Android app:
- ✅ Splash screen displays
- ✅ Home screen shows 3 exams
- ✅ Exam detail shows exam info
- ✅ Exam screen shows questions with timer
- ✅ Can select answers and navigate
- ✅ Submit works and shows results
- ✅ Result screen shows score and pass/fail
- ✅ Review screen shows correct/incorrect answers

## Security Verification

Important: Verify that correct answers are NOT exposed during active exam:

**During exam (start endpoint)**:
```json
{
  "options": [
    {
      "id": "uuid",
      "option_text": "4",
      "option_order": 1
      // NO is_correct field
    }
  ]
}
```

**After submission (submit endpoint)**:
```json
{
  "options": [
    {
      "id": "uuid",
      "option_text": "4",
      "option_order": 1,
      "is_correct": true  // NOW included
    }
  ]
}
```

## Troubleshooting

If you encounter issues:

1. **Database connection failed**: Check PostgreSQL is running and credentials in `.env` are correct
2. **Port already in use**: Kill process on port 3000 or change PORT in `.env`
3. **Migration errors**: Drop and recreate database
4. **Android can't connect**: Check BASE_URL in NetworkModule.kt (use 10.0.2.2 for emulator)
5. **Gradle sync fails**: Check internet connection and invalidate caches

See `TESTING_GUIDE.md` for detailed troubleshooting steps.

## Next Steps

After successful testing:
1. Review the test results
2. Customize sample questions if needed
3. Add more exams/questions to database
4. Test with different user accounts
5. Test edge cases (timeouts, empty answers, etc.)
6. Deploy to production environment

For detailed instructions, refer to `TESTING_GUIDE.md`.
