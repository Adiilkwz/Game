package com.hero.game.strategies;

import com.hero.game.heroes.Hero;

public class MagicAttack implements AttackStrategy {
    @Override
    public void attack(Hero attacker, Hero target) {
        int damage = 25 + attacker.getDamageBoost();
        target.takeDamage(damage);
        attacker.notifyObservers(attacker.getName() + " performs a magic attack on " + target.getName() + " for " + damage + " damage!");
    }
}