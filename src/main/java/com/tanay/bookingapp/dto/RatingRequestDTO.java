package com.tanay.bookingapp.dto;

public class RatingRequestDTO {

private Long providerId;
private int score;
private String review;

public RatingRequestDTO(){}

public Long getProviderId(){
return providerId;
}

public void setProviderId(Long providerId){
this.providerId = providerId;
}

public int getScore(){
return score;
}

public void setScore(int score){
this.score = score;
}

public String getReview(){
return review;
}

public void setReview(String review){
this.review = review;
}

}