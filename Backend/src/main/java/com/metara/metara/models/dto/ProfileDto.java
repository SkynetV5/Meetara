package com.metara.metara.models.dto;

import com.metara.metara.models.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data requires to create profile")
public class ProfileDto {

    @Schema(description = "First Name", example = "Michał")
    private String firstName;
    @Schema(description = "Last Name", example = "Kalinowski")
    private String lastName;
    @Schema(description = "Phone number", example = "542981451")
    private String phoneNumber;

    @Schema(description = "User", example = "{\"id\":1,\"username\":\"michal.kalinowski\",\"email\":\"michal@example.com\",\"enabled\":true,\"createdAt\":\"2025-11-20T12:00:00\",\"roles\":[]}")
    private User user;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
