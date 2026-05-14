package com.example.ecommerce.controller;

import com.example.ecommerce.dto.AuthRequest;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;       // verifies username and password

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;                   // for hashing passwords

    // Registers a new user
    @PostMapping("/register")
    public String register(@RequestBody AuthRequest authRequest) {

        User user = new User();

        user.setName(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setEmail(authRequest.getUserEmail());
        user.setPhone(authRequest.getUserPhone());

        userRepository.save(user);

        return "User registered!";
    }

    // Logs in and returns a JWT token
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {

        // Verify credentials — Spring internally loads user from DB and compares hashed password
        // Throws BadCredentialsException automatically if wrong
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            System.out.println("LOGIN SUCCESS");

            // Credentials are correct — generate and return JWT token to client
            // Client must store this and send it in every future request as: Authorization: Bearer <token>
            return jwtUtil.generateToken(authRequest.getUsername());
        } catch (Exception e) {

            e.printStackTrace();

            return "ERROR : " + e.getMessage();
        }
    }
}