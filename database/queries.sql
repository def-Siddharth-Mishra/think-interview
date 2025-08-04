-- Verification and analysis queries for the database

-- 1. Count total users and orders
SELECT 'Users' as table_name, COUNT(*) as total_records FROM users
UNION ALL
SELECT 'Orders' as table_name, COUNT(*) as total_records FROM orders;

-- 2. Check data integrity - orders with valid user references
SELECT 
    COUNT(*) as total_orders,
    COUNT(CASE WHEN u.id IS NOT NULL THEN 1 END) as orders_with_valid_users,
    COUNT(CASE WHEN u.id IS NULL THEN 1 END) as orders_with_invalid_users
FROM orders o
LEFT JOIN users u ON o.user_id = u.id;

-- 3. User demographics breakdown
SELECT 
    gender,
    COUNT(*) as count,
    ROUND(AVG(age), 2) as avg_age,
    MIN(age) as min_age,
    MAX(age) as max_age
FROM users 
WHERE gender IS NOT NULL
GROUP BY gender;

-- 4. Orders by status
SELECT 
    status,
    COUNT(*) as order_count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM orders), 2) as percentage
FROM orders
GROUP BY status
ORDER BY order_count DESC;

-- 5. Top 10 countries by user count
SELECT 
    country,
    COUNT(*) as user_count
FROM users
WHERE country IS NOT NULL
GROUP BY country
ORDER BY user_count DESC
LIMIT 10;

-- 6. Traffic source analysis
SELECT 
    traffic_source,
    COUNT(*) as user_count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM users), 2) as percentage
FROM users
WHERE traffic_source IS NOT NULL
GROUP BY traffic_source
ORDER BY user_count DESC;

-- 7. Orders per user statistics
SELECT 
    AVG(order_count) as avg_orders_per_user,
    MIN(order_count) as min_orders_per_user,
    MAX(order_count) as max_orders_per_user
FROM (
    SELECT user_id, COUNT(*) as order_count
    FROM orders
    GROUP BY user_id
) user_orders;

-- 8. Monthly order trends
SELECT 
    DATE_TRUNC('month', created_at) as month,
    COUNT(*) as order_count
FROM orders
GROUP BY DATE_TRUNC('month', created_at)
ORDER BY month;

-- 9. Users with most orders
SELECT 
    u.id,
    u.first_name,
    u.last_name,
    u.email,
    COUNT(o.order_id) as total_orders
FROM users u
JOIN orders o ON u.id = o.user_id
GROUP BY u.id, u.first_name, u.last_name, u.email
ORDER BY total_orders DESC
LIMIT 10;