# Hướng dẫn Cài đặt và Chạy Hệ thống Microservices

## Yêu cầu

- Java 21 trở lên
- Maven 3.8.1 trở lên
- PostgreSQL 12 trở lên
- Git

## Cấu trúc Dự án

```
bai2/
├── pom.xml (Parent POM)
├── eureka-server/
├── customer-service/
├── product-service/
└── order-service/
```

## Bước 1: Chuẩn bị Cơ sở dữ liệu PostgreSQL

Tạo 3 cơ sở dữ liệu cho 3 service:

```sql
-- Kết nối với PostgreSQL
psql -U postgres

-- Tạo Database
CREATE DATABASE customer_db;
CREATE DATABASE product_db;
CREATE DATABASE order_db;

-- Kiểm tra
\l
```

## Bước 2: Build Dự án

Từ thư mục gốc `bai2`, chạy:

```bash
mvn clean install
```

Lệnh này sẽ build tất cả các module con.

## Bước 3: Khởi động các Service theo thứ tự

### 3.1 Eureka Server (Bắt buộc chạy trước)

```bash
# Từ thư mục eureka-server
mvn spring-boot:run
# Hoặc
java -jar target/eureka-server-1.0.jar
```

**Xác nhận**: Truy cập http://localhost:8761
- Phải thấy Eureka Dashboard

### 3.2 Customer Service

```bash
# Từ thư mục customer-service
mvn spring-boot:run
```

**Xác nhận**: 
- http://localhost:8081/api/customers (phải trả về dữ liệu)
- Kiểm tra Eureka Dashboard: http://localhost:8761

### 3.3 Product Service

```bash
# Từ thư mục product-service
mvn spring-boot:run
```

**Xác nhận**: 
- http://localhost:8082/api/products (phải trả về dữ liệu)
- Kiểm tra Eureka Dashboard: http://localhost:8761

### 3.4 Order Service

```bash
# Từ thư mục order-service
mvn spring-boot:run
```

**Xác nhận**: 
- http://localhost:8083/api/orders (phải trả về dữ liệu)
- Kiểm tra Eureka Dashboard: http://localhost:8761

## Bước 4: Kiểm tra Eureka Dashboard

Truy cập: http://localhost:8761

Bạn phải thấy:

```
Application              Status
CUSTOMER-SERVICE         UP
PRODUCT-SERVICE          UP
ORDER-SERVICE            UP
```

## API Endpoints

### Customer Service (Port 8081)
```
GET    /api/customers           - Lấy tất cả khách hàng
GET    /api/customers/{id}      - Lấy khách hàng theo ID
POST   /api/customers           - Tạo khách hàng mới
PUT    /api/customers/{id}      - Cập nhật khách hàng
DELETE /api/customers/{id}      - Xóa khách hàng
```

### Product Service (Port 8082)
```
GET    /api/products            - Lấy tất cả sản phẩm
GET    /api/products/{id}       - Lấy sản phẩm theo ID
POST   /api/products            - Tạo sản phẩm mới
PUT    /api/products/{id}       - Cập nhật sản phẩm
DELETE /api/products/{id}       - Xóa sản phẩm
```

### Order Service (Port 8083)
```
GET    /api/orders              - Lấy tất cả đơn hàng
GET    /api/orders/{id}         - Lấy đơn hàng theo ID
POST   /api/orders              - Tạo đơn hàng mới
PUT    /api/orders/{id}         - Cập nhật đơn hàng
DELETE /api/orders/{id}         - Xóa đơn hàng
```

## Test API

### Tạo Customer

```bash
curl -X POST http://localhost:8081/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890",
    "address": "123 Main St"
  }'
```

### Tạo Product

```bash
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1000.00,
    "quantity": 50,
    "category": "Electronics"
  }'
```

### Tạo Order

```bash
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "productId": 1,
    "quantity": 2,
    "totalPrice": 2000.00,
    "status": "PENDING",
    "orderDate": "2024-06-18T10:00:00",
    "notes": "Giao hàng nhanh"
  }'
```

## Xử lý sự cố

### Service không đăng ký với Eureka
- Kiểm tra property: `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/`
- Kiểm tra Eureka Server đang chạy

### Lỗi kết nối Database
- Kiểm tra PostgreSQL đang chạy
- Kiểm tra thông tin kết nối trong application.properties
- Kiểm tra database đã được tạo

### Port đã bị sử dụng
- Thay đổi port trong application.properties
- Hoặc kill tiến trình đang dùng port

## Tiếp theo

Sau khi hoàn thành, bạn có thể:
- Thêm OpenFeign để gọi Service-to-Service Communication
- Thêm API Gateway để route requests
- Thêm Config Server cho centralized configuration
- Thêm Circuit Breaker để handle failures

## Tài liệu tham khảo

- Spring Cloud Eureka: https://spring.io/projects/spring-cloud-netflix
- Spring Cloud Documentation: https://spring.io/projects/spring-cloud
- Spring Boot: https://spring.io/projects/spring-boot

