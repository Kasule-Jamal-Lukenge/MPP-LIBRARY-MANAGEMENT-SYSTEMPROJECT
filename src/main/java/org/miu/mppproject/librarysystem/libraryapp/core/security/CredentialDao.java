package org.miu.mppproject.librarysystem.libraryapp.core.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class CredentialDao {

    private final Connection connection;

    public CredentialDao(Connection connection) {
        this.connection = connection;
    }

    public UserInfo fetchUser(String username) throws SQLException {
        String sql = """
                    SELECT username, password, role FROM user_credential 
                    WHERE username = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UserInfo(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                );
            }
            return null;
        }
    }

    public boolean registerUser(String username, String password, String role) throws SQLException {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

        String sql = "INSERT INTO user_credential (id, username, password, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, java.util.UUID.randomUUID().toString());
            stmt.setString(2, username);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, role);
            return stmt.executeUpdate() > 0;
        }
    }
}
