-- Load data from CSV files into PostgreSQL tables
\c think41_interview;

-- Load users data
COPY users(id, first_name, last_name, email, age, gender, state, street_address, postal_code, city, country, latitude, longitude, traffic_source, created_at)
FROM '/path/to/your/project/users.csv'
DELIMITER ','
CSV HEADER;

-- Load orders data
COPY orders(order_id, user_id, status, gender, created_at, returned_at, shipped_at, delivered_at, num_of_item)
FROM '/path/to/your/project/orders.csv'
DELIMITER ','
CSV HEADER;