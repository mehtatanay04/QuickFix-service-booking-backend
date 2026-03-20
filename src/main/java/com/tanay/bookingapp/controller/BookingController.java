package com.tanay.bookingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tanay.bookingapp.dto.BookingRequestDTO;
import com.tanay.bookingapp.entity.Booking;
import com.tanay.bookingapp.service.BookingService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class BookingController {

@Autowired
private BookingService bookingService;

@GetMapping("/my-bookings")
public List<Booking> getUserBookings(HttpServletRequest request){

Number userIdNumber = (Number) request.getAttribute("userId");
Long userId = userIdNumber.longValue();

return bookingService.getUserBookings(userId);
}

@PostMapping("/book")
public String createBooking(
@RequestBody BookingRequestDTO dto,
HttpServletRequest request
){

Number userIdNumber = (Number) request.getAttribute("userId");
Long userId = userIdNumber.longValue();

return bookingService.createBooking(userId, dto);
}

@PutMapping("/cancel/{bookingId}")
public String cancelBooking(
@PathVariable Long bookingId,
HttpServletRequest request
){

Number userIdNumber = (Number) request.getAttribute("userId");
Long userId = userIdNumber.longValue();

return bookingService.cancelBooking(userId, bookingId);
}

}