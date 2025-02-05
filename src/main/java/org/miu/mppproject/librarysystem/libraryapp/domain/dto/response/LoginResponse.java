package org.miu.mppproject.librarysystem.libraryapp.domain.dto.response;

import org.miu.mppproject.librarysystem.libraryapp.domain.dto.AppError;
import org.miu.mppproject.librarysystem.libraryapp.domain.dto.Data;

public class LoginResponse extends BaseResponse<Data> {


    public LoginResponse(Data data, boolean isSuccess, AppError error) {
        super(data, isSuccess, error);
    }
}
