package com.worldcretornica.dungeonme.schematic.v1_7;

import com.worldcretornica.dungeonme.schematic.AbstractSchematicElement;

public class Ench extends AbstractSchematicElement {

    private static final long serialVersionUID = 8903585791760402815L;
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
