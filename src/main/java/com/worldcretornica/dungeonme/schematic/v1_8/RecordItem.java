package com.worldcretornica.dungeonme.schematic.v1_8;

import com.worldcretornica.dungeonme.schematic.AbstractSchematicElement;

public class RecordItem extends AbstractSchematicElement {

    private static final long serialVersionUID = -7017507616421449624L;
    private Byte count;
    private Short damage;
    private Short id;
    
    public RecordItem(Byte count, Short damage, Short id) {
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
    
    public Short getId() {
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
