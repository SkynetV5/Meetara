package com.metara.metara.controller;

import com.metara.metara.models.dto.UserRegisterDto;
import com.metara.metara.models.entity.User;
import com.metara.metara.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/users")
@Tag(name = "User Management", description = "API Users")
public class UserController {

    @Autowired
    private UserService userService;

    // CREATE

    @Operation(summary = "Register new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDto dto){
        try{
            User user = userService.registerUser(dto.getEmail(), dto.getUsername(), dto.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);

        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
