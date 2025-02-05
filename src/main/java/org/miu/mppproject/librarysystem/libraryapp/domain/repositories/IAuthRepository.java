package org.miu.mppproject.librarysystem.libraryapp.domain.repositories;

import org.miu.mppproject.librarysystem.libraryapp.domain.dto.request.LoginRequest;
import org.miu.mppproject.librarysystem.libraryapp.domain.dto.response.LoginResponse;

public interface IAuthRepository {


    public LoginResponse login(LoginRequest request);

    public void logout();
}
