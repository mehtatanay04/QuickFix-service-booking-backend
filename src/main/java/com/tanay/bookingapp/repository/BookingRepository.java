package com.tanay.bookingapp.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tanay.bookingapp.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

List<Booking> findByUserId(Long userId);

List<Booking> findByProviderId(Long providerId);

boolean existsByServiceIdAndBookingDateAndTimeSlot(
Long serviceId,
LocalDate bookingDate,
String timeSlot
);

}