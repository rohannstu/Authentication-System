package org.example.authenticationsystem.auth.services.Impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.example.authenticationsystem.auth.dtos.AuthResponseDTO;
import org.example.authenticationsystem.auth.dtos.LoginDTO;
import org.example.authenticationsystem.auth.dtos.RegisterDTO;
import org.example.authenticationsystem.auth.services.AuthService;

public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponseDTO signUp(RegisterDTO registerRequest) throws MessagingException {
        return null;
    }

    @Override
    public AuthResponseDTO signIn(LoginDTO loginRequest, HttpServletResponse response) {
        return null;
    }

    @Override
    public AuthResponseDTO verifyCode(String email, String verifyCode) {
        return null;
    }
}
