package com.hero.game.observers;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

public class UIDisplay implements Observer {
    private TextArea displayArea;

    public UIDisplay(TextArea displayArea) {
        this.displayArea = displayArea;
    }

    @Override
    public void update(String message) {
        Platform.runLater(() -> displayArea.appendText(message + "\n"));
    }
}