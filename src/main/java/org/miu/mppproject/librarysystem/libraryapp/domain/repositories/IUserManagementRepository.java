package org.miu.mppproject.librarysystem.libraryapp.domain.repositories;

import org.miu.mppproject.librarysystem.libraryapp.data.entities.Permission;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.Role;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserManagementRepository {

    User findUserByUsername(String username) throws SQLException;

    Long deleteRole(String roleId) throws SQLException;

    void assignRoleToUser(String userId, String roleId) throws SQLException;

    void addUser(User user) throws SQLException;

    List<Role> getAllRoles() throws SQLException;

    void addRole(Role role) throws SQLException;

    void addPermissions(List<Permission> permissions) throws SQLException;

//    void addPermissionsToRole(String roleId, List<String> permissionIds);

    Long deleteUser(String userId) throws SQLException;


}
