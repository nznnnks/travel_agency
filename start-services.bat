@echo off
echo ========================================
echo Travel Agency Microservices Launcher
echo ========================================

echo.
echo Starting Trip Service on port 8081...
start "Trip Service" cmd /k "cd /d %~dp0trip-service && mvn spring-boot:run"

echo.
echo Starting Review Service on port 8082...
start "Review Service" cmd /k "cd /d %~dp0review-service && mvn spring-boot:run"

echo.
echo Starting Match Service on port 8083...
start "Match Service" cmd /k "cd /d %~dp0match-service && mvn spring-boot:run"

echo.
echo Starting User Service on port 8084...
start "User Service" cmd /k "cd /d %~dp0user-service && mvn spring-boot:run"

echo.
echo All services are starting...
echo Trip Service: http://localhost:8081
echo Review Service: http://localhost:8082
echo Match Service: http://localhost:8083
echo User Service: http://localhost:8084
echo.
echo Wait for services to start, then run test-all-services.bat
echo ========================================
