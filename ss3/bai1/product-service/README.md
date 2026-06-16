# Product Service

## Description
Product-Service quản lý thông tin sản phẩm trong hệ thống E-Commerce.

## Technologies
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok

## API Endpoints

### Create Product
`POST /api/products`

### Get Product by ID
`GET /api/products/{id}`

### Get Product by Name
`GET /api/products/name/{name}`

### Get All Products
`GET /api/products`

### Update Product
`PUT /api/products/{id}`

### Update Stock
`PATCH /api/products/{id}/stock?quantity={quantity}`

### Delete Product
`DELETE /api/products/{id}`

## Database Configuration
- Host: localhost
- Port: 5432
- Database: product_db
- Username: postgres
- Password: password

## Build & Run

### Build
```bash
mvn clean install
```

### Run
```bash
mvn spring-boot:run
```

The service will start on: `http://localhost:8082`

