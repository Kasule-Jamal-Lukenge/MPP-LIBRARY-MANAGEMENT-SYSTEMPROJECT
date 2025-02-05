package org.miu.mppproject.librarysystem.libraryapp.domain.service.contract;

import io.reactivex.Single;
import org.miu.mppproject.librarysystem.libraryapp.domain.dto.request.LoginRequest;
import org.miu.mppproject.librarysystem.libraryapp.domain.dto.response.LoginResponse;

public interface IAuthService {


    Single<LoginResponse> login(LoginRequest request);

    void logout();


}
