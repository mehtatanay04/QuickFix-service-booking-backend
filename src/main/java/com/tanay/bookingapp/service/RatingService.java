package com.tanay.bookingapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanay.bookingapp.dto.RatingRequestDTO;
import com.tanay.bookingapp.entity.Provider;
import com.tanay.bookingapp.entity.Rating;
import com.tanay.bookingapp.entity.User;
import com.tanay.bookingapp.repository.ProviderRepository;
import com.tanay.bookingapp.repository.RatingRepository;
import com.tanay.bookingapp.repository.UserRepository;

@Service
public class RatingService {

@Autowired
private RatingRepository ratingRepository;

@Autowired
private ProviderRepository providerRepository;

@Autowired
private UserRepository userRepository;

public String rateProvider(Long userId, RatingRequestDTO dto) {

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