package com.hero.game.observers;

public class GameLogger implements Observer {
    @Override
    public void update(String message) {
        System.out.println("[LOG] " + message);
    }
}