# API Testing Quick Reference

## 🚀 Quick Start

### 1. Start the Application
```bash
cd D:\Quickfix\bookingapp\bookingapp
mvnw.cmd spring-boot:run
```

### 2. Run Automated Tests
```bash
mvn test
```

### 3. Open Manual Test Script
```bash
cd D:\Quickfix\bookingapp\bookingapp
test_api.cmd
```

---

## 📋 Essential API Commands

### User Registration
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"john@test.com","password":"pass123"}'
```

### User Login
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@test.com","password":"pass123"}'
```

### Get Services
```bash
curl -X GET http://localhost:8081/api/user/services
```

### Create Booking
```bash
curl -X POST http://localhost:8081/api/user/book \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "serviceId":1,
    "bookingDate":"2026-03-25",
    "timeSlot":"10:00 AM - 12:00 PM",
    "address":"123 Main St",
    "notes":"Test booking"
  }'
```

### Get My Bookings
```bash
curl -X GET http://localhost:8081/api/user/my-bookings \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Provider Registration
```bash
curl -X POST http://localhost:8081/api/provider/register \
  -H "Content-Type: application/json" \
  -d '{
    "name":"John Smith",
    "email":"john@provider.com",
    "phone":"9876543210",
    "category":"Plumbing",
    "experience":5,
    "password":"pass123"
  }'
```

### Get Provider Jobs
```bash
curl -X GET http://localhost:8081/api/provider/my-jobs \
  -H "Authorization: Bearer PROVIDER_TOKEN"
```

### Accept Job
```bash
curl -X PUT http://localhost:8081/api/provider/accept-job/1 \
  -H "Authorization: Bearer PROVIDER_TOKEN"
```

### Get All Bookings (Admin)
```bash
curl -X GET http://localhost:8081/api/admin/bookings \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

### Approve Provider (Admin)
```bash
curl -X PUT http://localhost:8081/api/admin/provider/1/approve \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

### Add Service (Admin)
```bash
curl -X POST http://localhost:8081/api/admin/add-service \
  -H "Authorization: Bearer ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name":"Electrical Repair",
    "description":"Professional electrical services",
    "price":500.0
  }'
```

### Get Analytics (Admin)
```bash
curl -X GET http://localhost:8081/api/admin/analytics
```

---

## 🔧 Testing Tools

| Tool | Purpose | Installation |
|------|---------|--------------|
| cURL | Manual API testing | Pre-installed on Windows |
| Postman | GUI API testing | Download from postman.com |
| JUnit | Automated unit tests | Maven dependency |
| Spring Boot Test | Integration testing | Maven dependency |
| MockMvc | HTTP testing | Spring Boot Test dependency |

---

## 📁 Test Files Location

| File | Path | Purpose |
|------|------|---------|
| Postman Collection | `/Postman_Collection.json` | Import in Postman |
| API Guide | `/API_TEST_GUIDE.md` | Comprehensive documentation |
| JUnit Tests | `/src/test/java/APIIntegrationTest.java` | Automated tests |
| Manual Script | `/test_api.cmd` | Interactive testing menu |
| Test Summary | `/TEST_SUMMARY.md` | Test overview |

---

## 🔐 Token Management

### Extracting Token from Response
```bash
# Login and capture token
FOR /F "tokens=*" %A IN ('curl -s -X POST http://localhost:8081/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"test@test.com\",\"password\":\"pass\"}" ^
  ^| jq -r ".token"') DO (SET TOKEN=%A)

# Use token in request
curl -X GET http://localhost:8081/api/user/dashboard \
  -H "Authorization: Bearer !TOKEN!"
```

### Token Header Format
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## ✅ Pre-Test Checklist

- [ ] MySQL running
- [ ] Database `service_booking_db` exists
- [ ] Spring Boot app started on port 8081
- [ ] curl or Postman installed
- [ ] Test data initialized
- [ ] Admin user created in database

---

## 📊 Expected Status Codes

| Code | Meaning | Example |
|------|---------|---------|
| 200 | OK - Success | GET requests, valid operations |
| 201 | Created | Resource successfully created |
| 400 | Bad Request | Invalid input data |
| 401 | Unauthorized | Missing/invalid token |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource doesn't exist |
| 500 | Server Error | Internal server error |

---

## 🐛 Troubleshooting

### Issue: Connection Refused (Port 8081)
```
Solution: Start the application with: mvnw.cmd spring-boot:run
```

### Issue: 403 Forbidden Error
```
Solution: 
1. Check token is valid
2. Verify user role (USER/PROVIDER/ADMIN)
3. Ensure endpoint is public or user is authenticated
```

### Issue: Database Connection Error
```
Solution:
1. Check MySQL is running
2. Verify credentials in application.properties
3. Ensure database exists: CREATE DATABASE service_booking_db;
```

### Issue: Token Expired
```
Solution: Re-login to get fresh token
```

### Issue: Admin Endpoints Returning 403
```
Solution: 
1. Login as admin user
2. Verify user has ADMIN role in database
3. Check JWT includes admin role
```

---

## 🎯 Test Workflow

### 1️⃣ Setup Phase
```bash
# Start application
mvnw.cmd spring-boot:run

# Wait for startup message
# "Started ServiceBookingBackendApplication"
```

### 2️⃣ Authentication Phase
```bash
# Register user
POST /api/auth/register

# Register provider  
POST /api/provider/register

# Login users
POST /api/auth/login
POST /api/provider/login
```

### 3️⃣ Business Logic Phase
```bash
# User books service
POST /api/user/book

# Admin assigns provider
PUT /api/admin/booking/{id}/assign-provider

# Provider accepts and completes job
PUT /api/provider/accept-job/{id}
PUT /api/provider/complete-job/{id}
```

### 4️⃣ Verification Phase
```bash
# Check results
GET /api/user/my-bookings
GET /api/provider/earnings
GET /api/admin/analytics
```

---

## 📝 Test Report Template

```
Test Execution Report
====================

Date: ________________
Tester: ________________

Environment:
- OS: Windows 10/11
- Java Version: 21
- Maven: 3.x
- Spring Boot: 4.0.2

Test Results:
- Total Tests: 20
- Passed: ____ 
- Failed: ____
- Skipped: ____

Issues Found:
1. ___________________
2. ___________________

Comments:
_______________________
_______________________

Status: PASSED / FAILED
```

---

## 🔄 Common Test Scenarios

### Scenario 1: Complete Booking Flow
1. Register user → Get token
2. Get services list
3. Create booking
4. View booking
5. Rate provider (after completion)

### Scenario 2: Provider Workflow
1. Register provider
2. Admin approves provider
3. Provider logs in
4. View assigned jobs
5. Accept and complete job
6. Check earnings

### Scenario 3: Admin Management
1. Login as admin
2. View all bookings/providers
3. Approve pending providers
4. Assign providers to bookings
5. Update booking status
6. View analytics

---

## 📚 Documentation Files

- **API_TEST_GUIDE.md** - Detailed API documentation with examples
- **TEST_SUMMARY.md** - Complete testing overview and strategy
- **Postman_Collection.json** - Ready-to-import Postman collection
- **APIIntegrationTest.java** - JUnit automated test suite
- **test_api.cmd** - Interactive Windows batch testing script

---

## 🎓 Learning Resources

- API Testing: https://postman.com/docs/
- JUnit Documentation: https://junit.org/junit5/docs/
- cURL Guide: https://curl.se/docs/
- Spring Boot Testing: https://spring.io/guides/gs/testing-web/
- REST API Best Practices: https://restfulapi.net/

---

## 💡 Pro Tips

1. **Save tokens in variables** - Easier to reuse across requests
2. **Use Postman Environment** - Set base URL once, use everywhere
3. **Test happy path first** - Then test error scenarios
4. **Document test results** - Keep records for regression testing
5. **Use dummy data** - Avoid testing with production data
6. **Test in order** - Follow logical workflow (auth → create → read → update → delete)
7. **Check response headers** - Verify content-type, cache-control, etc.
8. **Validate JSON** - Use online JSON validators

---

**Created:** March 20, 2026  
**Version:** 1.0  
**Status:** Ready for Testing
