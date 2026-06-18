#!/bin/bash

echo "Starting Microservices..."
echo ""

# Start Eureka Server
echo "Starting Eureka Server on port 8761..."
cd eureka-server && mvn spring-boot:run &
EUREKA_PID=$!
sleep 5

# Start Customer Service
echo "Starting Customer Service on port 8081..."
cd ../customer-service && mvn spring-boot:run &
CUSTOMER_PID=$!
sleep 3

# Start Product Service
echo "Starting Product Service on port 8082..."
cd ../product-service && mvn spring-boot:run &
PRODUCT_PID=$!
sleep 3

# Start Order Service
echo "Starting Order Service on port 8083..."
cd ../order-service && mvn spring-boot:run &
ORDER_PID=$!

echo ""
echo "All services starting..."
echo "Eureka Dashboard: http://localhost:8761"
echo ""
echo "Process IDs:"
echo "  Eureka Server: $EUREKA_PID"
echo "  Customer Service: $CUSTOMER_PID"
echo "  Product Service: $PRODUCT_PID"
echo "  Order Service: $ORDER_PID"
echo ""
echo "To stop all services, run: kill $EUREKA_PID $CUSTOMER_PID $PRODUCT_PID $ORDER_PID"

# Wait for all processes
wait

