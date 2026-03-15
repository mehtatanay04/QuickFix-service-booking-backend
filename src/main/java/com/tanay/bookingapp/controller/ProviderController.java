package com.tanay.bookingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanay.bookingapp.dto.ProviderLoginDTO;
import com.tanay.bookingapp.dto.ProviderRequestDTO;
import com.tanay.bookingapp.entity.Booking;
import com.tanay.bookingapp.entity.BookingStatus;
import com.tanay.bookingapp.entity.Provider;
import com.tanay.bookingapp.repository.BookingRepository;
import com.tanay.bookingapp.repository.ProviderRepository;
import com.tanay.bookingapp.security.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/provider")
public class ProviderController {
	@Autowired
	private JwtUtil jwtUtil;

@Autowired
private ProviderRepository providerRepository;

@Autowired
private BookingRepository bookingRepository;

@PostMapping("/register")
public String registerProvider(@RequestBody ProviderRequestDTO dto) {

Provider provider = new Provider();

provider.setName(dto.getName());
provider.setEmail(dto.getEmail());
provider.setPhone(dto.getPhone());
provider.setCategory(dto.getCategory());
provider.setExperience(dto.getExperience());

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
provider.setPassword(encoder.encode(dto.getPassword()));

providerRepository.save(provider);

return "Provider registered successfully. Waiting for admin approval.";
}

@PostMapping("/login")
public String providerLogin(@RequestBody ProviderLoginDTO dto){

Provider provider = providerRepository.findByEmail(dto.getEmail());

if(provider == null){
throw new RuntimeException("Provider not found");
}

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

if(!encoder.matches(dto.getPassword(), provider.getPassword())){
throw new RuntimeException("Invalid credentials");
}

if(!provider.isApproved()){
throw new RuntimeException("Provider not approved yet");
}

String token = jwtUtil.generateToken(
provider.getId(),
provider.getEmail(),
"PROVIDER"
);

return token;
}

@GetMapping("/my-jobs")
public List<Booking> getProviderJobs(HttpServletRequest request){

Number providerIdNumber = (Number) request.getAttribute("userId");
Long providerId = providerIdNumber.longValue();

return bookingRepository.findByProviderId(providerId);
}
@PutMapping("/booking/{id}/complete")
public String completeBooking(
@PathVariable Long id,
HttpServletRequest request
){

Number providerIdNumber = (Number) request.getAttribute("userId");
Long providerId = providerIdNumber.longValue();

Booking booking = bookingRepository.findById(id)
.orElseThrow(() -> new RuntimeException("Booking not found"));

if(booking.getProvider() == null){
throw new RuntimeException("No provider assigned");
}

if(!booking.getProvider().getId().equals(providerId)){
throw new RuntimeException("You are not assigned to this booking");
}

booking.setStatus(BookingStatus.COMPLETED);

bookingRepository.save(booking);

return "Booking completed successfully";
}
@PutMapping("/accept-job/{bookingId}")
public String acceptJob(
@PathVariable Long bookingId,
HttpServletRequest request
){

Number providerIdNumber = (Number) request.getAttribute("userId");
Long providerId = providerIdNumber.longValue();

Booking booking = bookingRepository.findById(bookingId)
.orElseThrow(() -> new RuntimeException("Booking not found"));

if(booking.getProvider() == null || !booking.getProvider().getId().equals(providerId)){
throw new RuntimeException("This job is not assigned to you");
}

if(booking.getStatus() != BookingStatus.PENDING){
throw new RuntimeException("Booking cannot be accepted");
}

booking.setStatus(BookingStatus.CONFIRMED);

bookingRepository.save(booking);

return "Job accepted successfully";
}
@PutMapping("/complete-job/{bookingId}")
public String completeJob(
@PathVariable Long bookingId,
HttpServletRequest request
){

Number providerIdNumber = (Number) request.getAttribute("userId");
Long providerId = providerIdNumber.longValue();

Booking booking = bookingRepository.findById(bookingId)
.orElseThrow(() -> new RuntimeException("Booking not found"));

if(booking.getProvider() == null || !booking.getProvider().getId().equals(providerId)){
throw new RuntimeException("This job is not assigned to you");
}

if(booking.getStatus() != BookingStatus.CONFIRMED){
throw new RuntimeException("Booking must be confirmed first");
}

booking.setStatus(BookingStatus.COMPLETED);

bookingRepository.save(booking);

return "Job completed successfully";
}


}