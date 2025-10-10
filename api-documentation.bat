@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Travel Agency API Documentation
echo ========================================

echo Opening API documentation for all services...
echo.

REM Function to open URL in default browser
set "services=Trip Service:8081 Review Service:8082 Match Service:8083 User Service:8084 Gateway:8080"

echo Available API Documentation URLs:
echo.

for %%s in (%services%) do (
    for /f "tokens=1,2 delims=:" %%a in ("%%s") do (
        set "service_name=%%a"
        set "service_port=%%b"
        
        echo !service_name! API Documentation:
        echo http://localhost:!service_port!/swagger-ui/index.html
        echo.
    )
)

echo ========================================
echo Quick Access Menu:
echo ========================================
echo 1. Open Trip Service API Docs
echo 2. Open Review Service API Docs  
echo 3. Open Match Service API Docs
echo 4. Open User Service API Docs
echo 5. Open Gateway API Docs
echo 6. Open All API Docs
echo 7. Exit
echo.

set /p choice="Enter your choice (1-7): "

if "%choice%"=="1" (
    echo Opening Trip Service API Documentation...
    start http://localhost:8081/swagger-ui/index.html
) else if "%choice%"=="2" (
    echo Opening Review Service API Documentation...
    start http://localhost:8082/swagger-ui/index.html
) else if "%choice%"=="3" (
    echo Opening Match Service API Documentation...
    start http://localhost:8083/swagger-ui/index.html
) else if "%choice%"=="4" (
    echo Opening User Service API Documentation...
    start http://localhost:8084/swagger-ui/index.html
) else if "%choice%"=="5" (
    echo Opening Gateway API Documentation...
    start http://localhost:8080/swagger-ui/index.html
) else if "%choice%"=="6" (
    echo Opening All API Documentation...
    start http://localhost:8081/swagger-ui/index.html
    timeout /t 2 /nobreak >nul
    start http://localhost:8082/swagger-ui/index.html
    timeout /t 2 /nobreak >nul
    start http://localhost:8083/swagger-ui/index.html
    timeout /t 2 /nobreak >nul
    start http://localhost:8084/swagger-ui/index.html
    timeout /t 2 /nobreak >nul
    start http://localhost:8080/swagger-ui/index.html
    echo All API documentation pages opened!
) else if "%choice%"=="7" (
    echo Exiting...
    exit /b 0
) else (
    echo Invalid choice. Please try again.
)

echo.
echo ========================================
echo Additional Information:
echo ========================================
echo - Swagger UI provides interactive API testing
echo - You can test endpoints directly from the documentation
echo - All endpoints include detailed descriptions and examples
echo - Circuit Breaker endpoints are documented with fallback scenarios
echo.
echo ========================================
echo API Endpoints Summary:
echo ========================================
echo Trip Service:
echo - POST /api/trips - Create trip
echo - GET /api/trips/{id} - Get trip by ID
echo - GET /api/trips - Get all trips
echo - GET /api/trips/user/{userId} - Get trips by user
echo - GET /api/trips/status/{status} - Get trips by status
echo - GET /api/trips/search?destination=X - Search trips
echo - PUT /api/trips/{id}/status - Update trip status
echo - DELETE /api/trips/{id} - Delete trip
echo - GET /api/trips/{id}/reviews/summary - Get review summary
echo - GET /api/trips/{id}/reviews/summary/async - Get review summary async
echo.
echo Review Service:
echo - POST /api/reviews - Create review
echo - GET /api/reviews/{id} - Get review by ID
echo - GET /api/reviews - Get all reviews
echo - GET /api/reviews/trip/{tripId} - Get reviews by trip
echo - GET /api/reviews/user/{userId} - Get reviews by user
echo - GET /api/reviews/trip/{tripId}/summary - Get trip review summary
echo.
echo Match Service:
echo - POST /api/matches - Create match
echo - GET /api/matches/{id} - Get match by ID
echo - GET /api/matches - Get all matches
echo - GET /api/matches/user/{userId} - Get matches by user
echo - GET /api/matches/trip/{tripId} - Get matches by trip
echo.
echo User Service:
echo - POST /api/users/register - Register user
echo - POST /api/users/login - Login user
echo - GET /api/users/{id} - Get user by ID
echo - GET /api/users - Get all users
echo - PUT /api/users/{id} - Update user
echo - DELETE /api/users/{id} - Delete user
echo.
echo Gateway Routes:
echo - All above endpoints are available via Gateway at /api/{service}/*
echo ========================================
pause
