package org.example.authenticationsystem.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record verifyCodeDTO(
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide valid email!")
        String email,
        @NotBlank(message = "Email is required")
        String verifyCode) {
}