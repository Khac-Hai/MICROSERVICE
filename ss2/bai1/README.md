# Order-Service

## Bài tập 1 - Chuẩn hóa cấu trúc thư mục & Quy tắc đặt tên

### Mục tiêu

* Nắm vững cấu trúc thư mục chuẩn của một Microservice.
* Áp dụng mô hình Layered Architecture.
* Tổ chức mã nguồn khoa học, dễ bảo trì.
* Tuân thủ quy tắc đặt tên trong dự án Spring Boot.

---

## Mô tả bài tập

Trong hệ thống E-Commerce, một dịch vụ **Order-Service** được xây dựng để quản lý đơn hàng.

Yêu cầu:

* Tách dự án thành các tầng:

    * controller
    * service
    * repository
    * entity
    * dto
    * exception
* Áp dụng quy tắc đặt tên chuẩn.
* Tạo API:

```http
GET /api/v1/orders/health-check
```

API trả về:

```text
Order Service is Up
```

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
    │   │               │
    │   │               └── exception
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

Tiếp nhận HTTP Request từ client và trả về Response.

Ví dụ:

```java
OrderController
```

### Service

Chứa logic nghiệp vụ của hệ thống.

Ví dụ:

```java
OrderService
OrderServiceImpl
```

### Repository

Thực hiện truy vấn dữ liệu từ Database.

Ví dụ:

```java
OrderRepository
```

### Entity

Đại diện cho bảng dữ liệu trong cơ sở dữ liệu.

Ví dụ:

```java
Order
```

### DTO

Dùng để trao đổi dữ liệu giữa các tầng của ứng dụng.

### Exception

Quản lý và xử lý ngoại lệ của hệ thống.

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
Order
User
Product
```

### Repository

Theo mẫu:

```java
<EntityName>Repository
```

Ví dụ:

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

## API Health Check

### Endpoint

```http
GET /api/v1/orders/health-check
```

### URL

```http
http://localhost:8080/api/v1/orders/health-check
```

### Response

```text
Order Service is Up
```

### HTTP Status

```text
200 OK
```

---

## Công nghệ sử dụng

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Lombok
* Maven

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

Method:

```http
GET
```

URL:

```http
http://localhost:8080/api/v1/orders/health-check
```

Kết quả mong đợi:

```text
Order Service is Up
```

---

## Kết quả đạt được

* Chuẩn hóa cấu trúc thư mục theo mô hình Microservice.
* Phân tách rõ Controller, Service, Repository và Entity.
* Áp dụng đúng quy tắc đặt tên.
* Xây dựng thành công API Health Check để kiểm tra trạng thái hoạt động của Service.
