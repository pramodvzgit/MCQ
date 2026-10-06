# Android App Testing Guide

## Prerequisites

✅ Backend server is running on http://localhost:3000
✅ Database is seeded with sample data
✅ Android app is configured with correct API URL

## Step 1: Open Project in Android Studio

```bash
# Open the project
open -a "Android Studio" /Applications/XAMPP/xamppfiles/htdocs/MCQ
```

## Step 2: Wait for Gradle Sync

- Android Studio will automatically sync Gradle
- Wait for the sync to complete (bottom progress bar)
- If sync fails, click "Sync Project with Gradle Files"

## Step 3: Start Android Emulator

1. In Android Studio, click the **Device Manager** icon (top right)
2. Click **Create Device** if no emulator exists
3. Choose a device (e.g., Pixel 6)
4. Choose a system image (API 33 or higher)
5. Click **Finish**
6. Click the **Play** button to start the emulator

## Step 4: Run the App

1. Click the **Run** button (green triangle) in Android Studio
2. Or press `Shift + F10`
3. Wait for the app to install and launch on the emulator

## Step 5: Test the App Flow

### 1. Splash Screen
- You'll see the MCQ Exam App branding
- Wait 2 seconds
- App will navigate to Login screen

### 2. Login Screen
- The app will auto-login (simplified for testing)
- Navigate to Home screen

### 3. Home Screen
- You should see **3 exams** listed:
  - Algebra Basics (8 questions, 30 min)
  - Calculus Fundamentals (7 questions, 45 min)
  - Physics Basics (6 questions, 30 min)
- Each exam shows: title, subject, duration, question count

### 4. Exam Detail Screen
- Tap on any exam
- You'll see:
  - Exam title and description
  - Subject name
  - Number of questions
  - Duration
  - Passing percentage
  - Instructions
  - **Start Exam** button

### 5. Exam Screen
- Click **Start Exam**
- You'll see:
  - Question text
  - 4 radio button options
  - Question navigation palette (numbered buttons)
  - Countdown timer (top right)
  - Previous/Next buttons
  - Submit button

### 6. Take the Exam
- Select answers by clicking options
- Navigate between questions
- Answered questions show as filled in the palette
- Timer counts down
- Click **Submit** when done

### 7. Result Screen
- After submission, you'll see:
  - **PASS/FAIL** indicator (green/red card)
  - Score display
  - Percentage
  - Statistics (Correct, Incorrect, Unanswered)
  - **Review Answers** button
  - **Retake Exam** button
  - **Back to Home** button

### 8. Review Screen
- Click **Review Answers**
- You'll see each question with:
  - Your selected answer (highlighted)
  - Correct answer (marked with ✓)
  - Explanation
  - Marks awarded

## Expected Behavior

### Home Screen
- ✅ Shows 3 exams from database
- ✅ Each exam has correct details
- ✅ Cards are clickable

### Exam Taking
- ✅ Questions load correctly
- ✅ Options display without correct answers (security)
- ✅ Timer counts down
- ✅ Navigation works
- ✅ Answers persist when navigating

### Results
- ✅ Score calculated by server
- ✅ Pass/Fail based on passing percentage
- ✅ Correct answers shown after submission
- ✅ Review shows detailed feedback

## Troubleshooting

### App shows error states
- **Check backend is running**: `curl http://localhost:3000/health`
- **Check emulator can access localhost**: The app uses 10.0.2.2 which should work
- **Check AndroidManifest.xml**: Has INTERNET permission

### API connection fails
- **Verify backend**: Make sure `npm run dev` is still running
- **Check NetworkModule.kt**: BASE_URL should be `http://10.0.2.2:3000/api/`
- **Check firewall**: Ensure port 3000 is not blocked

### Emulator won't start
- **Check system image**: Download API 33+ in SDK Manager
- **Check HAXM**: For Intel Mac, ensure HAXM is installed
- **Try different device**: Create a new emulator with different specs

### Gradle sync fails
- **Check internet connection**
- **Update Gradle**: File → Settings → Gradle → Use Gradle wrapper
- **Invalidate caches**: File → Invalidate Caches / Restart

## Sample Questions to Test

### Algebra Basics (8 questions)
1. What is the value of x in the equation 2x + 5 = 15?
   - Correct: x = 5

2. Simplify: 3(x + 2) - 2x
   - Correct: x + 6

### Calculus Fundamentals (7 questions)
1. What is the derivative of f(x) = x³?
   - Correct: 3x²

2. What is the integral of 2x dx?
   - Correct: x² + C

### Physics Basics (6 questions)
1. What is the SI unit of force?
   - Correct: Newton (N)

2. What is Newton's Second Law of Motion?
   - Correct: F = ma

## Test Credentials

For any authentication features:
- **Email**: test@example.com
- **Password**: password123

## Success Criteria

The app is working correctly if:
- ✅ Home screen shows 3 exams
- ✅ Exam details load correctly
- ✅ Questions display without correct answers during exam
- ✅ Timer counts down
- ✅ Navigation between questions works
- ✅ Submit works and shows results
- ✅ Results show correct answers
- ✅ Review screen shows detailed feedback

## Next Steps After Testing

1. **Verify all screens work correctly**
2. **Test complete exam flow**
3. **Check error handling** (try with network off)
4. **Test with different answers**
5. **Verify score calculation**

## Need Help?

If you encounter issues:
1. Check backend logs in terminal
2. Check Android Studio Logcat
3. Verify network connectivity
4. Check API responses with curl
