#!/bin/bash

# Setup script for testing the backend
# This creates the .env file for local testing

cat > backend/.env << 'EOF'
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=mcq_exam_db
DB_USER=postgres
DB_PASSWORD=postgres

# Server Configuration
PORT=3000
NODE_ENV=development

# JWT Configuration
JWT_SECRET=super_secret_jwt_key_for_testing_change_in_production
JWT_EXPIRES_IN=7d

# CORS Configuration
CORS_ORIGIN=http://localhost:8080
EOF

echo "Created .env file in backend directory"
echo "Please update the database credentials if needed:"
echo "  DB_USER=your_postgres_user"
echo "  DB_PASSWORD=your_postgres_password"
echo "  DB_NAME=mcq_exam_db"
