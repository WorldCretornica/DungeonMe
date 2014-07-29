package com.worldcretornica.dungeonme.schematic;

import java.util.List;

public class Display {

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
}
