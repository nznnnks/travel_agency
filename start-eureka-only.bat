@echo off
echo ========================================
echo Starting Eureka Server Only
echo ========================================

echo Starting Eureka Server on port 8761...
start "Eureka Server" cmd /k "cd /d %~dp0eureka-server && echo Starting Eureka Server... && ..\mvnw.cmd spring-boot:run"

echo.
echo Eureka Server is starting...
echo Please wait 60-90 seconds for it to fully start
echo.
echo Eureka Dashboard: http://localhost:8761
echo.
echo Press any key to continue...
pause >nul
