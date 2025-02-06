package org.miu.mppproject.librarysystem.libraryapp.frontend.features;

public class ShowScreenEvent implements ScreenEvent{

    private Screen screen;

    public ShowScreenEvent(Screen screen) {
        this.screen = screen;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}
