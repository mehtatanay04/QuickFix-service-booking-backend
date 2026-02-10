package com.tanay.bookingapp.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanay.bookingapp.security.RoleChecker;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

@GetMapping("/dashboard")
public String adminDashboard(HttpServletRequest request) {

if(!RoleChecker.hasRole(request, "ADMIN")) {
return "Forbidden: Admin access only";
}

return "Welcome ADMIN, your ID = " + request.getAttribute("userId");
}
}
