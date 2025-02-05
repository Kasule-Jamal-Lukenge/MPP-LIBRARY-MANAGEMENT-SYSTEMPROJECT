package org.miu.mppproject.librarysystem.libraryapp.core.navigation;

import javafx.event.EventTarget;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import org.miu.mppproject.librarysystem.libraryapp.presentation.features.auth.LoginScreen;
import org.miu.mppproject.librarysystem.libraryapp.presentation.features.auth.DashBoardScreen;

public class NavigationController {
    private final ScreenManager screenManager;

    private final StackPane root;

    public NavigationController(ScreenManager screenManager, StackPane root) {
        this.screenManager = screenManager;
        this.root = root;

        // Listen for navigation events fired from anywhere within `root`
        root.addEventHandler(NavigationEvent.NAVIGATION, this::onNavigationEvent);
    }

    // Handle navigation event
    private void onNavigationEvent(NavigationEvent event) {
        switch (event.getScreen()) {
            case LOGIN:
                screenManager.showScreen(new LoginScreen(root));
                break;
            case DASHBOARD:
                screenManager.showScreen(new DashBoardScreen(screenManager));
                break;
            default:
                System.out.println("Unknown screen: " + event.getScreen());
                break;
        }
    }

    // Navigate back
    public void navigateBack() {
        screenManager.popScreen();
    }
}
