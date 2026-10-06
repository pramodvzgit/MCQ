# Quick Setup with Supabase (Free Cloud PostgreSQL)

## Why Supabase?
- ✅ Free (500MB database)
- ✅ Setup in 2-3 minutes
- ✅ No installation required
- ✅ Works exactly like local PostgreSQL
- ✅ Access from anywhere

## Step 1: Create Supabase Account

1. Go to https://supabase.com
2. Click "Start your project"
3. Sign up with GitHub or email
4. Create a new organization (free tier)
5. Create a new project
   - Name: mcq-exam-app
   - Database password: (choose a strong password, save it!)
   - Region: Choose nearest to you
   - Click "Create new project"

## Step 2: Get Database Credentials

After creating the project (wait 1-2 minutes for it to initialize):

1. Go to Settings → Database
2. Scroll to "Connection string"
3. Copy the connection string
4. It will look like:
   ```
   postgresql://postgres.[PROJECT-REF]:[PASSWORD]@aws-0-[REGION].pooler.supabase.com:6543/postgres
   ```

5. Extract the values:
   - DB_HOST: `aws-0-[REGION].pooler.supabase.com` (or similar)
   - DB_PORT: `6543` (Supabase uses 6543, not 5432)
   - DB_NAME: `postgres`
   - DB_USER: `postgres`
   - DB_PASSWORD: `[YOUR-PASSWORD]`

## Step 3: Setup Backend

```bash
cd backend

# Create .env file with Supabase credentials
cat > .env << 'EOF'
DB_HOST=your-supabase-host.pooler.supabase.com
DB_PORT=6543
DB_NAME=postgres
DB_USER=postgres
DB_PASSWORD=your-supabase-password
PORT=3000
NODE_ENV=development
JWT_SECRET=your_super_secret_jwt_key_change_this
JWT_EXPIRES_IN=7d
CORS_ORIGIN=http://localhost:8080
EOF
```

Replace the values with your actual Supabase credentials.

## Step 4: Run Migrations

```bash
npm run migrate
```

## Step 5: Seed Database

```bash
npm run seed
```

## Step 6: Start Server

```bash
npm run dev
```

Server will start on http://localhost:3000

## Step 7: Test API

In another terminal:

```bash
./test-api.sh
```

Or manually test:

```bash
# Health check
curl http://localhost:3000/health

# Get exams
curl http://localhost:3000/api/exams
```

## Troubleshooting

**Connection failed:**
- Verify Supabase project is fully initialized (wait 2-3 minutes after creation)
- Check credentials in .env
- Make sure DB_PORT is 6543 (Supabase's port)

**Migration errors:**
- Make sure you're using the direct connection (not pooler) for migrations
- Some Supabase projects require direct connection for DDL operations

**Alternative: Use direct connection string**
If pooler doesn't work, use direct connection:
- DB_HOST: `db.[PROJECT-REF].supabase.co`
- DB_PORT: `5432`

## Next Steps

After backend is running:
1. Configure Android app to use http://10.0.2.2:3000/api/
2. Run Android app in emulator
3. Test the complete flow
