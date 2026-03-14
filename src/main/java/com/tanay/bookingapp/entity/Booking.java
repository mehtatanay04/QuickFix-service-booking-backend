package com.tanay.bookingapp.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "user_id")
private User user;

@ManyToOne
@JoinColumn(name = "service_id")
private ServiceEntity service;

@ManyToOne
@JoinColumn(name = "provider_id")
private Provider provider;

private LocalDate bookingDate;

private String timeSlot;

private String address;

private String notes;

@Enumerated(EnumType.STRING)
private BookingStatus status;

public Booking() {}

public Long getId() {
return id;
}

public User getUser() {
return user;
}

public void setUser(User user) {
this.user = user;
}

public ServiceEntity getService() {
return service;
}

public void setService(ServiceEntity service) {
this.service = service;
}

public Provider getProvider() {
return provider;
}

public void setProvider(Provider provider) {
this.provider = provider;
}

public LocalDate getBookingDate() {
return bookingDate;
}

public void setBookingDate(LocalDate bookingDate) {
this.bookingDate = bookingDate;
}

public String getTimeSlot() {
return timeSlot;
}

public void setTimeSlot(String timeSlot) {
this.timeSlot = timeSlot;
}

public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}

public String getNotes() {
return notes;
}

public void setNotes(String notes) {
this.notes = notes;
}

public BookingStatus getStatus() {
return status;
}

public void setStatus(BookingStatus status) {
this.status = status;
}
}