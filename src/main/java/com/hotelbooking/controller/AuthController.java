package com.hotelbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotelbooking.dto.AuthResponse;
import com.hotelbooking.dto.LoginRequest;
import com.hotelbooking.dto.UserDTO;
import com.hotelbooking.entity.User;
import com.hotelbooking.security.JwtService;
import com.hotelbooking.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{
	@Autowired
    private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        UserDTO createdUser = userService.registerUser(user);
        return ResponseEntity.ok(createdUser);
    }
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

	    User user = userService.findByEmail(request.getEmail());

	    if (user == null) {
	        return ResponseEntity.status(401).build();
	    }

	    boolean matches = passwordEncoder.matches(
	            request.getPassword(),
	            user.getPassword()
	    );

	    if (!matches) {
	        return ResponseEntity.status(401).build();
	    }

	    // ✅ Generate token using JwtService
	    String token = jwtService.generateToken(user);

	    // ✅ Create response object
	    AuthResponse response = new AuthResponse();
	    response.setToken(token);
	    response.setUserId(user.getId());
	    response.setFullName(user.getFullName());
	    response.setEmail(user.getEmail());
	    response.setRole(user.getRole());

	    return ResponseEntity.ok(response);
	}


}
