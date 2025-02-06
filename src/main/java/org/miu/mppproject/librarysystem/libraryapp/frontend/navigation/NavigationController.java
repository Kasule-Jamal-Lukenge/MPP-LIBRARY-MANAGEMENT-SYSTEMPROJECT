package org.miu.mppproject.librarysystem.libraryapp.frontend.navigation;

import io.reactivex.rxjava3.subjects.PublishSubject;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.miu.mppproject.librarysystem.libraryapp.frontend.features.Screen;
import org.miu.mppproject.librarysystem.libraryapp.frontend.features.ScreenEvent;
import org.miu.mppproject.librarysystem.libraryapp.frontend.features.ShowAlertEvent;
import org.miu.mppproject.librarysystem.libraryapp.frontend.features.ShowScreenEvent;

public class NavigationController {

    private final Stage primaryStage;
    private final PublishSubject<ScreenEvent> navigationEvents = PublishSubject.create();

    public NavigationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Initialize the navigation controller to listen to navigation events.
     */
    public void initialize() {
        // Listen for navigation events
        navigationEvents.subscribe(this::handleNavigationEvent);
    }

    /**
     * Handle navigation event.
     */
    private void handleNavigationEvent(ScreenEvent event) {
        if (event instanceof ShowScreenEvent) {
            showScreen(((ShowScreenEvent) event).getScreen());
        } else if (event instanceof ShowAlertEvent) {
            showAlert(((ShowAlertEvent) event).title(), ((ShowAlertEvent) event).message(), AlertType.INFORMATION);
        }
    }

    /**
     * Navigate to the specified screen.
     */
    private void showScreen(Screen screen) {
        Pane screenView = screen.getView();
        Scene scene = new Scene(screenView);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Show an alert with the specified title and message.
     */
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Publish an event to navigate or show an alert.
     */
    public void publishEvent(ScreenEvent event) {
        navigationEvents.onNext(event);
    }
}
