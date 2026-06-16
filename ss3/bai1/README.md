# E-Commerce Microservices

## Bài tập 1 - Thiết kế kiến trúc phân rã (Decomposition)

### Mục tiêu

* Áp dụng tư duy phân tách module trong kiến trúc Microservice.
* Thực hành xác định Bounded Context theo Domain-Driven Design (DDD).
* Thiết kế các Service độc lập theo từng nghiệp vụ.
* Hiểu nguyên tắc tách biệt dữ liệu và trách nhiệm giữa các Service.

---

## Mô tả bài tập

Trong hệ thống E-Commerce, yêu cầu phân tách hệ thống thành nhiều Service độc lập thay vì xây dựng dưới dạng Monolithic.

### Yêu cầu

Thiết kế 3 Spring Boot Project độc lập:

* Customer-Service
* Product-Service
* Order-Service

Mỗi Service quản lý dữ liệu và nghiệp vụ của riêng mình.

---

## Kiến trúc hệ thống

```text
                    E-Commerce System

    ┌─────────────────────────────────────────┐
    │                                         │
    │              Order Service              │
    │                                         │
    └─────────────────┬───────────────────────┘
                      │
                      │ customerId
                      │
        ┌─────────────┴─────────────┐
        │                           │
        │                           │
┌───────────────┐         ┌────────────────┐
│Customer       │         │Product         │
│Service        │         │Service         │
└───────────────┘         └────────────────┘
```

---

## Phân tích Domain

### Customer Domain

Chịu trách nhiệm quản lý thông tin khách hàng.

#### Entity

```java
Customer
```

#### Thuộc tính

```text
id
fullName
email
password
address
```

---

### Product Domain

Chịu trách nhiệm quản lý sản phẩm.

#### Entity

```java
Product
```

#### Thuộc tính

```text
id
name
price
stockQuantity
description
```

---

### Order Domain

Chịu trách nhiệm quản lý đơn hàng.

#### Entity

```java
Order
```

#### Thuộc tính

```text
id
customerId
orderDate
totalAmount
status
```

---

## Thiết kế các Service

### Customer-Service

Quản lý:

* Đăng ký khách hàng
* Cập nhật thông tin khách hàng
* Tra cứu thông tin khách hàng

#### Cấu trúc Entity

```java
public class Customer {

    private Long id;

    private String fullName;

    private String email;

    private String password;

    private String address;
}
```

---

### Product-Service

Quản lý:

* Danh sách sản phẩm
* Giá sản phẩm
* Tồn kho

#### Cấu trúc Entity

```java
public class Product {

    private Long id;

    private String name;

    private Double price;

    private Integer stockQuantity;

    private String description;
}
```

---

### Order-Service

Quản lý:

* Tạo đơn hàng
* Quản lý trạng thái đơn hàng
* Lưu thông tin tham chiếu khách hàng

#### Cấu trúc Entity

```java
public class Order {

    private Long id;

    private Long customerId;

    private LocalDateTime orderDate;

    private Double totalAmount;

    private String status;
}
```

---

## Tại sao Order chỉ lưu customerId?

### Thiết kế sai

```java
public class Order {

    private Long id;

    private Customer customer;
}
```

Vấn đề:

* Order-Service phụ thuộc trực tiếp vào Customer-Service.
* Vi phạm nguyên tắc độc lập của Microservice.
* Khi Customer thay đổi cấu trúc sẽ ảnh hưởng tới Order.
* Khó triển khai và mở rộng hệ thống.

---

### Thiết kế đúng

```java
public class Order {

    private Long id;

    private Long customerId;
}
```

Ưu điểm:

* Order-Service độc lập hoàn toàn.
* Không phụ thuộc code của Customer-Service.
* Dễ triển khai riêng lẻ.
* Hỗ trợ mở rộng và bảo trì tốt hơn.

---

## Bounded Context

### Customer Context

```text
Customer
├── id
├── fullName
├── email
├── password
└── address
```

---

### Product Context

```text
Product
├── id
├── name
├── price
├── stockQuantity
└── description
```

---

### Order Context

```text
Order
├── id
├── customerId
├── orderDate
├── totalAmount
└── status
```

---

## Cấu trúc dự án

### Customer-Service

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
    │   │               │
    │   │               ├── CustomerServiceApplication.java
    │   │               │
    │   │               ├── controller
    │   │               ├── service
    │   │               ├── repository
    │   │               └── entity
    │   │                   └── Customer.java
```

---

### Product-Service

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
    │   │               │
    │   │               ├── ProductServiceApplication.java
    │   │               ├── controller
    │   │               ├── service
    │   │               ├── repository
    │   │               └── entity
    │   │                   └── Product.java
```

---

### Order-Service

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
    │   │               │
    │   │               ├── OrderServiceApplication.java
    │   │               ├── controller
    │   │               ├── service
    │   │               ├── repository
    │   │               └── entity
    │   │                   └── Order.java
```

---

## Nguyên tắc thiết kế

### Service Independence

Mỗi Service:

* Có source code riêng.
* Có database riêng.
* Có vòng đời triển khai riêng.

---

### Loose Coupling

Các Service không được:

* Truy cập trực tiếp Repository của nhau.
* Import Entity của nhau.
* Dùng chung Database.

Ví dụ không được làm:

```java
@Autowired
private CustomerRepository customerRepository;
```

trong Order-Service.

---

## Công nghệ sử dụng

* Java 17
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Maven
* Lombok

---

## Kết quả đạt được

* Phân tách thành công hệ thống thành 3 Service độc lập.
* Xác định rõ ràng Bounded Context cho từng Domain.
* Thiết kế Entity phù hợp với kiến trúc Microservice.
* Hiểu nguyên tắc Service Independence.
* Áp dụng tư duy Domain-Driven Design (DDD) trong thiết kế hệ thống.
