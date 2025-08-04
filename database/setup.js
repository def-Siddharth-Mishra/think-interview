import { Pool } from 'pg';
import fs from 'fs';
import path from 'path';
import csv from 'csv-parser';
import { fileURLToPath } from 'url';
import dotenv from 'dotenv';

// Load environment variables
dotenv.config();

// Get __dirname equivalent in ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Database configuration
const pool = new Pool({
  user: process.env.DB_USER || 'postgres',
  host: process.env.DB_HOST || 'localhost',
  database: process.env.DB_NAME || 'think41_interview',
  password: process.env.DB_PASSWORD || 'password',
  port: process.env.DB_PORT || 5432,
});

async function createTables() {
  const client = await pool.connect();
  try {
    console.log('Dropping existing tables...');
    
    // Drop tables if they exist (in correct order due to foreign keys)
    await client.query('DROP TABLE IF EXISTS orders CASCADE');
    await client.query('DROP TABLE IF EXISTS users CASCADE');
    
    console.log('Creating tables...');
    
    // Create users table
    await client.query(`
      CREATE TABLE users (
        id INTEGER PRIMARY KEY,
        first_name VARCHAR(100) NOT NULL,
        last_name VARCHAR(100) NOT NULL,
        email VARCHAR(255) NOT NULL,
        age INTEGER CHECK (age >= 0 AND age <= 150),
        gender CHAR(1) CHECK (gender IN ('M', 'F')),
        state VARCHAR(100),
        street_address TEXT,
        postal_code VARCHAR(20),
        city VARCHAR(100),
        country VARCHAR(100),
        latitude DECIMAL(10, 8),
        longitude DECIMAL(11, 8),
        traffic_source VARCHAR(50),
        created_at TIMESTAMP WITH TIME ZONE NOT NULL
      )
    `);

    // Create orders table
    await client.query(`
      CREATE TABLE orders (
        order_id INTEGER PRIMARY KEY,
        user_id INTEGER NOT NULL,
        status VARCHAR(50) NOT NULL,
        gender CHAR(1) CHECK (gender IN ('M', 'F')),
        created_at TIMESTAMP WITH TIME ZONE NOT NULL,
        returned_at TIMESTAMP WITH TIME ZONE,
        shipped_at TIMESTAMP WITH TIME ZONE,
        delivered_at TIMESTAMP WITH TIME ZONE,
        num_of_item INTEGER CHECK (num_of_item > 0),
        FOREIGN KEY (user_id) REFERENCES users(id)
      )
    `);

    // Create indexes
    await client.query('CREATE INDEX idx_users_email ON users(email)');
    await client.query('CREATE INDEX idx_users_state ON users(state)');
    await client.query('CREATE INDEX idx_users_country ON users(country)');
    await client.query('CREATE INDEX idx_users_created_at ON users(created_at)');
    await client.query('CREATE INDEX idx_orders_user_id ON orders(user_id)');
    await client.query('CREATE INDEX idx_orders_status ON orders(status)');
    await client.query('CREATE INDEX idx_orders_created_at ON orders(created_at)');

    console.log('Tables created successfully!');
  } catch (error) {
    console.error('Error creating tables:', error);
    throw error;
  } finally {
    client.release();
  }
}

async function loadUsersData() {
  const client = await pool.connect();
  try {
    console.log('Loading users data...');
    
    const users = [];
    return new Promise((resolve, reject) => {
      fs.createReadStream(path.join(__dirname, '..', 'users.csv'))
        .pipe(csv())
        .on('data', (row) => {
          users.push([
            parseInt(row.id),
            row.first_name,
            row.last_name,
            row.email,
            parseInt(row.age) || null,
            row.gender || null,
            row.state || null,
            row.street_address || null,
            row.postal_code || null,
            row.city || null,
            row.country || null,
            parseFloat(row.latitude) || null,
            parseFloat(row.longitude) || null,
            row.traffic_source || null,
            row.created_at
          ]);
        })
        .on('end', async () => {
          try {
            // Insert users in batches
            const batchSize = 1000;
            for (let i = 0; i < users.length; i += batchSize) {
              const batch = users.slice(i, i + batchSize);
              const values = batch.map((_, index) => 
                `($${index * 15 + 1}, $${index * 15 + 2}, $${index * 15 + 3}, $${index * 15 + 4}, $${index * 15 + 5}, $${index * 15 + 6}, $${index * 15 + 7}, $${index * 15 + 8}, $${index * 15 + 9}, $${index * 15 + 10}, $${index * 15 + 11}, $${index * 15 + 12}, $${index * 15 + 13}, $${index * 15 + 14}, $${index * 15 + 15})`
              ).join(', ');
              
              const query = `
                INSERT INTO users (id, first_name, last_name, email, age, gender, state, street_address, postal_code, city, country, latitude, longitude, traffic_source, created_at)
                VALUES ${values}
                ON CONFLICT (id) DO NOTHING
              `;
              
              await client.query(query, batch.flat());
            }
            
            console.log(`Loaded ${users.length} users successfully!`);
            resolve();
          } catch (error) {
            reject(error);
          }
        })
        .on('error', reject);
    });
  } catch (error) {
    console.error('Error loading users data:', error);
    throw error;
  } finally {
    client.release();
  }
}

async function loadOrdersData() {
  const client = await pool.connect();
  try {
    console.log('Loading orders data...');
    
    const orders = [];
    return new Promise((resolve, reject) => {
      fs.createReadStream(path.join(__dirname, '..', 'orders.csv'))
        .pipe(csv())
        .on('data', (row) => {
          orders.push([
            parseInt(row.order_id),
            parseInt(row.user_id),
            row.status,
            row.gender || null,
            row.created_at,
            row.returned_at || null,
            row.shipped_at || null,
            row.delivered_at || null,
            parseInt(row.num_of_item) || null
          ]);
        })
        .on('end', async () => {
          try {
            // Insert orders in batches
            const batchSize = 1000;
            for (let i = 0; i < orders.length; i += batchSize) {
              const batch = orders.slice(i, i + batchSize);
              const values = batch.map((_, index) => 
                `($${index * 9 + 1}, $${index * 9 + 2}, $${index * 9 + 3}, $${index * 9 + 4}, $${index * 9 + 5}, $${index * 9 + 6}, $${index * 9 + 7}, $${index * 9 + 8}, $${index * 9 + 9})`
              ).join(', ');
              
              const query = `
                INSERT INTO orders (order_id, user_id, status, gender, created_at, returned_at, shipped_at, delivered_at, num_of_item)
                VALUES ${values}
                ON CONFLICT (order_id) DO NOTHING
              `;
              
              await client.query(query, batch.flat());
            }
            
            console.log(`Loaded ${orders.length} orders successfully!`);
            resolve();
          } catch (error) {
            reject(error);
          }
        })
        .on('error', reject);
    });
  } catch (error) {
    console.error('Error loading orders data:', error);
    throw error;
  } finally {
    client.release();
  }
}

async function runVerificationQueries() {
  const client = await pool.connect();
  try {
    console.log('\n=== Database Verification ===');
    
    // Count records
    const countResult = await client.query(`
      SELECT 'Users' as table_name, COUNT(*) as total_records FROM users
      UNION ALL
      SELECT 'Orders' as table_name, COUNT(*) as total_records FROM orders
    `);
    console.log('\nRecord counts:');
    countResult.rows.forEach(row => {
      console.log(`${row.table_name}: ${row.total_records}`);
    });

    // Check data integrity
    const integrityResult = await client.query(`
      SELECT 
        COUNT(*) as total_orders,
        COUNT(CASE WHEN u.id IS NOT NULL THEN 1 END) as orders_with_valid_users,
        COUNT(CASE WHEN u.id IS NULL THEN 1 END) as orders_with_invalid_users
      FROM orders o
      LEFT JOIN users u ON o.user_id = u.id
    `);
    console.log('\nData integrity check:');
    console.log(integrityResult.rows[0]);

    // Order status breakdown
    const statusResult = await client.query(`
      SELECT 
        status,
        COUNT(*) as order_count,
        ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM orders), 2) as percentage
      FROM orders
      GROUP BY status
      ORDER BY order_count DESC
    `);
    console.log('\nOrder status breakdown:');
    statusResult.rows.forEach(row => {
      console.log(`${row.status}: ${row.order_count} (${row.percentage}%)`);
    });

  } catch (error) {
    console.error('Error running verification queries:', error);
    throw error;
  } finally {
    client.release();
  }
}

async function setupDatabase() {
  try {
    await createTables();
    await loadUsersData();
    await loadOrdersData();
    await runVerificationQueries();
    console.log('\n✅ Database setup completed successfully!');
  } catch (error) {
    console.error('❌ Database setup failed:', error);
    process.exit(1);
  } finally {
    await pool.end();
  }
}

// Run setup if this file is executed directly
if (import.meta.url === `file://${process.argv[1]}`) {
  setupDatabase();
}

export { pool, setupDatabase };