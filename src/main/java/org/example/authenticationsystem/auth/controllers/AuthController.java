package org.example.authenticationsystem.auth.controllers;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.authenticationsystem.auth.dtos.AuthResponseDTO;
import org.example.authenticationsystem.auth.dtos.LoginDTO;
import org.example.authenticationsystem.auth.dtos.RegisterDTO;
import org.example.authenticationsystem.auth.dtos.verifyCodeDTO;
import org.example.authenticationsystem.auth.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterDTO registerRequest) throws MessagingException {
        AuthResponseDTO authResponse = authService.signUp(registerRequest);
        log.info("autoresponder = {}", authResponse);
        if (authResponse == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authService.signIn(loginRequest, response));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<AuthResponseDTO> verifyCode(@Valid @RequestBody verifyCodeDTO verifyCodeDto) {
        String email = verifyCodeDto.email();
        String verifyCode = verifyCodeDto.verifyCode();
        return ResponseEntity.ok(authService.verifyCode(email, verifyCode));
    }
}