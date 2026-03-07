package com.tanay.bookingapp.dto;

public class ProviderRequestDTO {

private String name;
private String email;
private String phone;
private String category;
private int experience;
private String password;

public ProviderRequestDTO(){}

public String getName(){
return name;
}

public void setName(String name){
this.name=name;
}

public String getEmail(){
return email;
}

public void setEmail(String email){
this.email=email;
}

public String getPhone(){
return phone;
}

public void setPhone(String phone){
this.phone=phone;
}

public String getCategory(){
return category;
}

public void setCategory(String category){
this.category=category;
}

public int getExperience(){
return experience;
}

public void setExperience(int experience){
this.experience=experience;
}

public String getPassword(){
return password;
}

public void setPassword(String password){
this.password=password;
}

}