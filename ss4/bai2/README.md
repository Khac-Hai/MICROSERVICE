# Bài tập 2 - Đăng ký "Hộ khẩu" cho các Service (Eureka Client)

## 1. Mục tiêu

### Kiến thức

- Hiểu cách các Microservice đăng ký với Eureka Server.
- Hiểu cơ chế Heartbeat giữa Eureka Client và Eureka Server.
- Hiểu cách Service Discovery hoạt động trong hệ thống Microservices.

### Kỹ năng

- Chuyển đổi các Service thành Eureka Client.
- Kết nối Customer Service, Product Service và Order Service với Eureka Server.
- Kiểm tra trạng thái Service trên Eureka Dashboard.

---

# 2. Tổng quan

Trong Session trước chúng ta đã xây dựng:

- customer-service
- product-service
- order-service

Tuy nhiên các Service này vẫn đang hoạt động độc lập.

Trong bài này chúng ta sẽ đăng ký các Service vào Eureka Server để tạo thành một hệ thống Microservices có khả năng Service Discovery.

---

# 3. Kiến trúc sau khi hoàn thành

```text
                    +----------------+
                    | Eureka Server  |
                    |    Port 8761   |
                    +-------+--------+
                            ^
                            |
        -----------------------------------------
        |                   |                   |
        |                   |                   |
        v                   v                   v

+----------------+ +----------------+ +----------------+
| Customer       | | Product        | | Order          |
| Service        | | Service        | | Service        |
| Port 8081      | | Port 8082      | | Port 8083      |
+----------------+ +----------------+ +----------------+
```

Mỗi Service sẽ gửi thông tin đăng ký và Heartbeat định kỳ tới Eureka Server.

---

# 4. Thêm Dependency Eureka Client

Thêm dependency sau vào file `pom.xml` của cả 3 Service:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

---

# 5. Thêm Spring Cloud BOM

Trong file `pom.xml`

```xml
<properties>
    <java.version>21</java.version>
    <spring-cloud.version>2025.0.1</spring-cloud.version>
</properties>
```

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

# 6. Cấu hình Customer Service

## CustomerServiceApplication.java

```java
@SpringBootApplication
@EnableDiscoveryClient
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

}
```

> Có thể dùng `@EnableDiscoveryClient` hoặc bỏ qua annotation này trong các phiên bản Spring Cloud mới vì cơ chế auto-configuration đã hỗ trợ sẵn.

---

## application.properties

```properties
server.port=8081

spring.application.name=customer-service

spring.datasource.url=jdbc:postgresql://localhost:5432/customer_db
spring.datasource.username=postgres
spring.datasource.password=123456

spring.jpa.hibernate.ddl-auto=update

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

eureka.instance.prefer-ip-address=true
```

---

# 7. Cấu hình Product Service

## ProductServiceApplication.java

```java
@SpringBootApplication
@EnableDiscoveryClient
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

}
```

## application.properties

```properties
server.port=8082

spring.application.name=product-service

spring.datasource.url=jdbc:postgresql://localhost:5432/product_db
spring.datasource.username=postgres
spring.datasource.password=123456

spring.jpa.hibernate.ddl-auto=update

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

eureka.instance.prefer-ip-address=true
```

---

# 8. Cấu hình Order Service

## OrderServiceApplication.java

```java
@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}
```

## application.properties

```properties
server.port=8083

spring.application.name=order-service

spring.datasource.url=jdbc:postgresql://localhost:5432/order_db
spring.datasource.username=postgres
spring.datasource.password=123456

spring.jpa.hibernate.ddl-auto=update

eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

eureka.instance.prefer-ip-address=true
```

---

# 9. Khởi động hệ thống

Khởi động theo thứ tự:

## Bước 1

Chạy Eureka Server

```text
Port 8761
```

Truy cập:

```text
http://localhost:8761
```

---

## Bước 2

Chạy Customer Service

```text
Port 8081
```

---

## Bước 3

Chạy Product Service

```text
Port 8082
```

---

## Bước 4

Chạy Order Service

```text
Port 8083
```

---

# 10. Kiểm tra kết quả

Mở Dashboard Eureka:

```text
http://localhost:8761
```

Trong mục:

```text
Instances currently registered with Eureka
```

Phải xuất hiện:

```text
CUSTOMER-SERVICE
PRODUCT-SERVICE
ORDER-SERVICE
```

Trạng thái:

```text
UP
```

Ví dụ:

```text
Application       Status

CUSTOMER-SERVICE  UP
PRODUCT-SERVICE   UP
ORDER-SERVICE     UP
```

---

# 11. Heartbeat là gì?

Sau khi đăng ký thành công:

```text
Customer Service
      ↓
Heartbeat
      ↓
Eureka Server
```

Service sẽ gửi tín hiệu sống định kỳ tới Eureka Server.

Nếu Service bị tắt:

```text
Customer Service ❌
```

Eureka sẽ tự động đánh dấu:

```text
DOWN
```

và loại khỏi danh sách khả dụng.

---

# 12. Ý nghĩa của Service Discovery

Không sử dụng Eureka:

```java
http://localhost:8082/api/products
```

Hard-code địa chỉ Service.

---

Sử dụng Eureka:

```java
http://PRODUCT-SERVICE/api/products
```

Hệ thống sẽ tự tìm instance phù hợp thông qua Eureka Server.

Lợi ích:

- Không hard-code IP
- Dễ mở rộng hệ thống
- Hỗ trợ Load Balancing
- Hỗ trợ API Gateway
- Tăng khả năng chịu lỗi

---

# 13. Kết quả mong đợi

✅ Eureka Server chạy tại port 8761

✅ Customer Service đăng ký thành công

✅ Product Service đăng ký thành công

✅ Order Service đăng ký thành công

✅ Dashboard hiển thị 3 Service ở trạng thái UP

✅ Sẵn sàng cho bài tiếp theo: Service-to-Service Communication bằng OpenFeign hoặc RestTemplate