package com.tanay.bookingapp.entity;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking {
@Id
@GeneratedValue (strategy = GenerationType.IDENTITY)

private Long id;

@ManyToOne
@JoinColumn(name = "user_id")
private User user;
@ManyToOne
@JoinColumn(name = "service_id")
private ServiceEntity service;
private LocalDateTime bookingTime;
private String status;

public Booking() {
}

public Booking (User user, ServiceEntity service, LocalDateTime bookingTime, String status) {
	this.user = user;
	this.service = service;
	this.bookingTime = bookingTime;
	this.status =status;
}

public Long getId() {
	return id;
}


public User getUser() {
	return user;
}


public ServiceEntity getService() {
	return service;
}

public LocalDateTime getBookingTime() {
	return bookingTime;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}


}
