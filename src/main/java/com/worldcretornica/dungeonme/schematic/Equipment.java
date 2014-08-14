package com.worldcretornica.dungeonme.schematic;

public class Equipment extends AbstractSchematicElement {

    private static final long serialVersionUID = 9193507742570437834L;
    private Byte count;
    private Short damage;
    private String id;
    
    public Equipment(Byte count, Short damage, String id) {
        this.count = count;
        this.damage = damage;
        this.id = id;
    }
    
    public Byte getCount() {
        return count;
    }
    
    public Short getDamage() {
        return damage;
    }
    
    public String getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return "{" + this.getClass().getName() + 
                ": count=" + Sanitize(count) + 
                ", damage=" + Sanitize(damage) +
                ", id=" + Sanitize(id) + "}"; 
    }

}
