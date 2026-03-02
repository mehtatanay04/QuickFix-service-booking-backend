package com.tanay.bookingapp.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tanay.bookingapp.entity.Role;
import com.tanay.bookingapp.entity.User;
import com.tanay.bookingapp.exception.EmailAlreadyExistsException;
import com.tanay.bookingapp.exception.InvalidCredentialsException;
import com.tanay.bookingapp.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ✅ REGISTER USER
    public User registerUser(User user) {

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 🔐 Encode password
        user.setPassword(encoder.encode(user.getPassword()));

        // ✅ Set default role using ENUM
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    // ✅ LOGIN USER
    public User loginUser(String email, String password) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return user;
    }
}