@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Travel Agency Services Monitoring
echo ========================================

echo Checking all services health and status...
echo.

REM Function to check service
set "services=Eureka:8761 Gateway:8080 Trip:8081 Review:8082 Match:8083 User:8084"

for %%s in (%services%) do (
    for /f "tokens=1,2 delims=:" %%a in ("%%s") do (
        set "service_name=%%a"
        set "service_port=%%b"
        
        echo Checking !service_name! Service (Port !service_port!)...
        
        REM Check health endpoint
        curl -s "http://localhost:!service_port!/actuator/health" >nul 2>&1
        if !errorlevel! equ 0 (
            echo ✓ !service_name! Service is UP
        ) else (
            echo ✗ !service_name! Service is DOWN
        )
        
        REM Check custom monitoring endpoint
        curl -s "http://localhost:!service_port!/api/!service_name:/=!/monitoring/status" >nul 2>&1
        if !errorlevel! equ 0 (
            echo ✓ !service_name! Monitoring endpoint is accessible
        ) else (
            echo ✗ !service_name! Monitoring endpoint is not accessible
        )
        
        echo.
    )
)

echo ========================================
echo Detailed Service Information
echo ========================================

echo.
echo Eureka Server Status:
curl -s "http://localhost:8761/actuator/health" 2>nul | echo Response: && type

echo.
echo API Gateway Status:
curl -s "http://localhost:8080/actuator/health" 2>nul | echo Response: && type

echo.
echo Trip Service Status:
curl -s "http://localhost:8081/api/trips/monitoring/status" 2>nul | echo Response: && type

echo.
echo Review Service Status:
curl -s "http://localhost:8082/api/reviews/monitoring/status" 2>nul | echo Response: && type

echo.
echo Match Service Status:
curl -s "http://localhost:8083/api/matches/monitoring/status" 2>nul | echo Response: && type

echo.
echo User Service Status:
curl -s "http://localhost:8084/api/users/monitoring/status" 2>nul | echo Response: && type

echo.
echo ========================================
echo Monitoring URLs:
echo ========================================
echo Eureka Dashboard:     http://localhost:8761
echo Gateway Health:       http://localhost:8080/actuator/health
echo Trip Service Health:  http://localhost:8081/actuator/health
echo Review Service Health: http://localhost:8082/actuator/health
echo Match Service Health: http://localhost:8083/actuator/health
echo User Service Health:  http://localhost:8084/actuator/health
echo.
echo Prometheus Metrics:
echo Gateway Metrics:      http://localhost:8080/actuator/prometheus
echo Trip Metrics:         http://localhost:8081/actuator/prometheus
echo Review Metrics:       http://localhost:8082/actuator/prometheus
echo Match Metrics:        http://localhost:8083/actuator/prometheus
echo User Metrics:         http://localhost:8084/actuator/prometheus
echo ========================================
pause
