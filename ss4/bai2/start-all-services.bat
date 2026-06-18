@echo off
REM Start Eureka Server
echo Starting Eureka Server on port 8761...
start cmd /k "cd eureka-server && mvn spring-boot:run"
timeout /t 5

REM Start Customer Service
echo Starting Customer Service on port 8081...
start cmd /k "cd customer-service && mvn spring-boot:run"
timeout /t 3

REM Start Product Service
echo Starting Product Service on port 8082...
start cmd /k "cd product-service && mvn spring-boot:run"
timeout /t 3

REM Start Order Service
echo Starting Order Service on port 8083...
start cmd /k "cd order-service && mvn spring-boot:run"

echo.
echo All services starting...
echo Eureka Dashboard: http://localhost:8761
pause

