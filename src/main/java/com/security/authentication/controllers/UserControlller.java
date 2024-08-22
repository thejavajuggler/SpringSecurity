package com.security.authentication.controllers;

import com.security.authentication.dto.AuthenticationResponse;
import com.security.authentication.entities.User;
import com.security.authentication.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControlller {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> addUser(@NonNull @RequestBody User user) {
        AuthenticationResponse authenticationResponse =  userService.registerUser(user);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUsers(@NonNull @RequestBody User user) {
        AuthenticationResponse authenticationResponse =  userService.authenticateUser(user);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String adminEndpoint() {
        return "This is the admin endpoint. Only users with the ADMIN role can access this.";
    }

    @PreAuthorize("hasAuthority('READ_PRIVILEGES')")
    @GetMapping("/user")
    public String userEndpoint() {
        return "This is the user endpoint. Users with ADMIN or MEMBER roles can access this.";
    }
}
