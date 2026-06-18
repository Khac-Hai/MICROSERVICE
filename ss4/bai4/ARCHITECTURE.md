# Kiến trúc Microservices Client-Side Load Balancing

---

## 📐 Sơ đồ Kiến trúc Tổng quan

```
┌─────────────────────────────────────────────────────────────────────┐
│                     Eureka Service Registry                         │
│                     (localhost:8761)                                │
│                          ↑                                          │
│                ┌─────────┼────────────┐                             │
│                │         │            │                             │
│         Register│        │Register    │Register                     │
│                │         │            │                             │
│                ↓         ↓            ↓                             │
│         ┌────────────┐  ┌────────────┐  ┌────────────┐             │
│         │ Order      │  │ Product    │  │ Product    │             │
│         │ Service    │  │ Service    │  │ Service    │             │
│         │ :8080      │  │ :8082      │  │ :8084      │             │
│         └────────────┘  └────────────┘  └────────────┘             │
│                 ▲            ↓            ↓                         │
│                 │       Database      Database                      │
│                 │         (DB)         (DB)                         │
│                 │                                                   │
└─────────────────│───────────────────────────────────────────────────┘
                  │
        @LoadBalanced RestTemplate
           (Client-Side LB)
                  │
                  │ 1. Hỏi Eureka
                  │ 2. Lấy danh sách instances
                  │ 3. Chọn instance (Round-Robin)
                  │ 4. Gửi request
                  │
        ┌─────────▼──────────┐
        │  Spring Cloud      │
        │  LoadBalancer      │
        └────────────────────┘
```

---

## 🏗️ Cấu trúc thư mục Dự án

```
bai4/
│
├── pom.xml                           # Parent POM (multi-module)
├── .gitignore                         # Git ignore rules
├── README.md                          # Tài liệu bài tập gốc
├── SETUP_GUIDE.md                    # Hướng dẫn cài đặt & chạy
├── TESTING_SCRIPT.md                 # Script test & kiểm tra
├── ARCHITECTURE.md                   # File này - mô tả kiến trúc
│
├── eureka-server/                    # Module 1: Eureka Server
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/com/microservice/eurekaserver/
│           │   └── EurekaServerApplication.java
│           └── resources/
│               └── application.properties
│
├── product-service/                  # Module 2: Product Service
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/com/microservice/productservice/
│           │   ├── ProductServiceApplication.java
│           │   ├── entity/
│           │   │   └── Product.java
│           │   ├── repository/
│           │   │   └── ProductRepository.java
│           │   ├── service/
│           │   │   └── ProductService.java
│           │   └── controller/
│           │       └── ProductController.java
│           └── resources/
│               └── application.properties
│
└── order-service/                    # Module 3: Order Service
    ├── pom.xml
    └── src/
        └── main/
            ├── java/com/microservice/orderservice/
            │   ├── OrderServiceApplication.java
            │   ├── config/
            │   │   └── AppConfig.java              # @LoadBalanced RestTemplate
            │   ├── dto/
            │   │   └── ProductDTO.java
            │   ├── entity/
            │   │   └── Order.java
            │   ├── repository/
            │   │   └── OrderRepository.java
            │   ├── service/
            │   │   └── OrderService.java           # Gọi Product Service
            │   └── controller/
            │       └── OrderController.java
            └── resources/
                └── application.properties
```

---

## 🔄 Flow Kiến trúc Client-Side Load Balancing

### Scenario: Order Service gọi Product Service

```
┌────────────────────┐
│  Order Service     │
│  Request từ client │
│ GET /api/v1/orders │
│ /check-product/1   │
└─────────┬──────────┘
          │
          │ OrderController.checkProductService(1)
          ↓
┌──────────────────────────────────────────┐
│ OrderService.getProduct(productId)       │
│ URL = "http://PRODUCT-SERVICE/..."       │
│ restTemplate.getForObject(url, ...)      │
└──────────┬───────────────────────────────┘
           │
           │ @LoadBalanced RestTemplate
           ↓
┌─────────────────────────────────────────┐
│ Spring Cloud LoadBalancer               │
│ 1. Detect Service Name: PRODUCT-SERVICE │
│ 2. Query Eureka Server                  │
│ 3. Get Instances:                       │
│    - localhost:8082                     │
│    - localhost:8084                     │
│ 4. Choose Instance (Round-Robin)        │
├─────────────────────────────────────────┤
│ Request 1 → localhost:8082 ✓            │
│ Request 2 → localhost:8084 ✓            │
│ Request 3 → localhost:8082 ✓            │
│ Request 4 → localhost:8084 ✓            │
└──────┬────────────────────┬─────────────┘
       │                    │
       ↓                    ↓
┌──────────────────┐  ┌──────────────────┐
│ Product Service  │  │ Product Service  │
│ Port 8082        │  │ Port 8084        │
│ Processing...    │  │ Processing...    │
└──────┬───────────┘  └────────┬─────────┘
       │                      │
       └──────────┬───────────┘
                  │
                  ↓
           ┌────────────────┐
           │ Response Data  │
           │ ProductDTO     │
           └────────┬───────┘
                    │
                    ↓
           ┌────────────────────┐
           │ Order Service      │
           │ Nhận response      │
           │ Trả lại client     │
           └────────────────────┘
```

---

## 🔐 Các thành phần chính

### 1️⃣ Eureka Server
- **Port:** 8761
- **Vai trò:** Service Discovery Registry
- **Chủ yếu:** Lưu trữ danh sách tất cả các service instances
- **Configuration:**
  - `eureka.client.register-with-eureka=false` (server không tự đăng ký)
  - `eureka.client.fetch-registry=false` (không fetch registry)

---

### 2️⃣ Product Service (Eureka Client)
- **Port:** 8082, 8084, 9090 (multiple instances)
- **Database:** PostgreSQL
- **Vai trò:** Business Service - Quản lý sản phẩm
- **Endpoints:**
  - `GET /api/v1/products` - Lấy tất cả
  - `GET /api/v1/products/{id}` - Lấy 1 product (+ port info)
  - `POST /api/v1/products` - Tạo
  - `PUT /api/v1/products/{id}` - Cập nhật
  - `DELETE /api/v1/products/{id}` - Xóa
- **Registration:** Tự động đăng ký với Eureka khi khởi động

---

### 3️⃣ Order Service (Eureka Client + LoadBalancer Client)
- **Port:** 8080
- **Database:** H2 In-Memory
- **Vai trò:** Business Service - Quản lý đơn hàng
- **Endpoints:**
  - `POST /api/v1/orders?productId=X&quantity=Y` - Tạo order
  - `GET /api/v1/orders/{id}` - Lấy 1 order
  - `GET /api/v1/orders` - Lấy tất cả
  - `GET /api/v1/orders/check-product/{id}` - Test load balancing
- **@LoadBalanced RestTemplate:** Gọi Product Service qua LoadBalancer

---

## 🔌 AppConfig: RestTemplate với @LoadBalanced

**File:** `order-service/src/main/java/.../ config/AppConfig.java`

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

### Giải thích:
- `@Bean`: Tạo Bean quản lý bởi Spring
- `@LoadBalanced`: Kích hoạt Load Balancing cho RestTemplate
- Spring Cloud tự động:
  1. Intercept HTTP requests
  2. Detect Service Name từ URL
  3. Query Eureka
  4. Load Balance giữa instances

---

## 🎯 OrderService: Gọi Product Service

**File:** `order-service/src/main/java/.../service/OrderService.java`

```java
@Service
@RequiredArgsConstructor
public class OrderService {

    private final RestTemplate restTemplate;

    public ProductDTO getProduct(Long productId) {
        // ✅ Dùng Service Name thay vì host:port
        String url = "http://PRODUCT-SERVICE/api/v1/products/" + productId;
        
        return restTemplate.getForObject(url, ProductDTO.class);
    }

    public Order createOrder(Long productId, Integer quantity) {
        ProductDTO product = getProduct(productId);  // Gọi Product Service
        
        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalPrice(product.getPrice() * quantity);
        // ...
        return orderRepository.save(order);
    }
}
```

### Key Points:
- ❌ `http://localhost:8082` - CỔ - Phải biết port
- ❌ `http://192.168.1.1:8082` - CỔ - Phải biết IP
- ✅ `http://PRODUCT-SERVICE` - MỚI - Service Name từ Eureka

---

## 🔄 Load Balancing Algorithm

### Round-Robin (Mặc định)
```
Request 1 → Instance 1 (8082)
Request 2 → Instance 2 (8084)
Request 3 → Instance 1 (8082)
Request 4 → Instance 2 (8084)
...
```

### Response Header Thay đổi Port
```json
// Request 1 từ Order Service (gọi port 8082)
{
  "id": 1,
  "name": "IPhone 17 - Port 8082",
  "price": 999.99
}

// Request 2 từ Order Service (gọi port 8084)
{
  "id": 1,
  "name": "IPhone 17 - Port 8084",
  "price": 999.99
}
```

---

## 📊 Dependencies & Versions

| Dependency | Version | Mục đích |
|-----------|---------|---------|
| spring-boot-starter-parent | 3.2.0 | Spring Boot Base |
| spring-cloud-dependencies | 2023.0.0 | Spring Cloud (Eureka, LB) |
| spring-cloud-starter-netflix-eureka-server | 2023.0.0 | Eureka Server |
| spring-cloud-starter-netflix-eureka-client | 2023.0.0 | Eureka Client |
| spring-cloud-starter-loadbalancer | 2023.0.0 | Load Balancer |
| spring-boot-starter-web | 3.2.0 | Web Server (Tomcat) |
| spring-boot-starter-data-jpa | 3.2.0 | ORM (Hibernate) |
| postgresql | Latest | Database Driver |
| h2 | Latest | In-Memory DB |
| lombok | Latest | Annotations |

---

## 🔗 Service Discovery Flow

### 1. Startup Phase

```
Product Service starts
    ↓
EurekaClient sends heartbeat
    ↓
POST /eureka/apps/PRODUCT-SERVICE
    {
      "instance": {
        "instanceId": "localhost:8082",
        "hostName": "localhost",
        "port": 8082,
        "ipAddr": "127.0.0.1"
      }
    }
    ↓
Eureka Server receives registration
    ↓
Eureka Dashboard shows:
   PRODUCT-SERVICE
   └── localhost:8082 [UP]
```

### 2. Heartbeat Phase

```
Every 30 seconds (configurable):
Product Service sends PUT /eureka/apps/PRODUCT-SERVICE/{instanceId}
    ↓
Eureka Server updates last heartbeat time
    ↓
If no heartbeat for 90 seconds → Remove instance
```

### 3. Request Phase

```
Order Service makes REST call
    ↓
RestTemplate intercepts (via @LoadBalanced)
    ↓
Spring Cloud LoadBalancer:
    1. Parse URL: "http://PRODUCT-SERVICE/..."
    2. Query Eureka: GET /eureka/apps/PRODUCT-SERVICE
    3. Get instances: [8082, 8084]
    4. SelectInstance: 8082 (round-robin)
    5. Forward: http://localhost:8082/...
    ↓
Request reaches Product Service (8082)
    ↓
Response returned
```

---

## ⚙️ Configuration Properties

### Eureka Server
```properties
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
eureka.server.enable-self-preservation=false
```

### Product Service
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.prefer-ip-address=true
server.port=${SERVER_PORT:8082}
```

### Order Service
```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
eureka.instance.prefer-ip-address=true
server.port=8080
```

---

## 🚀 Deployment Scenarios

### Scenario 1: Local Development
```
Machine gồm:
- Eureka Server (8761)
- Product Service (8082)
- Product Service (8084)
- Order Service (8080)
```

### Scenario 2: Kubernetes
```
Node 1, Pod 1:
- Product Service (replica-1)

Node 2, Pod 2:
- Product Service (replica-2)

Node 3, Pod 3:
- Order Service

Shared:
- Eureka Server
```

### Scenario 3: AWS EC2
```
EC2 Instance 1:
- Product Service

EC2 Instance 2:
- Product Service

EC2 Instance 3:
- Order Service

Eureka Server on separate instance or RDS
```

---

## 🔐 Advantages of Client-Side Load Balancing

✅ **No Extra Infrastructure**: Không cần load balancer riêng (Nginx, HAProxy)
✅ **Simple**: Dễ cấu hình, dễ debug
✅ **Service Discovery Integration**: Tích hợp tự động với Eureka
✅ **Flexible**: Mỗi client quyết định chọn instance
✅ **Lower Latency**: Không qua intermediary

---

## ⚠️ Disadvantages & Limitations

❌ **Client Complexity**: Logic phán bản tại client
❌ **Scaling Issues**: Mỗi client cần cập nhật instance list
❌ **No Sticky Session**: Mỗi request có thể tới instance khác
❌ **Debugging Complexity**: Khó trace request flow

---

## 🔄 Alternative: Server-Side Load Balancing

```
Client → Nginx (LB) → Product Service (8082)
                   → Product Service (8084)
                   → Product Service (9090)
```

**Ưu điểm:** UI đơn giản, tập trung quản lý
**Nhược điểm:** Extra hop, thêm server cần maintain

---

## 📈 Monitoring & Observability

### Metrics cần theo dõi:

1. **Eureka Metrics:**
   - Number of registered instances
   - Registration response time
   - Heartbeat count

2. **Load Balancer Metrics:**
   - Request distribution per instance
   - Response time per instance
   - Failed requests per instance

3. **Application Metrics:**
   - API response time
   - Error rate
   - Throughput

---

## 🎯 Next Steps

Sau khi hiểu Client-Side Load Balancing, bạn có thể học:

1. **OpenFeign Client** - Thay RestTemplate bằng interface-based clients
2. **Circuit Breaker** - Xử lý failure gracefully
3. **Distributed Tracing** - Debug request flow (Zipkin, Jaeger)
4. **Server-Side Load Balancing** - API Gateway (Spring Cloud Gateway)
5. **Container Orchestration** - Kubernetes deployment

---


