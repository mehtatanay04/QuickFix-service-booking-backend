package com.tanay.bookingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tanay.bookingapp.dto.ServiceRequestDTO;
import com.tanay.bookingapp.entity.ServiceEntity;
import com.tanay.bookingapp.repository.ServiceRepository;
import com.tanay.bookingapp.security.RoleChecker;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

@Autowired
private ServiceRepository serviceRepository;

@GetMapping("/dashboard")
public String adminDashboard(HttpServletRequest request) {

if(!RoleChecker.hasRole(request, "ADMIN")) {
return "Forbidden: Admin access only";
}

return "Welcome ADMIN, your ID = " + request.getAttribute("userId");
}

@PostMapping("/add-service")
public String addService(
@RequestBody ServiceRequestDTO dto,
HttpServletRequest request
) {

if(!RoleChecker.hasRole(request, "ADMIN")) {
return "Forbidden: Admin access only";
}

ServiceEntity service = new ServiceEntity(
dto.getName(),
dto.getDescription(),
dto.getPrice()
);

serviceRepository.save(service);

return "Service created successfully";
}

}
