package com.tanay.bookingapp.dto;

import java.time.LocalDate;

public class BookingRequestDTO {

private Long serviceId;
private LocalDate bookingDate;
private String timeSlot;
private String address;
private String notes;

public BookingRequestDTO(){}

public Long getServiceId() {
return serviceId;
}

public void setServiceId(Long serviceId) {
this.serviceId = serviceId;
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

}