package com.worldcretornica.dungeonme.schematic;

public class Equipment extends AbstractSchematicElement {

    private static final long serialVersionUID = 9193507742570437834L;
    private Byte count;
    private Short damage;
    private Short id;
    private ItemTag itemtag;
    
    public Equipment(Byte count, Short damage, Short id, ItemTag itemtag) {
        this.count = count;
        this.damage = damage;
        this.id = id;
        this.itemtag = itemtag;
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
    
    public ItemTag getTag() {
        return itemtag;
    }
    
    @Override
    public String toString() {
        return "{" + this.getClass().getName() + 
                ": count=" + Sanitize(count) + 
                ", damage=" + Sanitize(damage) +
                ", id=" + Sanitize(id) + 
                ", itemtag=" + Sanitize(itemtag) + "}"; 
    }

}
