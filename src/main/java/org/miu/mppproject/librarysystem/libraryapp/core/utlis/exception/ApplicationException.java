package org.miu.mppproject.librarysystem.libraryapp.core.utlis.exception;

public sealed class ApplicationException extends RuntimeException permits AuthenticationException, BookException, CheckoutException, MemberException, UserException {


    public ApplicationException(String message) {
        super(message);
    }
}
