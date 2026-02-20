package com.tanay.bookingapp.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
	
	public User registerUser(User user) {

		User existingUser = userRepository.findByEmail(user.getEmail());

		if(existingUser != null) {
		throw new EmailAlreadyExistsException("Email already registered");
		}

		BCryptPasswordEncoder encoder  = new BCryptPasswordEncoder();

		// ✅ encode password
		user.setPassword(encoder.encode(user.getPassword()));

		// 🔥 ADD THIS LINE (IMPORTANT)
		user.setRole("USER");

		return userRepository.save(user);
		}
		public User loginUser(String email, String password) {
			User user = userRepository.findByEmail(email);
			if (user == null) {
				throw new RuntimeException("Invalid email or password");
			}
			
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			
			if (!encoder.matches(password, user.getPassword())) {
				throw new InvalidCredentialsException("Invalid email or password");
			}
			return user;
		}

}

