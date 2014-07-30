package com.worldcretornica.dungeonme.elements;

public abstract class AbstractDoor extends AbstractElement {

    private int doorposition;
    
    public AbstractDoor(double chance, int doorposition) {
        super(chance);
        this.doorposition = doorposition;
    }
    
    public int getDoorPosition() {
        return doorposition;
    }

}
