package com.worldcretornica.dungeonme.elements;

import org.bukkit.Location;

public abstract class AbstractSection extends AbstractElement {

    private AbstractDoor northdoor;
    private AbstractDoor westdoor;
    private AbstractStair stair;
    private AbstractFloor floor;
    
    public AbstractSection(double chance, AbstractDoor northdoor, AbstractDoor westdoor, AbstractStair stair, AbstractFloor floor) {
        super(chance);
        this.northdoor = northdoor;
        this.westdoor = westdoor;
        this.stair = stair;
        this.floor = floor;
    }
        
    public void generate(Location loc) {
        northdoor.generate(loc);
        westdoor.generate(loc);
        stair.generate(loc);
        floor.generate(loc);
    }
}
