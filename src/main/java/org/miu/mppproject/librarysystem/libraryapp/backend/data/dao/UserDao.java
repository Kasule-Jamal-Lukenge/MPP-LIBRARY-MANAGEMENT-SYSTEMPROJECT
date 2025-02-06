package org.miu.mppproject.librarysystem.libraryapp.backend.data.dao;

import org.miu.mppproject.librarysystem.libraryapp.backend.core.dataaccessfacade.DataSource;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

public class UserDao {
    private DataSource dataSource;
    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

    @Inject
    public UserDao(@Named("Lib") DataSource dataSource) {
        this.dataSource = dataSource;
        this.connection = dataSource.getConnection();
    }

    public void addUser(User user) throws SQLException {
        String insertUserSQL = "INSERT INTO User (id, username, contact_info_id, role_id) VALUES (?, ?, ?, ?)";
        String insertContactInfoSQL = "INSERT INTO ContactInfo (id, firstname, lastname, phone_number, street, city, state, zip_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        connection.setAutoCommit(false);

        try (PreparedStatement userStmt = connection.prepareStatement(insertUserSQL);
             PreparedStatement contactStmt = connection.prepareStatement(insertContactInfoSQL)) {

            // Insert Contact Info
            ContactInfo contactInfo = user.getContactInfo();
            String contactInfoId = UUID.randomUUID().toString();  // Generate a new ID for ContactInfo
            contactStmt.setString(1, contactInfoId);
            contactStmt.setString(2, contactInfo.getFirstName());
            contactStmt.setString(3, contactInfo.getLastName());
            contactStmt.setString(4, contactInfo.getPhoneNumber());
            contactStmt.setString(5, contactInfo.getAddress().street());
            contactStmt.setString(6, contactInfo.getAddress().city());
            contactStmt.setString(7, contactInfo.getAddress().state());
            contactStmt.setString(8, contactInfo.getAddress().zipCode());
            contactStmt.executeUpdate();

            // Insert User
            userStmt.setString(1, user.getId());
            userStmt.setString(2, user.getUsername());
            userStmt.setString(3, contactInfoId);
            userStmt.setString(4, user.getAssignedRole().getId());
            userStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Error adding user: " + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public User findUserByUsername(String username) throws SQLException {
        String sql = """
                    SELECT u.id AS user_id, u.username, 
                           ci.id AS contact_info_id, ci.firstname, ci.lastname, ci.phone_number, ci.street, ci.city, ci.state, ci.zip_code, 
                           r.id AS role_id, r.name AS role_name
                    FROM User u
                    LEFT JOIN ContactInfo ci ON u.contact_info_id = ci.id
                    LEFT JOIN Role r ON u.role_id = r.id
                    WHERE u.username = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Create Contact Info
                ContactInfo contactInfo = new ContactInfo(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("phone_number"),
                        new Address(
                                rs.getString("street"),
                                rs.getString("city"),
                                rs.getString("state"),
                                rs.getString("zip_code")
                        )
                );

                // Create Role and Permissions
                Role role = new Role(
                        rs.getString("role_name"),
                        fetchPermissions(rs.getString("role_id"))
                );

                // Create User with Contact Info and Role
                return new User(
                        rs.getString("username"),
                        role,
                        contactInfo
                );
            }
            return null; // User not found
        }
    }

    private List<Permission> fetchPermissions(String roleId) throws SQLException {
        String sql = """
                    SELECT p.id, p.action_name
                    FROM Permission p
                    JOIN Role_Permissions rp ON p.id = rp.permission_id
                    WHERE rp.role_id = ?
                """;

        List<Permission> permissions = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roleId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                permissions.add(new Permission(
                        rs.getString("action_name")
                ));
            }
        }
        return permissions;
    }

    public void addRole(Role role) throws SQLException {
        String insertRoleSQL = "INSERT INTO Role (id, name) VALUES (?, ?)";
        String insertRolePermissionsSQL = "INSERT INTO Role_Permissions (role_id, permission_id) VALUES (?, ?)";

        connection.setAutoCommit(false);

        try (PreparedStatement roleStmt = connection.prepareStatement(insertRoleSQL);
             PreparedStatement rolePermissionStmt = connection.prepareStatement(insertRolePermissionsSQL)) {

            // Insert Role
            roleStmt.setString(1, role.getId());
            roleStmt.setString(2, role.getName());
            roleStmt.executeUpdate();

            // Insert Permissions for Role
            for (Permission permission : role.getRolePermissions()) {
                rolePermissionStmt.setString(1, role.getId());
                rolePermissionStmt.setString(2, permission.getId());
                rolePermissionStmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Error adding role: " + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public Long deleteUser(String userId) throws SQLException {
        String deleteContactInfoSQL = "DELETE FROM ContactInfo WHERE id = ?";
        String deleteUserSQL = "DELETE FROM User WHERE id = ?";

        connection.setAutoCommit(false);

        try (PreparedStatement deleteContactInfoStmt = connection.prepareStatement(deleteContactInfoSQL);
             PreparedStatement deleteUserStmt = connection.prepareStatement(deleteUserSQL)) {

            // Remove Contact Info
            deleteContactInfoStmt.setString(1, userId);
            deleteContactInfoStmt.executeUpdate();

            // Delete User
            deleteUserStmt.setString(1, userId);
            int affectedRows = deleteUserStmt.executeUpdate();

            connection.commit();
            return (long) affectedRows;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Error deleting user: " + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void addPermissions(List<Permission> permissions) throws SQLException {
        String insertPermissionSQL = "INSERT INTO Permission (id, action_name) VALUES (?, ?)";
        connection.setAutoCommit(false);

        try (PreparedStatement permissionStmt = connection.prepareStatement(insertPermissionSQL)) {
            for (Permission permission : permissions) {
                permissionStmt.setString(1, permission.getId());
                permissionStmt.setString(2, permission.getActionName());
                permissionStmt.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Error adding permissions: " + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public List<Role> getAllRoles() throws SQLException {
        String sql = "SELECT * FROM Role";
        List<Role> roles = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                roles.add(new Role(
                        rs.getString("name"),
                        fetchPermissions(rs.getString("id"))
                ));
            }
        }
        return roles;
    }

    public void assignRoleToUser(String userId, String roleId) throws SQLException {
        String sql = "UPDATE User SET role_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roleId);
            stmt.setString(2, userId);
            stmt.executeUpdate();
        }
    }

    public Long deleteRole(String roleId) throws SQLException {
        String deleteRolePermissionsSQL = "DELETE FROM Role_Permissions WHERE role_id = ?";
        String deleteRoleSQL = "DELETE FROM Role WHERE id = ?";

        connection.setAutoCommit(false);

        try (PreparedStatement deleteRolePermissionsStmt = connection.prepareStatement(deleteRolePermissionsSQL);
             PreparedStatement deleteRoleStmt = connection.prepareStatement(deleteRoleSQL)) {

            // Remove Role Permissions
            deleteRolePermissionsStmt.setString(1, roleId);
            deleteRolePermissionsStmt.executeUpdate();

            // Delete Role
            deleteRoleStmt.setString(1, roleId);
            int affectedRows = deleteRoleStmt.executeUpdate();

            connection.commit();
            return (long) affectedRows;
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Error deleting role: " + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
