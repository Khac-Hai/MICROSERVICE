# Bài tập 4 - Cân bằng tải phía Client với @LoadBalanced

## 1. Mục tiêu

### Kiến thức

- Hiểu cơ chế Client-Side Load Balancing.
- Hiểu vai trò của Spring Cloud LoadBalancer.
- Hiểu cách Eureka phối hợp với LoadBalancer.

### Kỹ năng

- Chạy nhiều instance của cùng một Service.
- Sử dụng `@LoadBalanced` với `RestTemplate`.
- Gọi Service bằng Service Name thay vì IP hoặc Port.
- Quan sát quá trình cân bằng tải giữa các Instance.

---

# 2. Tổng quan

Trong bài trước, Order Service sử dụng:

```java
DiscoveryClient
```

để lấy danh sách các Instance của Product Service.

Ví dụ:

```text
PRODUCT-SERVICE

├── localhost:8082
├── localhost:8084
└── localhost:9090
```

Tuy nhiên code phải tự chọn:

```java
instances.get(0)
```

Điều này không thực hiện cân bằng tải.

---

## Giải pháp

Sử dụng:

```java
@LoadBalanced
```

để Spring Cloud tự động:

- Lấy danh sách Instance từ Eureka
- Chọn Instance phù hợp
- Thực hiện Load Balancing

---

# 3. Kiến trúc

```text
                Eureka Server
                      |
                      |
      ---------------------------------
      |               |               |
      |               |               |
      v               v               v

 PRODUCT-SERVICE  PRODUCT-SERVICE  PRODUCT-SERVICE
      8082             8084            9090

                ^
                |
                |
        Spring Cloud
         LoadBalancer
                ^
                |
                |
          ORDER-SERVICE
```

---

# 4. Chạy nhiều Instance Product Service

## Bước 1

Giữ nguyên:

```properties
server.port=8082
```

Chạy Product Service lần đầu.

---

## Bước 2

Cho phép chạy nhiều Instance trong IntelliJ

### IntelliJ IDEA

```text
Run
 └── Edit Configurations
```

Chọn:

```text
Modify Options
```

Bật:

```text
Allow multiple instances
```

---

## Bước 3

Tạo Instance thứ 2

Trong:

```text
Program arguments
```

thêm:

```text
--server.port=8084
```

Run thêm lần nữa.

---

Kết quả:

```text
PRODUCT-SERVICE

Instance 1 -> Port 8082
Instance 2 -> Port 8084
```

---

# 5. Cấu hình Product Service

Cho phép ghi đè Port từ tham số khi chạy.

```properties
spring.application.name=product-service

server.port=${SERVER_PORT:8082}

spring.datasource.url=jdbc:postgresql://localhost:5432/product_db
spring.datasource.username=postgres
spring.datasource.password=123456

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

---

# 6. Thêm Spring Cloud LoadBalancer

Trong `pom.xml` của Order Service:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

---

# 7. Tạo Bean RestTemplate

## AppConfig.java

```java
@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
```

---

## Giải thích

```java
@LoadBalanced
```

giúp Spring:

- Kết nối Eureka
- Lấy danh sách Instance
- Chọn Instance phù hợp
- Tự động cân bằng tải

---

# 8. Inject RestTemplate

```java
@RequiredArgsConstructor
@Service
public class OrderService {

    private final RestTemplate restTemplate;

}
```

---

# 9. Gọi Product Service

Trước đây:

```java
http://localhost:8082/api/v1/products/1
```

hoặc:

```java
DiscoveryClient
```

---

Bây giờ:

```java
public Product getProduct(Long productId) {

    String url =
            "http://PRODUCT-SERVICE/api/v1/products/" + productId;

    return restTemplate.getForObject(
            url,
            Product.class
    );
}
```

---

## Điểm quan trọng

Không còn:

```java
localhost
```

Không còn:

```java
8082
```

Không còn:

```java
DiscoveryClient
```

Chỉ dùng:

```java
PRODUCT-SERVICE
```

---

# 10. Eureka Dashboard

Truy cập:

```text
http://localhost:8761
```

Kết quả:

```text
PRODUCT-SERVICE
    localhost:8082

PRODUCT-SERVICE
    localhost:8084
```

Hai Instance cùng đăng ký dưới một Service Name.

---

# 11. Kiểm tra Load Balancing

## Product Controller

Tạm thời thêm:

```java
@GetMapping("/{id}")
public Product getProduct(@PathVariable Long id) {

    Product product = productService.findById(id);

    product.setName(
            product.getName()
            + " - Port "
            + environment.getProperty("local.server.port")
    );

    return product;
}
```

---

Ví dụ Response

Lần 1:

```json
{
  "id": 1,
  "name": "IPhone 17 - Port 8082"
}
```

Lần 2:

```json
{
  "id": 1,
  "name": "IPhone 17 - Port 8084"
}
```

Lần 3:

```json
{
  "id": 1,
  "name": "IPhone 17 - Port 8082"
}
```

---

Điều này chứng minh:

```text
Request đang được phân phối
qua nhiều Instance khác nhau
```

---

# 12. So sánh

## DiscoveryClient

```java
List<ServiceInstance> instances =
        discoveryClient.getInstances(
                "PRODUCT-SERVICE"
        );

ServiceInstance instance =
        instances.get(0);
```

Nhược điểm:

- Tự viết logic
- Tự chọn Instance
- Khó bảo trì

---

## @LoadBalanced

```java
http://PRODUCT-SERVICE
```

Ưu điểm:

- Code ngắn gọn
- Tự động Load Balancing
- Dễ mở rộng
- Tích hợp Eureka

---

# 13. Cơ chế hoạt động

Khi gọi:

```java
http://PRODUCT-SERVICE/api/v1/products/1
```

Spring thực hiện:

### Bước 1

Hỏi Eureka:

```text
PRODUCT-SERVICE có những Instance nào?
```

Nhận được:

```text
localhost:8082
localhost:8084
```

---

### Bước 2

Spring Cloud LoadBalancer chọn Instance.

Ví dụ:

```text
Request 1 -> 8082
Request 2 -> 8084
Request 3 -> 8082
Request 4 -> 8084
```

---

### Bước 3

Forward Request tới Instance được chọn.

---

# 14. Kết quả mong đợi

✅ Chạy được nhiều Product Service Instance

✅ Eureka hiển thị nhiều Instance cùng Service Name

✅ Order Service gọi bằng:

```java
http://PRODUCT-SERVICE
```

✅ Không cần biết Port

✅ Không cần DiscoveryClient

✅ Request được cân bằng tải giữa các Instance

✅ Hiểu cơ chế Client-Side Load Balancing trong Spring Cloud

---

# 15. Chuẩn bị cho bài tiếp theo

Sau bài này hệ thống đã có:

- Eureka Server
- Eureka Client
- Service Discovery
- Client-Side Load Balancing

Bước tiếp theo thường là:

```text
OpenFeign Client
```

để thay thế RestTemplate bằng cách gọi Service theo interface:

```java
@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}")
    Product getProduct(
        @PathVariable Long id
    );
}
```

giúp code ngắn gọn và dễ bảo trì hơn.