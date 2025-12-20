package com.wello.wellobackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String createVerificationToken(String email, String password, String otp) {
        // Hash password before putting in token
        String hashedPassword = passwordEncoder.encode(password);

        // Create token with 5 minutes expiration
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 5 * 60 * 1000); // 5 minutes

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(email)
                .claim("hashedPassword", hashedPassword)
                .claim("otp", otp)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Map<String, String> verifyToken(String token, String userOtp) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String hashedPassword = claims.get("hashedPassword", String.class);
            String tokenOtp = claims.get("otp", String.class);

            // Verify OTP
            if (!tokenOtp.equals(userOtp)) {
                throw new RuntimeException("Invalid OTP");
            }

            // Return email and hashed password
            Map<String, String> result = new HashMap<>();
            result.put("email", email);
            result.put("hashedPassword", hashedPassword);
            return result;

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new RuntimeException("OTP has expired. Please request a new one.");
        } catch (Exception e) {
            throw new RuntimeException("Invalid verification token");
        }
    }
}
