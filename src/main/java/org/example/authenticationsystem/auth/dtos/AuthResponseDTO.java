package org.example.authenticationsystem.auth.dtos;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuthResponseDTO {
    private String name;
    private String email;
    private String accessToken;
    private String refreshToken;
    private Boolean isVerified;
    private String role;
    private Boolean success;
    private String message;
}