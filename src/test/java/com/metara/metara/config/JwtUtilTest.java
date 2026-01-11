package com.metara.metara.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {
    
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }

    @Test
    void generateToken_ShouldGenerateValidToken(){
        //Given
        String username = "testUser";

        //When
        String token = jwtUtil.generateToken(username);

        //Then
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername(){
        //Given
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        //When
        String extractedUsername = jwtUtil.extractUsername(token);

        //Then
        assertEquals(username, extractedUsername);
    }

    @Test
    void validateToken_ShouldReturnTrue_ForValidToken(){
        //Given
        String invalidToken = "invalid.token.here";
        UserDetails userDetails = User.withUsername("testUser").password("password").roles("USER").build();

        //When
        boolean isValid = jwtUtil.validateToken(invalidToken,userDetails);

        //Then
        assertFalse(isValid);
    }
}