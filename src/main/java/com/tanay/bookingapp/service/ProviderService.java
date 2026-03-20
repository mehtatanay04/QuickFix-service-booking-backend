package com.tanay.bookingapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tanay.bookingapp.dto.ProviderLoginDTO;
import com.tanay.bookingapp.dto.ProviderRequestDTO;
import com.tanay.bookingapp.entity.Booking;
import com.tanay.bookingapp.entity.BookingStatus;
import com.tanay.bookingapp.entity.Provider;
import com.tanay.bookingapp.repository.BookingRepository;
import com.tanay.bookingapp.repository.ProviderRepository;
import com.tanay.bookingapp.security.JwtUtil;

@Service
public class ProviderService {

@Autowired
private ProviderRepository providerRepository;

@Autowired
private BookingRepository bookingRepository;

@Autowired
private JwtUtil jwtUtil;

// ✅ reuse encoder (better practice)
private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

//////////////// REGISTER //////////////////

public String registerProvider(ProviderRequestDTO dto){

// 🔥 prevent duplicate email
Provider existing = providerRepository.findByEmail(dto.getEmail());
if(existing != null){
throw new RuntimeException("Email already registered");
}

Provider provider = new Provider();

provider.setName(dto.getName());
provider.setEmail(dto.getEmail());
provider.setPhone(dto.getPhone());
provider.setCategory(dto.getCategory());
provider.setExperience(dto.getExperience());
provider.setPassword(encoder.encode(dto.getPassword()));

// default values
provider.setApproved(false);
provider.setAvailable(true);

providerRepository.save(provider);

return "Provider registered successfully. Waiting for admin approval.";
}

//////////////// LOGIN //////////////////

public String loginProvider(ProviderLoginDTO dto){

Provider provider = providerRepository.findByEmail(dto.getEmail());

if(provider == null){
throw new RuntimeException("Provider not found");
}

if(!encoder.matches(dto.getPassword(), provider.getPassword())){
throw new RuntimeException("Invalid credentials");
}

if(!provider.isApproved()){
throw new RuntimeException("Provider not approved yet");
}

return jwtUtil.generateToken(
provider.getId(),
provider.getEmail(),
"PROVIDER"
);
}

//////////////// JOBS //////////////////

public List<Booking> getProviderJobs(Long providerId){
return bookingRepository.findByProviderId(providerId);
}

//////////////// ACCEPT //////////////////

public String acceptJob(Long providerId, Long bookingId){

Booking booking = bookingRepository.findById(bookingId)
.orElseThrow(() -> new RuntimeException("Booking not found"));

if(booking.getProvider() == null || !booking.getProvider().getId().equals(providerId)){
throw new RuntimeException("This job is not assigned to you");
}

if(booking.getStatus() != BookingStatus.PENDING){
throw new RuntimeException("Only PENDING bookings can be accepted");
}

booking.setStatus(BookingStatus.CONFIRMED);
bookingRepository.save(booking);

return "Job accepted successfully";
}

//////////////// COMPLETE //////////////////

public String completeJob(Long providerId, Long bookingId){

Booking booking = bookingRepository.findById(bookingId)
.orElseThrow(() -> new RuntimeException("Booking not found"));

if(booking.getProvider() == null || !booking.getProvider().getId().equals(providerId)){
throw new RuntimeException("This job is not assigned to you");
}

if(booking.getStatus() != BookingStatus.CONFIRMED){
throw new RuntimeException("Booking must be CONFIRMED first");
}

/* 💰 earnings calculation */

double price = booking.getService().getPrice();

double commission = price * 0.2; // 20%
double earning = price - commission;

// avoid overwriting if already calculated
if(booking.getProviderEarning() == null){
booking.setPlatformCommission(commission);
booking.setProviderEarning(earning);
}

booking.setStatus(BookingStatus.COMPLETED);

bookingRepository.save(booking);

return "Job completed. You earned ₹" + earning;
}

//////////////// EARNINGS //////////////////

public Double getEarnings(Long providerId){

List<Booking> bookings = bookingRepository.findByProviderId(providerId);

return bookings.stream()
.filter(b -> b.getStatus() == BookingStatus.COMPLETED)
.mapToDouble(b -> b.getProviderEarning() != null ? b.getProviderEarning() : 0.0)
.sum();
}

//////////////// AVAILABILITY //////////////////

public String toggleAvailability(Long providerId){

Provider provider = providerRepository.findById(providerId)
.orElseThrow(() -> new RuntimeException("Provider not found"));

provider.setAvailable(!provider.isAvailable());

providerRepository.save(provider);

return "Availability updated to " + provider.isAvailable();
}

}