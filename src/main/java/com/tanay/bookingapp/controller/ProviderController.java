package com.tanay.bookingapp.controller;

import java.util.List;
import com.tanay.bookingapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanay.bookingapp.dto.ProviderLoginDTO;
import com.tanay.bookingapp.dto.ProviderRequestDTO;
import com.tanay.bookingapp.entity.Booking;
import com.tanay.bookingapp.entity.Provider;
import com.tanay.bookingapp.repository.BookingRepository;
import com.tanay.bookingapp.repository.ProviderRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/provider")
public class ProviderController {
	@Autowired
	private JwtUtil jwtUtil;

@Autowired
private ProviderRepository providerRepository;

@Autowired
private BookingRepository bookingRepository;

@PostMapping("/register")
public String registerProvider(@RequestBody ProviderRequestDTO dto) {

Provider provider = new Provider();

provider.setName(dto.getName());
provider.setEmail(dto.getEmail());
provider.setPhone(dto.getPhone());
provider.setCategory(dto.getCategory());
provider.setExperience(dto.getExperience());

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
provider.setPassword(encoder.encode(dto.getPassword()));

providerRepository.save(provider);

return "Provider registered successfully. Waiting for admin approval.";
}

@PostMapping("/login")
public String providerLogin(@RequestBody ProviderLoginDTO dto){

Provider provider = providerRepository.findByEmail(dto.getEmail());

if(provider == null){
throw new RuntimeException("Provider not found");
}

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

if(!encoder.matches(dto.getPassword(), provider.getPassword())){
throw new RuntimeException("Invalid credentials");
}

if(!provider.isApproved()){
throw new RuntimeException("Provider not approved yet");
}

String token = jwtUtil.generateToken(
provider.getId(),
provider.getEmail(),
"PROVIDER"
);

return token;
}

@GetMapping("/my-jobs")
public List<Booking> getProviderJobs(HttpServletRequest request){

Number providerIdNumber = (Number) request.getAttribute("userId");
Long providerId = providerIdNumber.longValue();

return bookingRepository.findByProviderId(providerId);
}
}