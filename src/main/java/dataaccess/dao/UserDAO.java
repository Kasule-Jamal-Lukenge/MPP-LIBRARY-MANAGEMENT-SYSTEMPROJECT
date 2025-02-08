package dataaccess.dao;

import dataaccess.Auth;
import dataaccess.User;
import dataaccess.storage.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class UserDAO {
    public HashMap<String, User> getAllUsers() {
        HashMap<String, User> users = new HashMap<>();
        String query = "SELECT * FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User(rs.getString("id"), rs.getString("password"), Auth.valueOf(rs.getString("auth_level")));
                users.put(user.getId(), user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    // Load Users into Database
    public void loadUsers(List<User> userList) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO users (id, password, auth_level) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (User user : userList) {
                    stmt.setString(1, user.getId());
                    stmt.setString(2, user.getPassword());
                    stmt.setString(3, user.getAuthorization().name());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

