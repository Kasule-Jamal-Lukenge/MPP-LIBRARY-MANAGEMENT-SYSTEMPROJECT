package org.miu.mppproject.librarysystem.libraryapp.backend.domain.repositories;

import org.miu.mppproject.librarysystem.libraryapp.backend.domain.dto.request.LoginRequest;
import org.miu.mppproject.librarysystem.libraryapp.backend.domain.dto.response.LoginResponse;

public interface IAuthRepository {


    public LoginResponse login(LoginRequest request);

    public void logout();
}
