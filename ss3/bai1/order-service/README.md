# Order Service

## Description
Order-Service quản lý đơn hàng trong hệ thống E-Commerce. Service này chỉ lưu customerId (không phụ thuộc trực tiếp vào Customer-Service) để đảm bảo tính độc lập.

## Technologies
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok

## API Endpoints

### Create Order
`POST /api/orders`

### Get Order by ID
`GET /api/orders/{id}`

### Get Orders by Customer ID
`GET /api/orders/customer/{customerId}`

### Get Orders by Status
`GET /api/orders/status/{status}`

### Get All Orders
`GET /api/orders`

### Update Order
`PUT /api/orders/{id}`

### Update Order Status
`PATCH /api/orders/{id}/status?status={status}`

### Delete Order
`DELETE /api/orders/{id}`

## Database Configuration
- Host: localhost
- Port: 5432
- Database: order_db
- Username: postgres
- Password: password

## Design Pattern
Order-Service chỉ lưu `customerId` (Long) thay vì lưu trữ toàn bộ Customer object. Điều này đảm bảo:
- Service Independence: Order-Service hoàn toàn độc lập
- Loose Coupling: Không phụ thuộc vào cấu trúc Customer
- Easy Deployment: Có thể triển khai riêng lẻ

## Build & Run

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

The service will start on: `http://localhost:8083`

