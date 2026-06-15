# Order-Service

## Bài tập 4 - Chuẩn hóa API Error Response (Custom Exception)

### Mục tiêu

* Nắm vững cách chuẩn hóa thông báo lỗi cho toàn hệ thống.
* Xây dựng cấu trúc phản hồi lỗi thống nhất.
* Triển khai Custom Exception trong Spring Boot.
* Sử dụng Global Exception Handler để xử lý lỗi tập trung.

---

## Mô tả bài tập

Trong hệ thống E-Commerce, dịch vụ **Order-Service** cần chuẩn hóa định dạng lỗi trả về để Frontend dễ dàng xử lý và hiển thị thông báo cho người dùng.

### Yêu cầu

* Tạo class:

```text
ApiResponseError
```

bao gồm các thuộc tính:

```text
timestamp
status
error
message
```

* Tạo Custom Exception:

```text
ResourceNotFoundException
```

* Sử dụng:

```java
@RestControllerAdvice
```

để xử lý ngoại lệ tập trung.

* Khi Order không tồn tại, hệ thống phải trả về lỗi chuẩn hóa.

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
    │   │               ├── exception
    │   │               │   ├── ApiResponseError.java
    │   │               │   ├── ResourceNotFoundException.java
    │   │               │   └── GlobalExceptionHandler.java
    │   │               │
    │   │               └── config
    │   │
    │   └── resources
    │       └── application.properties
    │
    └── test
        └── java
```

---

## Chức năng các thành phần

### ApiResponseError

Đại diện cho cấu trúc lỗi chuẩn của hệ thống.

```java
public class ApiResponseError {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;
}
```

---

### ResourceNotFoundException

Custom Exception được sử dụng khi tài nguyên không tồn tại.

Ví dụ:

```java
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

---

### GlobalExceptionHandler

Xử lý tập trung các Exception trong toàn bộ ứng dụng.

Ví dụ:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

}
```

---

## Triển khai Exception Handler

### Xử lý ResourceNotFoundException

```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ApiResponseError> handleNotFound(
        ResourceNotFoundException ex) {

    ApiResponseError error = new ApiResponseError(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            ex.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(error);
}
```

---

## Service Logic

Trong Service, khi Order không tồn tại:

```java
Order order = orderRepository.findById(id)
        .orElseThrow(() ->
                new ResourceNotFoundException(
                        "Order với ID " + id + " không tồn tại"));
```

---

## API Kiểm tra lỗi

### Endpoint

```http
GET /api/v1/orders/{id}
```

---

### URL

```http
http://localhost:8080/api/v1/orders/999
```

Giả sử Order có ID 999 không tồn tại.

---

## Response Error Chuẩn Hóa

### HTTP Status

```text
404 Not Found
```

### Response Body

```json
{
  "timestamp": "2024-03-20T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Order với ID 999 không tồn tại"
}
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

---

### Custom Exception

Theo mẫu:

```java
<ResourceName>Exception
```

Ví dụ:

```java
ResourceNotFoundException
OrderNotFoundException
UserNotFoundException
```

---

### Exception Handler

```java
GlobalExceptionHandler
```

---

### Response DTO

```java
ApiResponseError
```

---

## Công nghệ sử dụng

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL
* Lombok
* Maven

---

## Hướng dẫn chạy dự án

### Build Project

```bash
mvn clean install
```

---

### Chạy ứng dụng

```bash
mvn spring-boot:run
```

Hoặc:

```bash
java -jar target/order-service.jar
```

---

## Kiểm tra bằng Postman

### Method

```http
GET
```

### URL

```http
http://localhost:8080/api/v1/orders/999
```

---

### Kết quả mong đợi

```json
{
  "timestamp": "2024-03-20T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Order với ID 999 không tồn tại"
}
```

---

## Kết quả đạt được

* Xây dựng thành công Custom Exception.
* Chuẩn hóa định dạng lỗi cho toàn bộ hệ thống.
* Áp dụng Global Exception Handler để xử lý lỗi tập trung.
* Frontend nhận được cấu trúc lỗi thống nhất.
* Dễ dàng mở rộng và bảo trì hệ thống trong tương lai.
