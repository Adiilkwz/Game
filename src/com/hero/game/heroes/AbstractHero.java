package com.hero.game.heroes;

import com.hero.game.observers.Observer;
import com.hero.game.strategies.AttackStrategy;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHero implements Hero {
    private String name;
    private int health;
    private int maxHealth;
    private AttackStrategy attackStrategy;
    private List<Observer> observers = new ArrayList<>();
    private int x, y;
    private int damageBoost = 0;
    private int healthBoost = 0;

    public AbstractHero(String name, int initialHealth) {
        this.name = name;
        this.health = initialHealth;
        this.maxHealth = initialHealth;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getMaxHealth() {
        return maxHealth + healthBoost;
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            notifyObservers(name + " takes " + damage + " damage and has died! Health now: 0/" + getMaxHealth());
        } else {
            notifyObservers(name + " takes " + damage + " damage. Health now: " + health + "/" + getMaxHealth());
        }
    }

    @Override
    public void setAttackStrategy(AttackStrategy strategy) {
        this.attackStrategy = strategy;
        notifyObservers(name + " switches to " + strategy.getClass().getSimpleName());
    }

    @Override
    public void attack(Hero target) {
        if (attackStrategy != null && isAlive() && target.isAlive()) {
            attackStrategy.attack(this, target);
        }
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int getDamageBoost() {
        return damageBoost;
    }

    @Override
    public void setDamageBoost(int boost) {
        this.damageBoost += boost;
        notifyObservers(name + " receives damage boost of " + boost);
    }

    @Override
    public int getHealthBoost() {
        return healthBoost;
    }

    @Override
    public void setHealthBoost(int boost) {
        this.healthBoost += boost;
        this.health += boost; // Apply to current health too
        notifyObservers(name + " receives health boost of " + boost + ". Max health now: " + getMaxHealth());
    }
}