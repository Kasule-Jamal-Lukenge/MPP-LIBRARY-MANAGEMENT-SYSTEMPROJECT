package org.miu.mppproject.librarysystem.libraryapp.frontend.features;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.miu.mppproject.librarysystem.libraryapp.frontend.navigation.NavigationController;


public class LoginScreen extends Screen {

    private final NavigationController navigationController;

    public LoginScreen(NavigationController navigationController) {
        this.navigationController = navigationController;
        createView();
    }

    private void createView() {
        // Main layout with dark theme and a gradient background
        BorderPane mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: linear-gradient(to right, #1f1c2c, #928dab);");
        mainLayout.setPadding(new Insets(40));

        // Title label at the top
        Label titleLabel = new Label("Login to the Library System");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        mainLayout.setTop(titleLabel);

        // Center pane to host the login form
        StackPane centerPane = new StackPane();
        centerPane.setPrefHeight(400);
        centerPane.setAlignment(Pos.CENTER);

        // Create a VBox for error message and form fields
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(20));
        formContainer.setStyle("-fx-background-color: rgba(0,0,0,0.7); " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15;");
        formContainer.setMaxWidth(600);

        // Error message label (initially invisible)
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px;");
        errorLabel.setVisible(false);

        // Create an HBox for the input fields and the login button
        HBox loginBox = new HBox(15);
        loginBox.setAlignment(Pos.CENTER);

        // Username Field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-padding: 10px; -fx-background-radius: 5px; -fx-font-size: 16px;");
        usernameField.setPrefWidth(200);

        // Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-padding: 10px; -fx-background-radius: 5px; -fx-font-size: 16px;");
        passwordField.setPrefWidth(200);

        // Login Button with curved edges and initial styling
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #e67e22; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #ffffff; " +
                "-fx-background-radius: 30;");
        loginButton.setPrefWidth(120);

        // Animate button on hover: scale and change background color
        final ScaleTransition scaleUp = new ScaleTransition(Duration.millis(200), loginButton);
        scaleUp.setToX(1.1);
        scaleUp.setToY(1.1);
        final ScaleTransition scaleDown = new ScaleTransition(Duration.millis(200), loginButton);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);
        loginButton.setOnMouseEntered(e -> {
            scaleUp.playFromStart();
            loginButton.setStyle("-fx-background-color: #d35400; " +
                    "-fx-font-size: 18px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-text-fill: #ffffff; " +
                    "-fx-background-radius: 30;");
        });
        loginButton.setOnMouseExited(e -> {
            scaleDown.playFromStart();
            loginButton.setStyle("-fx-background-color: #e67e22; " +
                    "-fx-font-size: 18px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-text-fill: #ffffff; " +
                    "-fx-background-radius: 30;");
        });

        // Listen to text changes to update login button visibility
        usernameField.textProperty().addListener((obs, oldVal, newVal) -> updateLoginButtonVisibility(usernameField, passwordField, loginButton));
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> updateLoginButtonVisibility(usernameField, passwordField, loginButton));

        // Handle login action on button click
        loginButton.setOnAction(e -> {
            // Clear previous error message
            errorLabel.setVisible(false);
            // Show progress overlay
            showProgressOverlay(formContainer);

            // Simulate login processing using a sequential transition (you could replace this with RxJava API call)
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(ev -> {
                hideProgressOverlay(formContainer);
                // Check credentials (this is just a demo, replace with your actual authentication logic)
                if (usernameField.getText().equals("admin") && passwordField.getText().equals("admin")) {
                    // Navigate to dashboard screen with proper role (for demo, assume ADMIN)
                    navigationController.publishEvent(new ShowScreenEvent(new DashboardScreen(navigationController, "BOTH","")));
                } else {
                    errorLabel.setText("Invalid username or password.");
                    errorLabel.setVisible(true);
                }
            });
            delay.play();
        });

        // Add the input fields and button to the loginBox (horizontal layout)
        loginBox.getChildren().addAll(usernameField, passwordField, loginButton);
        // Assemble the form container
        formContainer.getChildren().addAll(errorLabel, loginBox);
        centerPane.getChildren().add(formContainer);
        mainLayout.setCenter(centerPane);
        this.setView(mainLayout);
    }

    /**
     * Updates the login button opacity based on whether both text fields have text.
     */
    private void updateLoginButtonVisibility(TextField username, TextField password, Button button) {
        boolean ready = !username.getText().trim().isEmpty() && !password.getText().trim().isEmpty();
        // Simple opacity change; for smoother effect, consider a FadeTransition.
        button.setOpacity(ready ? 1.0 : 0.5);
    }

    /**
     * Shows a dimmed overlay with a circular progress indicator over the given container.
     */
    private void showProgressOverlay(VBox container) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        overlay.setPrefSize(container.getWidth(), container.getHeight());
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(80, 80);
        overlay.getChildren().add(progressIndicator);
        container.getChildren().add(overlay);

        // Animate the overlay fading in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), overlay);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Store the overlay in the container's properties for later removal
        container.setUserData(overlay);
    }

    /**
     * Hides and removes the progress overlay from the given container.
     */
    private void hideProgressOverlay(VBox container) {
        Object data = container.getUserData();
        if (data instanceof StackPane) {
            StackPane overlay = (StackPane) data;
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), overlay);
            fadeOut.setFromValue(overlay.getOpacity());
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> container.getChildren().remove(overlay));
            fadeOut.play();
        }
    }
}
