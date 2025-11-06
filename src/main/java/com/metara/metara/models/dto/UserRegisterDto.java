package com.metara.metara.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;



@Schema(description = "Dane potrzebne do rejestracji użytkownika")
public class UserRegisterDto {

    @Schema(description = "Adres e-mail użytkownika", example = "string@example.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Nazwa użytkownika", example = "string")
    @NotBlank
    private String username;

    @Schema(description = "Hasło użytkownika", example = "string")
    @NotBlank
    private String password;

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


}
