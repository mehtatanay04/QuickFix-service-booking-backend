package com.tanay.bookingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tanay.bookingapp.dto.RatingRequestDTO;
import com.tanay.bookingapp.entity.ServiceEntity;
import com.tanay.bookingapp.repository.ServiceRepository;
import com.tanay.bookingapp.service.RatingService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

@Autowired
private ServiceRepository serviceRepository;

@Autowired
private RatingService ratingService;

@GetMapping("/dashboard")
public String userDashboard(HttpServletRequest request) {
return "Welcome USER, your ID = " + request.getAttribute("userId");
}

@GetMapping("/services")
public List<ServiceEntity> getAllServices() {
return serviceRepository.findAll();
}

@PostMapping("/rate-provider")
public String rateProvider(
@RequestBody RatingRequestDTO dto,
HttpServletRequest request
){

Number userIdNumber = (Number) request.getAttribute("userId");
Long userId = userIdNumber.longValue();

return ratingService.rateProvider(userId, dto);
}

}