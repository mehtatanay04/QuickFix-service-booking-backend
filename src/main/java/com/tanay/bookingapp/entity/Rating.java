package com.tanay.bookingapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ratings")
public class Rating {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@ManyToOne
@JoinColumn(name = "user_id")
private User user;

@ManyToOne
@JoinColumn(name = "provider_id")
private Provider provider;

private int score; // 1-5

private String review;

public Rating() {}

public Rating(User user, Provider provider, int score, String review) {
this.user = user;
this.provider = provider;
this.score = score;
this.review = review;
}

public Long getId() {
return id;
}

public User getUser() {
return user;
}

public Provider getProvider() {
return provider;
}

public int getScore() {
return score;
}

public void setScore(int score) {
this.score = score;
}

public String getReview() {
return review;
}

public void setReview(String review) {
this.review = review;
}
}