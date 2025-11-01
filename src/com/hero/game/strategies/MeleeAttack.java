package com.hero.game.strategies;

import com.hero.game.heroes.Hero;

public class MeleeAttack implements AttackStrategy {
    @Override
    public void attack(Hero attacker, Hero target) {
        int damage = 20 + attacker.getDamageBoost();
        target.takeDamage(damage);
        attacker.notifyObservers(attacker.getName() + " performs a melee attack on " + target.getName() + " for " + damage + " damage!");
    }
}