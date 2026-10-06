# Quick Start with Supabase

## Option 1: Automated Setup (Recommended)

Run the interactive setup script:

```bash
cd backend
./setup-supabase.sh
```

This will prompt you for your Supabase credentials and create the `.env` file automatically.

## Option 2: Manual Setup

### Step 1: Get Supabase Credentials

1. Go to https://supabase.com
2. Create account and project
3. Go to Settings → Database
4. Copy connection string

### Step 2: Create .env File

```bash
cd backend
```

Create `.env` file with your credentials:

```env
DB_HOST=your-host.pooler.supabase.com
DB_PORT=6543
DB_NAME=postgres
DB_USER=postgres
DB_PASSWORD=your-password
PORT=3000
NODE_ENV=development
JWT_SECRET=your-jwt-secret
JWT_EXPIRES_IN=7d
CORS_ORIGIN=http://localhost:8080
```

### Step 3: Run Migrations

```bash
npm run migrate
```

### Step 4: Seed Database

```bash
npm run seed
```

### Step 5: Start Server

```bash
npm run dev
```

Server will be available at http://localhost:3000

### Step 6: Test

```bash
curl http://localhost:3000/health
```

## Troubleshooting

**Connection Error:**
- Ensure Supabase project is fully initialized (wait 2-3 minutes)
- Try using direct connection instead of pooler:
  - Change DB_HOST to `db.[PROJECT-REF].supabase.co`
  - Change DB_PORT to `5432`

**Migration Error:**
- Use direct connection for migrations (DB_PORT=5432)
- Then switch to pooler for runtime (DB_PORT=6543)

**Need Help?**
Check SUPABASE_SETUP.md for detailed instructions.
