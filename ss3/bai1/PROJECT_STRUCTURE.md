# Project Structure Overview

## Cấu trúc toàn bộ hệ thống

```
bai1/
│
├── README.md (File gốc - mô tả bài tập)
├── SETUP.md (Hướng dẫn cài đặt chi tiết)
├── API_TESTING.md (Hướng dẫn kiểm thử API)
├── DOCKER_GUIDE.md (Hướng dẫn sử dụng Docker)
├── .gitignore (Các file/folder cần bỏ qua khi commit)
├── docker-compose.yml (Cấu hình Docker Compose)
│
├── customer-service/
│   ├── pom.xml (Maven configuration)
│   ├── Dockerfile (Docker image configuration)
│   ├── README.md (Service-specific documentation)
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── re/edu/customerservice/
│       │   │       ├── CustomerServiceApplication.java (Main application class)
│       │   │       ├── controller/
│       │   │       │   └── CustomerController.java (REST API endpoints)
│       │   │       ├── service/
│       │   │       │   └── CustomerService.java (Business logic)
│       │   │       ├── repository/
│       │   │       │   └── CustomerRepository.java (Data access layer)
│       │   │       └── entity/
│       │   │           └── Customer.java (Domain entity)
│       │   └── resources/
│       │       └── application.properties (Configuration)
│       └── test/
│           └── (Unit tests)
│
├── product-service/
│   ├── pom.xml
│   ├── Dockerfile
│   ├── README.md
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── re/edu/productservice/
│       │   │       ├── ProductServiceApplication.java
│       │   │       ├── controller/
│       │   │       │   └── ProductController.java
│       │   │       ├── service/
│       │   │       │   └── ProductService.java
│       │   │       ├── repository/
│       │   │       │   └── ProductRepository.java
│       │   │       └── entity/
│       │   │           └── Product.java
│       │   └── resources/
│       │       └── application.properties
│       └── test/
│           └── (Unit tests)
│
└── order-service/
    ├── pom.xml
    ├── Dockerfile
    ├── README.md
    └── src/
        ├── main/
        │   ├── java/
        │   │   └── re/edu/orderservice/
        │   │       ├── OrderServiceApplication.java
        │   │       ├── controller/
        │   │       │   └── OrderController.java
        │   │       ├── service/
        │   │       │   └── OrderService.java
        │   │       ├── repository/
        │   │       │   └── OrderRepository.java
        │   │       └── entity/
        │   │           └── Order.java
        │   └── resources/
        │       └── application.properties
        └── test/
            └── (Unit tests)
```

## Layer Architecture

Mỗi service tuân theo kiến trúc 4 tầng (4-Tier Architecture):

### 1. **Entity Layer** (`entity/`)
- Đại diện cho domain model
- Mapping với database table
- Sử dụng JPA annotations

### 2. **Repository Layer** (`repository/`)
- Data access object (DAO)
- Extends JpaRepository cho CRUD operations
- Custom queries nếu cần

### 3. **Service Layer** (`service/`)
- Business logic
- Xử lý validation
- Transaction management
- Orchestration giữa repositories

### 4. **Controller Layer** (`controller/`)
- REST API endpoints
- Request/Response handling
- HTTP status management

## Design Patterns Applied

### 1. **Service Independence**
```
❌ Order-Service không import Customer class
✅ Order-Service chỉ lưu customerId (Long)
```

### 2. **Repository Pattern**
```java
// Abstraction of data access
public interface CustomerRepository extends JpaRepository<Customer, Long>
```

### 3. **Dependency Injection**
```java
@Service
@RequiredArgsConstructor  // Lombok annotation
public class CustomerService {
    private final CustomerRepository customerRepository;
}
```

### 4. **RESTful API Design**
```
POST   /api/customers          - Create
GET    /api/customers/{id}     - Read
PUT    /api/customers/{id}     - Update
DELETE /api/customers/{id}     - Delete
```

## Database Schema

### Customer Table
```sql
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    address VARCHAR(255)
);
```

### Product Table
```sql
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    stock_quantity INTEGER NOT NULL,
    description TEXT
);
```

### Order Table
```sql
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP NOT NULL,
    total_amount DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL
);
```

## Dependencies

### All Services Use
- **Spring Boot 3.1.5** - Framework
- **Spring Web** - REST support
- **Spring Data JPA** - ORM
- **PostgreSQL Driver** - Database
- **Lombok** - Reduce boilerplate
- **JUnit 5** - Testing

## Port Assignment

| Service | Port | Database |
|---------|------|----------|
| Customer Service | 8081 | customer_db |
| Product Service | 8082 | product_db |
| Order Service | 8083 | order_db |

## Key Files Description

### pom.xml
- Maven project configuration
- Dependency management
- Build plugins

### application.properties
- Server port configuration
- Database connection settings
- JPA/Hibernate configuration
- Logging levels

### *ServiceApplication.java
- Entry point of Spring Boot application
- Component scanning configuration

### Dockerfile
- Container image definition
- Base image: OpenJDK 17
- Jar file copy
- Port exposure

### docker-compose.yml
- Multi-container orchestration
- Service dependencies
- Environment variables
- Volume management
- Network configuration

## Bounded Context (DDD Perspective)

### Customer Domain
- Quản lý dữ liệu khách hàng
- Xác thực và đăng ký

### Product Domain
- Quản lý catalog sản phẩm
- Quản lý tồn kho

### Order Domain
- Quản lý vòng đời đơn hàng
- Tham chiếu (reference) đến Customer qua ID

## Communication Between Services

**Note**: Hiện tại services hoạt động độc lập.

Trong thực tế, các service có thể giao tiếp qua:
1. **REST API calls** (Synchronous)
2. **Message Broker** (RabbitMQ, Kafka) (Asynchronous)
3. **gRPC** (High performance)

## Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn test -P integration
```

### API Tests
- Xem `API_TESTING.md` cho curl examples
- Xem `API_TESTING.md` cho Postman collection

## Deployment

### Local Development
```bash
mvn spring-boot:run
```

### Docker Deployment
```bash
docker-compose up
```

### Production
- Kubernetes (Recommended)
- AWS ECS / Azure Container Apps
- Cloud Run / Heroku

## Next Steps for Enhancement

1. **Service-to-Service Communication**
   - Integrate Customer-Service with Order-Service
   - Implement circuit breaker pattern

2. **Security**
   - Add Spring Security
   - JWT token authentication
   - Role-based access control

3. **API Gateway**
   - Add Spring Cloud Gateway
   - Centralized routing and filtering

4. **Monitoring**
   - Add Spring Actuator
   - Integrate with Prometheus/Grafana
   - ELK stack for logging

5. **Caching**
   - Add Redis cache
   - Endpoint caching

6. **Documentation**
   - Add Springdoc OpenAPI (Swagger)
   - Generate API documentation

7. **Testing**
   - Unit tests for all layers
   - Integration tests
   - Contract testing for APIs

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Microservices Patterns](https://microservices.io/)
- [Docker Documentation](https://docs.docker.com/)

