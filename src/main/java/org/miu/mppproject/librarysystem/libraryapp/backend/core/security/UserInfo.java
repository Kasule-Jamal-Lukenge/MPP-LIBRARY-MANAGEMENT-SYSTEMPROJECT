package org.miu.mppproject.librarysystem.libraryapp.backend.core.security;

public class UserInfo {
    private final String username;
    private final String password;
    private final String role;

    public UserInfo(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
