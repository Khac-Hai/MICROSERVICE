# API Testing Guide

## Postman Collection JSON

Bạn có thể import file này vào Postman để test tất cả các API.

```json
{
  "info": {
    "name": "E-Commerce Microservices",
    "description": "API collection for Customer, Product, and Order Services"
  },
  "item": [
    {
      "name": "Customer Service",
      "item": [
        {
          "name": "Register Customer",
          "request": {
            "method": "POST",
            "url": "http://localhost:8081/api/customers/register",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "raw": "{\n  \"fullName\": \"John Doe\",\n  \"email\": \"john@example.com\",\n  \"password\": \"password123\",\n  \"address\": \"123 Main St\"\n}"
            }
          }
        },
        {
          "name": "Get All Customers",
          "request": {
            "method": "GET",
            "url": "http://localhost:8081/api/customers"
          }
        }
      ]
    },
    {
      "name": "Product Service",
      "item": [
        {
          "name": "Create Product",
          "request": {
            "method": "POST",
            "url": "http://localhost:8082/api/products",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "raw": "{\n  \"name\": \"Laptop\",\n  \"price\": 999.99,\n  \"stockQuantity\": 10,\n  \"description\": \"High-performance laptop\"\n}"
            }
          }
        },
        {
          "name": "Get All Products",
          "request": {
            "method": "GET",
            "url": "http://localhost:8082/api/products"
          }
        }
      ]
    },
    {
      "name": "Order Service",
      "item": [
        {
          "name": "Create Order",
          "request": {
            "method": "POST",
            "url": "http://localhost:8083/api/orders",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "raw": "{\n  \"customerId\": 1,\n  \"totalAmount\": 999.99\n}"
            }
          }
        },
        {
          "name": "Get All Orders",
          "request": {
            "method": "GET",
            "url": "http://localhost:8083/api/orders"
          }
        }
      ]
    }
  ]
}
```

## Testing with cURL

### Customer Service

```bash
# 1. Create a customer
curl -X POST http://localhost:8081/api/customers/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Nguyen Van A",
    "email": "nguyenvana@example.com",
    "password": "Pass@123",
    "address": "123 Nguyen Hue, Ho Chi Minh City"
  }'

# 2. Get all customers
curl http://localhost:8081/api/customers

# 3. Get customer by ID
curl http://localhost:8081/api/customers/1

# 4. Get customer by email
curl http://localhost:8081/api/customers/email/nguyenvana@example.com

# 5. Update customer
curl -X PUT http://localhost:8081/api/customers/1 \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Nguyen Van A Updated",
    "email": "nguyenvana@example.com",
    "password": "NewPass@123",
    "address": "456 Tran Hung Dao, Ho Chi Minh City"
  }'

# 6. Delete customer
curl -X DELETE http://localhost:8081/api/customers/1
```

### Product Service

```bash
# 1. Create a product
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 14",
    "price": 999.99,
    "stockQuantity": 50,
    "description": "Latest Apple iPhone"
  }'

# 2. Get all products
curl http://localhost:8082/api/products

# 3. Get product by ID
curl http://localhost:8082/api/products/1

# 4. Get product by name
curl http://localhost:8082/api/products/name/iPhone%2014

# 5. Update product
curl -X PUT http://localhost:8082/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "iPhone 15",
    "price": 1099.99,
    "stockQuantity": 40,
    "description": "Latest Apple iPhone 15"
  }'

# 6. Update stock
curl -X PATCH "http://localhost:8082/api/products/1/stock?quantity=35"

# 7. Delete product
curl -X DELETE http://localhost:8082/api/products/1
```

### Order Service

```bash
# 1. Create an order
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "totalAmount": 999.99,
    "status": "PENDING"
  }'

# 2. Get all orders
curl http://localhost:8083/api/orders

# 3. Get order by ID
curl http://localhost:8083/api/orders/1

# 4. Get orders by customer ID
curl http://localhost:8083/api/orders/customer/1

# 5. Get orders by status
curl http://localhost:8083/api/orders/status/PENDING

# 6. Update order
curl -X PUT http://localhost:8083/api/orders/1 \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "totalAmount": 1099.99,
    "status": "COMPLETED"
  }'

# 7. Update order status
curl -X PATCH "http://localhost:8083/api/orders/1/status?status=SHIPPED"

# 8. Delete order
curl -X DELETE http://localhost:8083/api/orders/1
```

## Testing Integration Scenario

### Step 1: Create a Customer
```bash
curl -X POST http://localhost:8081/api/customers/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Tran Thi B",
    "email": "tranthib@example.com",
    "password": "Pass@456",
    "address": "789 Ly Thuong Kiet, Hanoi"
  }'

# Response will have customerId: 1
```

### Step 2: Create Products
```bash
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Samsung 65-inch 4K TV",
    "price": 799.99,
    "stockQuantity": 20,
    "description": "Ultra HD Smart TV"
  }'

# Response will have productId: 1

curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sony Headphones",
    "price": 299.99,
    "stockQuantity": 50,
    "description": "Noise-cancelling headphones"
  }'

# Response will have productId: 2
```

### Step 3: Create an Order
```bash
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "totalAmount": 1099.98,
    "status": "PENDING"
  }'

# Response will have orderId: 1
```

### Step 4: Track Order
```bash
# Get the order
curl http://localhost:8083/api/orders/1

# Update order status to Processing
curl -X PATCH "http://localhost:8083/api/orders/1/status?status=PROCESSING"

# Update order status to Shipped
curl -X PATCH "http://localhost:8083/api/orders/1/status?status=SHIPPED"

# Update order status to Delivered
curl -X PATCH "http://localhost:8083/api/orders/1/status?status=DELIVERED"
```

### Step 5: Update Product Stock (after order)
```bash
# Update Sony Headphones stock
curl -X PATCH "http://localhost:8082/api/products/2/stock?quantity=49"
```

## Response Examples

### Successful Customer Creation (201 Created)
```json
{
  "id": 1,
  "fullName": "Nguyen Van A",
  "email": "nguyenvana@example.com",
  "password": "Pass@123",
  "address": "123 Nguyen Hue, Ho Chi Minh City"
}
```

### Successful Product Creation (201 Created)
```json
{
  "id": 1,
  "name": "iPhone 14",
  "price": 999.99,
  "stockQuantity": 50,
  "description": "Latest Apple iPhone"
}
```

### Successful Order Creation (201 Created)
```json
{
  "id": 1,
  "customerId": 1,
  "orderDate": "2024-06-16T10:30:45.123456",
  "totalAmount": 999.99,
  "status": "PENDING"
}
```

### Error Response (404 Not Found)
```json
{
  "timestamp": "2024-06-16T10:30:45.123456",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with id: 999",
  "path": "/api/customers/999"
}
```

## Notes

- Thay thế `localhost` bằng IP/domain của server nếu services chạy trên máy khác
- Port mặc định: Customer (8081), Product (8082), Order (8083)
- Tất cả request body đều yêu cầu `Content-Type: application/json`
- Sử dụng `%20` cho spaces trong URL (từ dùng URL encoding)

