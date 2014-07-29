package com.worldcretornica.dungeonme.schematic;

public class Ench {

    private Short id;
    private Short lvl;
        
    public Ench(Short id, Short lvl) {
        this.id = id;
        this.lvl = lvl;
    }
    
    public Short getId() {
        return id;
    }
    
    public Short getLvl() {
        return lvl;
    }
}
