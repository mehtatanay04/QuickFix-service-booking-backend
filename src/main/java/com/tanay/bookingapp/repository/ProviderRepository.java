package com.tanay.bookingapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tanay.bookingapp.entity.Provider;

public interface ProviderRepository extends JpaRepository<Provider, Long> {

Provider findByEmail(String email);

Optional<Provider> findFirstByCategoryAndApprovedTrue(String category);

}