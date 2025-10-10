@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Travel Agency Metrics Dashboard
echo ========================================

echo Collecting metrics from all services...
echo.

REM Function to get metrics
set "services=Eureka:8761 Gateway:8080 Trip:8081 Review:8082 Match:8083 User:8084"

for %%s in (%services%) do (
    for /f "tokens=1,2 delims=:" %%a in ("%%s") do (
        set "service_name=%%a"
        set "service_port=%%b"
        
        echo ========================================
        echo !service_name! Service Metrics (Port !service_port!)
        echo ========================================
        
        REM Get basic metrics
        echo Basic Metrics:
        curl -s "http://localhost:!service_port!/actuator/metrics" 2>nul | echo Response: && type
        echo.
        
        REM Get Prometheus metrics (first 20 lines)
        echo Prometheus Metrics (first 20 lines):
        curl -s "http://localhost:!service_port!/actuator/prometheus" 2>nul | powershell "Get-Content | Select-Object -First 20"
        echo.
        
        REM Get custom monitoring metrics
        echo Custom Monitoring Metrics:
        curl -s "http://localhost:!service_port!/api/!service_name:/=!/monitoring/metrics/summary" 2>nul | echo Response: && type
        echo.
        
        echo Press any key to continue to next service...
        pause >nul
        echo.
    )
)

echo ========================================
echo System Performance Summary
echo ========================================

echo.
echo Memory Usage:
powershell "Get-Process java | Select-Object ProcessName, WorkingSet, CPU | Format-Table"

echo.
echo Disk Space:
powershell "Get-WmiObject -Class Win32_LogicalDisk | Select-Object DeviceID, @{Name='Size(GB)';Expression={[math]::Round($_.Size/1GB,2)}}, @{Name='FreeSpace(GB)';Expression={[math]::Round($_.FreeSpace/1GB,2)}} | Format-Table"

echo.
echo Network Connections:
netstat -an | findstr ":808"

echo.
echo ========================================
echo Quick Health Check
echo ========================================

echo Checking all services quickly...
for %%s in (%services%) do (
    for /f "tokens=1,2 delims=:" %%a in ("%%s") do (
        set "service_name=%%a"
        set "service_port=%%b"
        
        curl -s "http://localhost:!service_port!/actuator/health" >nul 2>&1
        if !errorlevel! equ 0 (
            echo ✓ !service_name! Service is UP
        ) else (
            echo ✗ !service_name! Service is DOWN
        )
    )
)

echo.
echo ========================================
echo Metrics URLs:
echo ========================================
echo Eureka Metrics:       http://localhost:8761/actuator/prometheus
echo Gateway Metrics:      http://localhost:8080/actuator/prometheus
echo Trip Metrics:         http://localhost:8081/actuator/prometheus
echo Review Metrics:       http://localhost:8082/actuator/prometheus
echo Match Metrics:        http://localhost:8083/actuator/prometheus
echo User Metrics:         http://localhost:8084/actuator/prometheus
echo ========================================
pause
