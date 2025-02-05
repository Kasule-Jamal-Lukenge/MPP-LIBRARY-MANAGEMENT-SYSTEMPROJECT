package org.miu.mppproject.librarysystem.libraryapp.core.security;

import org.miu.mppproject.librarysystem.libraryapp.core.utlis.exception.AuthenticationException;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import java.sql.SQLException;
import java.time.Duration;

public class AuthenticationServer {

    @Inject
    private CredentialDao credentialDao;

    private Token token;
    private UserInfo userInfo;

    public String authenticate(String username, String password) {
        try {
            userInfo = credentialDao.fetchUser(username);
            if (userInfo == null) throw new AuthenticationException("Invalid username or password");

            if (isValidCredentials(password, userInfo)) {
                token = new Token(username, getRoleForUser(), Duration.ofMinutes(30));
                return token.toString();
            }
        } catch (SQLException e) {
            throw new AuthenticationException("Database error occurred");
        }

        throw new AuthenticationException("Invalid credentials");
    }

    public boolean validateToken(String tokenToValidate) {
        if (token == null || tokenToValidate == null || !tokenToValidate.equals(token.toString())) {
            return false;
        }
        return token.isValid() && !token.isExpired();
    }

    private boolean isValidCredentials(String password, UserInfo userInfo) {
        return BCrypt.checkpw(password, userInfo.getPassword());
    }

    private Role getRoleForUser() {
        return switch (userInfo.getRole().toLowerCase()) {
            case "admin" -> Role.ADMIN;
            case "librarian" -> Role.LIBRARIAN;
            case "both" -> Role.BOTH;
            default -> throw new IllegalStateException("Unexpected role: " + userInfo.getRole());
        };
    }

    public Token getToken() {
        return token;
    }

}
