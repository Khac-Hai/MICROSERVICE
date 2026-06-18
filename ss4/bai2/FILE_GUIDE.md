# 🎯 HƯỚNG DẪN SỬ DỤNG DỰ ÁN MICROSERVICES

## 📖 Bạn nên đọc theo thứ tự này:

### 1️⃣ **Bắt đầu nhanh** (5 phút)
👉 Đọc: **QUICKSTART.md**
- Hướng dẫn chạy dự án ngay lập tức
- Yêu cầu tối thiểu
- Command cơ bản

### 2️⃣ **Hiểu chi tiết** (20 phút)
👉 Đọc: **README.md** (file gốc)
- Khái niệm về Eureka
- Service Discovery là gì
- Heartbeat cơ chế
- Kiến trúc hệ thống

### 3️⃣ **Cài đặt chi tiết** (30 phút)
👉 Đọc: **SETUP.md**
- Cài đặt từng bước
- Cấu hình Database
- Build project
- Troubleshooting

### 4️⃣ **Tóm tắt dự án** (10 phút)
👉 Đọc: **PROJECT_SUMMARY.md**
- Tất cả file được tạo
- Cách tổ chức code
- API endpoints
- Tài liệu tham khảo

---

## 🚀 RUN NHANH (3 câu lệnh)

```bash
# 1. Khởi động PostgreSQL
docker-compose up -d

# 2. Build project
mvn clean install

# 3. Chạy services (4 terminal)
# Terminal 1: cd eureka-server && mvn spring-boot:run
# Terminal 2: cd customer-service && mvn spring-boot:run
# Terminal 3: cd product-service && mvn spring-boot:run
# Terminal 4: cd order-service && mvn spring-boot:run
```

## ✅ Kiểm tra thành công

Xem Eureka Dashboard: **http://localhost:8761**

Bạn sẽ thấy:
```
CUSTOMER-SERVICE    UP
PRODUCT-SERVICE     UP
ORDER-SERVICE       UP
```

---

## 📁 Cây Thư mục Dự án

```
bai2/
│
├── 📄 Tài liệu
│   ├── README.md              ← Tài liệu gốc
│   ├── SETUP.md               ← Hướng dẫn cài đặt
│   ├── QUICKSTART.md          ← Bắt đầu nhanh
│   ├── PROJECT_SUMMARY.md     ← Tóm tắt dự án
│   └── 📄 FILE_GUIDE.md        ← File này
│
├── 🔧 Công cụ
│   ├── pom.xml                ← Parent Maven
│   ├── docker-compose.yml     ← Docker Compose
│   ├── init-databases.sql     ← Database init
│   ├── start-all-services.bat ← Script Windows
│   ├── start-all-services.sh  ← Script Linux/Mac
│   └── start-all-services.ps1 ← Script PowerShell
│
├── 🌐 Eureka Server (Port 8761)
│   ├── eureka-server/
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/.../EurekaServerApplication.java
│   │       └── resources/application.properties
│
├── 👤 Customer Service (Port 8081)
│   ├── customer-service/
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/...
│   │       │   ├── CustomerServiceApplication.java
│   │       │   ├── controller/CustomerController.java
│   │       │   ├── entity/Customer.java
│   │       │   └── repository/CustomerRepository.java
│   │       └── resources/application.properties
│
├── 📦 Product Service (Port 8082)
│   ├── product-service/
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/...
│   │       │   ├── ProductServiceApplication.java
│   │       │   ├── controller/ProductController.java
│   │       │   ├── entity/Product.java
│   │       │   └── repository/ProductRepository.java
│   │       └── resources/application.properties
│
└── 🛒 Order Service (Port 8083)
    ├── order-service/
    │   ├── pom.xml
    │   └── src/main/
    │       ├── java/...
    │       │   ├── OrderServiceApplication.java
    │       │   ├── controller/OrderController.java
    │       │   ├── entity/Order.java
    │       │   └── repository/OrderRepository.java
    │       └── resources/application.properties
```

---

## 🎓 Khái niệm Quan trọng

### Eureka Server
- **Là gì?** Service Registry (sổ đăng ký dịch vụ)
- **Port:** 8761
- **URL:** http://localhost:8761

### Eureka Client
- **Là gì?** Mỗi microservice sẽ đăng ký với Eureka Server
- **Annotation:** `@EnableDiscoveryClient`
- **Property:** `eureka.client.service-url.defaultZone`

### Service Discovery
- **Là gì?** Tự động tìm địa chỉ của service yêu cầu
- **Thay vì:** Hard-code URL như `http://localhost:8082/api/products`
- **Sử dụng:** Tên service như `PRODUCT-SERVICE` hoặc `http://product-service/api/products`

### Heartbeat
- **Là gì?** Tín hiệu sống định kỳ gửi từ service đến Eureka
- **Mục đích:** Kể cho Eureka biết service còn sống
- **Nếu không nhận được:** Eureka sẽ đánh dấu service là DOWN

---

## 🔗 API Endpoints

### Tạo dữ liệu (TEST)

```bash
# Customer
curl -X POST http://localhost:8081/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"Nguyen Van A","email":"a@example.com","phone":"0123456789","address":"123 Street"}'

# Product
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","description":"Gaming Laptop","price":1000,"quantity":50,"category":"Electronics"}'

# Order
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":1,"productId":1,"quantity":2,"totalPrice":2000,"status":"PENDING","orderDate":"2024-06-18T10:00:00","notes":"Fast delivery"}'
```

### Lấy dữ liệu (TEST)

```bash
curl http://localhost:8081/api/customers
curl http://localhost:8082/api/products
curl http://localhost:8083/api/orders
```

---

## 🛠️ Cấu hình Chính

### Connection String Database
```properties
# Customer Service
spring.datasource.url=jdbc:postgresql://localhost:5432/customer_db
spring.datasource.username=postgres
spring.datasource.password=123456

# Product Service
spring.datasource.url=jdbc:postgresql://localhost:5432/product_db

# Order Service
spring.datasource.url=jdbc:postgresql://localhost:5432/order_db
```

### Eureka Configuration
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.prefer-ip-address=true
```

---

## 🐛 Các Vấn đề Thường Gặp

| Vấn đề | Nguyên Nhân | Giải pháp |
|--------|-----------|----------|
| Service không xuất hiện trên Eureka | Eureka Server chưa khởi động | Khởi động Eureka Server trước |
| Kết nối Database thất bại | PostgreSQL chưa chạy | Chạy `docker-compose up -d` |
| Port đã bị dùng | Tiến trình khác sử dụng port | Thay đổi port trong `application.properties` |
| 404 Not Found | Service chưa khởi động | Chờ service khởi động xong |
| Connection timeout | Không kết nối được Eureka | Kiểm tra firewall, URL Eureka |

---

## 📋 Checklist Khởi Động

- [ ] Java 21+ được cài đặt
- [ ] Maven được cài đặt
- [ ] PostgreSQL đang chạy (hoặc Docker)
- [ ] Port 8761 trống (Eureka)
- [ ] Port 8081 trống (Customer)
- [ ] Port 8082 trống (Product)
- [ ] Port 8083 trống (Order)
- [ ] Port 5432 trống (Database)

---

## 🎯 Học Tiếp Theo (Sau khi hoàn thành)

- [ ] **Service-to-Service Communication** (OpenFeign)
- [ ] **API Gateway** (Spring Cloud Gateway)
- [ ] **Config Server** (Centralized Config)
- [ ] **Circuit Breaker** (Resilience4j)
- [ ] **Message Queue** (RabbitMQ/Kafka)
- [ ] **Monitoring** (Micrometer + Prometheus)

---

## 📞 Hỗ trợ

1. Xem **SETUP.md** cho hướng dẫn chi tiết
2. Xem **QUICKSTART.md** cho hướng dẫn nhanh
3. Kiểm tra logs trong mỗi terminal chạy service

---

## 🎉 Bắt đầu nào!

1. Mở **QUICKSTART.md** ngay bây giờ
2. Theo dõi các bước
3. Thưởng thức Microservices! 🚀

---

**Happy Coding!** 👨‍💻👩‍💻

