# Customer Service

## Description
Customer-Service quản lý thông tin khách hàng trong hệ thống E-Commerce.

## Technologies
- Java 17
- Spring Boot 3.1.5
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok

## API Endpoints

### Register Customer
`POST /api/customers/register`

### Get Customer by ID
`GET /api/customers/{id}`

### Get Customer by Email
`GET /api/customers/email/{email}`

### Get All Customers
`GET /api/customers`

### Update Customer
`PUT /api/customers/{id}`

### Delete Customer
`DELETE /api/customers/{id}`

## Database Configuration
- Host: localhost
- Port: 5432
- Database: customer_db
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

The service will start on: `http://localhost:8081`

