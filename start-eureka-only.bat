@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Starting Eureka Server Only
echo ========================================

REM Проверяем, запущен ли Eureka
echo Checking Eureka Server...
curl -s http://localhost:8761 >nul 2>&1
if %errorlevel% neq 0 (
    echo Eureka Server is not running, starting it...
    echo Starting Eureka Server on port 8761...
    start "Eureka Server" cmd /k "cd /d %~dp0eureka-server && echo Starting Eureka Server... && mvn spring-boot:run"
    echo Waiting 60 seconds for Eureka Server to fully start...
    timeout /t 60 /nobreak >nul
    
    REM Проверяем, что Eureka запустился
    echo Testing Eureka Server...
    curl -s http://localhost:8761 >nul 2>&1
    if %errorlevel% equ 0 (
        echo ✓ Eureka Server is running successfully
        echo You can now start microservices using start-services.bat
    ) else (
        echo ✗ Eureka Server failed to start
        echo Check the Eureka Server console window for errors
    )
) else (
    echo ✓ Eureka Server is already running
)

echo.
echo Eureka Dashboard: http://localhost:8761
echo.
pause
