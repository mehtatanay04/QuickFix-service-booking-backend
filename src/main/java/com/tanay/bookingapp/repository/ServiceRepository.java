package com.tanay.bookingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tanay.bookingapp.entity.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long>{

}
