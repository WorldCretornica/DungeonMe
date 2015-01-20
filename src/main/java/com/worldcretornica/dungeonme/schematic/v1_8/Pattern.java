package com.worldcretornica.dungeonme.schematic.v1_8;

import com.worldcretornica.dungeonme.schematic.AbstractSchematicElement;

public class Pattern extends AbstractSchematicElement {

    private static final long serialVersionUID = 9110115056957717506L;

    private Integer color;
    
    private String pattern;
    
    public Pattern(Integer color, String pattern) {
        this.color = color;
        this.pattern = pattern;
    }
    
    public Integer getColor() { return color; }
    public String getPattern() { return pattern; }
    
    @Override
    public String toString() {
        return "{" + this.getClass().getName() + 
                ": color=" + Sanitize(color) +
                ", pattern=" + Sanitize(pattern) + "}";
    }

}
