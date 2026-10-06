#!/bin/bash

cat > .env << 'EOF'
# Database Connection String (using pooler port 6543)
DATABASE_URL=postgresql://postgres:Prabha1234pra@db.tyvudtbehfbjjjhqkvfz.supabase.co:6543/postgres

# Alternative: Individual config (using pooler)
DB_HOST=db.tyvudtbehfbjjjhqkvfz.supabase.co
DB_PORT=6543
DB_NAME=postgres
DB_USER=postgres
DB_PASSWORD=Prabha1234pra

# Server Configuration
PORT=3000
NODE_ENV=development

# JWT Configuration
JWT_SECRET=mcq_exam_jwt_secret_key_2024_secure_change_in_production
JWT_EXPIRES_IN=7d

# CORS Configuration
CORS_ORIGIN=http://localhost:8080
EOF

echo "✅ .env file created with pooler port 6543!"
