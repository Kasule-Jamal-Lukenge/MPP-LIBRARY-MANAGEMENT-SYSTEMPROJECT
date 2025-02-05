package org.miu.mppproject.librarysystem.libraryapp.presentation.features.auth;

import org.miu.mppproject.librarysystem.libraryapp.presentation.shared.MviIntent;
public sealed interface AuthIntent extends MviIntent permits AuthIntent.LoginIntent, AuthIntent.LogoutIntent {

    record LoginIntent(String username, String password) implements AuthIntent {}

    record LogoutIntent() implements AuthIntent {}

}

