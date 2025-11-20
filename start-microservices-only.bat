@echo off
echo ========================================
echo Starting Microservices Only
echo (Eureka Server must be running first)
echo ========================================

echo.
echo Starting microservices...
echo.

echo Starting Trip Service on port 8081...
start "Trip Service" cmd /k "cd /d %~dp0trip-service && echo Starting Trip Service... && ..\mvnw.cmd spring-boot:run"
timeout /t 15 /nobreak >nul

echo Starting Review Service on port 8082...
start "Review Service" cmd /k "cd /d %~dp0review-service && echo Starting Review Service... && ..\mvnw.cmd spring-boot:run"
timeout /t 15 /nobreak >nul

echo Starting Match Service on port 8083...
start "Match Service" cmd /k "cd /d %~dp0match-service && echo Starting Match Service... && ..\mvnw.cmd spring-boot:run"
timeout /t 15 /nobreak >nul

echo Starting User Service on port 8084...
start "User Service" cmd /k "cd /d %~dp0user-service && echo Starting User Service... && ..\mvnw.cmd spring-boot:run"
timeout /t 15 /nobreak >nul

echo Starting API Gateway on port 8080...
start "API Gateway" cmd /k "cd /d %~dp0api-gateway && echo Starting API Gateway... && ..\mvnw.cmd spring-boot:run"

echo.
echo ========================================
echo All microservices are starting...
echo.
echo Service URLs:
echo - Eureka Server:   http://localhost:8761
echo - API Gateway:     http://localhost:8080
echo - Trip Service:    http://localhost:8081/api/trips
echo - Review Service: http://localhost:8082/api/reviews  
echo - Match Service:  http://localhost:8083/api/matches
echo - User Service:   http://localhost:8084/api/users
echo.
echo Wait 60-90 seconds for services to fully start...
echo Then check Eureka Dashboard: http://localhost:8761
echo ========================================
pause
