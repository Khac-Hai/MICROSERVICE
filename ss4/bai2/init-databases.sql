-- Create databases for microservices
CREATE DATABASE customer_db WITH ENCODING 'UTF8';
CREATE DATABASE product_db WITH ENCODING 'UTF8';
CREATE DATABASE order_db WITH ENCODING 'UTF8';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE customer_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE product_db TO postgres;
GRANT ALL PRIVILEGES ON DATABASE order_db TO postgres;

