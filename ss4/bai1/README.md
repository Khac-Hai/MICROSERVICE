# Discovery Server (Eureka Server)

## 1. Mục tiêu

Xây dựng một **Service Registry** trong hệ thống Microservices bằng cách sử dụng **Netflix Eureka Server**.

Sau khi hoàn thành bài tập, các Microservice như:

- customer-service
- product-service
- order-service
- api-gateway

có thể đăng ký thông tin của mình lên Eureka và tìm kiếm lẫn nhau mà không cần biết địa chỉ IP hoặc Port cụ thể. :contentReference[oaicite:0]{index=0}

---

# 2. Công nghệ sử dụng

- Java 21
- Spring Boot 3.x
- Spring Cloud
- Netflix Eureka Server
- Maven

---

# 3. Tạo Project

Tên project:

```text
discovery-server
```

Package:

```text
com.example.discoveryserver
```

---

# 4. Cấu hình pom.xml

Thêm dependency Eureka Server:

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

Eureka Server được kích hoạt thông qua dependency:

```text
spring-cloud-starter-netflix-eureka-server
```

:contentReference[oaicite:1]{index=1}

---

# 5. Cấu hình Application

## DiscoveryServerApplication.java

```java
package com.example.discoveryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiscoveryServerApplication.class, args);
    }

}
```

Annotation:

```java
@EnableEurekaServer
```

sẽ biến ứng dụng Spring Boot thành một Eureka Server. :contentReference[oaicite:2]{index=2}

---

# 6. Cấu hình application.properties

Tạo file:

```text
src/main/resources/application.properties
```

Nội dung:

```properties
spring.application.name=discovery-server

server.port=8761

eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

Giải thích:

| Thuộc tính | Ý nghĩa |
|------------|----------|
| spring.application.name | Tên ứng dụng |
| server.port | Port chạy Eureka |
| register-with-eureka=false | Eureka Server không tự đăng ký chính nó |
| fetch-registry=false | Eureka Server không lấy danh sách service từ chính nó |

:contentReference[oaicite:3]{index=3}

---

# 7. Cấu trúc thư mục

```text
discovery-server
│
├── src
│   └── main
│       ├── java
│       │   └── com.example.discoveryserver
│       │       └── DiscoveryServerApplication.java
│       │
│       └── resources
│           └── application.properties
│
├── pom.xml
│
└── README.md
```

---

# 8. Chạy ứng dụng

Chạy class:

```text
DiscoveryServerApplication
```

Hoặc sử dụng Maven:

```bash
mvn spring-boot:run
```

---

# 9. Kiểm tra kết quả

Mở trình duyệt:

```text
http://localhost:8761
```

Kết quả mong muốn:

```text
Spring Eureka Dashboard
```

Hiển thị:

- System Status
- DS Replicas
- Instances currently registered with Eureka

Ban đầu chưa có service nào đăng ký nên danh sách sẽ trống.

---

# 10. Đăng ký Service vào Eureka

Ví dụ tại order-service:

```properties
spring.application.name=order-service

eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```

Sau khi khởi động:

```text
order-service
```

sẽ xuất hiện trên Dashboard Eureka. :contentReference[oaicite:4]{index=4}

---

# 11. Kết quả mong đợi

Dashboard Eureka:

```text
http://localhost:8761
```

Hiển thị:

```text
Instances currently registered with Eureka
```

Ví dụ:

```text
ORDER-SERVICE
PRODUCT-SERVICE
CUSTOMER-SERVICE
API-GATEWAY
```

Tất cả trạng thái:

```text
UP
```

---

# 12. Ý nghĩa trong Microservices

Nếu không có Eureka:

```text
Order Service
    ↓
http://localhost:8082
```

Khi Product Service đổi Port:

```text
8082 -> 9000
```

Order Service sẽ lỗi.

---

Khi sử dụng Eureka:

```text
Order Service
    ↓
PRODUCT-SERVICE
    ↓
Eureka Server
    ↓
localhost:9000
```

Service chỉ cần biết:

```text
PRODUCT-SERVICE
```

không cần biết:

```text
IP
Port
```

Điều này giúp hệ thống:

- Dễ mở rộng
- Dễ scale
- Hỗ trợ Load Balancing
- Hỗ trợ API Gateway
- Giảm cấu hình cứng (Hardcode) giữa các Service

:contentReference[oaicite:5]{index=5}

---

# 13. Hoàn thành

Sau khi hoàn thành bài tập:

✅ Tạo được Eureka Discovery Server

✅ Truy cập được Dashboard

✅ Sẵn sàng cho các Service đăng ký

✅ Chuẩn bị cho bước tích hợp:

- Customer Service
- Product Service
- Order Service
- API Gateway