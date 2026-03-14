package com.tanay.bookingapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanay.bookingapp.dto.BookingRequestDTO;
import com.tanay.bookingapp.entity.Booking;
import com.tanay.bookingapp.entity.BookingStatus;
import com.tanay.bookingapp.entity.Provider;
import com.tanay.bookingapp.entity.ServiceEntity;
import com.tanay.bookingapp.entity.User;
import com.tanay.bookingapp.repository.BookingRepository;
import com.tanay.bookingapp.repository.ProviderRepository;
import com.tanay.bookingapp.repository.ServiceRepository;
import com.tanay.bookingapp.repository.UserRepository;

@Service
public class BookingService {

@Autowired
private BookingRepository bookingRepository;

@Autowired
private ServiceRepository serviceRepository;

@Autowired
private UserRepository userRepository;

@Autowired
private ProviderRepository providerRepository;


public List<Booking> getUserBookings(Long userId){
return bookingRepository.findByUserId(userId);
}


public String createBooking(Long userId, BookingRequestDTO dto){

User user = userRepository.findById(userId)
.orElseThrow(() -> new RuntimeException("User not found"));

ServiceEntity service = serviceRepository.findById(dto.getServiceId())
.orElseThrow(() -> new RuntimeException("Service not found"));

boolean alreadyBooked = bookingRepository
.existsByServiceIdAndBookingDateAndTimeSlot(
dto.getServiceId(),
dto.getBookingDate(),
dto.getTimeSlot()
);

if(alreadyBooked){
throw new RuntimeException("This time slot is already booked for this service");
}

/* FIND PROVIDER */

Provider provider = providerRepository
.findFirstByCategoryAndApprovedTrue(service.getName())
.orElse(null);

Booking booking = new Booking();

booking.setUser(user);
booking.setService(service);
booking.setProvider(provider);
booking.setBookingDate(dto.getBookingDate());
booking.setTimeSlot(dto.getTimeSlot());
booking.setAddress(dto.getAddress());
booking.setNotes(dto.getNotes());
booking.setStatus(BookingStatus.PENDING);

bookingRepository.save(booking);

return "Booking created successfully and provider assigned";
}

}