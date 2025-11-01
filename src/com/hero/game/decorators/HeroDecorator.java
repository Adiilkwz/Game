package com.hero.game.decorators;

import com.hero.game.heroes.Hero;
import com.hero.game.observers.Observer;
import com.hero.game.strategies.AttackStrategy;

public abstract class HeroDecorator implements Hero {
    protected Hero decoratedHero;

    public HeroDecorator(Hero decoratedHero) {
        this.decoratedHero = decoratedHero;
    }

    @Override
    public String getName() {
        return decoratedHero.getName();
    }

    @Override
    public int getHealth() {
        return decoratedHero.getHealth();
    }

    @Override
    public int getMaxHealth() {
        return decoratedHero.getMaxHealth();
    }

    @Override
    public void takeDamage(int damage) {
        decoratedHero.takeDamage(damage);
    }

    @Override
    public void setAttackStrategy(AttackStrategy strategy) {
        decoratedHero.setAttackStrategy(strategy);
    }

    @Override
    public void attack(Hero target) {
        decoratedHero.attack(target);
    }

    @Override
    public void registerObserver(Observer observer) {
        decoratedHero.registerObserver(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        decoratedHero.removeObserver(observer);
    }

    @Override
    public void notifyObservers(String message) {
        decoratedHero.notifyObservers(message);
    }

    @Override
    public boolean isAlive() {
        return decoratedHero.isAlive();
    }

    @Override
    public int getX() {
        return decoratedHero.getX();
    }

    @Override
    public int getY() {
        return decoratedHero.getY();
    }

    @Override
    public void setPosition(int x, int y) {
        decoratedHero.setPosition(x, y);
    }

    @Override
    public int getDamageBoost() {
        return decoratedHero.getDamageBoost();
    }

    @Override
    public void setDamageBoost(int boost) {
        decoratedHero.setDamageBoost(boost);
    }

    @Override
    public int getHealthBoost() {
        return decoratedHero.getHealthBoost();
    }

    @Override
    public void setHealthBoost(int boost) {
        decoratedHero.setHealthBoost(boost);
    }
}