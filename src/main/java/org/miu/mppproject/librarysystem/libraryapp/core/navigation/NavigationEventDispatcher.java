package org.miu.mppproject.librarysystem.libraryapp.core.navigation;

import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import org.miu.mppproject.librarysystem.libraryapp.presentation.features.auth.LoginScreen;
import org.miu.mppproject.librarysystem.libraryapp.presentation.features.auth.DashBoardScreen;

public class NavigationEventDispatcher implements EventHandler<NavigationEvent> {
    private final StackPane root;

    public NavigationEventDispatcher(StackPane root) {
        this.root = root;
    }

    @Override
    public void handle(NavigationEvent event) {
        root.getChildren().clear(); // Remove current screen

        switch (event.getScreen()) {
            case DASHBOARD -> root.getChildren().add(new DashBoardScreen(new ScreenManager(root)));
            case LOGIN -> root.getChildren().add(new LoginScreen(root));
        }
    }
}
