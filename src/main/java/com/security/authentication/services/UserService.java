package com.security.authentication.services;

import com.security.authentication.config.JwtUtil;
import com.security.authentication.dto.AuthenticationResponse;
import com.security.authentication.entities.Role;
import com.security.authentication.entities.User;
import com.security.authentication.exceptions.UserNotFoundException;
import com.security.authentication.repositories.PermissionRepository;
import com.security.authentication.repositories.RoleRepository;
import com.security.authentication.repositories.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;


    public AuthenticationResponse authenticateUser(@NonNull User user) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        var dbUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        System.out.println(dbUser.getEmail());
        String jwtToken = jwtUtil.generateToken(dbUser);
        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse registerUser(@NonNull User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setId(UUID.randomUUID());
        user.setPassword(encodedPassword);

        Set<Role> roles = new HashSet<>();
        for (Role role : user.getRoles()) {
            Role dbRole = roleRepository.findByName(role.getName())
                    .orElseThrow(() -> new RuntimeException("Role not found: " + role.getName()));
            roles.add(dbRole);
        }
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        String jwtToken = jwtUtil.generateToken(savedUser);
        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .build();
    }
}
