# Bài tập 3 - Tự động phát hiện và gọi Service (DiscoveryClient)

## 1. Mục tiêu

### Kiến thức

- Hiểu cách lấy thông tin Service từ Eureka Registry.
- Hiểu cách Service Discovery hoạt động trong thực tế.
- Hiểu sự khác biệt giữa gọi Service bằng URL cứng và gọi Service thông qua Eureka.

### Kỹ năng

- Sử dụng `DiscoveryClient`.
- Tìm danh sách Instance của một Service.
- Gọi Product Service từ Order Service mà không cần hard-code Port.
- Xử lý lỗi khi Service không tồn tại hoặc đang bị tắt.

---

# 2. Tổng quan

Ở bài trước:

```text
CUSTOMER-SERVICE
PRODUCT-SERVICE
ORDER-SERVICE
```

đã đăng ký thành công lên Eureka Server.

Tuy nhiên Order Service vẫn đang gọi Product Service theo cách:

```java
http://localhost:8082/api/v1/products/{id}
```

Điều này tạo ra sự phụ thuộc vào Port cụ thể.

---

## Vấn đề

Nếu Product Service đổi Port:

```text
8082 -> 9090
```

thì Order Service sẽ lỗi.

---

## Giải pháp

Thay vì hỏi:

```text
Product Service đang ở port nào?
```

Order Service sẽ hỏi Eureka:

```text
PRODUCT-SERVICE đang ở đâu?
```

Eureka sẽ trả về danh sách Instance hiện có.

---

# 3. Kiến trúc

```text
Order Service
      |
      | DiscoveryClient
      |
      v
 Eureka Server
      |
      v
 PRODUCT-SERVICE
      |
      v
 http://localhost:8082
```

---

# 4. Thêm Dependency

Nếu đã làm bài Eureka Client thì không cần thêm mới.

Đảm bảo có:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

---

# 5. Inject DiscoveryClient

Trong Order Service:

```java
import org.springframework.cloud.client.discovery.DiscoveryClient;
```

```java
@RequiredArgsConstructor
@Service
public class OrderService {

    private final DiscoveryClient discoveryClient;

}
```

---

# 6. Gọi Product Service bằng DiscoveryClient

Ví dụ:

```java
public Product getProductById(Long productId) {

    List<ServiceInstance> instances =
            discoveryClient.getInstances("PRODUCT-SERVICE");

    if (instances == null || instances.isEmpty()) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "PRODUCT-SERVICE hiện không khả dụng"
        );
    }

    ServiceInstance productInstance = instances.get(0);

    String baseUrl =
            productInstance.getUri().toString();

    String targetUrl =
            baseUrl + "/api/v1/products/" + productId;

    RestClient restClient = RestClient.create();

    return restClient.get()
            .uri(targetUrl)
            .retrieve()
            .body(Product.class);
}
```

---

# 7. Giải thích từng bước

## Bước 1

Lấy danh sách Instance

```java
List<ServiceInstance> instances =
        discoveryClient.getInstances("PRODUCT-SERVICE");
```

Ví dụ Eureka trả về:

```text
localhost:8082
localhost:9090
```

---

## Bước 2

Kiểm tra Service có tồn tại không

```java
if (instances.isEmpty())
```

Nếu không tồn tại:

```text
503 Service Unavailable
```

---

## Bước 3

Lấy Instance đầu tiên

```java
ServiceInstance productInstance =
        instances.get(0);
```

Ví dụ:

```text
http://localhost:8082
```

---

## Bước 4

Lấy URI

```java
String baseUrl =
        productInstance.getUri().toString();
```

Kết quả:

```text
http://localhost:8082
```

---

## Bước 5

Ghép Endpoint

```java
String targetUrl =
        baseUrl + "/api/v1/products/" + productId;
```

Ví dụ:

```text
http://localhost:8082/api/v1/products/1
```

---

## Bước 6

Gọi API

```java
return restClient.get()
        .uri(targetUrl)
        .retrieve()
        .body(Product.class);
```

---

# 8. Xử lý lỗi

Nếu không tìm thấy Product Service:

```java
throw new ResponseStatusException(
        HttpStatus.SERVICE_UNAVAILABLE,
        "PRODUCT-SERVICE hiện không khả dụng"
);
```

Kết quả:

```json
{
  "status": 503,
  "error": "Service Unavailable",
  "message": "PRODUCT-SERVICE hiện không khả dụng"
}
```

---

# 9. Kiểm tra kết quả

## Trường hợp 1

Product Service đang chạy.

```text
PRODUCT-SERVICE -> UP
```

Request:

```http
GET /api/v1/orders/getProduct/1
```

Response:

```json
{
  "id": 1,
  "name": "IPhone 17",
  "price": 1500
}
```

---

## Trường hợp 2

Tắt Product Service.

Dashboard Eureka:

```text
PRODUCT-SERVICE -> DOWN
```

Request:

```http
GET /api/v1/orders/getProduct/1
```

Response:

```json
{
  "status": 503,
  "error": "Service Unavailable"
}
```

---

## Trường hợp 3

Đổi Port Product Service

Từ:

```properties
server.port=8082
```

sang:

```properties
server.port=9090
```

Khởi động lại Product Service.

Dashboard Eureka:

```text
PRODUCT-SERVICE -> localhost:9090
```

Order Service vẫn hoạt động bình thường.

Không cần sửa:

```java
String url =
"http://localhost:8082";
```

vì URL không còn bị hard-code nữa.

---

# 10. So sánh

## Cách cũ

```java
http://localhost:8082
```

Nhược điểm:

- Hard-code Port
- Khó mở rộng
- Khó Scale
- Dễ lỗi khi đổi Port

---

## DiscoveryClient

```java
discoveryClient.getInstances("PRODUCT-SERVICE")
```

Ưu điểm:

- Không cần biết Port
- Không cần biết IP
- Tự động tìm Service
- Hỗ trợ Scale nhiều Instance

---

# 11. Hạn chế của DiscoveryClient

Ví dụ:

```java
instances.get(0)
```

Lúc nào cũng chọn Instance đầu tiên.

Nếu có:

```text
localhost:8082
localhost:9090
localhost:10000
```

thì không có Load Balancing.

---

Do đó trong thực tế người ta thường dùng:

- OpenFeign
- Spring Cloud LoadBalancer
- API Gateway

thay vì sử dụng DiscoveryClient trực tiếp.

---

# 12. Kết quả mong đợi

✅ Order Service lấy được thông tin Product từ Eureka

✅ Không còn hard-code URL

✅ Khi Product Service đổi Port vẫn hoạt động

✅ Khi Product Service tắt sẽ trả lỗi 503

✅ Hiểu nguyên lý Service Discovery trước khi học OpenFeign và Load Balancer

## How to run the sample project

This workspace contains a simple multi-module Maven project with three modules:

- `eureka-server` (Eureka registry) - runs on port 8761
- `product-service` (a very small product API) - runs on port 8082 and registers as `PRODUCT-SERVICE`
- `order-service` (uses DiscoveryClient to find PRODUCT-SERVICE) - runs on port 8081

Prerequisites: Java 11+, Maven

From the repository root (where this README and parent `pom.xml` live) you can build and run:

1) Build the project

```powershell
mvn -T 1C clean package -DskipTests
```

2) Start Eureka server

```powershell
mvn -pl eureka-server spring-boot:run
```

3) Start Product service (in another terminal)

```powershell
mvn -pl product-service spring-boot:run
```

4) Start Order service (in another terminal)

```powershell
mvn -pl order-service spring-boot:run
```

5) Test

Product service endpoint (direct):

```http
GET http://localhost:8082/api/v1/products/1
```

Order service (via DiscoveryClient -> Product):

```http
GET http://localhost:8081/api/v1/orders/getProduct/1
```

If PRODUCT-SERVICE is down, the Order endpoint will return HTTP 503 Service Unavailable.
