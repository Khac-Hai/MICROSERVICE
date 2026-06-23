# API Gateway - Tổng hợp 4 bài thực hành trong 1 dự án

# 1. Mục tiêu

Xây dựng một dự án **API Gateway** hoàn chỉnh trong kiến trúc Microservices bao gồm:

- Bài 1: Thiết lập API Gateway và định tuyến tĩnh.
- Bài 2: Định tuyến động kết hợp Eureka.
- Bài 3: Tiền xử lý với Pre-Filter (Log Request).
- Bài 4: Hậu xử lý với Post-Filter (Add Header).

---

# 2. Công nghệ sử dụng

- Java 21
- Spring Boot 3.4.6
- Spring Cloud 2024.0.1
- Spring Cloud Gateway
- Eureka Client
- Maven

---

# 3. Cấu trúc dự án cuối cùng

```text
api-gateway
│
├── src
│   └── main
│       ├── java
│       │   └── com.example.apigateway
│       │       │
│       │       ├── ApiGatewayApplication.java
│       │       │
│       │       └── filter
│       │             ├── LoggingFilter.java
│       │             └── ResponseHeaderFilter.java
│       │
│       └── resources
│               └── application.properties
│
├── pom.xml
└── README.md
```

---

# 4. Tạo dự án Spring Boot

Tên project:

```text
api-gateway
```

Packaging:

```text
Jar
```

Java:

```text
21
```

---

# 5. Cấu hình pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.6</version>
        <relativePath/>
    </parent>

    <groupId>com.example</groupId>
    <artifactId>api-gateway</artifactId>
    <version>0.0.1-SNAPSHOT</version>

    <properties>
        <java.version>21</java.version>
        <spring-cloud.version>2024.0.1</spring-cloud.version>
    </properties>

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

    <dependencies>

        <!-- Gateway -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>

        <!-- Eureka Client -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <!-- Actuator -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

    </dependencies>

</project>
```

---

# 6. Tạo lớp khởi động

## ApiGatewayApplication.java

```java
package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
```

> Lưu ý:
>
> Spring Boot 3.x không cần sử dụng:
>
> ```java
> @EnableEurekaClient
> ```

---

# 7. Bài 1 - Định tuyến tĩnh

## application.properties

```properties
spring.application.name=api-gateway

server.port=8080

# Kết nối Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

#####################################################
# BÀI 1 - STATIC ROUTE
#####################################################

spring.cloud.gateway.routes[0].id=customer-service-route

spring.cloud.gateway.routes[0].uri=http://localhost:8081

spring.cloud.gateway.routes[0].predicates[0]=Path=/api/v1/customers/**
```

---

## Kiểm tra

Khởi động:

```text
Customer Service chạy port 8081
```

Gọi:

```http
GET http://localhost:8080/api/v1/customers/1
```

Gateway sẽ tự động chuyển tiếp đến:

```http
GET http://localhost:8081/api/v1/customers/1
```

---

# 8. Bài 2 - Định tuyến động bằng Eureka

Khi Customer Service đã đăng ký lên Eureka:

Ví dụ:

```properties
spring.application.name=CustomerService
```

Truy cập:

```text
http://localhost:8761
```

Nếu thấy:

```text
CUSTOMERSERVICE
```

thì sửa:

```properties
#####################################################
# BÀI 2 - DYNAMIC ROUTE
#####################################################

spring.cloud.gateway.routes[0].id=customer-service-route

spring.cloud.gateway.routes[0].uri=lb://CustomerService

spring.cloud.gateway.routes[0].predicates[0]=Path=/api/v1/auth/**
```

---

## Luồng hoạt động

```text
Client
   |
   v
Gateway
   |
   v
Eureka Server
   |
   v
Customer Service
```

---

## Kiểm tra

```http
GET http://localhost:8080/api/v1/auth/login
```

Gateway sẽ tự tìm địa chỉ của CustomerService thông qua Eureka.

---

# 9. Bài 3 - Pre Filter (Log Request)

Tạo package:

```text
filter
```

---

## LoggingFilter.java

```java
package com.example.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String path = exchange.getRequest()
                .getURI()
                .getPath();

        logger.info("Incoming request to: {}", path);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
```

---

## Kết quả

Console:

```text
Incoming request to: /api/v1/auth/login
```

---

# 10. Bài 4 - Post Filter (Add Header)

## Cách 1: Cấu hình trực tiếp

```properties
spring.cloud.gateway.routes[0].filters[0]=AddResponseHeader=X-System-Name, Api-Gateway-System
```

---

## Cách 2: Tạo Custom Filter

### ResponseHeaderFilter.java

```java
package com.example.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ResponseHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {

                    exchange.getResponse()
                            .getHeaders()
                            .add(
                                    "X-System-Name",
                                    "Api-Gateway-System"
                            );

                }));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
```

---

# 11. Kết quả sau khi hoàn thành 4 bài

Khi Client gửi request:

```http
GET http://localhost:8080/api/v1/auth/login
```

Gateway sẽ:

```text
1. Ghi log Request.
2. Hỏi Eureka vị trí CustomerService.
3. Chuyển Request tới CustomerService.
4. Nhận Response.
5. Thêm Header vào Response.
6. Trả kết quả về Client.
```

---

# 12. Luồng hoạt động tổng thể

```text
Client
   |
   v
API Gateway
   |
   |------ LoggingFilter
   |               |
   |               |---- Log Request
   |
   v
Eureka Server
   |
   v
Customer Service
   |
   v
ResponseHeaderFilter
   |
   |------ Add Header
   |
   v
Client
```

---

# 13. Thứ tự chạy hệ thống

## Bước 1

Chạy:

```text
Eureka Server
```

---

## Bước 2

Chạy:

```text
Customer Service
```

---

## Bước 3

Chạy:

```text
API Gateway
```

---

# 14. Một số lỗi thường gặp

## Lỗi

```text
Connection refused
```

Nguyên nhân:

```text
Eureka Server chưa chạy.
```

---

## Lỗi

```text
No servers available for service
```

Nguyên nhân:

```text
Customer Service chưa đăng ký Eureka.
```

---

## Lỗi

```text
Spring Boot is not compatible
```

Nguyên nhân:

```text
Sai phiên bản Spring Boot và Spring Cloud.
```

Khuyến nghị:

```text
Java 21
Spring Boot 3.4.6
Spring Cloud 2024.0.1
```

---

# 15. Kết luận

Sau khi hoàn thành, API Gateway có khả năng:

- Định tuyến Request.
- Kết nối Eureka.
- Ghi log Request.
- Xử lý Response.
- Mở rộng cho JWT Authentication.
- Mở rộng cho Security và Authorization.
- Hỗ trợ hệ thống Microservices lớn.