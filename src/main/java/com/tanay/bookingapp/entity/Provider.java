package com.tanay.bookingapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "providers")
public class Provider {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false)
private String name;

@Column(unique = true, nullable = false)
private String email;

@Column(nullable = false)
private String phone;

@Column(nullable = false)
@JsonIgnore
private String password;

@Column(nullable = false)
private String category;

@Column(nullable = false)
private int experience;

private double rating = 0.0;

@Column(nullable = false)
private boolean approved = false;

@Column(nullable = false)
private boolean available = true;

public Provider() {}

//////////////// GETTERS & SETTERS //////////////////

public Long getId() {
return id;
}

public String getName() {
return name;
}
public void setName(String name) {
this.name = name;
}

public String getEmail() {
return email;
}
public void setEmail(String email) {
this.email = email;
}

public String getPhone() {
return phone;
}
public void setPhone(String phone) {
this.phone = phone;
}

public String getPassword() {
return password;
}
public void setPassword(String password) {
this.password = password;
}

public String getCategory() {
return category;
}
public void setCategory(String category) {
this.category = category;
}

public int getExperience() {
return experience;
}
public void setExperience(int experience) {
this.experience = experience;
}

public double getRating() {
return rating;
}
public void setRating(double rating) {
this.rating = rating;
}

public boolean isApproved() {
return approved;
}
public void setApproved(boolean approved) {
this.approved = approved;
}

public boolean isAvailable() {
return available;
}
public void setAvailable(boolean available) {
this.available = available;
}

}