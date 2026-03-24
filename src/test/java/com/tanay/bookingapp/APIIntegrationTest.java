package com.tanay.bookingapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanay.bookingapp.dto.LoginRequestDTO;
import com.tanay.bookingapp.dto.RegisterRequestDTO;
import com.tanay.bookingapp.dto.BookingRequestDTO;
import com.tanay.bookingapp.dto.ProviderRequestDTO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;

@SpringBootTest
public class APIIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String userToken;
    private String providerToken;
    private String adminToken;
    private Long bookingId;
    private Long serviceId = 1L;
    private Long providerId = 1L;

    @BeforeEach
    public void setUp() throws Exception {
        // Setup test data if needed
    }

    // ============ AUTHENTICATION TESTS ============

    @Test
    public void testUserRegistration() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setName("Test User");
        request.setEmail("testuser@example.com");
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    public void testUserRegistrationWithDuplicateEmail() throws Exception {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setName("Duplicate User");
        request.setEmail("testuser@example.com");
        request.setPassword("password123");

        // First registration
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Second registration with same email should fail
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testUserLogin() throws Exception {
        // Register user first
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setName("Login Test User");
        registerRequest.setEmail("logintest@example.com");
        registerRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // Now login
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("logintest@example.com");
        loginRequest.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").value("USER"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        userToken = objectMapper.readTree(response).get("token").asText();
    }

    @Test
    public void testUserLoginWithIncorrectPassword() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is4xxClientError());
    }

    // ============ USER OPERATIONS TESTS ============

    @Test
    public void testGetUserDashboard() throws Exception {
        // Login first to get token
        testUserLogin();

        mockMvc.perform(get("/api/user/dashboard")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome USER")));
    }

    @Test
    public void testGetUserDashboardWithoutToken() throws Exception {
        mockMvc.perform(get("/api/user/dashboard"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testGetAllServices() throws Exception {
        mockMvc.perform(get("/api/user/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    public void testRateProvider() throws Exception {
        // Login first
        testUserLogin();

        String ratingRequestBody = "{\"providerId\": 1, \"score\": 5, \"review\": \"Excellent service\"}";

        mockMvc.perform(post("/api/user/rate-provider")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + userToken)
                .content(ratingRequestBody))
                .andExpect(status().isOk());
    }

    // ============ BOOKING TESTS ============

    @Test
    public void testGetUserBookings() throws Exception {
        testUserLogin();

        mockMvc.perform(get("/api/user/my-bookings")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(0))));
    }

    @Test
    public void testCreateBooking() throws Exception {
        testUserLogin();

        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setServiceId(1L);
        bookingRequest.setBookingDate(LocalDate.now().plusDays(1));
        bookingRequest.setTimeSlot("10:00 AM - 12:00 PM");
        bookingRequest.setAddress("123 Main Street, City");
        bookingRequest.setNotes("Test booking");

        MvcResult result = mockMvc.perform(post("/api/user/book")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + userToken)
                .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        // Extract booking ID if needed
    }

    @Test
    public void testCancelBooking() throws Exception {
        testUserLogin();

        // Create booking first
        testCreateBooking();

        // Cancel with ID 1 (should exist after creation)
        mockMvc.perform(put("/api/user/cancel/1")
                .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk());
    }

    // ============ PROVIDER TESTS ============

    @Test
    public void testProviderRegistration() throws Exception {
        ProviderRequestDTO request = new ProviderRequestDTO();
        request.setName("Test Provider");
        request.setEmail("provider@example.com");
        request.setPhone("9876543210");
        request.setCategory("Plumbing");
        request.setExperience(5);
        request.setPassword("provider123");

        mockMvc.perform(post("/api/provider/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void testProviderLogin() throws Exception {
        // Register provider first
        testProviderRegistration();

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("provider@example.com");
        loginRequest.setPassword("provider123");

        // Provider login would fail if not approved by admin
        // This test demonstrates the expected behavior
        MvcResult result = mockMvc.perform(post("/api/provider/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn();

        // Status depends on whether provider is approved
        assert(result.getResponse().getStatus() == 200 || result.getResponse().getStatus() == 401);
    }

    @Test
    public void testGetProviderJobs() throws Exception {
        // This would require provider login token
        // Placeholder for provider job retrieval test
    }

    @Test
    public void testAcceptJob() throws Exception {
        // This would require provider token and assigned job
        // Placeholder for job acceptance test
    }

    @Test
    public void testCompleteJob() throws Exception {
        // This would require provider token and accepted job
        // Placeholder for job completion test
    }

    @Test
    public void testGetProviderEarnings() throws Exception {
        // This would require provider token
        // Placeholder for earnings retrieval test
    }

    @Test
    public void testToggleProviderAvailability() throws Exception {
        // This would require provider token
        // Placeholder for availability toggle test
    }

    // ============ ADMIN TESTS ============

    @Test
    public void testGetAdminDashboard() throws Exception {
        // This requires admin token
        // Placeholder for admin dashboard test
    }

    @Test
    public void testGetAllBookingsAdmin() throws Exception {
        // This requires admin token
        // Placeholder for getting all bookings test
    }

    @Test
    public void testUpdateBookingStatus() throws Exception {
        // This requires admin token and booking ID
        // Placeholder for booking status update test
    }

    @Test
    public void testGetAllProviders() throws Exception {
        // This requires admin token
        // Placeholder for getting all providers test
    }

    @Test
    public void testApproveProvider() throws Exception {
        // This requires admin token and provider ID
        // Placeholder for provider approval test
    }

    @Test
    public void testAddService() throws Exception {
        // This requires admin token
        // Placeholder for adding service test
    }

    @Test
    public void testAssignProviderToBooking() throws Exception {
        // This requires admin token, booking ID, and provider ID
        // Placeholder for provider assignment test
    }

    @Test
    public void testGetAnalytics() throws Exception {
        mockMvc.perform(get("/api/admin/analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBookings").exists())
                .andExpect(jsonPath("$.totalRevenue").exists())
                .andExpect(jsonPath("$.totalCommission").exists());
    }

    // ============ EDGE CASES & ERROR HANDLING ============

    @Test
    public void testAccessProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/user/my-bookings"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testAccessWithInvalidToken() throws Exception {
        mockMvc.perform(get("/api/user/dashboard")
                .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreateBookingWithInvalidServiceId() throws Exception {
        testUserLogin();

        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setServiceId(999999L); // Non-existent service
        bookingRequest.setBookingDate(LocalDate.now().plusDays(1));
        bookingRequest.setTimeSlot("10:00 AM - 12:00 PM");
        bookingRequest.setAddress("123 Main Street");
        bookingRequest.setNotes("Test");

        mockMvc.perform(post("/api/user/book")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + userToken)
                .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testCreateBookingWithPastDate() throws Exception {
        testUserLogin();

        BookingRequestDTO bookingRequest = new BookingRequestDTO();
        bookingRequest.setServiceId(1L);
        bookingRequest.setBookingDate(LocalDate.now().minusDays(1)); // Past date
        bookingRequest.setTimeSlot("10:00 AM - 12:00 PM");
        bookingRequest.setAddress("123 Main Street");
        bookingRequest.setNotes("Test");

        mockMvc.perform(post("/api/user/book")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + userToken)
                .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().is4xxClientError());
    }
}
