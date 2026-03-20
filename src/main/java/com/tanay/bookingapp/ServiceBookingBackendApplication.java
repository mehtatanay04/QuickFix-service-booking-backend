package com.tanay.bookingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServiceBookingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceBookingBackendApplication.class, args);
	}

}
/* [
{
    "address": "Kota Rajasthan",
    "bookingDate": "2026-03-20",
    "id": 2,
    "notes": "Pipe leakage",
    "provider": {
        "approved": true,
        "category": "Plumbing",
        "email": "ramesh@provider.com",
        "experience": 5,
        "id": 1,
        "name": "Ramesh Kumar",
        "password": null,
        "phone": "9999999999",
        "rating": 0.0
    },
    "service": {
        "name": "Plumbing",
        "description": "Pipe fixing service",
        "price": 500.0,
        "id": 1
    },
    "status": "PENDING",
    "timeSlot": "10AM-12PM",
    "user": {
        "email": "tanay@test.com",
        "id": 11,
        "name": "Tanay",
        "password": "$2a$10$AZgPizfRr9xEwKhzxn./8uQ.gura5Y2kwOtgei5dnY.cmiXyEszSW",
        "role": "USER"
    }
}
] */