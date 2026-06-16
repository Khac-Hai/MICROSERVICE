-- E-Commerce Microservices Database Setup Script

-- ===================================
-- CUSTOMER DATABASE
-- ===================================

-- Create customer_db database
-- psql -U postgres -c "CREATE DATABASE customer_db;"

-- Create Customer table
CREATE TABLE IF NOT EXISTS customers (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on email for faster lookups
CREATE INDEX IF NOT EXISTS idx_customer_email ON customers(email);

-- Insert sample data
INSERT INTO customers (full_name, email, password, address)
VALUES
    ('Nguyen Van A', 'nguyenvana@example.com', 'pass123', '123 Nguyen Hue, HCM'),
    ('Tran Thi B', 'tranthib@example.com', 'pass456', '456 Le Loi, Hanoi'),
    ('Le Van C', 'levanc@example.com', 'pass789', '789 Pham Ngu Lao, HCMC')
ON CONFLICT (email) DO NOTHING;

-- ===================================
-- PRODUCT DATABASE
-- ===================================

-- Create product_db database
-- psql -U postgres -c "CREATE DATABASE product_db;"

-- Create Product table
CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    stock_quantity INTEGER NOT NULL DEFAULT 0,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on product name
CREATE INDEX IF NOT EXISTS idx_product_name ON products(name);

-- Insert sample data
INSERT INTO products (name, price, stock_quantity, description)
VALUES
    ('iPhone 14', 999.99, 50, 'Latest Apple smartphone with A16 Bionic chip'),
    ('Samsung 65 inch 4K TV', 799.99, 20, 'Ultra HD Smart TV with QLED technology'),
    ('Sony WH-1000XM5 Headphones', 399.99, 35, 'Premium noise-cancelling wireless headphones'),
    ('MacBook Pro 16', 2499.99, 15, 'Powerful laptop for professionals'),
    ('iPad Air', 599.99, 30, 'Versatile tablet with M1 chip')
ON CONFLICT DO NOTHING;

-- ===================================
-- ORDER DATABASE
-- ===================================

-- Create order_db database
-- psql -U postgres -c "CREATE DATABASE order_db;"

-- Create Order table
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for faster queries
CREATE INDEX IF NOT EXISTS idx_order_customer_id ON orders(customer_id);
CREATE INDEX IF NOT EXISTS idx_order_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_order_date ON orders(order_date);

-- Insert sample data
INSERT INTO orders (customer_id, total_amount, status)
VALUES
    (1, 1799.98, 'COMPLETED'),
    (1, 999.99, 'SHIPPED'),
    (2, 599.99, 'PENDING'),
    (3, 2499.99, 'SHIPPED')
ON CONFLICT DO NOTHING;

-- ===================================
-- VERIFICATION QUERIES
-- ===================================

-- Run these to verify data was created

-- Switch to customer_db
-- \c customer_db

-- List all customers
-- SELECT * FROM customers;

-- Switch to product_db
-- \c product_db

-- List all products
-- SELECT * FROM products;

-- Switch to order_db
-- \c order_db

-- List all orders
-- SELECT * FROM orders;

-- Get orders with customer reference (conceptual join)
-- SELECT o.id, o.customer_id, c.full_name, o.total_amount, o.status
-- FROM orders o
-- LEFT JOIN customers c ON o.customer_id = c.id;

-- ===================================
-- CLEANUP QUERIES
-- ===================================

-- Drop tables if needed (use with caution!)
-- DROP TABLE IF EXISTS customers CASCADE;
-- DROP TABLE IF EXISTS products CASCADE;
-- DROP TABLE IF EXISTS orders CASCADE;

-- Drop databases if needed (use with caution!)
-- DROP DATABASE IF EXISTS customer_db;
-- DROP DATABASE IF EXISTS product_db;
-- DROP DATABASE IF EXISTS order_db;

