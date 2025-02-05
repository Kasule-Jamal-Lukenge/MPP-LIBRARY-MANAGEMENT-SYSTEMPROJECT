package org.miu.mppproject.librarysystem.libraryapp.core.navigation;

import javafx.event.Event;
import javafx.event.EventType;

public class NavigationEvent extends Event {
    public static final EventType<NavigationEvent> NAVIGATION = new EventType<>(Event.ANY, "NAVIGATION");

    public enum Screen {
        LOGIN, DASHBOARD
    }

    private final Screen screen;

    public NavigationEvent(Screen screen) {
        super(NAVIGATION);
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }
}
