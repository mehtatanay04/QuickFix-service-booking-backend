package com.tanay.bookingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tanay.bookingapp.dto.ProviderLoginDTO;
import com.tanay.bookingapp.dto.ProviderRequestDTO;
import com.tanay.bookingapp.entity.Booking;
import com.tanay.bookingapp.service.ProviderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/provider")
public class ProviderController {

@Autowired
private ProviderService providerService;

//////////////// REGISTER //////////////////

@PostMapping("/register")
public String registerProvider(@RequestBody ProviderRequestDTO dto){
return providerService.registerProvider(dto);
}

//////////////// LOGIN //////////////////

@PostMapping("/login")
public String login(@RequestBody ProviderLoginDTO dto){
return providerService.loginProvider(dto);
}

//////////////// MY JOBS //////////////////

@GetMapping("/my-jobs")
public List<Booking> getJobs(HttpServletRequest request){

Long providerId = ((Number) request.getAttribute("userId")).longValue();

return providerService.getProviderJobs(providerId);
}

//////////////// ACCEPT //////////////////

@PutMapping("/accept-job/{bookingId}")
public String acceptJob(
@PathVariable Long bookingId,
HttpServletRequest request
){

Long providerId = ((Number) request.getAttribute("userId")).longValue();

return providerService.acceptJob(providerId, bookingId);
}

//////////////// COMPLETE //////////////////

@PutMapping("/complete-job/{bookingId}")
public String completeJob(
@PathVariable Long bookingId,
HttpServletRequest request
){

Long providerId = ((Number) request.getAttribute("userId")).longValue();

return providerService.completeJob(providerId, bookingId);
}

//////////////// EARNINGS //////////////////

@GetMapping("/earnings")
public Double getEarnings(HttpServletRequest request){

Long providerId = ((Number) request.getAttribute("userId")).longValue();

return providerService.getEarnings(providerId);
}

//////////////// AVAILABILITY //////////////////

@PutMapping("/toggle-availability")
public String toggleAvailability(HttpServletRequest request){

Long providerId = ((Number) request.getAttribute("userId")).longValue();

return providerService.toggleAvailability(providerId);
}

}