#!/bin/bash

echo "=========================================="
echo "Supabase Setup for MCQ Exam Backend"
echo "=========================================="
echo ""
echo "Please follow these steps:"
echo ""
echo "1. Go to https://supabase.com"
echo "2. Create a free account and project"
echo "3. Go to Settings → Database"
echo "4. Copy your connection string"
echo ""
echo "Enter your Supabase credentials below:"
echo ""

read -p "DB_HOST (e.g., aws-0-us-east-1.pooler.supabase.com): " DB_HOST
read -p "DB_PORT (default: 6543): " DB_PORT
DB_PORT=${DB_PORT:-6543}
read -p "DB_NAME (default: postgres): " DB_NAME
DB_NAME=${DB_NAME:-postgres}
read -p "DB_USER (default: postgres): " DB_USER
DB_USER=${DB_USER:-postgres}
read -s -p "DB_PASSWORD: " DB_PASSWORD
echo ""
read -p "JWT_SECRET (press Enter for random): " JWT_SECRET

if [ -z "$JWT_SECRET" ]; then
  JWT_SECRET=$(openssl rand -base64 32)
fi

echo ""
echo "Creating .env file..."

cat > .env << EOF
# Database Configuration
DB_HOST=$DB_HOST
DB_PORT=$DB_PORT
DB_NAME=$DB_NAME
DB_USER=$DB_USER
DB_PASSWORD=$DB_PASSWORD

# Server Configuration
PORT=3000
NODE_ENV=development

# JWT Configuration
JWT_SECRET=$JWT_SECRET
JWT_EXPIRES_IN=7d

# CORS Configuration
CORS_ORIGIN=http://localhost:8080
EOF

echo "✅ .env file created successfully!"
echo ""
echo "Next steps:"
echo "1. cd backend"
echo "2. npm run migrate"
echo "3. npm run seed"
echo "4. npm run dev"
echo ""
echo "Server will start on http://localhost:3000"
