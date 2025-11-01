package com.hero.game.factories;

import com.hero.game.heroes.Archer;
import com.hero.game.heroes.Hero;
import com.hero.game.heroes.Mage;
import com.hero.game.heroes.Warrior;

public class HeroFactory {
    public Hero createHero(String type, String name) {
        switch (type.toLowerCase()) {
            case "warrior":
                return new Warrior(name);
            case "mage":
                return new Mage(name);
            case "archer":
                return new Archer(name);
            default:
                throw new IllegalArgumentException("Unknown hero type: " + type);
        }
    }
}