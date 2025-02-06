package org.miu.mppproject.librarysystem.libraryapp.backend.domain.dto;

import org.miu.mppproject.librarysystem.libraryapp.backend.data.entities.User;

public class Data {

    private final String token;

    private final User user;

    public Data(String token, User user) {
        this.token = token;
        this.user = user;
    }


}
