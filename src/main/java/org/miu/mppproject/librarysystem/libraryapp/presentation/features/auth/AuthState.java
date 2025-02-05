package org.miu.mppproject.librarysystem.libraryapp.presentation.features.auth;

import org.miu.mppproject.librarysystem.libraryapp.presentation.shared.MviState;

public enum AuthState implements MviState {
    IDLE, LOADING, SUCCESS, ERROR
}