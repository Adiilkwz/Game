package com.hero.game.decorators;

import com.hero.game.heroes.Hero;

public class DamageBoostDecorator extends HeroDecorator {
    private int damageBoost;

    public DamageBoostDecorator(Hero decoratedHero, int damageBoost) {
        super(decoratedHero);
        this.damageBoost = damageBoost;
        decoratedHero.setDamageBoost(decoratedHero.getDamageBoost() + damageBoost);
    }

    @Override
    public void attack(Hero target) {
        super.attack(target);
    }
}