package org.miu.mppproject.librarysystem.libraryapp.domain.dto.response;

import org.miu.mppproject.librarysystem.libraryapp.domain.dto.AppError;
import org.miu.mppproject.librarysystem.libraryapp.domain.dto.Data;

public class BaseResponse<T> {
    private final T data;

    private final boolean isSuccess;

    private final AppError appError;


    public BaseResponse(T data, boolean isSuccess, AppError error) {
        this.appError = error;
        this.isSuccess = isSuccess;
        this.data = data;
    }


    public boolean isSuccess() {
        return isSuccess;
    }

    public T getData() {
        return data;
    }

    public AppError getError() {
        return appError;
    }
}
