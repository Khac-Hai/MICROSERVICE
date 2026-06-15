# User-Service & Inventory-Service

## Bài tập 3 - Thiết kế Database riêng biệt (Physical Isolation)

### Mục tiêu

* Thực hành nguyên tắc Database per Service với PostgreSQL.
* Quản lý nhiều Database độc lập trên cùng một hệ quản trị.
* Đảm bảo dữ liệu giữa các Service được tách biệt hoàn toàn.
* Cấu hình nhiều ứng dụng Spring Boot kết nối tới Database riêng.

---

## Mô tả bài tập

Trong hệ thống E-Commerce, hai dịch vụ được tách biệt hoàn toàn:

* User-Service
* Inventory-Service

Mỗi Service sử dụng một Database riêng nhằm đảm bảo tính độc lập dữ liệu và giảm sự phụ thuộc giữa các Service.

### Yêu cầu

* Tạo 2 Database riêng biệt trong PostgreSQL:

```text
user_db
inventory_db
```

* Xây dựng 2 ứng dụng Spring Boot độc lập:

```text
user-service
inventory-service
```

* User-Service kết nối tới:

```text
user_db
```

* Inventory-Service kết nối tới:

```text
inventory_db
```

* Tạo bảng:

```text
users
products
```

* Viết API để thêm dữ liệu vào từng Service.
* Chứng minh dữ liệu được lưu vào đúng Database tương ứng.

---

## Kiến trúc hệ thống

```text
                PostgreSQL Server
                        │
        ┌───────────────┴───────────────┐
        │                               │
     user_db                      inventory_db
        │                               │
        │                               │
 ┌──────────────┐               ┌──────────────┐
 │ User-Service │               │ Inventory    │
 │              │               │ Service      │
 └──────────────┘               └──────────────┘
```

---

## Cấu trúc thư mục

### User-Service

```text
user-service
│
├── pom.xml
│
└── src
    ├── main
    │   ├── java
    │   │   └── re
    │   │       └── edu
    │   │           └── userservice
    │   │               │
    │   │               ├── UserServiceApplication.java
    │   │               │
    │   │               ├── controller
    │   │               │   └── UserController.java
    │   │               │
    │   │               ├── service
    │   │               │   ├── UserService.java
    │   │               │   └── impl
    │   │               │       └── UserServiceImpl.java
    │   │               │
    │   │               ├── repository
    │   │               │   └── UserRepository.java
    │   │               │
    │   │               └── entity
    │   │                   └── User.java
    │   │
    │   └── resources
    │       └── application.properties
    │
    └── test
        └── java
```

---

### Inventory-Service

```text
inventory-service
│
├── pom.xml
│
└── src
    ├── main
    │   ├── java
    │   │   └── re
    │   │       └── edu
    │   │           └── inventoryservice
    │   │               │
    │   │               ├── InventoryServiceApplication.java
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
    │   │               └── entity
    │   │                   └── Product.java
    │   │
    │   └── resources
    │       └── application.properties
    │
    └── test
        └── java
```

---

## Tạo Database PostgreSQL

Đăng nhập PostgreSQL:

```sql
psql -U postgres
```

Tạo Database:

```sql
CREATE DATABASE user_db;
CREATE DATABASE inventory_db;
```

Kiểm tra:

```sql
\l
```

Kết quả mong đợi:

```text
user_db
inventory_db
```

---

## Cấu hình User-Service

### application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/user_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8081
```

---

## Entity User

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;
}
```

---

## API User

### Endpoint

```http
POST /api/v1/users
```

### Request Body

```json
{
  "name": "Nguyen Van A",
  "email": "a@gmail.com"
}
```

---

## Cấu hình Inventory-Service

### application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventory_db
spring.datasource.username=postgres
spring.datasource.password=postgres

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8082
```

---

## Entity Product

```java
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer quantity;
}
```

---

## API Product

### Endpoint

```http
POST /api/v1/products
```

### Request Body

```json
{
  "name": "Laptop Dell",
  "quantity": 10
}
```

---

## Kiểm tra Database Isolation

### User Database

```sql
\c user_db

SELECT * FROM users;
```

Kết quả ví dụ:

```text
 id |      name       |      email
----+-----------------+----------------
  1 | Nguyen Van A    | a@gmail.com
```

---

### Inventory Database

```sql
\c inventory_db

SELECT * FROM products;
```

Kết quả ví dụ:

```text
 id |     name      | quantity
----+---------------+----------
  1 | Laptop Dell   | 10
```

---

## Chứng minh dữ liệu được tách biệt

Trong user_db:

```sql
SELECT * FROM products;
```

Kết quả:

```text
ERROR: relation "products" does not exist
```

Trong inventory_db:

```sql
SELECT * FROM users;
```

Kết quả:

```text
ERROR: relation "users" does not exist
```

Điều này chứng minh mỗi Service chỉ quản lý dữ liệu trong Database của riêng mình.

---

## Công nghệ sử dụng

* Java 17
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Lombok
* Maven

---

## Hướng dẫn chạy dự án

### Build User-Service

```bash
mvn clean install
```

### Chạy User-Service

```bash
mvn spring-boot:run
```

Hoặc:

```bash
java -jar target/user-service.jar
```

---

### Build Inventory-Service

```bash
mvn clean install
```

### Chạy Inventory-Service

```bash
mvn spring-boot:run
```

Hoặc:

```bash
java -jar target/inventory-service.jar
```

---

## Kiểm tra bằng Postman

### User-Service

```http
POST http://localhost:8081/api/v1/users
```

Body:

```json
{
  "name": "Nguyen Van A",
  "email": "a@gmail.com"
}
```

---

### Inventory-Service

```http
POST http://localhost:8082/api/v1/products
```

Body:

```json
{
  "name": "Laptop Dell",
  "quantity": 10
}
```

---

## Kết quả đạt được

* Tạo thành công 2 Database độc lập trên PostgreSQL.
* Mỗi Service kết nối tới Database riêng.
* User-Service chỉ thao tác với user_db.
* Inventory-Service chỉ thao tác với inventory_db.
* Dữ liệu được tách biệt hoàn toàn theo mô hình Physical Isolation.
* Áp dụng thành công nguyên tắc Database per Service trong Microservice.
