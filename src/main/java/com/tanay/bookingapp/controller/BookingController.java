package com.tanay.bookingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanay.bookingapp.dto.BookingRequestDTO;
import com.tanay.bookingapp.entity.Booking;
import com.tanay.bookingapp.entity.BookingStatus;
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

    // 🔹 Get logged-in user's bookings
    @GetMapping("/my-bookings")
    public List<Booking> getUserBookings(HttpServletRequest request) {

        Number userIdNumber = (Number) request.getAttribute("userId");
        Long userId = userIdNumber.longValue();

        return bookingRepository.findByUserId(userId);
    }

    // 🔹 Create booking with date, slot, address, notes
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

        // 🔥 SLOT CHECK
        boolean alreadyBooked = bookingRepository
                .existsByServiceIdAndBookingDateAndTimeSlot(
                        dto.getServiceId(),
                        dto.getBookingDate(),
                        dto.getTimeSlot()
                );

        if (alreadyBooked) {
            throw new RuntimeException("This time slot is already booked for this service");
        }

        Booking booking = new Booking();

        booking.setUser(user);
        booking.setService(service);
        booking.setBookingDate(dto.getBookingDate());
        booking.setTimeSlot(dto.getTimeSlot());
        booking.setAddress(dto.getAddress());
        booking.setNotes(dto.getNotes());
        booking.setStatus(BookingStatus.PENDING);

        bookingRepository.save(booking);

        return "Booking created successfully with status PENDING";
    }
}