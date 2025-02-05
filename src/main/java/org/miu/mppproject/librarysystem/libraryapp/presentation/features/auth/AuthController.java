package org.miu.mppproject.librarysystem.libraryapp.presentation.features.auth;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.ProgressIndicator;

import javax.inject.Inject;

public class AuthController implements Initializable {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ProgressIndicator progressIndicator; // Add ProgressIndicator

    @Inject
    AuthViewModel viewModel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        progressIndicator.setVisible(false); // Initially hidden

        viewModel.getState().subscribe(state -> {
            Platform.runLater(() -> { // Ensure UI updates run on JavaFX thread
                progressIndicator.setVisible(state == AuthState.LOADING); // Show spinner
                if (state == AuthState.SUCCESS) {
                    // Navigate to dashboard
                }
            });
        });
    }

    @FXML
    private void onLogin() {
        viewModel.processIntent(new AuthIntent.LoginIntent(
                usernameField.getText(),
                passwordField.getText()
        ));
    }
}
