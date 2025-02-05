package org.miu.mppproject.librarysystem.libraryapp.presentation.features.auth;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.miu.mppproject.librarysystem.libraryapp.core.navigation.ScreenManager;

public class DashBoardScreen extends VBox {
    private final ScreenManager screenManager;

    public DashBoardScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;

        Label titleLabel = new Label("Dashboard");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> screenManager.showScreen(new LoginScreen(screenManager)));

        setSpacing(10);
        setAlignment(Pos.CENTER);
        getChildren().addAll(titleLabel, logoutButton);
    }
}

