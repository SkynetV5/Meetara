package com.metara.metara.models.dto;

import com.metara.metara.models.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;

@Schema(description = "Data requires to create role")
public class RoleDto {
    @Schema(description = "Name", example = "USER")
    @NotBlank
    private String name;

    @Schema(description = "Users", example = "[{\"id\":1,\"username\":\"jan.kowalski\",\"email\":\"jan@example.com\",\"enabled\":true,\"createdAt\":\"2025-11-20T12:00:00\",\"roles\":[]}, {\"id\":2,\"username\":\"anna.nowak\",\"email\":\"anna@example.com\",\"enabled\":false,\"createdAt\":\"2025-11-19T08:30:00\",\"roles\":[]}]")
    private Set<User> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}

