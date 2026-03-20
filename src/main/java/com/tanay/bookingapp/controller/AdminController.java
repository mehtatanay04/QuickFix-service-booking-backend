package com.tanay.bookingapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanay.bookingapp.dto.AssignProviderDTO;
import com.tanay.bookingapp.dto.ServiceRequestDTO;
import com.tanay.bookingapp.dto.UpdateBookingStatusDTO;
import com.tanay.bookingapp.entity.Booking;
import com.tanay.bookingapp.entity.BookingStatus;
import com.tanay.bookingapp.entity.Provider;
import com.tanay.bookingapp.entity.ServiceEntity;
import com.tanay.bookingapp.repository.BookingRepository;
import com.tanay.bookingapp.repository.ProviderRepository;
import com.tanay.bookingapp.repository.ServiceRepository;
import com.tanay.bookingapp.security.RoleChecker;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	private BookingRepository bookingRepository;

	@GetMapping("/bookings")
	public List<Booking> getAllBookings(HttpServletRequest request) {

	if(!RoleChecker.hasRole(request, "ADMIN")) {
	throw new RuntimeException("Forbidden: Admin access only");
	}

	return bookingRepository.findAll();
	}


@Autowired
private ServiceRepository serviceRepository;


@PutMapping("/booking/{id}/status")
public String updateBookingStatus(
@PathVariable Long id,
@RequestBody UpdateBookingStatusDTO dto,
HttpServletRequest request
) {

if(!RoleChecker.hasRole(request, "ADMIN")) {
throw new RuntimeException("Forbidden: Admin access only");
}

Booking booking = bookingRepository.findById(id)
.orElseThrow(() -> new RuntimeException("Booking not found"));

booking.setStatus(BookingStatus.valueOf(dto.getStatus()));


bookingRepository.save(booking);

return "Booking status updated to " + dto.getStatus();
}
@Autowired
private ProviderRepository providerRepository;
@GetMapping("/providers")
public List<Provider> getAllProviders(HttpServletRequest request) {

if(!RoleChecker.hasRole(request, "ADMIN")) {
throw new RuntimeException("Forbidden: Admin access only");
}

return providerRepository.findAll();
}
@PutMapping("/provider/{id}/approve")
public String approveProvider(
@PathVariable Long id,
HttpServletRequest request
) {

if(!RoleChecker.hasRole(request, "ADMIN")) {
throw new RuntimeException("Forbidden: Admin access only");
}

Provider provider = providerRepository.findById(id)
.orElseThrow(() -> new RuntimeException("Provider not found"));

provider.setApproved(true);

providerRepository.save(provider);

return "Provider approved successfully";
}

@GetMapping("/dashboard")
public String adminDashboard(HttpServletRequest request) {

if(!RoleChecker.hasRole(request, "ADMIN")) {
return "Forbidden: Admin access only";
}

return "Welcome ADMIN, your ID = " + request.getAttribute("userId");
}

@PostMapping("/add-service")
public String addService(
@RequestBody ServiceRequestDTO dto,
HttpServletRequest request
) {

if(!RoleChecker.hasRole(request, "ADMIN")) {
return "Forbidden: Admin access only";
}

ServiceEntity service = new ServiceEntity(
dto.getName(),
dto.getDescription(),
dto.getPrice()
);

serviceRepository.save(service);

return "Service created successfully";
}

@PutMapping("/booking/{id}/assign-provider")
public String assignProviderToBooking(
@PathVariable Long id,
@RequestBody AssignProviderDTO dto,
HttpServletRequest request
) {

if(!RoleChecker.hasRole(request, "ADMIN")) {
throw new RuntimeException("Forbidden: Admin access only");
}

Booking booking = bookingRepository.findById(id)
.orElseThrow(() -> new RuntimeException("Booking not found"));

Provider provider = providerRepository.findById(dto.getProviderId())
.orElseThrow(() -> new RuntimeException("Provider not found"));

booking.setProvider(provider);

bookingRepository.save(booking);

return "Provider assigned successfully";
}
@GetMapping("/analytics")
public Map<String, Object> getAnalytics(){

List<Booking> bookings = bookingRepository.findAll();

long totalBookings = bookings.size();

double totalRevenue = bookings.stream()
.filter(b -> b.getStatus() == BookingStatus.COMPLETED)
.mapToDouble(b -> b.getService().getPrice())
.sum();

double totalCommission = bookings.stream()
.filter(b -> b.getStatus() == BookingStatus.COMPLETED)
.mapToDouble(b -> b.getPlatformCommission() != null ? b.getPlatformCommission() : 0.0)
.sum();

Map<String, Object> data = new HashMap<>();
data.put("totalBookings", totalBookings);
data.put("totalRevenue", totalRevenue);
data.put("totalCommission", totalCommission);

return data;
}

}
