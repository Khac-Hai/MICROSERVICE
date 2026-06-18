# 🚀 Quick Start Guide - Microservices với Eureka

## Tùy chọn 1: Sử dụng Docker (Khuyến nghị)

### Bước 1: Khởi động PostgreSQL với Docker

```bash
docker-compose up -d
```

Kiểm tra:
```bash
docker-compose ps
```

### Bước 2: Build toàn bộ dự án

```bash
mvn clean install
```

### Bước 3: Khởi động các Service

Mở 4 terminal riêng biệt và chạy:

**Terminal 1 - Eureka Server:**
```bash
cd eureka-server
mvn spring-boot:run
```

**Terminal 2 - Customer Service:**
```bash
cd customer-service
mvn spring-boot:run
```

**Terminal 3 - Product Service:**
```bash
cd product-service
mvn spring-boot:run
```

**Terminal 4 - Order Service:**
```bash
cd order-service
mvn spring-boot:run
```

## Tùy chọn 2: Script tự động (Windows)

```bash
start-all-services.bat
```

## Tùy chọn 3: Script tự động (Linux/Mac)

```bash
chmod +x start-all-services.sh
./start-all-services.sh
```

## ✅ Kiểm tra kết quả

### 1. Eureka Dashboard
Truy cập: **http://localhost:8761**

Bạn sẽ thấy:
```
CUSTOMER-SERVICE    UP
PRODUCT-SERVICE     UP
ORDER-SERVICE       UP
```

### 2. Test API

**Tạo Customer:**
```bash
curl -X POST http://localhost:8081/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@test.com","phone":"123","address":"123 St"}'
```

**Lấy danh sách Customer:**
```bash
curl http://localhost:8081/api/customers
```

## 🔧 Cấu hình

### PostgreSQL
- Host: localhost:5432
- Username: postgres
- Password: 123456
- Databases: customer_db, product_db, order_db

### Services
```
Eureka Server:    http://localhost:8761
Customer Service: http://localhost:8081
Product Service:  http://localhost:8082
Order Service:    http://localhost:8083
```

## 🐛 Troubleshooting

### Service không kết nối Eureka
- Đảm bảo Eureka Server đang chạy
- Kiểm tra cấu hình eureka.client.service-url.defaultZone

### Port đã bị dùng
```bash
# Windows
netstat -ano | findstr :8081

# Linux/Mac
lsof -i :8081
```

### Database kết nối thất bại
- Kiểm tra PostgreSQL đang chạy
- Nếu dùng Docker: `docker-compose ps`
- Kiểm tra credentials trong application.properties

## 📁 Cấu trúc File

```
bai2/
├── parent pom.xml
├── eureka-server/
│   ├── pom.xml
│   └── src/main/
│       ├── java/.../EurekaServerApplication.java
│       └── resources/application.properties
├── customer-service/
│   ├── pom.xml
│   └── src/main/
│       ├── java/.../controller/CustomerController.java
│       ├── java/.../entity/Customer.java
│       ├── java/.../repository/CustomerRepository.java
│       └── resources/application.properties
├── product-service/
│   └── (tương tự)
├── order-service/
│   └── (tương tự)
├── docker-compose.yml
├── init-databases.sql
├── SETUP.md
└── README.md
```

## 📚 Tài liệu đính kèm

- **README.md** - Tài liệu chi tiết (Vietnamese)
- **SETUP.md** - Hướng dẫn cài đặt chi tiết
- **Tệp này** - Quick Start Guide

## 🎯 Bước tiếp theo

✅ Sau khi hoàn thành, bạn có thể học:
- Service-to-Service Communication (OpenFeign/RestTemplate)
- API Gateway Pattern
- Circuit Breaker Pattern
- Distributed Tracing
- Config Server

---

**Chúc bạn thành công!** 🎉

