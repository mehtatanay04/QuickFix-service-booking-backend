package com.tanay.bookingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tanay.bookingapp.entity.ServiceEntity;
import com.tanay.bookingapp.repository.ServiceRepository;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

@Autowired
private ServiceRepository serviceRepository;

@GetMapping("/dashboard")
public String userDashboard(HttpServletRequest request) {
return "Welcome USER, your ID = " + request.getAttribute("userId");
}

@GetMapping("/services")
public List<ServiceEntity> getAllServices() {
return serviceRepository.findAll();
}

}
