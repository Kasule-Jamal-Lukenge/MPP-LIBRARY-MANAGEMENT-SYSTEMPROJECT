package org.miu.mppproject.librarysystem.libraryapp.frontend.features;

// Event to show an alert, implemented as a record
public record ShowAlertEvent(String title, String message) implements ScreenEvent {
}
