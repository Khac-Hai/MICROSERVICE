# Order-Service

## Bài tập 4 - Triển khai Order Service & Tư duy liên kết Microservices

### Mục tiêu

* Hiểu cách lưu trữ dữ liệu trong Microservices khi các thực thể nằm ở các Database khác nhau.
* Thực hành thiết kế DTO tổng hợp.
* Hiểu lý do không sử dụng quan hệ JPA (@ManyToOne, @JoinColumn) giữa các Service độc lập.
* Xây dựng Order Service hoạt động độc lập với Customer Service và Product Service.
* Thực hiện xử lý lỗi theo chuẩn API Error.

---

## Mô tả bài tập

Trong hệ thống E-Commerce, các dịch vụ được tách riêng thành:

* Customer-Service
* Product-Service
* Order-Service

Mỗi Service sử dụng Database riêng.

Do đó:

* Customer nằm trong customer_db.
* Product nằm trong product_db.
* Order nằm trong order_db.

Order Service chỉ lưu:

```java
customerId
productId
```

thay vì sử dụng:

```java
@ManyToOne
@JoinColumn
```

vì Customer và Product thuộc Service khác.

---

## Cấu trúc thư mục

```text
order-service
│
├── pom.xml
│
└── src
    ├── main
    │   ├── java
    │   │   └── re
    │   │       └── edu
    │   │           └── orderservice
    │   │
    │   │               ├── OrderServiceApplication.java
    │   │               │
    │   │               ├── controller
    │   │               │   └── OrderController.java
    │   │               │
    │   │               ├── service
    │   │               │   ├── OrderService.java
    │   │               │   └── impl
    │   │               │       └── OrderServiceImpl.java
    │   │               │
    │   │               ├── repository
    │   │               │   └── OrderRepository.java
    │   │               │
    │   │               ├── entity
    │   │               │   └── Order.java
    │   │               │
    │   │               ├── dto
    │   │               │   ├── OrderRequestDTO.java
    │   │               │   └── OrderResponseDTO.java
    │   │               │
    │   │               └── exception
    │   │                   ├── ApiResponseError.java
    │   │                   ├── ResourceNotFoundException.java
    │   │                   └── GlobalExceptionHandler.java
    │   │
    │   └── resources
    │       └── application.properties
    │
    └── test
        └── java
```

---

## Chức năng các tầng

### Controller

Tiếp nhận HTTP Request và trả về Response.

Ví dụ:

```java
OrderController
```

### Service

Chứa toàn bộ business logic.

Ví dụ:

```java
OrderService
OrderServiceImpl
```

### Repository

Làm việc với PostgreSQL.

Ví dụ:

```java
OrderRepository
```

### Entity

Đại diện cho bảng dữ liệu Order.

Ví dụ:

```java
Order
```

### DTO

Dùng để trao đổi dữ liệu giữa Client và Server.

Ví dụ:

```java
OrderRequestDTO
OrderResponseDTO
```

### Exception

Xử lý lỗi tập trung.

Ví dụ:

```java
ApiResponseError
GlobalExceptionHandler
ResourceNotFoundException
```

---

## Quy tắc đặt tên

### Package

```text
controller
service
repository
entity
dto
exception
```

### Entity

```java
Order
Customer
Product
```

### Repository

```java
OrderRepository
```

### Service

Interface:

```java
OrderService
```

Implementation:

```java
OrderServiceImpl
```

### Controller

```java
OrderController
```

---

# Database

### Tạo Database

```sql
CREATE DATABASE order_db;
```

---

## Entity Order

```java
private Long id;

private Long customerId;

private Long productId;

private LocalDateTime orderDate;

private Double totalAmount;
```

---

## Thiết kế Microservice

### Không sử dụng

```java
@ManyToOne
@JoinColumn
private Customer customer;
```

Hoặc:

```java
@ManyToOne
@JoinColumn
private Product product;
```

---

### Sử dụng

```java
private Long customerId;

private Long productId;
```

---

### Lý do

Customer và Product được quản lý bởi các Service khác nhau.

```text
customer-service  -> customer_db
product-service   -> product_db
order-service     -> order_db
```

Các Service không truy cập trực tiếp Entity của nhau.

Order Service chỉ lưu ID tham chiếu.

---

# API Create Order

### Endpoint

```http
POST /api/v1/orders
```

### URL

```http
http://localhost:8083/api/v1/orders
```

### Request Body

```json
{
  "customerId": 1,
  "productId": 1,
  "quantity": 2
}
```

### Logic xử lý

```text
totalAmount = quantity × productPrice
```

Trong bài tập này có thể giả lập:

```java
productPrice = 100.0;
```

---

### Response

```json
{
  "id": 1,
  "customerId": 1,
  "productId": 1,
  "orderDate": "2025-06-01T10:00:00",
  "totalAmount": 200.0
}
```

### HTTP Status

```text
201 CREATED
```

---

# API Get Order By Id

### Endpoint

```http
GET /api/v1/orders/{id}
```

### URL

```http
http://localhost:8083/api/v1/orders/1
```

### Response

```json
{
  "id": 1,
  "customerId": 1,
  "productId": 1,
  "orderDate": "2025-06-01T10:00:00",
  "totalAmount": 200.0
}
```

### HTTP Status

```text
200 OK
```

---

# API Validation Error

### Request

```json
{
  "customerId": 1,
  "productId": 1,
  "quantity": 0
}
```

### Response

```json
{
  "timestamp": "2025-06-01T10:00:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Quantity must be greater than 0"
}
```

### HTTP Status

```text
400 BAD REQUEST
```

---

# API Order Not Found

### URL

```http
http://localhost:8083/api/v1/orders/999
```

### Response

```json
{
  "timestamp": "2025-06-01T10:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Order not found with id 999"
}
```

### HTTP Status

```text
404 NOT FOUND
```

---

# API Internal Server Error

### Response

```json
{
  "timestamp": "2025-06-01T10:00:00",
  "status": 500,
  "error": "INTERNAL_SERVER_ERROR",
  "message": "Database error occurred"
}
```

### HTTP Status

```text
500 INTERNAL SERVER ERROR
```

---

## Cấu hình PostgreSQL

### application.properties

```properties
server.port=8083

spring.datasource.url=jdbc:postgresql://localhost:5432/order_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

## Dependencies cần sử dụng

```xml
spring-boot-starter-web
spring-boot-starter-data-jpa
postgresql
lombok
spring-boot-starter-validation
```

---

## Hướng dẫn chạy dự án

### Build Project

```bash
mvn clean install
```

### Chạy ứng dụng

```bash
mvn spring-boot:run
```

Hoặc:

```bash
java -jar target/order-service.jar
```

---

## Kiểm tra API bằng Postman

### Create Order

```http
POST
http://localhost:8083/api/v1/orders
```

Body:

```json
{
  "customerId": 1,
  "productId": 1,
  "quantity": 2
}
```

---

### Get Order

```http
GET
http://localhost:8083/api/v1/orders/1
```

---

## Kết quả đạt được

* Kết nối thành công PostgreSQL.
* Xây dựng Order Service độc lập.
* Hiểu tư duy Database Per Service trong Microservices.
* Không sử dụng @ManyToOne giữa các Service khác nhau.
* Lưu customerId và productId dưới dạng khóa tham chiếu.
* Chuẩn hóa API Error.
* Hoàn thành API Create Order và Get Order By Id.