package com.metara.metara.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;



@Schema(description = "Data requires to register user ")
public class UserRegisterDto {

    @Schema(description = "Email Adress", example = "string@example.com")
    @Email
    @NotBlank(message = "{validation.email.required}")
    private String email;

    @Schema(description = "Username", example = "string")
    @NotBlank(message = "{validation.username.required}")
    private String username;

    @Schema(description = "Password", example = "string")
    @NotBlank(message = "{validation.password.required}")
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
