package org.miu.mppproject.librarysystem.libraryapp.core.navigation;

import javafx.animation.FadeTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.Stack;

public class ScreenManager {
    private final StackPane root;
    private final Stack<Node> screenStack = new Stack<>();

    public ScreenManager(StackPane root) {
        this.root = root;
    }

    public void showScreen(Node screen) {
        if (!root.getChildren().contains(screen)) {
            if (!screenStack.isEmpty()) {
                // Hide the current screen with fade-out animation
                animateFadeOut(screenStack.peek());
            }

            // Show the new screen with fade-in animation
            root.getChildren().clear();
            root.getChildren().add(screen);
            animateFadeIn(screen);

            screenStack.push(screen);
        }
    }

    public void popScreen() {
        if (screenStack.size() > 1) {
            // Fade out the current screen
            animateFadeOut(screenStack.peek());

            screenStack.pop();
            Node previousScreen = screenStack.peek();

            // Show the previous screen
            root.getChildren().clear();
            root.getChildren().add(previousScreen);
            animateFadeIn(previousScreen);
        }
    }

    private void animateFadeIn(Node node) {
        node.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), node);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private void animateFadeOut(Node node) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), node);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> node.setVisible(false));
        fadeOut.play();
    }

    public StackPane getRoot() {
        return root;
    }
}
