package com.worldcretornica.dungeonme.schematic.v1_7;

import com.worldcretornica.dungeonme.schematic.AbstractSchematicElement;

public class Item extends AbstractSchematicElement {

    private static final long serialVersionUID = 8615036231087494336L;
    private Byte count;
    private Byte slot;
    private Short damage;
    private Short id;
    private ItemTag tag;
    
    public Item(Byte count, Byte slot, Short damage, Short id, ItemTag tag) {
        this.count = count;
        this.slot = slot;
        this.damage = damage;
        this.id = id;
        this.tag = tag;
    }
    
    public Byte getCount() {
        return count;
    }

    public Byte getSlot() {
        return slot;
    }

    public Short getDamage() {
        return damage;
    }

    public Short getId() {
        return id;
    }

    public ItemTag getTag() {
        return tag;
    }
    
    public String toString() {
        return "{" + this.getClass().getName() + 
                ": count=" + Sanitize(count) + 
                ", slot=" + Sanitize(slot) + 
                ", damage=" + Sanitize(damage) + 
                ", id=" + Sanitize(id) + 
                ", tag=" + Sanitize(tag) + "}";
        
    }
}
