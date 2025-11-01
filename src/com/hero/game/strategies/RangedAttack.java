package com.hero.game.strategies;

import com.hero.game.heroes.Hero;

public class RangedAttack implements AttackStrategy {
    @Override
    public void attack(Hero attacker, Hero target) {
        int damage = 15 + attacker.getDamageBoost();
        target.takeDamage(damage);
        attacker.notifyObservers(attacker.getName() + " performs a ranged attack on " + target.getName() + " for " + damage + " damage!");
    }
}