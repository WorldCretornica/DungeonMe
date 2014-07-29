package com.worldcretornica.dungeonme.schematic;

import java.util.List;

public class Item {

    private Byte count;
    private Byte slot;
    private Short damage;
    private Short id;
    private List<ItemTag> tags;
    
    public Item(Byte count, Byte slot, Short damage, Short id, List<ItemTag> tags) {
        this.count = count;
        this.slot = slot;
        this.damage = damage;
        this.id = id;
        this.tags = tags;
    }
    
    public Byte getCount() {return count;}
    public Byte getSlot() {return slot;}
    public Short getDamage() {return damage;}
    public Short getId() {return id;}
    public List<ItemTag> getTags() {return tags;}
    
}
