package com.metara.metara.models.dto;


import com.metara.metara.models.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Data requires to create user")
public class UserDto {

    @Schema(description = "Email Adress", example = "string@example.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Username", example = "string")
    @NotBlank
    private String username;

    @Schema(description = "Password", example = "string")
    @NotBlank
    private String password;

    @Schema(description = "Enabled", example = "true")
    @NotBlank
    private boolean enabled;

    @Schema(description = "Created at")
    @NotBlank
    private LocalDateTime createdAt;

    @Schema(description = "User Roles")
    @NotBlank
    private Set<Role> roles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
