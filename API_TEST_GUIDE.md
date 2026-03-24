# Service Booking Backend - API Testing Guide

## Base URL
```
http://localhost:8081
```

---

## 1. AUTHENTICATION ENDPOINTS

### 1.1 User Registration
**Endpoint:** `POST /api/auth/register`

**Description:** Register a new user account

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "role": "USER"
}
```

**Test Cases:**
- ✅ Register with valid credentials
- ✅ Register with duplicate email (should fail)
- ✅ Register with empty fields (should fail)
- ✅ Register with invalid email format (should fail)

---

### 1.2 User Login
**Endpoint:** `POST /api/auth/login`

**Description:** Authenticate user and get JWT token

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "john@example.com",
  "role": "USER"
}
```

**Test Cases:**
- ✅ Login with correct credentials
- ✅ Login with incorrect password (should fail)
- ✅ Login with non-existent email (should fail)
- ✅ Token should be valid for subsequent requests

---

## 2. USER OPERATIONS ENDPOINTS

### 2.1 Get User Dashboard
**Endpoint:** `GET /api/user/dashboard`

**Description:** Get user dashboard information

**Headers:**
```
Authorization: Bearer {token}
```

**cURL Command:**
```bash
curl -X GET http://localhost:8081/api/user/dashboard \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response (200 OK):**
```
Welcome USER, your ID = 1
```

**Test Cases:**
- ✅ Access with valid token
- ✅ Access without token (should fail - 403)
- ✅ Access with invalid token (should fail - 403)

---

### 2.2 Get All Services
**Endpoint:** `GET /api/user/services`

**Description:** Get list of all available services

**cURL Command:**
```bash
curl -X GET http://localhost:8081/api/user/services
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Plumbing",
    "description": "Professional plumbing services",
    "price": 300.0
  },
  {
    "id": 2,
    "name": "Electrical Repair",
    "description": "Electrical repair and maintenance",
    "price": 500.0
  }
]
```

**Test Cases:**
- ✅ Retrieve all services (public endpoint, no auth required)
- ✅ Verify service list is not empty
- ✅ Verify price information is included

---

### 2.3 Rate Provider
**Endpoint:** `POST /api/user/rate-provider`

**Description:** Rate and review a service provider

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "providerId": 1,
  "score": 5,
  "review": "Excellent service, very professional!"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8081/api/user/rate-provider \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "providerId": 1,
    "score": 5,
    "review": "Excellent service, very professional!"
  }'
```

**Expected Response (200 OK):**
```
Rating submitted successfully
```

**Test Cases:**
- ✅ Rate with valid provider ID
- ✅ Rate with score between 1-5
- ✅ Rate with non-existent provider ID (should fail)
- ✅ Rate without authentication (should fail)

---

## 3. BOOKING OPERATIONS ENDPOINTS

### 3.1 Get My Bookings
**Endpoint:** `GET /api/user/my-bookings`

**Description:** Get all bookings made by the logged-in user

**Headers:**
```
Authorization: Bearer {token}
```

**cURL Command:**
```bash
curl -X GET http://localhost:8081/api/user/my-bookings \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "user": {
      "id": 1,
      "name": "John Doe"
    },
    "service": {
      "id": 1,
      "name": "Plumbing",
      "price": 300.0
    },
    "bookingDate": "2026-03-25",
    "timeSlot": "10:00 AM - 12:00 PM",
    "address": "123 Main Street, City",
    "status": "PENDING"
  }
]
```

**Test Cases:**
- ✅ Retrieve user's bookings
- ✅ Empty list if no bookings exist
- ✅ Verify user ID is extracted from token
- ✅ Cannot see other users' bookings

---

### 3.2 Create Booking
**Endpoint:** `POST /api/user/book`

**Description:** Create a new service booking

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "serviceId": 1,
  "bookingDate": "2026-03-25",
  "timeSlot": "10:00 AM - 12:00 PM",
  "address": "123 Main Street, City, State",
  "notes": "Please bring your own equipment"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8081/api/user/book \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "serviceId": 1,
    "bookingDate": "2026-03-25",
    "timeSlot": "10:00 AM - 12:00 PM",
    "address": "123 Main Street, City, State",
    "notes": "Please bring your own equipment"
  }'
```

**Expected Response (200 OK):**
```
Booking created successfully
```

**Test Cases:**
- ✅ Create booking with valid service ID
- ✅ Verify booking date is in future
- ✅ Create with non-existent service ID (should fail)
- ✅ Create without required fields (should fail)
- ✅ Verify user ID is auto-assigned from token

---

### 3.3 Cancel Booking
**Endpoint:** `PUT /api/user/cancel/{bookingId}`

**Description:** Cancel an existing booking

**Headers:**
```
Authorization: Bearer {token}
```

**URL Parameters:**
- `bookingId` (Long): The ID of the booking to cancel

**cURL Command:**
```bash
curl -X PUT http://localhost:8081/api/user/cancel/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response (200 OK):**
```
Booking cancelled successfully
```

**Test Cases:**
- ✅ Cancel existing booking
- ✅ Cancel non-existent booking (should fail)
- ✅ User can only cancel their own bookings
- ✅ Cannot cancel already completed booking

---

## 4. PROVIDER OPERATIONS ENDPOINTS

### 4.1 Provider Registration
**Endpoint:** `POST /api/provider/register`

**Description:** Register a new service provider

**Request Body:**
```json
{
  "name": "John Smith",
  "email": "john.smith@example.com",
  "phone": "9876543210",
  "category": "Plumbing",
  "experience": 5,
  "password": "provider123"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8081/api/provider/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith",
    "email": "john.smith@example.com",
    "phone": "9876543210",
    "category": "Plumbing",
    "experience": 5,
    "password": "provider123"
  }'
```

**Expected Response (200 OK):**
```
Provider registered successfully
```

**Test Cases:**
- ✅ Register with valid credentials
- ✅ Register with duplicate email (should fail)
- ✅ Register with invalid phone (should fail)
- ✅ Provider starts as unapproved by default

---

### 4.2 Provider Login
**Endpoint:** `POST /api/provider/login`

**Description:** Authenticate provider and get JWT token

**Request Body:**
```json
{
  "email": "john.smith@example.com",
  "password": "provider123"
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8081/api/provider/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.smith@example.com",
    "password": "provider123"
  }'
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "john.smith@example.com",
  "role": "PROVIDER"
}
```

**Test Cases:**
- ✅ Login with correct credentials
- ✅ Login with incorrect password (should fail)
- ✅ Only approved providers can login
- ✅ Token should have PROVIDER role

---

### 4.3 Get Provider Jobs
**Endpoint:** `GET /api/provider/my-jobs`

**Description:** Get all jobs assigned to the provider

**Headers:**
```
Authorization: Bearer {token}
```

**cURL Command:**
```bash
curl -X GET http://localhost:8081/api/provider/my-jobs \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "service": {
      "id": 1,
      "name": "Plumbing",
      "price": 300.0
    },
    "bookingDate": "2026-03-25",
    "timeSlot": "10:00 AM - 12:00 PM",
    "address": "123 Main Street, City",
    "status": "ASSIGNED"
  }
]
```

**Test Cases:**
- ✅ Retrieve provider's assigned jobs
- ✅ Empty list if no jobs assigned
- ✅ Cannot see other providers' jobs

---

### 4.4 Accept Job
**Endpoint:** `PUT /api/provider/accept-job/{bookingId}`

**Description:** Provider accepts a job assignment

**Headers:**
```
Authorization: Bearer {token}
```

**URL Parameters:**
- `bookingId` (Long): The ID of the booking/job to accept

**cURL Command:**
```bash
curl -X PUT http://localhost:8081/api/provider/accept-job/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response (200 OK):**
```
Job accepted successfully
```

**Test Cases:**
- ✅ Accept assigned job
- ✅ Job status changes to ACCEPTED
- ✅ Cannot accept someone else's job
- ✅ Cannot accept already completed job

---

### 4.5 Complete Job
**Endpoint:** `PUT /api/provider/complete-job/{bookingId}`

**Description:** Mark a job as completed

**Headers:**
```
Authorization: Bearer {token}
```

**URL Parameters:**
- `bookingId` (Long): The ID of the booking/job to complete

**cURL Command:**
```bash
curl -X PUT http://localhost:8081/api/provider/complete-job/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response (200 OK):**
```
Job completed successfully
```

**Test Cases:**
- ✅ Complete accepted job
- ✅ Job status changes to COMPLETED
- ✅ Only accepted jobs can be completed
- ✅ Update provider earnings

---

### 4.6 Get Provider Earnings
**Endpoint:** `GET /api/provider/earnings`

**Description:** Get total earnings of the provider

**Headers:**
```
Authorization: Bearer {token}
```

**cURL Command:**
```bash
curl -X GET http://localhost:8081/api/provider/earnings \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response (200 OK):**
```
5000.0
```

**Test Cases:**
- ✅ Retrieve provider earnings
- ✅ Only completed jobs count towards earnings
- ✅ Returns 0.0 if no completed jobs

---

### 4.7 Toggle Availability
**Endpoint:** `PUT /api/provider/toggle-availability`

**Description:** Toggle provider's availability status

**Headers:**
```
Authorization: Bearer {token}
```

**cURL Command:**
```bash
curl -X PUT http://localhost:8081/api/provider/toggle-availability \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Expected Response (200 OK):**
```
Availability toggled successfully
```

**Test Cases:**
- ✅ Toggle availability on/off
- ✅ Status changes in database
- ✅ Unavailable providers don't get new jobs

---

## 5. ADMIN OPERATIONS ENDPOINTS

### 5.1 Admin Dashboard
**Endpoint:** `GET /api/admin/dashboard`

**Description:** Get admin dashboard overview

**Headers:**
```
Authorization: Bearer {admin_token}
```

**cURL Command:**
```bash
curl -X GET http://localhost:8081/api/admin/dashboard \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

**Expected Response (200 OK):**
```
Welcome ADMIN, your ID = 1
```

**Test Cases:**
- ✅ Access with valid admin token
- ✅ Deny access to non-admin users
- ✅ Deny access without token

---

### 5.2 Get All Bookings (Admin)
**Endpoint:** `GET /api/admin/bookings`

**Description:** Get all bookings in the system (admin only)

**Headers:**
```
Authorization: Bearer {admin_token}
```

**cURL Command:**
```bash
curl -X GET http://localhost:8081/api/admin/bookings \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "user": { "id": 1, "name": "John Doe" },
    "service": { "id": 1, "name": "Plumbing" },
    "provider": { "id": 1, "name": "John Smith" },
    "status": "COMPLETED",
    "bookingDate": "2026-03-25"
  }
]
```

**Test Cases:**
- ✅ Retrieve all bookings
- ✅ Only admin can access
- ✅ Deny access to regular users

---

### 5.3 Update Booking Status (Admin)
**Endpoint:** `PUT /api/admin/booking/{id}/status`

**Description:** Update the status of a booking

**Headers:**
```
Authorization: Bearer {admin_token}
Content-Type: application/json
```

**URL Parameters:**
- `id` (Long): The ID of the booking

**Request Body:**
```json
{
  "status": "COMPLETED"
}
```

**cURL Command:**
```bash
curl -X PUT http://localhost:8081/api/admin/booking/1/status \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{
    "status": "COMPLETED"
  }'
```

**Expected Response (200 OK):**
```
Booking status updated to COMPLETED
```

**Valid Status Values:**
- `PENDING` - New booking
- `ASSIGNED` - Provider assigned
- `ACCEPTED` - Provider accepted
- `COMPLETED` - Service completed
- `CANCELLED` - Booking cancelled

**Test Cases:**
- ✅ Update status to valid state
- ✅ Update to invalid status (should fail)
- ✅ Update non-existent booking (should fail)
- ✅ Only admin can update status

---

### 5.4 Get All Providers (Admin)
**Endpoint:** `GET /api/admin/providers`

**Description:** Get list of all service providers

**Headers:**
```
Authorization: Bearer {admin_token}
```

**cURL Command:**
```bash
curl -X GET http://localhost:8081/api/admin/providers \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "name": "John Smith",
    "email": "john.smith@example.com",
    "phone": "9876543210",
    "category": "Plumbing",
    "experience": 5,
    "approved": false
  }
]
```

**Test Cases:**
- ✅ Retrieve all providers
- ✅ Show approval status
- ✅ Only admin can access

---

### 5.5 Approve Provider (Admin)
**Endpoint:** `PUT /api/admin/provider/{id}/approve`

**Description:** Approve a service provider

**Headers:**
```
Authorization: Bearer {admin_token}
```

**URL Parameters:**
- `id` (Long): The ID of the provider

**cURL Command:**
```bash
curl -X PUT http://localhost:8081/api/admin/provider/1/approve \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

**Expected Response (200 OK):**
```
Provider approved successfully
```

**Test Cases:**
- ✅ Approve pending provider
- ✅ Provider can login after approval
- ✅ Approve non-existent provider (should fail)
- ✅ Only admin can approve

---

### 5.6 Add Service (Admin)
**Endpoint:** `POST /api/admin/add-service`

**Description:** Add a new service to the platform

**Headers:**
```
Authorization: Bearer {admin_token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Electrical Repair",
  "description": "Professional electrical repair and maintenance services",
  "price": 500.0
}
```

**cURL Command:**
```bash
curl -X POST http://localhost:8081/api/admin/add-service \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{
    "name": "Electrical Repair",
    "description": "Professional electrical repair and maintenance services",
    "price": 500.0
  }'
```

**Expected Response (200 OK):**
```
Service created successfully
```

**Test Cases:**
- ✅ Add service with valid data
- ✅ Service is available to users
- ✅ Invalid price (should fail)
- ✅ Only admin can add service

---

### 5.7 Assign Provider to Booking (Admin)
**Endpoint:** `PUT /api/admin/booking/{id}/assign-provider`

**Description:** Assign a provider to a booking

**Headers:**
```
Authorization: Bearer {admin_token}
Content-Type: application/json
```

**URL Parameters:**
- `id` (Long): The ID of the booking

**Request Body:**
```json
{
  "providerId": 1
}
```

**cURL Command:**
```bash
curl -X PUT http://localhost:8081/api/admin/booking/1/assign-provider \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{
    "providerId": 1
  }'
```

**Expected Response (200 OK):**
```
Provider assigned successfully
```

**Test Cases:**
- ✅ Assign valid provider to booking
- ✅ Assign non-existent provider (should fail)
- ✅ Assign to non-existent booking (should fail)
- ✅ Status changes to ASSIGNED
- ✅ Only admin can assign

---

### 5.8 Get Analytics (Admin)
**Endpoint:** `GET /api/admin/analytics`

**Description:** Get platform analytics and statistics

**cURL Command:**
```bash
curl -X GET http://localhost:8081/api/admin/analytics
```

**Expected Response (200 OK):**
```json
{
  "totalBookings": 10,
  "totalRevenue": 5000.0,
  "totalCommission": 1000.0
}
```

**Test Cases:**
- ✅ Retrieve analytics data
- ✅ Calculate correct total bookings
- ✅ Only completed bookings count for revenue
- ✅ Commission calculated correctly

---

## TESTING WORKFLOW

### Step 1: User Registration & Login
1. Register a test user
2. Note the returned `userId` and `token`
3. Login to get fresh token

### Step 2: Provider Setup (Optional)
1. Register a test provider
2. Login as provider
3. Admin approves provider

### Step 3: Service Management
1. Check available services
2. Admin can add new services
3. Verify services appear for users

### Step 4: Booking Flow
1. User creates booking
2. Retrieve user's bookings
3. Check booking status

### Step 5: Provider Jobs (Optional)
1. Admin assigns provider to booking
2. Provider accepts job
3. Provider completes job
4. Check provider earnings

### Step 6: Ratings & Reviews
1. User rates completed provider
2. Verify rating saved

### Step 7: Admin Analytics
1. Check total bookings count
2. Check revenue calculations
3. Verify commission tracking

---

## AUTHENTICATION NOTES

1. **JWT Token**: Obtained from login endpoints
2. **Token Header**: Use `Authorization: Bearer {token}` format
3. **Token Expiry**: Set in JwtUtil configuration
4. **Role-Based Access**: Endpoints check user role
   - `USER`: Can book, rate, cancel
   - `PROVIDER`: Can accept/complete jobs
   - `ADMIN`: Can manage all resources

---

## ERROR HANDLING

**Common Error Responses:**

**401 Unauthorized:**
```json
{
  "error": "Invalid or missing token"
}
```

**403 Forbidden:**
```json
{
  "error": "Insufficient permissions"
}
```

**404 Not Found:**
```json
{
  "error": "Resource not found"
}
```

**400 Bad Request:**
```json
{
  "error": "Invalid request data"
}
```

---

## POSTMAN COLLECTION

A Postman collection file (`Postman_Collection.json`) is provided with:
- All 20 endpoints configured
- Pre-configured request bodies
- Variable placeholders for tokens
- Environment setup guide

### How to Import:
1. Open Postman
2. Click "Import" → "Upload Files"
3. Select `Postman_Collection.json`
4. Create environment with variables:
   - `user_token`
   - `provider_token`
   - `admin_token`

---

## DATABASE REQUIREMENTS

Ensure MySQL is running with:
- Database: `service_booking_db`
- Tables auto-created by Hibernate (ddl-auto: update)

Database credentials in `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/service_booking_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

## NOTES FOR TESTING

1. **Ensure database is initialized** with proper tables
2. **Use consistent test data** across test runs
3. **Check token expiry** if getting 401 errors
4. **Verify admin user exists** for admin endpoints
5. **Date format**: Use `YYYY-MM-DD` for booking dates
6. **Phone validation**: Must be 10 digits for providers
