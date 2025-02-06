package org.miu.mppproject.librarysystem.libraryapp.backend.data.repositories;

import org.miu.mppproject.librarysystem.libraryapp.backend.core.utlis.exception.AuthenticationException;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.dao.AuthDao;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.dao.UserDao;
import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.User;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.dto.AppError;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.dto.Data;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.dto.request.LoginRequest;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.dto.response.LoginResponse;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories.IAuthRepository;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthRepositoryImpl implements IAuthRepository {
    private static final Logger logger = Logger.getLogger(AuthRepositoryImpl.class.getName());
    private final AuthDao authDao;
    private final UserDao userDao;

    @Inject
    public AuthRepositoryImpl(AuthDao authDao, UserDao userDao) {
        this.authDao = authDao;
        this.userDao = userDao;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            // Authenticate user via Resource Server
            String token = authDao.authenticateUser(request.getUserId(), request.getPassword());

            if (token == null) {
                logger.warning("Authentication failed for user: " + request.getUserId());
                return new LoginResponse(null, false, new AppError("Invalid Credentials", 400));
            }

            // Fetch user details
            Optional<User> optionalUser = Optional.ofNullable(userDao.findUserByUsername(request.getUserId()));

            if (optionalUser.isEmpty()) {
                logger.warning("User details not found for authenticated user: " + request.getUserId());
                return new LoginResponse(null, false, new AppError("User data retrieval failed", 500));
            }

            User user = optionalUser.get();
            logger.info("User authenticated successfully: " + user.getUsername());

            return new LoginResponse(new Data(token, user), true, null);

        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Database error while fetching user details", exception);
            throw new AuthenticationException("Something went wrong while authenticating");
        }
    }

    @Override
    public void logout() {
        logger.info("User logged out");
    }
}
