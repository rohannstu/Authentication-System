package org.example.authenticationsystem.auth.config;

import java.io.IOException;

import org.example.authenticationsystem.auth.dtos.Token;
import org.example.authenticationsystem.auth.utils.JwtUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtils jwtUtils, UserDetailsService userDetails) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetails;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        String header = request.getHeader("Authorization");
        String token;
        if (header == null || !header.startsWith("Bearer ")) {
//            Cookie[] cookies = request.getCookies();
            //log.info("Cookies received in JWTFilter: {}", Arrays.toString(cookies));
            token = jwtUtils.getTokenFromCookie(request, Token.ACCESS);
        } else {
            token = header.substring(7);
        }

        //log.info("Token received in JWTFilter: {}", token);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        final String accessToken = token;
        String userEmail = null;
        try {
            userEmail = jwtUtils.extractUsername(accessToken);
        }catch (Exception e) {
            log.info("Exception occurred while extracting username from token in JWTFilter: {}", e.getMessage());
            filterChain.doFilter(request, response);
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtUtils.validateToken(accessToken, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                //System.out.println("Filter completed!");
            }
        }
        filterChain.doFilter(request, response);
    }
}