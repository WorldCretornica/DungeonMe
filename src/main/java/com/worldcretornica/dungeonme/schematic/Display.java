package com.worldcretornica.dungeonme.schematic;

import java.util.List;

public class Display extends AbstractSchematicElement {

    private static final long serialVersionUID = 8415661672242271183L;
    private String name;
    private List<String> lore;
 
    
    public Display(String name, List<String> lore) {
        this.name = name;
        this.lore = lore;
    }
    
    public String getName() {
        return name;
    }
    
    public List<String> getLore() {
        return lore;
    }
    
    public String toString()
    {
    	return "{" + this.getClass().getName() + 
    	        ": name=" + Sanitize(name) + 
    	        ", lore=" + Sanitize(lore) + "}"; 
    }
}
