package com.tanay.bookingapp.dto;

public class ServiceRequestDTO {

private String name;
private String description;
private Double price;

public ServiceRequestDTO() {
}

public String getName() {
return name;
}

public String getDescription() {
return description;
}

public Double getPrice() {
return price;
}

public void setName(String name) {
this.name = name;
}

public void setDescription(String description) {
this.description = description;
}

public void setPrice(Double price) {
this.price = price;
}
}
