package org.miu.mppproject.librarysystem.libraryapp.backend.domain.dto.response;

import org.miu.mppproject.librarysystem.libraryapp.backend.domain.dto.AppError;

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
