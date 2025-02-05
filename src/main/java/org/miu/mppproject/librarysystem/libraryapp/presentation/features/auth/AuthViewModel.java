package org.miu.mppproject.librarysystem.libraryapp.presentation.features.auth;

import io.reactivex.Scheduler;
import javafx.application.Platform;
import org.miu.mppproject.librarysystem.libraryapp.domain.dto.request.LoginRequest;
import org.miu.mppproject.librarysystem.libraryapp.presentation.shared.MviViewModel;
import org.miu.mppproject.librarysystem.libraryapp.domain.service.contract.IAuthService;

import javax.inject.Inject;


public class AuthViewModel extends MviViewModel<AuthState, AuthIntent> {

    private final IAuthService service;
    private final Scheduler ioScheduler;

    @Inject
    public AuthViewModel(IAuthService service, Scheduler ioScheduler) {
        this.ioScheduler = ioScheduler;
        this.service = service;
        setState(AuthState.IDLE);
    }


    @Override
    public void processIntent(AuthIntent intent) {
        if (intent instanceof AuthIntent.LoginIntent) {
            handleLogin((AuthIntent.LoginIntent) intent);
        }
    }

    private void handleLogin(AuthIntent.LoginIntent intent) {
        setState(AuthState.LOADING);
        service.login(new LoginRequest(intent.username(), intent.password()))
                .subscribeOn(ioScheduler)  // Runs on background thread
                .subscribe(result -> Platform.runLater(() -> { // Ensures UI update happens on JavaFX Thread
                    if (result.isSuccess()) {
                        setState(AuthState.SUCCESS);
                    } else {
                        setState(AuthState.ERROR);
                    }
                }));
    }



}
