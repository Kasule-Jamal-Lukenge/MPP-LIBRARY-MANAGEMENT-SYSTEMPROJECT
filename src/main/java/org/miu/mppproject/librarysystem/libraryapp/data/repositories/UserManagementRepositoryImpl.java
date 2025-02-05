package org.miu.mppproject.librarysystem.libraryapp.data.repositories;

import org.miu.mppproject.librarysystem.libraryapp.core.utlis.exception.UserException;
import org.miu.mppproject.librarysystem.libraryapp.data.dao.UserDao;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.Permission;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.Role;
import org.miu.mppproject.librarysystem.libraryapp.data.entities.User;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.IUserManagementRepository;

import java.sql.SQLException;
import java.util.List;

public class UserManagementRepositoryImpl implements IUserManagementRepository {


    private final UserDao userDao;


    public UserManagementRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User findUserByUsername(String username) throws SQLException {
        return userDao.findUserByUsername(username);
    }

    @Override
    public Long deleteRole(String roleId) {
        try {
            return userDao.deleteRole(roleId);
        } catch (SQLException exception) {
            throw new UserException("Unable to delete role");
        }

    }

    @Override
    public void assignRoleToUser(String userId, String roleId) {
        try {
            userDao.assignRoleToUser(userId,roleId);
        } catch (SQLException e) {
            throw new UserException("Something went wrong");
        }
    }


    @Override
    public void addUser(User user) {
        try {
            userDao.addUser(user);
        } catch (SQLException e) {
            throw new UserException("Error adding user");
        }

    }

    @Override
    public List<Role> getAllRoles() throws SQLException {
        return userDao.getAllRoles();
    }

    @Override
    public void addRole(Role role) throws SQLException {
        userDao.addRole(role);
    }


    @Override
    public void addPermissions(List<Permission> permissions)  throws SQLException{
        userDao.addPermissions(permissions);
    }



    @Override
    public Long deleteUser(String userId) {
        try {
            return userDao.deleteUser(userId);
        } catch (SQLException exception) {
            throw new UserException("Unable to delete user");
        }
    }





}
