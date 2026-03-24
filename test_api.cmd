@echo off
REM Service Booking Backend API - Manual Testing Script
REM This script provides easy curl commands for testing all 20 APIs

setlocal enabledelayedexpansion

REM Configuration
set BASE_URL=http://localhost:8081
set USER_TOKEN=
set PROVIDER_TOKEN=
set ADMIN_TOKEN=

REM Colors (for Windows cmd)
set SUCCESS=[SUCCESS]
set ERROR=[ERROR]
set INFO=[INFO]

echo.
echo ========================================
echo Service Booking Backend - API Test Suite
echo ========================================
echo.

REM ============ AUTHENTICATION TESTS ============

:menu
cls
echo.
echo ===== API TESTING MENU =====
echo.
echo 1. User Registration
echo 2. User Login
echo 3. Get User Dashboard
echo 4. Get All Services
echo 5. Rate Provider
echo 6. Get My Bookings
echo 7. Create Booking
echo 8. Cancel Booking
echo 9. Provider Registration
echo 10. Provider Login
echo 11. Get Provider Jobs
echo 12. Accept Job
echo 13. Complete Job
echo 14. Get Provider Earnings
echo 15. Toggle Availability
echo 16. Get Admin Dashboard
echo 17. Get All Bookings (Admin)
echo 18. Update Booking Status
echo 19. Get All Providers (Admin)
echo 20. Approve Provider
echo 21. Add Service
echo 22. Assign Provider to Booking
echo 23. Get Analytics
echo 0. Exit
echo.
set /p choice="Enter your choice (0-23): "

if "%choice%"=="0" goto :eof
if "%choice%"=="1" goto :user_register
if "%choice%"=="2" goto :user_login
if "%choice%"=="3" goto :user_dashboard
if "%choice%"=="4" goto :get_services
if "%choice%"=="5" goto :rate_provider
if "%choice%"=="6" goto :get_my_bookings
if "%choice%"=="7" goto :create_booking
if "%choice%"=="8" goto :cancel_booking
if "%choice%"=="9" goto :provider_register
if "%choice%"=="10" goto :provider_login
if "%choice%"=="11" goto :get_provider_jobs
if "%choice%"=="12" goto :accept_job
if "%choice%"=="13" goto :complete_job
if "%choice%"=="14" goto :get_earnings
if "%choice%"=="15" goto :toggle_availability
if "%choice%"=="16" goto :admin_dashboard
if "%choice%"=="17" goto :get_all_bookings
if "%choice%"=="18" goto :update_booking_status
if "%choice%"=="19" goto :get_all_providers
if "%choice%"=="20" goto :approve_provider
if "%choice%"=="21" goto :add_service
if "%choice%"=="22" goto :assign_provider
if "%choice%"=="23" goto :get_analytics

echo Invalid choice. Please try again.
pause
goto :menu

:user_register
echo.
echo === USER REGISTRATION ===
set /p name="Enter name: "
set /p email="Enter email: "
set /p password="Enter password: "

curl -X POST %BASE_URL%/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"%name%\",\"email\":\"%email%\",\"password\":\"%password%\"}"

echo.
pause
goto :menu

:user_login
echo.
echo === USER LOGIN ===
set /p email="Enter email: "
set /p password="Enter password: "

curl -X POST %BASE_URL%/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"%email%\",\"password\":\"%password%\"}"

echo.
echo Please copy the token from the response above.
set /p USER_TOKEN="Enter the token here: "
echo Token saved: %USER_TOKEN%

echo.
pause
goto :menu

:user_dashboard
echo.
echo === GET USER DASHBOARD ===
if "%USER_TOKEN%"=="" (
  echo Error: User token not set. Please login first (option 2).
  pause
  goto :menu
)

curl -X GET %BASE_URL%/api/user/dashboard ^
  -H "Authorization: Bearer %USER_TOKEN%"

echo.
pause
goto :menu

:get_services
echo.
echo === GET ALL SERVICES ===

curl -X GET %BASE_URL%/api/user/services

echo.
pause
goto :menu

:rate_provider
echo.
echo === RATE PROVIDER ===
if "%USER_TOKEN%"=="" (
  echo Error: User token not set. Please login first (option 2).
  pause
  goto :menu
)

set /p provider_id="Enter provider ID: "
set /p score="Enter score (1-5): "
set /p review="Enter review: "

curl -X POST %BASE_URL%/api/user/rate-provider ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %USER_TOKEN%" ^
  -d "{\"providerId\":%provider_id%,\"score\":%score%,\"review\":\"%review%\"}"

echo.
pause
goto :menu

:get_my_bookings
echo.
echo === GET MY BOOKINGS ===
if "%USER_TOKEN%"=="" (
  echo Error: User token not set. Please login first (option 2).
  pause
  goto :menu
)

curl -X GET %BASE_URL%/api/user/my-bookings ^
  -H "Authorization: Bearer %USER_TOKEN%"

echo.
pause
goto :menu

:create_booking
echo.
echo === CREATE BOOKING ===
if "%USER_TOKEN%"=="" (
  echo Error: User token not set. Please login first (option 2).
  pause
  goto :menu
)

set /p service_id="Enter service ID: "
set /p booking_date="Enter booking date (YYYY-MM-DD): "
set /p time_slot="Enter time slot (e.g., 10:00 AM - 12:00 PM): "
set /p address="Enter address: "
set /p notes="Enter notes: "

curl -X POST %BASE_URL%/api/user/book ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %USER_TOKEN%" ^
  -d "{\"serviceId\":%service_id%,\"bookingDate\":\"%booking_date%\",\"timeSlot\":\"%time_slot%\",\"address\":\"%address%\",\"notes\":\"%notes%\"}"

echo.
pause
goto :menu

:cancel_booking
echo.
echo === CANCEL BOOKING ===
if "%USER_TOKEN%"=="" (
  echo Error: User token not set. Please login first (option 2).
  pause
  goto :menu
)

set /p booking_id="Enter booking ID to cancel: "

curl -X PUT %BASE_URL%/api/user/cancel/%booking_id% ^
  -H "Authorization: Bearer %USER_TOKEN%"

echo.
pause
goto :menu

:provider_register
echo.
echo === PROVIDER REGISTRATION ===
set /p name="Enter provider name: "
set /p email="Enter email: "
set /p phone="Enter phone (10 digits): "
set /p category="Enter category (e.g., Plumbing): "
set /p experience="Enter years of experience: "
set /p password="Enter password: "

curl -X POST %BASE_URL%/api/provider/register ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"%name%\",\"email\":\"%email%\",\"phone\":\"%phone%\",\"category\":\"%category%\",\"experience\":%experience%,\"password\":\"%password%\"}"

echo.
pause
goto :menu

:provider_login
echo.
echo === PROVIDER LOGIN ===
set /p email="Enter provider email: "
set /p password="Enter password: "

curl -X POST %BASE_URL%/api/provider/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"%email%\",\"password\":\"%password%\"}"

echo.
echo Please copy the token from the response above.
set /p PROVIDER_TOKEN="Enter the token here: "
echo Token saved: %PROVIDER_TOKEN%

echo.
pause
goto :menu

:get_provider_jobs
echo.
echo === GET PROVIDER JOBS ===
if "%PROVIDER_TOKEN%"=="" (
  echo Error: Provider token not set. Please login first (option 10).
  pause
  goto :menu
)

curl -X GET %BASE_URL%/api/provider/my-jobs ^
  -H "Authorization: Bearer %PROVIDER_TOKEN%"

echo.
pause
goto :menu

:accept_job
echo.
echo === ACCEPT JOB ===
if "%PROVIDER_TOKEN%"=="" (
  echo Error: Provider token not set. Please login first (option 10).
  pause
  goto :menu
)

set /p booking_id="Enter booking ID to accept: "

curl -X PUT %BASE_URL%/api/provider/accept-job/%booking_id% ^
  -H "Authorization: Bearer %PROVIDER_TOKEN%"

echo.
pause
goto :menu

:complete_job
echo.
echo === COMPLETE JOB ===
if "%PROVIDER_TOKEN%"=="" (
  echo Error: Provider token not set. Please login first (option 10).
  pause
  goto :menu
)

set /p booking_id="Enter booking ID to complete: "

curl -X PUT %BASE_URL%/api/provider/complete-job/%booking_id% ^
  -H "Authorization: Bearer %PROVIDER_TOKEN%"

echo.
pause
goto :menu

:get_earnings
echo.
echo === GET PROVIDER EARNINGS ===
if "%PROVIDER_TOKEN%"=="" (
  echo Error: Provider token not set. Please login first (option 10).
  pause
  goto :menu
)

curl -X GET %BASE_URL%/api/provider/earnings ^
  -H "Authorization: Bearer %PROVIDER_TOKEN%"

echo.
pause
goto :menu

:toggle_availability
echo.
echo === TOGGLE AVAILABILITY ===
if "%PROVIDER_TOKEN%"=="" (
  echo Error: Provider token not set. Please login first (option 10).
  pause
  goto :menu
)

curl -X PUT %BASE_URL%/api/provider/toggle-availability ^
  -H "Authorization: Bearer %PROVIDER_TOKEN%"

echo.
pause
goto :menu

:admin_dashboard
echo.
echo === GET ADMIN DASHBOARD ===
if "%ADMIN_TOKEN%"=="" (
  echo Error: Admin token not set. Please login as admin first.
  pause
  goto :menu
)

curl -X GET %BASE_URL%/api/admin/dashboard ^
  -H "Authorization: Bearer %ADMIN_TOKEN%"

echo.
pause
goto :menu

:get_all_bookings
echo.
echo === GET ALL BOOKINGS (ADMIN) ===
if "%ADMIN_TOKEN%"=="" (
  echo Error: Admin token not set. Please login as admin first.
  pause
  goto :menu
)

curl -X GET %BASE_URL%/api/admin/bookings ^
  -H "Authorization: Bearer %ADMIN_TOKEN%"

echo.
pause
goto :menu

:update_booking_status
echo.
echo === UPDATE BOOKING STATUS ===
if "%ADMIN_TOKEN%"=="" (
  echo Error: Admin token not set. Please login as admin first.
  pause
  goto :menu
)

set /p booking_id="Enter booking ID: "
set /p status="Enter status (PENDING, ASSIGNED, ACCEPTED, COMPLETED, CANCELLED): "

curl -X PUT %BASE_URL%/api/admin/booking/%booking_id%/status ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %ADMIN_TOKEN%" ^
  -d "{\"status\":\"%status%\"}"

echo.
pause
goto :menu

:get_all_providers
echo.
echo === GET ALL PROVIDERS (ADMIN) ===
if "%ADMIN_TOKEN%"=="" (
  echo Error: Admin token not set. Please login as admin first.
  pause
  goto :menu
)

curl -X GET %BASE_URL%/api/admin/providers ^
  -H "Authorization: Bearer %ADMIN_TOKEN%"

echo.
pause
goto :menu

:approve_provider
echo.
echo === APPROVE PROVIDER ===
if "%ADMIN_TOKEN%"=="" (
  echo Error: Admin token not set. Please login as admin first.
  pause
  goto :menu
)

set /p provider_id="Enter provider ID: "

curl -X PUT %BASE_URL%/api/admin/provider/%provider_id%/approve ^
  -H "Authorization: Bearer %ADMIN_TOKEN%"

echo.
pause
goto :menu

:add_service
echo.
echo === ADD SERVICE ===
if "%ADMIN_TOKEN%"=="" (
  echo Error: Admin token not set. Please login as admin first.
  pause
  goto :menu
)

set /p name="Enter service name: "
set /p description="Enter description: "
set /p price="Enter price: "

curl -X POST %BASE_URL%/api/admin/add-service ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %ADMIN_TOKEN%" ^
  -d "{\"name\":\"%name%\",\"description\":\"%description%\",\"price\":%price%}"

echo.
pause
goto :menu

:assign_provider
echo.
echo === ASSIGN PROVIDER TO BOOKING ===
if "%ADMIN_TOKEN%"=="" (
  echo Error: Admin token not set. Please login as admin first.
  pause
  goto :menu
)

set /p booking_id="Enter booking ID: "
set /p provider_id="Enter provider ID: "

curl -X PUT %BASE_URL%/api/admin/booking/%booking_id%/assign-provider ^
  -H "Content-Type: application/json" ^
  -H "Authorization: Bearer %ADMIN_TOKEN%" ^
  -d "{\"providerId\":%provider_id%}"

echo.
pause
goto :menu

:get_analytics
echo.
echo === GET ANALYTICS ===

curl -X GET %BASE_URL%/api/admin/analytics

echo.
pause
goto :menu
