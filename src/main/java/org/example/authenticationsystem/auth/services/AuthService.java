package org.example.authenticationsystem.auth.services;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.example.authenticationsystem.auth.dtos.AuthResponseDTO;
import org.example.authenticationsystem.auth.dtos.LoginDTO;
import org.example.authenticationsystem.auth.dtos.RegisterDTO;

public interface AuthService {

    AuthResponseDTO signUp(RegisterDTO registerRequest) throws MessagingException;

    AuthResponseDTO signIn(LoginDTO loginRequest, HttpServletResponse response);

    AuthResponseDTO verifyCode(String email, String verifyCode);

}