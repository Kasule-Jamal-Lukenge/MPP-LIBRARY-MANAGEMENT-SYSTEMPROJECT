package org.miu.mppproject.librarysystem.libraryapp.core.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;

public class Token {
    private final String username;
    private final Role role;
    private final Instant expirationTime;
    private final String signature;
    private static final String SECRET_KEY = "your-very-secret-key"; // Change to a secure key

    public Token(String username, Role role, Duration duration) {
        this.username = username;
        this.role = role;
        this.expirationTime = Instant.now().plus(duration);
        this.signature = generateSignature();
    }

    private String generateSignature() {
        String data = username + role + expirationTime.toString();
        return hmacSHA256(data, SECRET_KEY);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expirationTime);
    }

    public boolean isValid() {
        return this.signature.equals(generateSignature());
    }

    private static String hmacSHA256(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC", e);
        }
    }

    @Override
    public String toString() {
        return username + ":" + role + ":" + expirationTime.getEpochSecond() + ":" + signature;
    }
}
