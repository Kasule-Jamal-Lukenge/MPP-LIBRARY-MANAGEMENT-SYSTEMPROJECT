package org.miu.mppproject.librarysystem.libraryapp.presentation.features.auth;

import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.miu.mppproject.librarysystem.libraryapp.core.navigation.NavigationEvent;
import org.miu.mppproject.librarysystem.libraryapp.core.navigation.ScreenManager;

public class LoginScreen extends VBox {
    public LoginScreen(StackPane pane) {
        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("btn"); // Bootstrap-like class
        loginButton.setOnAction(e -> fireEvent(new NavigationEvent(NavigationEvent.Screen.DASHBOARD)));

        getChildren().add(loginButton);
    }
}

