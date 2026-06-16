# Quick Start Guide

## 5 Minute Setup

### Prerequisites Check
```bash
java -version        # Should be 17+
mvn -version         # Should be 3.6.0+
```

### 1. Setup PostgreSQL (One-time)

#### Option A: Local PostgreSQL
```bash
# If PostgreSQL is running, create databases
psql -U postgres -c "CREATE DATABASE customer_db;"
psql -U postgres -c "CREATE DATABASE product_db;"
psql -U postgres -c "CREATE DATABASE order_db;"
```

#### Option B: Docker PostgreSQL
```bash
# Run PostgreSQL in Docker
docker run --name postgres-ecommerce -e POSTGRES_PASSWORD=password -p 5432:5432 -d postgres:15

# Then create databases
docker exec postgres-ecommerce psql -U postgres -c "CREATE DATABASE customer_db;"
docker exec postgres-ecommerce psql -U postgres -c "CREATE DATABASE product_db;"
docker exec postgres-ecommerce psql -U postgres -c "CREATE DATABASE order_db;"
```

### 2. Build All Services

```bash
# Customer Service
cd customer-service && mvn clean install && cd ..

# Product Service
cd product-service && mvn clean install && cd ..

# Order Service
cd order-service && mvn clean install && cd ..
```

### 3. Run Services

**Option A: Terminal (3 separate terminals)**

Terminal 1:
```bash
cd customer-service && mvn spring-boot:run
```

Terminal 2:
```bash
cd product-service && mvn spring-boot:run
```

Terminal 3:
```bash
cd order-service && mvn spring-boot:run
```

**Option B: IDE (IntelliJ IDEA)**

1. Right-click `*ServiceApplication.java`
2. Select "Run"
3. Repeat for all 3 services

### 4. Test Services

```bash
# Customer Service
curl http://localhost:8081/api/customers

# Product Service
curl http://localhost:8082/api/products

# Order Service
curl http://localhost:8083/api/orders
```

## Common Tasks

### Register a Customer
```bash
curl -X POST http://localhost:8081/api/customers/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "address": "123 Main St"
  }'
```

### Create a Product
```bash
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "price": 999.99,
    "stockQuantity": 10,
    "description": "High-performance laptop"
  }'
```

### Create an Order
```bash
curl -X POST http://localhost:8083/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "totalAmount": 999.99
  }'
```

## Troubleshooting Quick Fixes

### Port 8081/8082/8083 already in use
```bash
# Windows
netstat -ano | findstr :8081

# macOS/Linux
lsof -i :8081

# Kill process
# Windows: taskkill /PID <process_id> /F
# macOS/Linux: kill -9 <process_id>
```

### Database connection error
```bash
# Check PostgreSQL is running
psql -U postgres -c "SELECT 1;"

# Verify databases exist
psql -U postgres -l | grep -E "customer_db|product_db|order_db"
```

### Maven build fails
```bash
# Clear cache
rm -rf ~/.m2/repository
mvn clean install
```

## Using Docker Compose (Easiest)

If you have Docker installed:

```bash
# Build all services
cd customer-service && mvn clean package -DskipTests && docker build -t customer-service:1.0.0 . && cd ..
cd product-service && mvn clean package -DskipTests && docker build -t product-service:1.0.0 . && cd ..
cd order-service && mvn clean package -DskipTests && docker build -t order-service:1.0.0 . && cd ..

# Start everything
docker-compose up

# Check services
curl http://localhost:8081/api/customers
curl http://localhost:8082/api/products
curl http://localhost:8083/api/orders
```

## Project Files Explained

| File | Purpose |
|------|---------|
| README.md | Original assignment description |
| SETUP.md | Detailed setup guide |
| QUICK_START.md | This file - quick start |
| API_TESTING.md | API examples and testing |
| DOCKER_GUIDE.md | Docker/Docker Compose guide |
| PROJECT_STRUCTURE.md | Architecture and structure |
| docker-compose.yml | Docker Compose configuration |

## Useful Resources

- **API Testing**: See `API_TESTING.md` for Postman collection
- **Docker**: See `DOCKER_GUIDE.md` for Docker setup
- **Full Setup**: See `SETUP.md` for complete guide
- **Architecture**: See `PROJECT_STRUCTURE.md` for architecture details

## Project Status

All services are ready to:
- ✅ Build with Maven
- ✅ Run standalone
- ✅ Run with Docker
- ✅ Connect to PostgreSQL
- ✅ Expose REST APIs
- ✅ Perform CRUD operations

## Next: API Testing

See `API_TESTING.md` for:
- curl examples
- Postman collection
- Integration scenarios
- Response examples

Enjoy your E-Commerce Microservices! 🚀

