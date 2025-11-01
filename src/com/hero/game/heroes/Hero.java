package com.hero.game.heroes;

import com.hero.game.observers.Observer;
import com.hero.game.strategies.AttackStrategy;

import java.util.ArrayList;
import java.util.List;

public interface Hero {
    String getName();
    int getHealth();
    int getMaxHealth();
    void takeDamage(int damage);
    void setAttackStrategy(AttackStrategy strategy);
    void attack(Hero target);
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String message);
    boolean isAlive();
    int getX();
    int getY();
    void setPosition(int x, int y);
    int getDamageBoost();
    void setDamageBoost(int boost);
    int getHealthBoost();
    void setHealthBoost(int boost);
}