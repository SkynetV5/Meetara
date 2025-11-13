package com.metara.metara.controller;

import com.metara.metara.models.dto.UserDto;
import com.metara.metara.models.dto.UserRegisterDto;
import com.metara.metara.models.entity.Role;
import com.metara.metara.models.entity.User;
import com.metara.metara.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDto userRegister){
        try{
            User user = userService.registerUser(userRegister.getEmail(), userRegister.getUsername(), userRegister.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(user);

        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Create new user", description = "Creates a new  user with custom data")
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userCreateDto){
        try{
            User newUser = new User();
            newUser.setUsername(userCreateDto.getUsername());
            newUser.setEmail(userCreateDto.getEmail());
            newUser.setPassword(userCreateDto.getPassword());
            newUser.setRoles(userCreateDto.getRoles());
            newUser.setEnabled(userCreateDto.isEnabled());
            newUser.setCreatedAt(userCreateDto.getCreatedAt());

            User createUser = userService.createUser(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(createUser);
        }
        catch (RuntimeException e){
            return  ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get all users", description = "Returns all users without pagination")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@Parameter(description = "User ID") @PathVariable Long id){
        try{
            User user = userService.getUserByIdOrThrow(id);
            return ResponseEntity.ok(user);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));

        }
    }

    @Operation(summary = "Search users by username")
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(
            @Parameter(description = "Username")
            @PathVariable String username
    ){
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search users by email")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(
            @Parameter(description = "Email")
            @PathVariable String email
    ){
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update user", description = "Updates user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @Parameter(description = "User ID")
            @PathVariable Long id,
            @RequestBody User updatedUser) {
        try {
            User user = userService.updateUser(id, updatedUser);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Change password", description = "Changes user password after verifying old password")
    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(
            @Parameter(description = "User ID")
            @PathVariable Long id,
            @RequestBody Map<String, String> passwords) {
        try {
            String oldPassword = passwords.get("oldPassword");
            String newPassword = passwords.get("newPassword");

            User user = userService.changePassword(id, oldPassword, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Activate user")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(
            @Parameter(description = "User ID")
            @PathVariable Long id) {
        try {
            User user = userService.enabledUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Deactivate user")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(
            @Parameter(description = "User ID")
            @PathVariable Long id) {
        try {
            User user = userService.unenabledUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @Operation(summary = "Delete user", description = "Deletes user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "User ID")
            @PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get user roles", description = "Returns all roles assigned to user")
    @GetMapping("/{id}/roles")
    public ResponseEntity<?> getUserRoles(
            @Parameter(description = "User ID")
            @PathVariable Long id) {
        try {
            Set<Role> roles = userService.getUserRoles(id);
            return ResponseEntity.ok(roles);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Assign roles to user", description = "Replaces all user roles with provided ones")
    @PutMapping("/{id}/roles")
    public ResponseEntity<?> assignRoles(
            @Parameter(description = "User ID")
            @PathVariable Long id,
            @RequestBody Set<String> roleNames) {
        try {
            User user = userService.assignRoles(id, roleNames);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Add role to user", description = "Adds a single role to user's existing roles")
    @PostMapping("/{id}/roles/{roleName}")
    public ResponseEntity<?> addRole(
            @Parameter(description = "User ID")
            @PathVariable Long id,
            @Parameter(description = "Role name (e.g., ROLE_ADMIN)")
            @PathVariable String roleName) {
        try {
            User user = userService.addRole(id, roleName);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Remove role from user")
    @DeleteMapping("/{id}/roles/{roleName}")
    public ResponseEntity<?> removeRole(
            @Parameter(description = "User ID")
            @PathVariable Long id,
            @Parameter(description = "Role name")
            @PathVariable String roleName) {
        try {
            User user = userService.removeRole(id, roleName);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Check if user has role")
    @GetMapping("/{id}/roles/{roleName}/check")
    public ResponseEntity<Map<String, Boolean>> checkUserHasRole(
            @Parameter(description = "User ID")
            @PathVariable Long id,
            @Parameter(description = "Role name")
            @PathVariable String roleName) {
        try {
            boolean hasRole = userService.hasRole(id, roleName);
            return ResponseEntity.ok(Map.of("hasRole", hasRole));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(Map.of("hasRole", false));
        }
    }

}
