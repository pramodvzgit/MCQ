# Testing Guide for MCQ Exam App

## Prerequisites

Before testing, ensure you have:
- PostgreSQL installed and running
- Node.js 20+ installed
- Android Studio (for Android testing)

## Backend Testing

### Step 1: Setup Environment

1. **Install dependencies**
   ```bash
   cd backend
   npm install
   ```

2. **Configure environment**
   ```bash
   # Run the setup script
   ./test-setup.sh

   # Or manually create .env file
   cp .env.example .env
   # Edit .env with your database credentials
   ```

3. **Create database**
   ```bash
   # Using PostgreSQL
   createdb mcq_exam_db

   # Or using psql
   psql -U postgres
   CREATE DATABASE mcq_exam_db;
   \q
   ```

4. **Run migrations**
   ```bash
   npm run migrate
   ```

5. **Seed database**
   ```bash
   npm run seed
   ```

   This creates:
   - 1 test user: `test@example.com` / `password123`
   - 2 subjects (Mathematics, Science)
   - 3 categories (Algebra, Calculus, Physics)
   - 3 exams
   - 21 sample questions

### Step 2: Start Backend Server

```bash
cd backend
npm run dev
```

Server will start on `http://localhost:3000`

### Step 3: Run API Tests

**Option 1: Using the test script**
```bash
./test-api.sh
```

**Option 2: Manual testing with curl**

1. **Health Check**
   ```bash
   curl http://localhost:3000/health
   ```

2. **Register User**
   ```bash
   curl -X POST http://localhost:3000/api/register \
     -H "Content-Type: application/json" \
     -d '{
       "name": "Test User",
       "email": "test@example.com",
       "password": "password123"
     }'
   ```

3. **Login**
   ```bash
   curl -X POST http://localhost:3000/api/login \
     -H "Content-Type: application/json" \
     -d '{
       "email": "test@example.com",
       "password": "password123"
     }'
   ```

   Save the token from the response for subsequent requests.

4. **Get All Exams**
   ```bash
   curl http://localhost:3000/api/exams
   ```

5. **Get Specific Exam**
   ```bash
   curl http://localhost:3000/api/exams/{exam_id}
   ```

6. **Start Exam** (requires token)
   ```bash
   curl -X POST http://localhost:3000/api/exams/{exam_id}/start \
     -H "Authorization: Bearer YOUR_TOKEN"
   ```

7. **Submit Exam** (requires token)
   ```bash
   curl -X POST http://localhost:3000/api/attempts/{attempt_id}/submit \
     -H "Authorization: Bearer YOUR_TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
       "answers": [
         {
           "question_id": "question-uuid",
           "selected_option_id": "option-uuid"
         }
       ]
     }'
   ```

8. **Get Review** (requires token)
   ```bash
   curl http://localhost:3000/api/attempts/{attempt_id}/review \
     -H "Authorization: Bearer YOUR_TOKEN"
   ```

9. **Get User History** (requires token)
   ```bash
   curl http://localhost:3000/api/attempts \
     -H "Authorization: Bearer YOUR_TOKEN"
   ```

### Step 4: Verify Sample Questions

After seeding, you can verify the sample questions in the database:

```sql
-- Connect to database
psql -U postgres mcq_exam_db

-- View subjects
SELECT * FROM subjects;

-- View categories
SELECT * FROM categories;

-- View exams
SELECT * FROM exams;

-- View questions
SELECT q.id, q.question_text, q.difficulty, c.name as category
FROM questions q
JOIN categories c ON q.category_id = c.id
LIMIT 5;

-- View options for a question
SELECT * FROM options WHERE question_id = 'some-uuid';

-- View exam questions
SELECT eq.question_order, q.question_text
FROM exam_questions eq
JOIN questions q ON eq.question_id = q.id
WHERE eq.exam_id = 'exam-uuid'
ORDER BY eq.question_order;
```

## Android Testing

### Step 1: Setup Android Project

1. **Open in Android Studio**
   - Open the project root directory
   - Wait for Gradle sync to complete

2. **Configure API URL**
   - Edit `app/src/main/java/com/mcq/exam/di/NetworkModule.kt`
   - For emulator: `http://10.0.2.2:3000/api/`
   - For real device: Use your computer's IP address

3. **Sync Gradle**
   - File → Sync Project with Gradle Files

### Step 2: Run Unit Tests

```bash
# Unit tests
./gradlew test

# Specific test class
./gradlew test --tests GetExamsUseCaseTest
```

### Step 3: Run Instrumented Tests

```bash
# Connect device or start emulator
./gradlew connectedAndroidTest
```

### Step 4: Manual UI Testing

1. **Launch the app**
   - Click Run in Android Studio
   - App will show splash screen → login → home

2. **Test Exam Flow**
   - View available exams on home screen
   - Tap an exam to see details
   - Click "Start Exam"
   - Answer questions (select options)
   - Navigate between questions
   - Submit exam
   - View results
   - Review answers
   - Retake exam or go home

### Step 5: Verify Sample Questions in App

After starting the backend with seeded data:

1. The home screen should show 3 exams:
   - Algebra Basics (8 questions, 30 min)
   - Calculus Fundamentals (7 questions, 45 min)
   - Physics Basics (6 questions, 30 min)

2. When you start an exam, you should see:
   - Questions with 4 options each
   - Timer counting down
   - Question navigation palette
   - Previous/Next buttons

3. Sample question example:
   ```
   What is the value of x in the equation 2x + 5 = 15?
   Options:
   ○ x = 5
   ○ x = 10
   ○ x = 7.5
   ○ x = 20
   ```

## Expected Test Results

### Backend API Tests

| Test | Expected Result |
|------|----------------|
| Health Check | `{"status":"ok","timestamp":"..."}` |
| Register | User created with JWT token |
| Login | JWT token returned |
| Get Exams | Array of 3 exams |
| Get Exam | Single exam with details |
| Start Exam | Attempt ID + questions (sans correct answers) |
| Submit Exam | Score + questions with correct answers |
| Get Review | Questions with user's answers |
| Get History | Array of user's attempts |

### Android UI Tests

| Screen | Expected Content |
|--------|------------------|
| Splash | App branding |
| Home | List of 3 exams |
| Exam Detail | Exam info, instructions, Start button |
| Exam | Questions, timer, navigation |
| Result | Score, pass/fail, statistics |
| Review | Questions with correct/incorrect indicators |

## Troubleshooting

### Backend Issues

**Database connection failed**
```bash
# Check PostgreSQL is running
pg_isready

# Check database exists
psql -U postgres -l

# Recreate database
dropdb mcq_exam_db
createdb mcq_exam_db
npm run migrate
npm run seed
```

**Port already in use**
```bash
# Find process using port 3000
lsof -ti:3000

# Kill process
lsof -ti:3000 | xargs kill

# Or change port in .env
PORT=3001 npm run dev
```

**Migration errors**
```bash
# Drop and recreate database
dropdb mcq_exam_db
createdb mcq_exam_db
npm run migrate
npm run seed
```

### Android Issues

**Can't connect to API**
- Verify backend is running: `curl http://localhost:3000/health`
- Check BASE_URL in NetworkModule.kt
- For emulator: Use `10.0.2.2`
- For real device: Use computer's IP address
- Check AndroidManifest.xml has INTERNET permission

**Build errors**
```bash
# Clean and rebuild
./gradlew clean
./gradlew build

# Invalidate caches
# Android Studio → File → Invalidate Caches / Restart
```

**Gradle sync fails**
- Check internet connection
- Update Gradle wrapper if needed
- Try offline mode: File → Settings → Build, Execution, Deployment → Gradle → Offline work

## Sample Question Verification

### Mathematics - Algebra

**Question 1:**
```
What is the value of x in the equation 2x + 5 = 15?
Correct Answer: x = 5
Difficulty: Easy
Marks: 1.0
Negative Marks: 0.25
```

**Question 2:**
```
Simplify: 3(x + 2) - 2x
Correct Answer: x + 6
Difficulty: Easy
Marks: 1.0
Negative Marks: 0.25
```

### Mathematics - Calculus

**Question 1:**
```
What is the derivative of f(x) = x³?
Correct Answer: 3x²
Difficulty: Easy
Marks: 1.0
Negative Marks: 0.25
```

**Question 2:**
```
What is the integral of 2x dx?
Correct Answer: x² + C
Difficulty: Easy
Marks: 1.0
Negative Marks: 0.25
```

### Science - Physics

**Question 1:**
```
What is the SI unit of force?
Correct Answer: Newton (N)
Difficulty: Easy
Marks: 1.0
Negative Marks: 0.25
```

**Question 2:**
```
What is Newton's Second Law of Motion?
Correct Answer: F = ma
Difficulty: Easy
Marks: 1.0
Negative Marks: 0.25
```

## Security Testing

### Verify Correct Answers Are Hidden

During active exam, options should NOT include `is_correct` field:

```bash
# Start exam
curl -X POST http://localhost:3000/api/exams/{id}/start \
  -H "Authorization: Bearer TOKEN"

# Check response - options should NOT have is_correct
```

After submission, options SHOULD include `is_correct`:

```bash
# Submit exam
curl -X POST http://localhost:3000/api/attempts/{id}/submit \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"answers": [...]}'

# Check response - options should have is_correct
```

## Performance Testing

### Load Testing

You can use tools like Apache Bench or JMeter to test API performance:

```bash
# Test exam list endpoint
ab -n 1000 -c 10 http://localhost:3000/api/exams

# Test with authentication
ab -n 100 -c 5 -H "Authorization: Bearer TOKEN" \
  http://localhost:3000/api/attempts
```

## Next Steps After Testing

After successful testing:

1. **Deploy Backend** - Deploy to a cloud provider (AWS, Heroku, etc.)
2. **Configure Production Database** - Use managed PostgreSQL service
3. **Update API URL** - Change NetworkModule.kt to production URL
4. **Build Release APK** - `./gradlew assembleRelease`
5. **Test on Real Device** - Install APK and test thoroughly
6. **Monitor** - Set up logging and monitoring
7. **Add Admin Panel** - Implement content management interface

## Continuous Testing

For ongoing development, consider:

- **Automated Tests**: Run tests on every commit
- **CI/CD Pipeline**: GitHub Actions or similar
- **Test Coverage**: Aim for >80% coverage
- **Integration Tests**: Test complete flows
- **E2E Tests**: Test from user perspective
