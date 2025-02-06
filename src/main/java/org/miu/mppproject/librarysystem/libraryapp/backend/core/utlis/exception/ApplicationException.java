package org.miu.mppproject.librarysystem.libraryapp.backend.core.utlis.exception;

public sealed class ApplicationException extends RuntimeException permits AuthenticationException, BookException, CheckoutException, MemberException, UserException {


    public ApplicationException(String message) {
        super(message);
    }
}
