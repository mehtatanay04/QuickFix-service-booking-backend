package com.tanay.bookingapp.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanay.bookingapp.dto.BookingRequestDTO;
import com.tanay.bookingapp.entity.Booking;
import com.tanay.bookingapp.entity.ServiceEntity;
import com.tanay.bookingapp.entity.User;
import com.tanay.bookingapp.repository.BookingRepository;
import com.tanay.bookingapp.repository.ServiceRepository;
import com.tanay.bookingapp.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class BookingController {

@Autowired
private BookingRepository bookingRepository;

@Autowired
private ServiceRepository serviceRepository;

@Autowired
private UserRepository userRepository;

@PostMapping("/book")
public String createBooking(
@RequestBody BookingRequestDTO dto,
HttpServletRequest request
) {

Number userIdNumber = (Number) request.getAttribute("userId");
Long userId = userIdNumber.longValue();
User user = userRepository.findById(userId)
.orElseThrow(() -> new RuntimeException("User not found"));

ServiceEntity service = serviceRepository.findById(dto.getServiceId())
.orElseThrow(() -> new RuntimeException("Service not found"));

Booking booking = new Booking(
user,
service,
LocalDateTime.now(),
"PENDING"
);

bookingRepository.save(booking);

return "Booking created successfully with status PENDING";
}
}
