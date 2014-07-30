package com.worldcretornica.dungeonme.elements;

import org.bukkit.Location;

public abstract class AbstractElement {

    private double chance;
    
    public AbstractElement(double chance) {
        this.chance = chance;
    }
    
    public double getChance() {
        return chance;
    }
    
    public abstract void generate(Location loc);
}
