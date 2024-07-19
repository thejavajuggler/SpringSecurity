package com.security.authentication.services;

import com.security.authentication.dto.AuthenticationResponse;
import com.security.authentication.entities.Users;
import com.security.authentication.exceptions.UserNotFoundException;
import com.security.authentication.repositories.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    public AuthenticationResponse authenticateUser(@NonNull Users users) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(users.getEmail(), users.getPassword()));
        var dbUser = userRepository.findByEmail(users.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));;
        String jwtToken = jwtService.generateToken(dbUser);
        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse registerUser(@NonNull Users users) {

        String encodedPassword = passwordEncoder.encode(users.getPassword());
        users.setId(UUID.randomUUID());
        users.setPassword(encodedPassword);
        Users savedUser = userRepository.save(users);
        String jwtToken = jwtService.generateToken(savedUser);
        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .build();
    }
}
