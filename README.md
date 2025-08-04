# Think41 Interview Project

A React + Vite application with PostgreSQL database for analyzing user and order data.

## Prerequisites

- Node.js (v16 or higher)
- PostgreSQL (v12 or higher)
- npm or yarn

## Database Setup

### 1. Install PostgreSQL
Make sure PostgreSQL is installed and running on your system.

### 2. Create Database
```bash
# Connect to PostgreSQL as superuser
psql -U postgres

# Create the database
CREATE DATABASE think41_interview;

# Exit psql
\q
```

### 3. Configure Environment
```bash
# Copy the example environment file
cp .env.example .env

# Edit .env with your PostgreSQL credentials
# Update DB_PASSWORD with your PostgreSQL password
```

### 4. Install Dependencies
```bash
npm install
```

### 5. Setup Database and Load Data
```bash
# This will create tables, load CSV data, and run verification queries
npm run db:setup
```

## Database Schema

### Users Table
- `id` (INTEGER, PRIMARY KEY)
- `first_name` (VARCHAR)
- `last_name` (VARCHAR)
- `email` (VARCHAR, UNIQUE)
- `age` (INTEGER)
- `gender` (CHAR)
- `state` (VARCHAR)
- `street_address` (TEXT)
- `postal_code` (VARCHAR)
- `city` (VARCHAR)
- `country` (VARCHAR)
- `latitude` (DECIMAL)
- `longitude` (DECIMAL)
- `traffic_source` (VARCHAR)
- `created_at` (TIMESTAMP WITH TIME ZONE)

### Orders Table
- `order_id` (INTEGER, PRIMARY KEY)
- `user_id` (INTEGER, FOREIGN KEY)
- `status` (VARCHAR)
- `gender` (CHAR)
- `created_at` (TIMESTAMP WITH TIME ZONE)
- `returned_at` (TIMESTAMP WITH TIME ZONE)
- `shipped_at` (TIMESTAMP WITH TIME ZONE)
- `delivered_at` (TIMESTAMP WITH TIME ZONE)
- `num_of_item` (INTEGER)

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run db:setup` - Setup database and load data
- `npm run db:query` - Run verification queries

## Data Analysis Queries

The project includes several pre-built queries in `database/queries.sql`:

1. **Record Counts** - Total users and orders
2. **Data Integrity** - Verify foreign key relationships
3. **User Demographics** - Age and gender breakdown
4. **Order Status Analysis** - Distribution of order statuses
5. **Geographic Analysis** - Top countries by user count
6. **Traffic Source Analysis** - User acquisition channels
7. **Order Statistics** - Orders per user metrics
8. **Trend Analysis** - Monthly order patterns
9. **Top Users** - Users with most orders

## Project Structure

```
├── database/
│   ├── schema.sql      # Database schema definition
│   ├── load_data.sql   # Data loading scripts
│   ├── queries.sql     # Analysis queries
│   └── setup.js        # Automated setup script
├── src/
│   └── ...            # React application files
├── users.csv          # User data
├── orders.csv         # Order data
└── README.md
```

## Development

1. Start the database setup:
   ```bash
   npm run db:setup
   ```

2. Start the development server:
   ```bash
   npm run dev
   ```

3. Open http://localhost:5173 in your browser

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Check your `.env` file credentials
- Verify the database `think41_interview` exists

### Data Loading Issues
- Ensure CSV files are in the project root
- Check file permissions
- Verify CSV format matches expected schema

### Performance
- The setup script loads data in batches for better performance
- Indexes are created automatically for common query patterns