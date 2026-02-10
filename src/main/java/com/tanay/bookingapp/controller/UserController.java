package com.tanay.bookingapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

@GetMapping("/dashboard")
public String userDashboard(HttpServletRequest request) {

return "Welcome USER, your ID = " + request.getAttribute("userId");
}
}
