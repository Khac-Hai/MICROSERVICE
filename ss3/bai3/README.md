# Product-Service

## Bài tập 3 - Triển khai Product Service & Bean Validation

### Mục tiêu

* Triển khai dịch vụ quản lý sản phẩm độc lập.
* Sử dụng Bean Validation để kiểm tra dữ liệu đầu vào.
* Kết nối PostgreSQL để lưu trữ dữ liệu sản phẩm.
* Xử lý lỗi Validation tập trung bằng Global Exception Handler.
* Chuẩn hóa cấu trúc dự án theo Layered Architecture.

---

## Mô tả bài tập

Trong hệ thống E-Commerce, một dịch vụ **Product-Service** được xây dựng để quản lý sản phẩm.

Yêu cầu:

* Kết nối PostgreSQL Database.
* Xây dựng Entity Product.
* Áp dụng Bean Validation cho dữ liệu đầu vào.
* Xử lý lỗi Validation bằng GlobalExceptionHandler.
* Chuẩn hóa API Error bằng ApiResponseError.
* Xây dựng các API:

### Tạo sản phẩm

```http
POST /api/v1/products
```

### Lấy chi tiết sản phẩm

```http
GET /api/v1/products/{id}
```

### Lấy danh sách sản phẩm

```http
GET /api/v1/products
```

---

## Cấu trúc thư mục

```text
product-service
│
├── pom.xml
│
└── src
    ├── main
    │   ├── java
    │   │   └── re
    │   │       └── edu
    │   │           └── productservice
    │   │
    │   │               ├── ProductServiceApplication.java
    │   │               │
    │   │               ├── controller
    │   │               │   └── ProductController.java
    │   │               │
    │   │               ├── service
    │   │               │   ├── ProductService.java
    │   │               │   └── impl
    │   │               │       └── ProductServiceImpl.java
    │   │               │
    │   │               ├── repository
    │   │               │   └── ProductRepository.java
    │   │               │
    │   │               ├── entity
    │   │               │   └── Product.java
    │   │               │
    │   │               ├── dto
    │   │               │   ├── ProductRequestDTO.java
    │   │               │   └── ProductResponseDTO.java
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

Tiếp nhận HTTP Request từ Client và trả về Response.

Ví dụ:

```java
ProductController
```

### Service

Chứa business logic của hệ thống.

Ví dụ:

```java
ProductService
ProductServiceImpl
```

### Repository

Thực hiện thao tác với Database.

Ví dụ:

```java
ProductRepository
```

### Entity

Đại diện cho bảng dữ liệu Product trong PostgreSQL.

Ví dụ:

```java
Product
```

### DTO

Dùng để trao đổi dữ liệu giữa Client và Server.

Ví dụ:

```java
ProductRequestDTO
ProductResponseDTO
```

### Exception

Quản lý lỗi và xử lý Exception tập trung.

Ví dụ:

```java
ApiResponseError
GlobalExceptionHandler
ResourceNotFoundException
```

---

## Quy tắc đặt tên

### Package

Sử dụng chữ thường:

```text
controller
service
repository
entity
dto
exception
```

### Entity

Sử dụng PascalCase:

```java
Product
Customer
Order
```

### Repository

Theo mẫu:

```java
<EntityName>Repository
```

Ví dụ:

```java
ProductRepository
```

### Service

Interface:

```java
ProductService
```

Implementation:

```java
ProductServiceImpl
```

### Controller

```java
ProductController
```

---

# Database

### Tạo Database

```sql
CREATE DATABASE product_db;
```

---

## Entity Product

```java
private Long id;
private String name;
private Double price;
private Integer stockQuantity;
```

---

## Validation Rules

### ProductRequestDTO

```java
@NotBlank(message = "Product name must not be blank")
private String name;

@Min(value = 1, message = "Price must be greater than 0")
private Double price;

@Min(value = 0, message = "Stock quantity must not be negative")
private Integer stockQuantity;
```

---

## Chuẩn hóa API Error

### ApiResponseError

```json
{
  "timestamp": "2025-01-01T10:00:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Product name must not be blank"
}
```

---

# API Create Product

### Endpoint

```http
POST /api/v1/products
```

### URL

```http
http://localhost:8082/api/v1/products
```

### Request Body

```json
{
  "name": "Iphone 15 Pro Max",
  "price": 35000000,
  "stockQuantity": 10
}
```

### Response

```json
{
  "id": 1,
  "name": "Iphone 15 Pro Max",
  "price": 35000000,
  "stockQuantity": 10
}
```

### HTTP Status

```text
201 CREATED
```

---

# API Get Product By Id

### Endpoint

```http
GET /api/v1/products/{id}
```

### URL

```http
http://localhost:8082/api/v1/products/1
```

### Response

```json
{
  "id": 1,
  "name": "Iphone 15 Pro Max",
  "price": 35000000,
  "stockQuantity": 10
}
```

### HTTP Status

```text
200 OK
```

---

# API Get All Products

### Endpoint

```http
GET /api/v1/products
```

### URL

```http
http://localhost:8082/api/v1/products
```

### Response

```json
[
  {
    "id": 1,
    "name": "Iphone 15 Pro Max",
    "price": 35000000,
    "stockQuantity": 10
  },
  {
    "id": 2,
    "name": "Samsung S24 Ultra",
    "price": 30000000,
    "stockQuantity": 20
  }
]
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
  "name": "",
  "price": 0,
  "stockQuantity": -1
}
```

### Response

```json
{
  "timestamp": "2025-01-01T10:00:00",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Product name must not be blank"
}
```

### HTTP Status

```text
400 BAD REQUEST
```

---

# API Product Not Found

### URL

```http
http://localhost:8082/api/v1/products/999
```

### Response

```json
{
  "timestamp": "2025-01-01T10:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Product not found with id 999"
}
```

### HTTP Status

```text
404 NOT FOUND
```

---

## Cấu hình PostgreSQL

### application.properties

```properties
server.port=8082

spring.datasource.url=jdbc:postgresql://localhost:5432/product_db
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
spring-boot-starter-validation
postgresql
lombok
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
java -jar target/product-service.jar
```

---

## Kiểm tra API bằng Postman

### Create Product

```http
POST
http://localhost:8082/api/v1/products
```

### Get Product By Id

```http
GET
http://localhost:8082/api/v1/products/1
```

### Get All Products

```http
GET
http://localhost:8082/api/v1/products
```

---

## Kết quả đạt được

* Kết nối thành công PostgreSQL.
* Áp dụng mô hình Layered Architecture.
* Xây dựng Product Service độc lập.
* Sử dụng Bean Validation để kiểm tra dữ liệu đầu vào.
* Chuẩn hóa API Error bằng ApiResponseError.
* Xử lý Validation Exception tập trung bằng GlobalExceptionHandler.
* Hoàn thành API Create Product, Get Product By Id và Get All Products.