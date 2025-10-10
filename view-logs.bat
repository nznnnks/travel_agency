@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Travel Agency Logs Viewer
echo ========================================

echo Available log files:
echo.

REM Check if logs directory exists
if not exist "logs" (
    echo No logs directory found. Creating it...
    mkdir logs
    echo Logs directory created.
    echo.
)

REM List available log files
echo Current log files:
dir /b logs\*.log 2>nul
echo.

echo ========================================
echo Log Viewing Options:
echo ========================================
echo 1. View Eureka Server logs
echo 2. View API Gateway logs
echo 3. View Trip Service logs
echo 4. View Review Service logs
echo 5. View Match Service logs
echo 6. View User Service logs
echo 7. View Error logs
echo 8. View all logs (tail)
echo 9. Exit
echo.

set /p choice="Enter your choice (1-9): "

if "%choice%"=="1" (
    echo Viewing Eureka Server logs...
    if exist "logs\application.log" (
        type "logs\application.log"
    ) else (
        echo No Eureka Server logs found.
    )
) else if "%choice%"=="2" (
    echo Viewing API Gateway logs...
    if exist "logs\gateway.log" (
        type "logs\gateway.log"
    ) else (
        echo No API Gateway logs found.
    )
) else if "%choice%"=="3" (
    echo Viewing Trip Service logs...
    if exist "logs\trip-service.log" (
        type "logs\trip-service.log"
    ) else (
        echo No Trip Service logs found.
    )
) else if "%choice%"=="4" (
    echo Viewing Review Service logs...
    if exist "logs\review-service.log" (
        type "logs\review-service.log"
    ) else (
        echo No Review Service logs found.
    )
) else if "%choice%"=="5" (
    echo Viewing Match Service logs...
    if exist "logs\match-service.log" (
        type "logs\match-service.log"
    ) else (
        echo No Match Service logs found.
    )
) else if "%choice%"=="6" (
    echo Viewing User Service logs...
    if exist "logs\user-service.log" (
        type "logs\user-service.log"
    ) else (
        echo No User Service logs found.
    )
) else if "%choice%"=="7" (
    echo Viewing Error logs...
    echo.
    echo Eureka Server Errors:
    if exist "logs\error.log" (
        type "logs\error.log"
    ) else (
        echo No Eureka Server error logs found.
    )
    echo.
    echo Gateway Errors:
    if exist "logs\gateway-error.log" (
        type "logs\gateway-error.log"
    ) else (
        echo No Gateway error logs found.
    )
    echo.
    echo Trip Service Errors:
    if exist "logs\trip-service-error.log" (
        type "logs\trip-service-error.log"
    ) else (
        echo No Trip Service error logs found.
    )
    echo.
    echo Review Service Errors:
    if exist "logs\review-service-error.log" (
        type "logs\review-service-error.log"
    ) else (
        echo No Review Service error logs found.
    )
    echo.
    echo Match Service Errors:
    if exist "logs\match-service-error.log" (
        type "logs\match-service-error.log"
    ) else (
        echo No Match Service error logs found.
    )
    echo.
    echo User Service Errors:
    if exist "logs\user-service-error.log" (
        type "logs\user-service-error.log"
    ) else (
        echo No User Service error logs found.
    )
) else if "%choice%"=="8" (
    echo Viewing all logs (last 50 lines each)...
    echo.
    echo ========================================
    echo Eureka Server Logs:
    echo ========================================
    if exist "logs\application.log" (
        powershell "Get-Content 'logs\application.log' | Select-Object -Last 50"
    ) else (
        echo No Eureka Server logs found.
    )
    echo.
    echo ========================================
    echo API Gateway Logs:
    echo ========================================
    if exist "logs\gateway.log" (
        powershell "Get-Content 'logs\gateway.log' | Select-Object -Last 50"
    ) else (
        echo No API Gateway logs found.
    )
    echo.
    echo ========================================
    echo Trip Service Logs:
    echo ========================================
    if exist "logs\trip-service.log" (
        powershell "Get-Content 'logs\trip-service.log' | Select-Object -Last 50"
    ) else (
        echo No Trip Service logs found.
    )
    echo.
    echo ========================================
    echo Review Service Logs:
    echo ========================================
    if exist "logs\review-service.log" (
        powershell "Get-Content 'logs\review-service.log' | Select-Object -Last 50"
    ) else (
        echo No Review Service logs found.
    )
    echo.
    echo ========================================
    echo Match Service Logs:
    echo ========================================
    if exist "logs\match-service.log" (
        powershell "Get-Content 'logs\match-service.log' | Select-Object -Last 50"
    ) else (
        echo No Match Service logs found.
    )
    echo.
    echo ========================================
    echo User Service Logs:
    echo ========================================
    if exist "logs\user-service.log" (
        powershell "Get-Content 'logs\user-service.log' | Select-Object -Last 50"
    ) else (
        echo No User Service logs found.
    )
) else if "%choice%"=="9" (
    echo Exiting...
    exit /b 0
) else (
    echo Invalid choice. Please try again.
)

echo.
echo ========================================
pause
