# Customer-Service

## Bài tập 2 - Triển khai Customer Service & Chuẩn hóa API Error

### Mục tiêu

* Làm quen với việc kết nối PostgreSQL thực tế.
* Xây dựng cấu trúc dự án chuẩn để quản lý dữ liệu khách hàng.
* Học cách phản hồi lỗi chuyên nghiệp cho phía Client.
* Thực hiện CRUD cơ bản với Spring Boot và JPA.
* Áp dụng Global Exception Handling.

---

## Mô tả bài tập

Trong hệ thống E-Commerce, một dịch vụ **Customer-Service** được xây dựng để quản lý thông tin khách hàng.

Yêu cầu:

* Kết nối PostgreSQL Database.
* Xây dựng Entity Customer.
* Tạo DTO cho Request và Response.
* Chuẩn hóa API Error bằng ApiResponseError.
* Xử lý Exception tập trung bằng GlobalExceptionHandler.
* Xây dựng các API:

### Đăng ký khách hàng

```http
POST /api/v1/customers/register
```

### Lấy thông tin khách hàng theo ID

```http
GET /api/v1/customers/{id}
```

### Đăng nhập

```http
POST /api/v1/customers/login
```

---

## Cấu trúc thư mục

```text
customer-service
│
├── pom.xml
│
└── src
    ├── main
    │   ├── java
    │   │   └── re
    │   │       └── edu
    │   │           └── customerservice
    │   │
    │   │               ├── CustomerServiceApplication.java
    │   │               │
    │   │               ├── controller
    │   │               │   └── CustomerController.java
    │   │               │
    │   │               ├── service
    │   │               │   ├── CustomerService.java
    │   │               │   └── impl
    │   │               │       └── CustomerServiceImpl.java
    │   │               │
    │   │               ├── repository
    │   │               │   └── CustomerRepository.java
    │   │               │
    │   │               ├── entity
    │   │               │   └── Customer.java
    │   │               │
    │   │               ├── dto
    │   │               │   ├── CustomerRequestDTO.java
    │   │               │   ├── CustomerResponseDTO.java
    │   │               │   └── LoginRequestDTO.java
    │   │               │
    │   │               ├── exception
    │   │               │   ├── ApiResponseError.java
    │   │               │   ├── ResourceNotFoundException.java
    │   │               │   └── GlobalExceptionHandler.java
    │   │               │
    │   │               └── config
    │   │                   └── SecurityConfig.java
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
CustomerController
```

### Service

Chứa toàn bộ business logic của hệ thống.

Ví dụ:

```java
CustomerService
CustomerServiceImpl
```

### Repository

Thực hiện truy vấn dữ liệu từ Database.

Ví dụ:

```java
CustomerRepository
```

### Entity

Đại diện cho bảng dữ liệu trong PostgreSQL.

Ví dụ:

```java
Customer
```

### DTO

Dùng để trao đổi dữ liệu giữa Client và Server.

Ví dụ:

```java
CustomerRequestDTO
CustomerResponseDTO
LoginRequestDTO
```

### Exception

Quản lý và xử lý lỗi tập trung.

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
config
```

### Entity

Sử dụng PascalCase:

```java
Customer
Order
Product
```

### Repository

Theo mẫu:

```java
<EntityName>Repository
```

Ví dụ:

```java
CustomerRepository
```

### Service

Interface:

```java
CustomerService
```

Implementation:

```java
CustomerServiceImpl
```

### Controller

```java
CustomerController
```

---

# Database

### Tạo Database

```sql
CREATE DATABASE customer_db;
```

---

## Entity Customer

```java
private Long id;
private String fullName;
private String email;
private String password;
```

Ràng buộc:

```java
email = unique
id = primary key
```

---

## Chuẩn hóa API Error

### ApiResponseError

```json
{
  "timestamp": "2025-01-01T10:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Customer not found with id 1"
}
```

---

# API Register Customer

### Endpoint

```http
POST /api/v1/customers/register
```

### URL

```http
http://localhost:8081/api/v1/customers/register
```

### Request Body

```json
{
  "fullName": "Nguyen Van A",
  "email": "a@gmail.com",
  "password": "123456"
}
```

### Response

```json
{
  "id": 1,
  "fullName": "Nguyen Van A",
  "email": "a@gmail.com"
}
```

### HTTP Status

```text
201 CREATED
```

---

# API Get Customer By Id

### Endpoint

```http
GET /api/v1/customers/{id}
```

### URL

```http
http://localhost:8081/api/v1/customers/1
```

### Response

```json
{
  "id": 1,
  "fullName": "Nguyen Van A",
  "email": "a@gmail.com"
}
```

### HTTP Status

```text
200 OK
```

---

# API Error Response

### URL

```http
http://localhost:8081/api/v1/customers/100
```

### Response

```json
{
  "timestamp": "2025-01-01T10:00:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Customer not found with id 100"
}
```

### HTTP Status

```text
404 NOT FOUND
```

---

# API Login

### Endpoint

```http
POST /api/v1/customers/login
```

### URL

```http
http://localhost:8081/api/v1/customers/login
```

### Request Body

```json
{
  "email": "a@gmail.com",
  "password": "123456"
}
```

### Response

```json
{
  "id": 1,
  "fullName": "Nguyen Van A",
  "email": "a@gmail.com"
}
```

### HTTP Status

```text
200 OK
```

### Sai tài khoản hoặc mật khẩu

```json
{
  "message": "email or password incorrect"
}
```

### HTTP Status

```text
401 UNAUTHORIZED
```

---

## Cấu hình PostgreSQL

### application.properties

```properties
server.port=8081

spring.datasource.url=jdbc:postgresql://localhost:5432/customer_db
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
spring-boot-starter-security
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
java -jar target/customer-service.jar
```

---

## Kiểm tra API bằng Postman

### Register

```http
POST
http://localhost:8081/api/v1/customers/register
```

### Get Customer

```http
GET
http://localhost:8081/api/v1/customers/1
```

### Login

```http
POST
http://localhost:8081/api/v1/customers/login
```

---

## Kết quả đạt được

* Kết nối thành công PostgreSQL.
* Xây dựng Customer Service theo mô hình Layered Architecture.
* Áp dụng DTO cho Request/Response.
* Mã hóa mật khẩu bằng BCryptPasswordEncoder.
* Chuẩn hóa API Error bằng ApiResponseError.
* Xử lý Exception tập trung với GlobalExceptionHandler.
* Hoàn thành API Register, Get Customer và Login.