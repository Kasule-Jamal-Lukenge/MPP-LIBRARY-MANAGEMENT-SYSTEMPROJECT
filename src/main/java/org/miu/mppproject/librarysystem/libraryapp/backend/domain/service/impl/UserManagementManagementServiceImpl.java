package org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.impl;

import org.miu.mppproject.librarysystem.libraryapp.backend.core.utlis.exception.UserException;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.Permission;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.Role;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.User;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories.IUserManagementRepository;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.service.contract.IUserManagementService;

import java.sql.SQLException;
import java.util.List;

public class UserManagementManagementServiceImpl implements IUserManagementService {

    private final IUserManagementRepository userRepository;

    public UserManagementManagementServiceImpl(IUserManagementRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        try {
            userRepository.addUser(user);
        } catch (SQLException exception) {
            throw new UserException("Error adding user");
        }
    }

    @Override
    public User findUserByUsername(String username) {
        try {
            return userRepository.findUserByUsername(username);
        } catch (SQLException e) {
            throw new UserException("Unable to fetch user");
        }
    }

    @Override
    public void addPermissions(List<Permission> permissions) {
        try {
            userRepository.addPermissions(permissions);
        } catch (SQLException e) {
            throw new UserException("Unable to add permissions");
        }
    }



    @Override
    public void addRole(Role role) {
        try {
            userRepository.addRole(role);
        } catch (SQLException e) {
            throw new UserException("Unable to add Role");
        }

    }

    @Override
    public List<Role> getAllRoles() {
        try {
            return userRepository.getAllRoles();
        } catch (SQLException exception) {
            throw new UserException("Error occurred fetching roles");
        }
    }

    @Override
    public Long deleteRole(String roleId) {
        try {
            return userRepository.deleteRole(roleId);
        } catch (SQLException e) {
            throw new UserException("Error occurred deleting role");
        }

    }

    @Override
    public void addRoleToUser(String roleId, String userId) {
        try {
            userRepository.assignRoleToUser(userId, roleId);
        } catch (SQLException exception) {
            throw new UserException("Error occurred adding role to a user");
        }

    }


    @Override
    public Long deleteUser(String userId) {
        try {
            return userRepository.deleteUser(userId);
        } catch (SQLException e) {
            throw new UserException("Error Deleting User");
        }
    }


}
