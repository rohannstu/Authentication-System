package org.example.authenticationsystem.auth.services.Impl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.example.authenticationsystem.auth.dtos.*;
import org.example.authenticationsystem.auth.entities.User;
import org.example.authenticationsystem.auth.repos.UserRepo;
import org.example.authenticationsystem.auth.services.AuthService;
import org.example.authenticationsystem.auth.utils.EmailUtils;
import org.example.authenticationsystem.auth.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final EmailUtils emailUtils;

    public AuthServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, EmailUtils emailUtils) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.emailUtils = emailUtils;
    }

    @Override
    public AuthResponseDTO signUp(RegisterDTO registerRequest) throws MessagingException {

        Optional<User> optionalUser = userRepo.findByEmail(registerRequest.email());
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (user.getIsVerified().equals(Boolean.TRUE)) {
                return AuthResponseDTO.builder()
                        .message("Email already exists!")
                        .success(false)
                        .build();
            } else if (user.getVerifyCodeExpiry().after(new Date())) {
                return AuthResponseDTO.builder()
                        .message("Email already exists!")
                        .success(false)
                        .build();
            }

            int verificationCode = (int) ((Math.random()+1) * 100000);

            user.setName(registerRequest.name());
            user.setEmail(registerRequest.email());
            user.setPassword(passwordEncoder.encode(registerRequest.password()));
            user.setVerifyCode(String.valueOf(verificationCode));
            user.setVerifyCodeExpiry(new Date(System.currentTimeMillis()+36000000));
            user.setRole("USER");
            user.setIsVerified(false);

            User savedUser = userRepo.save(user);

            // send mail
            final String subject = "Verify your account";
            final String EMAIL_TEMPLATE = """
                    <html>
                         <body>
                            <h1>Welcome!!!</h1>
                            <p>You have successfully registered to our application.</p>
                            <p>Verification code is %d.</p>
                            <p>Please click on the below link to verify account:</p>
                            <a href="http://localhost:5173/verify">Verify Email</a>
                             <p>This link will expire in 30 minutes.</p>
                         </body>
                    </html>
                    """.formatted(verificationCode);

            emailUtils.sendMail(new MailBody(savedUser.getEmail(), subject, EMAIL_TEMPLATE));

            return AuthResponseDTO.builder()
                    .message("User registered successfully!")
                    .success(true)
                    .build();
        }
        int verificationCode = (int) ((Math.random()+1) * 100000);
        User newUser = User.builder()
                .name(registerRequest.name())
                .email(registerRequest.email())
                .role("USER")
                .isVerified(false)
                .password(passwordEncoder.encode(registerRequest.password()))
                .verifyCode(String.valueOf(verificationCode))
                .verifyCodeExpiry(new Date(System.currentTimeMillis()+360000))
                .build();

        User savedUser = userRepo.save(newUser);

        // send mail
        final String subject = "Verify your account";
        final String EMAIL_TEMPLATE = """
                    <html>
                         <body>
                            <h1>Welcome!!!</h1>
                            <p>You have successfully registered to our application.</p>
                            <p>Verification code is %d.</p>
                            <p>Please click on the below link to verify account:</p>
                            <a href="http://localhost:5173/verify">Verify Email</a>
                             <p>This link will expire in 30 minutes.</p>
                         </body>
                    </html>
                    """.formatted(verificationCode);

        emailUtils.sendMail(new MailBody(savedUser.getEmail(), subject, EMAIL_TEMPLATE));

        return AuthResponseDTO.builder()
                .message("User registered successfully!")
                .success(true)
                .build();
    }

    @Override
    public AuthResponseDTO signIn(LoginDTO loginRequest, HttpServletResponse response) {

        Optional<User> optionalUser = userRepo.findByEmail(loginRequest.email());
        User user;
        if (optionalUser.isEmpty()) {
            return AuthResponseDTO.builder()
                    .message("User Not Found!")
                    .success(false)
                    .build();
        }

        user = optionalUser.get();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(), loginRequest.password()
                )
        );

        if (user.getIsVerified() && authentication.isAuthenticated()) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("email", user.getEmail());
            claims.put("name", user.getName());

            String accessToken = jwtUtils.generateToken(claims, user, response, Token.ACCESS);
            String refreshToken = jwtUtils.generateToken(claims, user, response, Token.REFRESH);

            user.setRefreshToken(refreshToken);

            User savedUser = userRepo.save(user);

            return AuthResponseDTO.builder()
                    .name(savedUser.getName())
                    .email(savedUser.getEmail())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .isVerified(Boolean.TRUE)
                    .success(Boolean.TRUE)
                    .role(savedUser.getRole())
                    .message("Sign in successful")
                    .build();

        }

        return AuthResponseDTO.builder()
                .message("User Not Authenticated!")
                .success(false)
                .build();
    }

    @Override
    public AuthResponseDTO verifyCode(String email, String verifyCode) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        User user;
        if (optionalUser.isEmpty()) {
            return AuthResponseDTO.builder()
                    .message("User Not Found!")
                    .success(false)
                    .build();
        }

        user = optionalUser.get();
        if (user.getVerifyCode().equals(verifyCode)) {
            if (user.getVerifyCodeExpiry().after(new Date())) {
                user.setIsVerified(true);
                user.setVerifyCode(null);
                user.setVerifyCodeExpiry(null);
                userRepo.save(user);
                return AuthResponseDTO.builder()
                        .message("User verified successfully!")
                        .success(true)
                        .build();
            } else {
                return AuthResponseDTO.builder()
                        .message("Verify Code expired, please sign up again to generate new code")
                        .success(false)
                        .build();
            }
        } else {
            return AuthResponseDTO.builder()
                    .message("Verify Code is invalid!")
                    .success(false)
                    .build();
        }

    }
}