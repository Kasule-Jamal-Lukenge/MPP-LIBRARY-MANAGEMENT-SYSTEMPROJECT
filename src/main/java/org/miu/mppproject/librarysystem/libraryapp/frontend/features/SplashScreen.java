package org.miu.mppproject.librarysystem.libraryapp.frontend.features;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.miu.mppproject.librarysystem.libraryapp.frontend.navigation.NavigationController;

public class SplashScreen extends Screen {

    private NavigationController navigationController;

    public SplashScreen(NavigationController navigationController) {
        this.navigationController = navigationController;

        StackPane splashPane = new StackPane();
        splashPane.setStyle("-fx-background-color: #2D3E50; -fx-padding: 20;"); // Dark theme background

        // Create a progress bar
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setStyle("-fx-accent: #4CAF50; -fx-padding: 20;");
        splashPane.getChildren().add(progressBar);

        // Display a title text with a fancy font
        Text loadingText = new Text("Welcome to the Library System");
        loadingText.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: white;");
        splashPane.getChildren().add(loadingText);

        // Set alignment and positioning
        StackPane.setAlignment(loadingText, javafx.geometry.Pos.TOP_CENTER);
        StackPane.setAlignment(progressBar, javafx.geometry.Pos.BOTTOM_CENTER);

        // Simulate loading process (progress bar animation)
        Timeline progressTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), e -> {
                    double progress = progressBar.getProgress() + 0.05;
                    progressBar.setProgress(progress);
                    if (progress >= 1) {
                        // Once loading is complete, navigate to LoginScreen
                        navigationController.publishEvent(new ShowScreenEvent(new LoginScreen(navigationController)));
                    }
                })
        );
        progressTimeline.setCycleCount(20);  // Runs for 2 seconds with 0.1 interval
        progressTimeline.play();

        // Set the view for this screen
        this.setView(splashPane);
    }
}
