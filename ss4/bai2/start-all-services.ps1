# Start Microservices with PowerShell

Write-Host "Starting Microservices..." -ForegroundColor Green
Write-Host ""

# Start Eureka Server
Write-Host "Starting Eureka Server on port 8761..." -ForegroundColor Cyan
Start-Process -FilePath "powershell" -ArgumentList "-NoExit -Command `"cd '$PSScriptRoot\eureka-server'; mvn spring-boot:run`""
Start-Sleep -Seconds 5

# Start Customer Service
Write-Host "Starting Customer Service on port 8081..." -ForegroundColor Cyan
Start-Process -FilePath "powershell" -ArgumentList "-NoExit -Command `"cd '$PSScriptRoot\customer-service'; mvn spring-boot:run`""
Start-Sleep -Seconds 3

# Start Product Service
Write-Host "Starting Product Service on port 8082..." -ForegroundColor Cyan
Start-Process -FilePath "powershell" -ArgumentList "-NoExit -Command `"cd '$PSScriptRoot\product-service'; mvn spring-boot:run`""
Start-Sleep -Seconds 3

# Start Order Service
Write-Host "Starting Order Service on port 8083..." -ForegroundColor Cyan
Start-Process -FilePath "powershell" -ArgumentList "-NoExit -Command `"cd '$PSScriptRoot\order-service'; mvn spring-boot:run`""

Write-Host ""
Write-Host "All services starting..." -ForegroundColor Green
Write-Host "Eureka Dashboard: http://localhost:8761" -ForegroundColor Yellow
Write-Host ""
Write-Host "Services ports:" -ForegroundColor Yellow
Write-Host "  Eureka Server: 8761" -ForegroundColor White
Write-Host "  Customer Service: 8081" -ForegroundColor White
Write-Host "  Product Service: 8082" -ForegroundColor White
Write-Host "  Order Service: 8083" -ForegroundColor White

