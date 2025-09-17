package org.example.authenticationsystem.auth.utils;

import org.example.authenticationsystem.auth.dtos.Token;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final long EXPIRATION_TIME = System.currentTimeMillis() + 25 * 60 * 1000;

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            HttpServletResponse response,
            Token tokenType
    ) {
        extraClaims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
        String token =  Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(EXPIRATION_TIME))
                .signWith(getSignInKey())
                .compact();

        Cookie cookie = new Cookie(tokenType.name(), token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // local || PROD -> make it as 'true' -> valid https
        cookie.setPath("/");
        cookie.setMaxAge((int) EXPIRATION_TIME);
        //cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);

        return token;
    }

    public String getTokenFromCookie(HttpServletRequest request, Token tokenType){
        Cookie cookie = WebUtils.getCookie(request, tokenType.name());
        if(cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public void removeTokenFromCookie(HttpServletResponse response, Token tokenType){
        Cookie cookie = new Cookie(tokenType.name(), null);
        cookie.setPath("/");
        cookie.setMaxAge(0);    // Immediately expire the cookie
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

    private SecretKey getSignInKey() {
        // decode SECRET_KEY
        String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token, UserDetails userDetails) throws JwtException {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}