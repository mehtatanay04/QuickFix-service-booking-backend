package com.tanay.bookingapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tanay.bookingapp.dto.RatingRequestDTO;
import com.tanay.bookingapp.entity.Provider;
import com.tanay.bookingapp.entity.Rating;
import com.tanay.bookingapp.entity.ServiceEntity;
import com.tanay.bookingapp.entity.User;
import com.tanay.bookingapp.repository.ProviderRepository;
import com.tanay.bookingapp.repository.RatingRepository;
import com.tanay.bookingapp.repository.ServiceRepository;
import com.tanay.bookingapp.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

@Autowired
private ServiceRepository serviceRepository;

@Autowired
private UserRepository userRepository;

@Autowired
private ProviderRepository providerRepository;

@Autowired
private RatingRepository ratingRepository;

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

User user = userRepository.findById(userId)
.orElseThrow(() -> new RuntimeException("User not found"));

Provider provider = providerRepository.findById(dto.getProviderId())
.orElseThrow(() -> new RuntimeException("Provider not found"));

Rating rating = new Rating(
user,
provider,
dto.getScore(),
dto.getReview()
);

ratingRepository.save(rating);

updateProviderRating(provider.getId());

return "Rating submitted successfully";
}

private void updateProviderRating(Long providerId){

Provider provider = providerRepository.findById(providerId)
.orElseThrow(() -> new RuntimeException("Provider not found"));

List<Rating> ratings = ratingRepository.findByProviderId(providerId);

double avg = ratings.stream()
.mapToInt(Rating::getScore)
.average()
.orElse(0.0);

provider.setRating(avg);

providerRepository.save(provider);
}
}