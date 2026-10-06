#!/bin/bash

# API Testing Script for MCQ Exam Backend
# This script tests the API endpoints with sample data

BASE_URL="http://localhost:3000/api"

echo "=========================================="
echo "MCQ Exam Backend API Testing"
echo "=========================================="
echo ""

# Test 1: Health Check
echo "Test 1: Health Check"
curl -s http://localhost:3000/health
echo -e "\n"

# Test 2: Register User
echo "Test 2: Register User"
REGISTER_RESPONSE=$(curl -s -X POST $BASE_URL/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }')
echo "$REGISTER_RESPONSE"
echo -e "\n"

# Extract token
TOKEN=$(echo $REGISTER_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Token: $TOKEN"
echo -e "\n"

# Test 3: Login
echo "Test 3: Login"
LOGIN_RESPONSE=$(curl -s -X POST $BASE_URL/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }')
echo "$LOGIN_RESPONSE"
echo -e "\n"

# Update token from login
TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Token: $TOKEN"
echo -e "\n"

# Test 4: Get Exams
echo "Test 4: Get All Exams"
curl -s $BASE_URL/exams
echo -e "\n"

# Test 5: Get Specific Exam (first exam)
echo "Test 5: Get Specific Exam"
EXAMS=$(curl -s $BASE_URL/exams)
FIRST_EXAM_ID=$(echo $EXAMS | grep -o '"id":"[^"]*' | head -1 | cut -d'"' -f4)
echo "First Exam ID: $FIRST_EXAM_ID"
curl -s $BASE_URL/exams/$FIRST_EXAM_ID
echo -e "\n"

# Test 6: Start Exam
echo "Test 6: Start Exam (requires token)"
if [ ! -z "$TOKEN" ]; then
  START_RESPONSE=$(curl -s -X POST $BASE_URL/exams/$FIRST_EXAM_ID/start \
    -H "Authorization: Bearer $TOKEN")
  echo "$START_RESPONSE"
  echo -e "\n"

  # Extract attempt ID
  ATTEMPT_ID=$(echo $START_RESPONSE | grep -o '"attempt_id":"[^"]*' | cut -d'"' -f4)
  echo "Attempt ID: $ATTEMPT_ID"
  echo -e "\n"

  # Test 7: Submit Exam
  echo "Test 7: Submit Exam"
  # For this test, we need actual question IDs from the start response
  # This is a placeholder - in real testing, you'd parse the questions
  curl -s -X POST $BASE_URL/attempts/$ATTEMPT_ID/submit \
    -H "Authorization: Bearer $TOKEN" \
    -H "Content-Type: application/json" \
    -d '{
      "answers": []
    }'
  echo -e "\n"

  # Test 8: Get Attempt Review
  echo "Test 8: Get Attempt Review"
  curl -s $BASE_URL/attempts/$ATTEMPT_ID/review \
    -H "Authorization: Bearer $TOKEN"
  echo -e "\n"

  # Test 9: Get User Attempts
  echo "Test 9: Get User Attempts"
  curl -s $BASE_URL/attempts \
    -H "Authorization: Bearer $TOKEN"
  echo -e "\n"
else
  echo "No token available, skipping protected endpoints"
fi

echo "=========================================="
echo "Testing Complete"
echo "=========================================="
