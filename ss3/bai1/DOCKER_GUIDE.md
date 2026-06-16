# Docker & Docker Compose Guide

## Prerequisites

- Docker Desktop installed ([Download](https://www.docker.com/products/docker-desktop))
- Docker Compose (included with Docker Desktop)

## Build Docker Images

Before running with Docker Compose, build each service:

### 1. Build Customer Service
```bash
cd customer-service
mvn clean package -DskipTests
docker build -t customer-service:1.0.0 .
cd ..
```

### 2. Build Product Service
```bash
cd product-service
mvn clean package -DskipTests
docker build -t product-service:1.0.0 .
cd ..
```

### 3. Build Order Service
```bash
cd order-service
mvn clean package -DskipTests
docker build -t order-service:1.0.0 .
cd ..
```

## Running with Docker Compose

### Start All Services
```bash
docker-compose up
```

### Start Services in Background
```bash
docker-compose up -d
```

### Stop All Services
```bash
docker-compose down
```

### Stop Services and Remove Volumes
```bash
docker-compose down -v
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f customer-service
docker-compose logs -f product-service
docker-compose logs -f order-service
```

## Verify Services are Running

```bash
# Check running containers
docker-compose ps

# Test API endpoints
curl http://localhost:8081/api/customers
curl http://localhost:8082/api/products
curl http://localhost:8083/api/orders
```

## Access PostgreSQL Databases

### Using psql
```bash
# Customer Database
psql -h localhost -p 5432 -U postgres -d customer_db

# Product Database
psql -h localhost -p 5433 -U postgres -d product_db

# Order Database
psql -h localhost -p 5434 -U postgres -d order_db
```

### Using pgAdmin (Web Interface)

1. Add pgAdmin service to docker-compose.yml:
```yaml
pgadmin:
  image: dpage/pgadmin4
  environment:
    PGADMIN_DEFAULT_EMAIL: admin@example.com
    PGADMIN_DEFAULT_PASSWORD: admin
  ports:
    - "5050:80"
  depends_on:
    - customer-db
    - product-db
    - order-db
```

2. Access pgAdmin at `http://localhost:5050`

## Rebuild and Restart Services

### Rebuild without Cache
```bash
docker-compose build --no-cache
docker-compose up
```

### Update and Rebuild Single Service
```bash
# Make code changes
cd customer-service

# Rebuild image
mvn clean package -DskipTests
docker build -t customer-service:1.0.0 .

# Restart service
docker-compose up -d customer-service
```

## Environment Variables

The docker-compose.yml file sets these environment variables:

### Customer Service
```
SPRING_DATASOURCE_URL=jdbc:postgresql://customer-db:5432/customer_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
```

### Product Service
```
SPRING_DATASOURCE_URL=jdbc:postgresql://product-db:5432/product_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
```

### Order Service
```
SPRING_DATASOURCE_URL=jdbc:postgresql://order-db:5432/order_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
```

## Useful Docker Compose Commands

```bash
# Create and start containers
docker-compose up

# Create containers without starting
docker-compose create

# Start previously created containers
docker-compose start

# Stop containers without removing
docker-compose stop

# Restart services
docker-compose restart

# Remove containers but keep images
docker-compose down

# Remove containers and images
docker-compose down --rmi all

# Remove volumes as well
docker-compose down -v

# Show resource usage
docker-compose stats

# Execute command in container
docker-compose exec customer-service bash

# Validate docker-compose.yml
docker-compose config
```

## Troubleshooting

### Services won't start
```bash
# Check logs
docker-compose logs

# Rebuild without cache
docker-compose build --no-cache
docker-compose up
```

### Port conflicts
```bash
# Change ports in docker-compose.yml
# Or stop other services using those ports
docker ps
docker stop <container_id>
```

### Database connection errors
```bash
# Verify database is ready
docker-compose exec customer-db pg_isready -U postgres

# Rebuild and restart
docker-compose down -v
docker-compose up
```

### Permission errors
- Run docker commands with `sudo` (Linux)
- Or add user to docker group: `sudo usermod -aG docker $USER`

## Production Considerations

For production deployment:

1. Use environment-specific configuration files
2. Configure proper resource limits
3. Set up logging aggregation
4. Implement health checks
5. Use container registries
6. Implement proper secrets management
7. Set up monitoring and alerting
8. Use Kubernetes for orchestration

See SETUP.md for more information.

