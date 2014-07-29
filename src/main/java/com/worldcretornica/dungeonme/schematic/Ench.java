package com.worldcretornica.dungeonme.schematic;

public class Ench extends AbstractSchematicElement {

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
    
    public String toString()
    {
    	return "{" + this.getClass().getName() + 
    	        ": id=" + Sanitize(id) + 
    	        ", lvl=" + Sanitize(lvl) + "}";
    }
}
