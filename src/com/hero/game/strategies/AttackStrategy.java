package com.hero.game.strategies;

import com.hero.game.heroes.Hero;

public interface AttackStrategy {
    void attack(Hero attacker, Hero target);
}