# Hướng dẫn Chạy Dự án Client-Side Load Balancing

## 📋 Chuẩn bị

### Yêu cầu hệ thống

- **Java 17+**
- **Maven 3.6+**
- **PostgreSQL** (cho Product Service)
- **IntelliJ IDEA** hoặc editor khác hỗ trợ Java
- **Postman** hoặc **curl** để test API

---

## 🗄️ Cấu trúc dự án

```
bai4/
├── eureka-server/          # Service Discovery Server (Port 8761)
├── product-service/        # Product Microservice (Port 8082, 8084, 9090)
├── order-service/          # Order Microservice với Load Balancing (Port 8080)
└── README.md              # Tài liệu bài tập
```

---

## 1️⃣ Cài đặt PostgreSQL

### Windows

Cài đặt PostgreSQL từ: https://www.postgresql.org/download/windows/

### Tạo Database

Mở **pgAdmin** hoặc command line:

```sql
CREATE DATABASE product_db;
```

**Thông tin kết nối:**
- Host: `localhost`
- Port: `5432`
- Username: `postgres`
- Password: `123456`
- Database: `product_db`

---

## 2️⃣ Chạy Eureka Server

### Mở Terminal

```bash
cd bai4\eureka-server
mvn clean install
mvn spring-boot:run
```

**Kết quả:**
```
Tomcat started on port(s): 8761
```

**Truy cập Dashboard:** http://localhost:8761

---

## 3️⃣ Chạy Product Service

### Lần 1 - Instance 1 (Port 8082)

Mở terminal mới:

```bash
cd bai4\product-service
mvn clean install
mvn spring-boot:run
```

**Kết quả:**
```
Tomcat started on port(s): 8082
PRODUCT-SERVICE is UP
```

---

### Lần 2 - Instance 2 (Port 8084)

#### Cách 1: Dùng Maven (Recommended)

Mở terminal mới:

```bash
cd bai4\product-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8084"
```

#### Cách 2: Dùng IntelliJ IDEA

1. **Run** → **Edit Configurations**
2. Chọn cấu hình hiện tại của Product Service
3. Tích chọn **Allow multiple instances**
4. **Program arguments:** `--server.port=8084`
5. Nhấn **OK**
6. Chạy lại (Shift + F10 hay nhấn Play)

**Kết quả:**
```
Tomcat started on port(s): 8084
PRODUCT-SERVICE is UP
```

---

### Lần 3 - Instance 3 (Port 9090) [Optional]

```bash
cd bai4\product-service
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

---

## 4️⃣ Chạy Order Service

Mở terminal mới:

```bash
cd bai4\order-service
mvn clean install
mvn spring-boot:run
```

**Kết quả:**
```
Tomcat started on port(s): 8080
ORDER-SERVICE is UP
```

---

## 5️⃣ Kiểm tra Eureka Dashboard

Truy cập: **http://localhost:8761**

**Kì vọng:**
- `PRODUCT-SERVICE` với 2 hoặc 3 instances (8082, 8084, 9090)
- `ORDER-SERVICE` với 1 instance (8080)

---

## 6️⃣ Khởi tạo dữ liệu Product

### API: Tạo Product

**POST** `http://localhost:8082/api/v1/products`

**Body (JSON):**
```json
{
  "name": "IPhone 17",
  "price": 999.99,
  "quantity": 100,
  "description": "Latest iPhone model"
}
```

**Hoặc tạo nhiều product:**

```json
{
  "name": "Samsung Galaxy S24",
  "price": 899.99,
  "quantity": 50,
  "description": "Android flagship"
}
```

```json
{
  "name": "MacBook Pro M4",
  "price": 1999.99,
  "quantity": 30,
  "description": "Performance laptop"
}
```

---

## 7️⃣ Kiểm tra Load Balancing

### Phương pháp 1: Gọi Product Service nhiều lần

**GET** `http://localhost:8082/api/v1/products/1`

Chạy API này 3-4 lần và quan sát **Port** trong response:

**Response từ Port 8082:**
```json
{
  "id": 1,
  "name": "IPhone 17 - Port 8082",
  "price": 999.99,
  "quantity": 100,
  "description": "Latest iPhone model"
}
```

**Response từ Port 8084:**
```json
{
  "id": 1,
  "name": "IPhone 17 - Port 8084",
  "price": 999.99,
  "quantity": 100,
  "description": "Latest iPhone model"
}
```

---

### Phương pháp 2: Gọi từ Order Service

Order Service sử dụng **@LoadBalanced RestTemplate** để gọi Product Service.

**GET** `http://localhost:8080/api/v1/orders/check-product/1`

Chạy nhiều lần và quan sát **Port** trong response:

**Lần 1:**
```json
{
  "id": 1,
  "name": "IPhone 17 - Port 8082",
  "price": 999.99,
  ...
}
```

**Lần 2:**
```json
{
  "id": 1,
  "name": "IPhone 17 - Port 8084",
  "price": 999.99,
  ...
}
```

**Lần 3:**
```json
{
  "id": 1,
  "name": "IPhone 17 - Port 8082",
  "price": 999.99,
  ...
}
```

✅ **Điều này chứng minh Load Balancing hoạt động!**

---

## 8️⃣ Tạo Order

**POST** `http://localhost:8080/api/v1/orders?productId=1&quantity=5`

**Response:**
```json
{
  "id": 1,
  "productId": 1,
  "quantity": 5,
  "totalPrice": 4999.95,
  "orderDate": "2026-06-18T10:30:00",
  "status": "SUCCESS",
  "productName": "IPhone 17 - Port 8084"
}
```

---

## 9️⃣ Các API chính

### Product Service

```
GET    http://localhost:8082/api/v1/products         (Lấy tất cả)
GET    http://localhost:8082/api/v1/products/1       (Lấy 1 product)
POST   http://localhost:8082/api/v1/products         (Tạo)
PUT    http://localhost:8082/api/v1/products/1       (Cập nhật)
DELETE http://localhost:8082/api/v1/products/1       (Xóa)
```

### Order Service

```
GET    http://localhost:8080/api/v1/orders           (Lấy tất cả orders)
GET    http://localhost:8080/api/v1/orders/1         (Lấy 1 order)
POST   http://localhost:8080/api/v1/orders?productId=1&quantity=5  (Tạo order)
GET    http://localhost:8080/api/v1/orders/check-product/1  (Kiểm tra load balancing)
```

---

## 🔟 Điểm quan trọng

### @LoadBalanced RestTemplate

**Order Service sử dụng:**
```java
String url = "http://PRODUCT-SERVICE/api/v1/products/" + productId;
return restTemplate.getForObject(url, ProductDTO.class);
```

**Thay vì:**
```java
// ❌ Cách cũ - phải biết port
String url = "http://localhost:8082/api/v1/products/" + productId;
```

---

### Cơ chế hoạt động

1. **Order Service** yêu cầu `http://PRODUCT-SERVICE/api/v1/products/1`
2. **Spring Cloud LoadBalancer** hỏi **Eureka**: "Product Service có những instance nào?"
3. **Eureka trả lời:** `localhost:8082`, `localhost:8084`, ...
4. **LoadBalancer** chọn instance theo Round-Robin hoặc Random
5. **Forward request** tới instance được chọn

---

## 🚨 Xử lý lỗi

### Lỗi: `UnknownHostException: PRODUCT-SERVICE`

**Nguyên nhân:** Eureka chưa khởi động hoặc Product Service chưa đăng ký

**Giải pháp:**
1. Kiểm tra Eureka Server chạy ở port 8761
2. Kiểm tra Product Service đã connect với Eureka
3. Đợi 30 giây để Product Service đăng ký với Eureka

---

### Lỗi: `Connection refused`

**Nguyên nhân:** Service chưa khởi động

**Giải pháp:**
- Kiểm tra terminal, xem service có đang chạy không
- Kiểm tra port không bị chiếm dụng: `netstat -ano | findstr :8082`

---

### Lỗi: PostgreSQL connection failed

**Nguyên nhân:** PostgreSQL chưa khởi động hoặc database chưa tạo

**Giải pháp:**
1. Khởi động PostgreSQL
2. Tạo database: `CREATE DATABASE product_db;`
3. Đảm bảo username/password đúng trong `application.properties`

---

## 📊 Theo dõi Log

### Terminal sẽ hiển thị:

**Eureka Server:**
```
o.s.c.n.e.s.EurekaServerInitializerConfiguration : Started Eureka Server
```

**Product Service:**
```
com.netflix.discovery.DiscoveryClient : DiscoveryClient initialized
o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8082
```

**Order Service:**
```
com.netflix.discovery.DiscoveryClient : DiscoveryClient initialized
o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8080
```

---

## ✅ Kết quả mong đợi

- ✅ Eureka Server hoạt động ở port 8761
- ✅ 2-3 Product Service instances đăng ký với Eureka
- ✅ Order Service gọi Product Service bằng tên service
- ✅ Request được cân bằng tải giữa các Product instances
- ✅ Không cần biết port số Product Service

---

## 📝 Ghi chú

Dự án này sử dụng:
- **Spring Boot 3.2.0**: Framework chính
- **Spring Cloud 2023.0.0**: Service Discovery (Eureka)
- **Spring Cloud LoadBalancer**: Client-Side Load Balancing
- **PostgreSQL**: Database cho Product Service
- **H2**: In-memory database cho Order Service

---

## 🎯 Bước tiếp theo

Sau khi hoàn thành bài này, bạn có thể học:
- **OpenFeign Client** - Thay thế RestTemplate
- **Circuit Breaker (Resilience4j)** - Xử lý failure
- **API Gateway** - Centralized routing
- **Config Server** - Centralized configuration


