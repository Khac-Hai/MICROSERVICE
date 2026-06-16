# E-Commerce Microservices Setup Guide

## Overview
Đây là một hệ thống E-Commerce được chia thành 3 Microservices độc lập:
- **Customer-Service** (Port: 8081)
- **Product-Service** (Port: 8082)
- **Order-Service** (Port: 8083)

## Prerequisites

### Required Software
- Java 17 hoặc mới hơn
- Maven 3.6.0 hoặc mới hơn
- PostgreSQL 12 hoặc mới hơn
- Git

### Installation

#### 1. Java 17
```bash
# Check Java version
java -version

# Should show: openjdk version "17.x.x" or similar
```

#### 2. Maven
```bash
# Check Maven version
mvn -version

# Should show: Apache Maven 3.6.0 or newer
```

#### 3. PostgreSQL
```bash
# Install PostgreSQL 12+
# Download from: https://www.postgresql.org/download/
```

## Database Setup

### Create Databases
```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create databases
CREATE DATABASE customer_db;
CREATE DATABASE product_db;
CREATE DATABASE order_db;

-- Verify
\l
```

### Database Connection Details
- **Host**: localhost
- **Port**: 5432
- **Username**: postgres
- **Password**: password (hoặc password của bạn)

> ⚠️ **Note**: Nếu password khác, cập nhật `application.properties` trong từng service

## Project Structure

```
bai1/
├── customer-service/
│   ├── src/main/java/re/edu/customerservice/
│   │   ├── entity/Customer.java
│   │   ├── repository/CustomerRepository.java
│   │   ├── service/CustomerService.java
│   │   ├── controller/CustomerController.java
│   │   └── CustomerServiceApplication.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
│
├── product-service/
│   ├── src/main/java/re/edu/productservice/
│   │   ├── entity/Product.java
│   │   ├── repository/ProductRepository.java
│   │   ├── service/ProductService.java
│   │   ├── controller/ProductController.java
│   │   └── ProductServiceApplication.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
│
├── order-service/
│   ├── src/main/java/re/edu/orderservice/
│   │   ├── entity/Order.java
│   │   ├── repository/OrderRepository.java
│   │   ├── service/OrderService.java
│   │   ├── controller/OrderController.java
│   │   └── OrderServiceApplication.java
│   ├── src/main/resources/application.properties
│   └── pom.xml
│
└── README.md
```

## Building the Services

### Build Customer-Service
```bash
cd customer-service
mvn clean install
```

### Build Product-Service
```bash
cd product-service
mvn clean install
```

### Build Order-Service
```bash
cd order-service
mvn clean install
```

## Running the Services

### Option 1: Run from IDE (IntelliJ IDEA, Eclipse, VSCode)
1. Mở project từng service
2. Right-click trên file `*ServiceApplication.java`
3. Chọn "Run"

### Option 2: Run from Terminal

#### Terminal 1 - Customer Service
```bash
cd customer-service
mvn spring-boot:run
```

#### Terminal 2 - Product Service
```bash
cd product-service
mvn spring-boot:run
```

#### Terminal 3 - Order Service
```bash
cd order-service
mvn spring-boot:run
```

### Verify Services are Running
```bash
# Customer Service
curl http://localhost:8081/api/customers

# Product Service
curl http://localhost:8082/api/products

# Order Service
curl http://localhost:8083/api/orders
```

## API Testing

### Using cURL

#### Customer Service Examples
```bash
# Create a customer
curl -X POST http://localhost:8081/api/customers/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "address": "123 Main St"
  }'

# Get all customers
curl http://localhost:8081/api/customers

# Get customer by ID
curl http://localhost:8081/api/customers/1
```

#### Product Service Examples
```bash
# Create a product
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "price": 999.99,
    "stockQuantity": 10,
    "description": "High-performance laptop"
  }'

# Get all products
curl http://localhost:8082/api/products

# Update stock
curl -X PATCH "http://localhost:8082/api/products/1/stock?quantity=8"
```

#### Order Service Examples
```bash
# Create an order
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "totalAmount": 999.99,
    "status": "PENDING"
  }'

# Get orders by customer
curl http://localhost:8083/api/orders/customer/1

# Update order status
curl -X PATCH "http://localhost:8083/api/orders/1/status?status=COMPLETED"
```

### Using Postman
1. Import requests từ từng service API
2. Set base URLs:
   - Customer: `http://localhost:8081`
   - Product: `http://localhost:8082`
   - Order: `http://localhost:8083`

## Architecture Principles

### Service Independence
- Mỗi service có source code riêng
- Mỗi service có database riêng
- Mỗi service có vòng đời triển khai riêng

### Loose Coupling
- Order-Service chỉ lưu `customerId` (Long)
- Không import Entity từ service khác
- Không chia sẻ database

### Domain-Driven Design (DDD)
- Mỗi service quản lý một Bounded Context
- Entity riêng biệt cho từng domain
- Business logic tách biệt rõ ràng

## Troubleshooting

### Port Already in Use
```bash
# Kill process using port 8081
# Windows
netstat -ano | findstr :8081
taskkill /PID <process_id> /F

# macOS/Linux
lsof -i :8081
kill -9 <process_id>
```

### Database Connection Error
1. Kiểm tra PostgreSQL đang chạy
2. Kiểm tra credentials trong `application.properties`
3. Kiểm tra database exists:
   ```sql
   psql -U postgres -c "\l"
   ```

### Build Error
```bash
# Clean Maven cache
mvn clean
rm -rf ~/.m2/repository

# Rebuild
mvn install
```

## Next Steps

1. ✅ Thiết lập hệ thống microservices
2. 📚 Tìm hiểu Service-to-Service Communication
3. 🔐 Thêm Spring Security
4. 🐳 Containerization với Docker
5. 🎯 Orchestration với Kubernetes
6. 📝 API Documentation với Swagger/SpringFox

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Microservices Architecture](https://martinfowler.com/microservices/)

## Support

For issues or questions, please refer to individual service README.md files.

