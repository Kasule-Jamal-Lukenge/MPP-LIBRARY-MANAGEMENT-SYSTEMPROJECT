package org.miu.mppproject.librarysystem.libraryapp.backend.domain.dto;

public class AppError {


    private final String message;

    private final int code;

    public AppError(String message, int code) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
