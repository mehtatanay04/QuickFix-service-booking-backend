package com.tanay.bookingapp.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanay.bookingapp.dto.LoginRequestDTO;
import com.tanay.bookingapp.dto.LoginResponseDTO;
import com.tanay.bookingapp.dto.RegisterRequestDTO;
import com.tanay.bookingapp.dto.UserResponseDTO;
import com.tanay.bookingapp.entity.User;
import com.tanay.bookingapp.security.JwtUtil;
import com.tanay.bookingapp.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

private final UserService userService;
private final JwtUtil jwtUtil;

public AuthController(UserService userService, JwtUtil jwtUtil) {
this.userService = userService;
this.jwtUtil = jwtUtil;
}

// ✅ REGISTER
@PostMapping("/register")
public UserResponseDTO register(@RequestBody RegisterRequestDTO request) {

User user = new User();
user.setName(request.getName());
user.setEmail(request.getEmail());
user.setPassword(request.getPassword());

User savedUser = userService.registerUser(user);

return new UserResponseDTO(
savedUser.getId(),
savedUser.getName(),
savedUser.getEmail(),
savedUser.getRole()
);
}

// ✅ LOGIN
@PostMapping("/login")
public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {

User user = userService.loginUser(
loginRequest.getEmail(),
loginRequest.getPassword()
);

// 🔥 FIXED HERE (no static call)
String token = jwtUtil.generateToken(
user.getId(),
user.getEmail(),
user.getRole()
);

return new LoginResponseDTO(
token,
user.getEmail(),
user.getRole()
);
}

}