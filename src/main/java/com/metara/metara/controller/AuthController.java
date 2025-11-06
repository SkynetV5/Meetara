package com.metara.metara.controller;

import com.metara.metara.config.JwtUtil;
import com.metara.metara.models.dto.AuthorizationLoginDto;
import com.metara.metara.models.entity.User;
import com.metara.metara.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login( @Valid  @RequestBody AuthorizationLoginDto dto){


        User user = userService.authorizeUser(dto.getEmail(), dto.getPassword());

        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
