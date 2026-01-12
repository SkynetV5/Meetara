package com.metara.metara.service;

import com.metara.metara.models.entity.Role;
import com.metara.metara.models.entity.User;
import com.metara.metara.repository.RoleRepository;
import com.metara.metara.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEnabled(true);

        role = new Role();
        role.setId(1L);
        role.setName("USER");

    }

    @Test
    void createUser_ShouldCreateUserSuccesfully(){
        //Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        //When
        User result = userService.createUser(user);

        //Then
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(user);
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailExists() {
        // Given
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.createUser(user));
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void getUserById_ShouldReturnUser_WhenExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userService.getUserById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void getUserById_ShouldReturnEmpty_WhenNotExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.getUserById(1L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void deleteUser_ShouldDelete_WhenExists() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenNotExists() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found with id:1", exception.getMessage());
    }

    @Test
    void getTotalUsers_ShouldReturnCount() {
        // Given
        when(userRepository.count()).thenReturn(10L);

        // When
        long result = userService.getTotalUsers();

        // Then
        assertEquals(10L, result);
    }

    @Test
    void getAllUsers_ShouldReturnPagedUsers() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user));
        when(userRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<User> result = userService.getAllUsers(pageable);

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals(user, result.getContent().get(0));
    }
}
