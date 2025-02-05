package org.miu.mppproject.librarysystem.libraryapp.data.dao;

import org.miu.mppproject.librarysystem.libraryapp.core.security.AuthenticationServer;

import javax.inject.Inject;
import java.util.logging.Logger;

public class AuthDao {

    private final AuthenticationServer authenticationServer;

    private static final Logger LOGGER = Logger.getLogger(AuthDao.class.getName());

    public AuthDao(AuthenticationServer authenticationServer) {
        this.authenticationServer = authenticationServer;
    }


    public String authenticateUser(String username, String password) {

        return authenticationServer.authenticate(username, password);
    }


}
