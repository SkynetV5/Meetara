package com.metara.metara.controller;

import com.metara.metara.config.JwtUtil;
import com.metara.metara.models.dto.AuthorizationLoginDto;
import com.metara.metara.models.entity.User;
import com.metara.metara.service.CustomUserDetailsService;
import com.metara.metara.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Login", description = "Authenticate and get JWT token")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthorizationLoginDto loginRequest) {
        try {


            // Authenticate
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            System.out.println("✅ Authenticated successfully!");

            // Generate token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "type", "Bearer",
                    "username", loginRequest.getUsername()
            ));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid username or password"));
        }
    }
}
