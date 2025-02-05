package org.miu.mppproject.librarysystem.libraryapp.domain.service.contract;

import org.miu.mppproject.librarysystem.libraryapp.data.entities.Permission;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.Role;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface IUserManagementService {


    void addUser(User user);

    User findUserByUsername(String username);

    void addPermissions(List<Permission> permissions);

//    Long deletePermission(String permissionId);

    void addRole(Role role);

    List<Role> getAllRoles();

    Long deleteRole(String roleId) throws SQLException;

    void addRoleToUser(String roleId, String userId) throws SQLException;


//    void addPermissionsToRole(String roleId, List<String> permissionIds);

    Long deleteUser(String userId);


}
