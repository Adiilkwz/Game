package com.hero.game.decorators;

import com.hero.game.heroes.Hero;

public class HealthBoostDecorator extends HeroDecorator {
    private int healthBoost;

    public HealthBoostDecorator(Hero decoratedHero, int healthBoost) {
        super(decoratedHero);
        this.healthBoost = healthBoost;
        decoratedHero.setHealthBoost(decoratedHero.getHealthBoost() + healthBoost);
    }

    @Override
    public int getHealth() {
        return super.getHealth();
    }

    @Override
    public int getMaxHealth() {
        return decoratedHero.getMaxHealth() + healthBoost;
    }
}