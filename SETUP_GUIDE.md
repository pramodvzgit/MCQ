# Quick Setup Guide

## Backend Setup

1. **Install Node.js dependencies**
   ```bash
   cd backend
   npm install
   ```

2. **Configure environment**
   ```bash
   cp .env.example .env
   # Edit .env with your database credentials
   ```

3. **Setup PostgreSQL**
   ```bash
   # Create database
   createdb mcq_exam_db

   # Run migrations
   npm run migrate

   # Seed data
   npm run seed
   ```

4. **Start backend server**
   ```bash
   npm run dev
   ```

   Server will run on http://localhost:3000

## Android Setup

1. **Open in Android Studio**
   - Open the project root directory
   - Wait for Gradle sync to complete

2. **Configure API URL**
   - Edit `app/src/main/java/com/mcq/exam/di/NetworkModule.kt`
   - For emulator: `http://10.0.2.2:3000/api/`
   - For real device: Use your computer's IP address

3. **Build and Run**
   - Connect device or start emulator
   - Click Run button in Android Studio

## Testing the Complete Flow

### Backend Testing

1. **Test health endpoint**
   ```bash
   curl http://localhost:3000/health
   ```

2. **Register a user**
   ```bash
   curl -X POST http://localhost:3000/api/register \
     -H "Content-Type: application/json" \
     -d '{"name":"Test User","email":"test@example.com","password":"password123"}'
   ```

3. **Login**
   ```bash
   curl -X POST http://localhost:3000/api/login \
     -H "Content-Type: application/json" \
     -d '{"email":"test@example.com","password":"password123"}'
   ```

4. **Get exams**
   ```bash
   curl http://localhost:3000/api/exams
   ```

5. **Start an exam** (use the token from login)
   ```bash
   curl -X POST http://localhost:3000/api/exams/{exam_id}/start \
     -H "Authorization: Bearer YOUR_TOKEN"
   ```

### Android Testing

1. **Launch the app**
   - The app will show a splash screen
   - Then navigate to Home screen
   - You'll see the list of available exams

2. **Browse exams**
   - Tap on any exam to see details
   - Review instructions and exam info

3. **Take an exam**
   - Click "Start Exam"
   - Answer questions by selecting options
   - Navigate between questions
   - Submit when ready

4. **View results**
   - See your score and pass/fail status
   - Review correct/incorrect answers
   - Retake exam or go back to home

## Troubleshooting

### Backend won't start
- Check if PostgreSQL is running
- Verify database credentials in .env
- Check if port 3000 is available

### Android can't connect to API
- Ensure backend is running
- Check BASE_URL in NetworkModule.kt
- For emulator: Use 10.0.2.2
- For real device: Use computer's IP address
- Check AndroidManifest.xml has INTERNET permission

### Database migration fails
- Drop and recreate database: `dropdb mcq_exam_db && createdb mcq_exam_db`
- Run migrations again: `npm run migrate`

## Next Steps

After setup, you can:

1. **Add new questions** via SQL or future admin panel
2. **Create new exams** by adding to database
3. **Customize the UI** in the presentation layer
4. **Add more features** like offline mode, analytics, etc.

For detailed documentation, see README.md and ARCHITECTURE.md.
