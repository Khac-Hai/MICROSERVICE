# Product-Service

## Bài tập 2 - Thiết kế DTO và Response chuẩn hóa

### Mục tiêu

* Hiểu tại sao không nên trả về Entity trực tiếp trong API.
* Xây dựng lớp **Data Transfer Object (DTO)** để bảo mật dữ liệu.
* Chuẩn hóa Response trả về cho client.

---

## Mô tả bài tập

Trong hệ thống E-Commerce, Entity **Product** trong DB có nhiều thông tin nhạy cảm (giá nhập, mã kho, ngày cập nhật).  
Bạn cần tạo API lấy thông tin sản phẩm cho khách hàng, nhưng chỉ trả về dữ liệu cần thiết.

**Yêu cầu:**

* Tạo **ProductEntity** với các trường:
    * id
    * name
    * sku
    * importPrice
    * sellPrice
    * stockQuantity
* Tạo **ProductResponseDTO** chỉ chứa:
    * id
    * name
    * sellPrice
* Viết Controller trả về một **ProductResponseDTO** đã được map dữ liệu từ Entity.

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
    │   │               │   └── ProductService.java
    │   │               │
    │   │               ├── entity
    │   │               │   └── ProductEntity.java
    │   │               │
    │   │               ├── dto
    │   │               │   └── ProductResponseDTO.java
    │   │               │
    │   │               └── exception
    │   │
    │   └── resources
    │       └── application.properties
    │
    └── test
        └── java
