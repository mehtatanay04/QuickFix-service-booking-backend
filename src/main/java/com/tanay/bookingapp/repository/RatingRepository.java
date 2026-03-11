package com.tanay.bookingapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tanay.bookingapp.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {

List<Rating> findByProviderId(Long providerId);

}