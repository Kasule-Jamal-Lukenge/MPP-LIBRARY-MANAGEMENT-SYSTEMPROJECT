package org.miu.mppproject.librarysystem.libraryapp.domain.service.impl;

import io.reactivex.Single;
import org.miu.mppproject.librarysystem.libraryapp.domain.dto.request.LoginRequest;
import org.miu.mppproject.librarysystem.libraryapp.domain.dto.response.LoginResponse;
import org.miu.mppproject.librarysystem.libraryapp.domain.repositories.IAuthRepository;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.contract.IAuthService;

import javax.inject.Inject;

public class AuthServiceImpl implements IAuthService {


    private final IAuthRepository authRepository;


    @Inject
    public AuthServiceImpl(IAuthRepository authRepository) {
        this.authRepository = authRepository;

    }


    @Override
    public Single<LoginResponse> login(LoginRequest request) {

        return Single.fromCallable(() -> authRepository.login(request));
    }

    @Override
    public void logout() {
        authRepository.logout();
    }
}
