package com.worldcretornica.dungeonme.schematic;

import java.util.List;

public class TileEntity extends AbstractSchematicElement {

    private static final long serialVersionUID = -2080234794735672945L;
    private Integer x;
    private Integer y;
    private Integer z;
    private String customname;
    private String id;
    private List<Item> items;
    private Byte rot;
    private Byte skulltype;
    private Short delay;
    private Short maxnearbyentities;
    private Short maxspawndelay;
    private Short minspawndelay;
    private Short requiredplayerrange;
    private Short spawncount;
    private Short spawnrange;
    private String entityid;
    private Short burntime;
    private Short cooktime;
    private String text1;
    private String text2;
    private String text3;
    private String text4;
    private Byte note;
    private Integer record;
    private RecordItem recorditem;
    private Short brewtime;
    private String command;
        
    public TileEntity(Integer x, Integer y, Integer z, String customname, String id, List<Item> items, Byte rot, 
            Byte skulltype, Short delay, Short maxnearbyentities, Short maxspawndelay, Short minspawndelay, 
            Short requiredplayerrange, Short spawncount, Short spawnrange, String entityid, Short burntime, Short cooktime,
            String text1, String text2, String text3, String text4, Byte note, Integer record, RecordItem recorditem,
            Short brewtime, String command) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.customname = customname;
        this.id = id;
        this.items = items;
        this.rot = rot;
        this.skulltype = skulltype;
        this.delay = delay;
        this.maxnearbyentities = maxnearbyentities;
        this.maxspawndelay = maxspawndelay;
        this.minspawndelay = minspawndelay;
        this.requiredplayerrange = requiredplayerrange;
        this.spawncount = spawncount;
        this.spawnrange = spawnrange;
        this.entityid = entityid;
        this.burntime = burntime;
        this.cooktime = cooktime;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.text4 = text4;
        this.note = note;
        this.record = record;
        this.recorditem = recorditem;
        this.brewtime = brewtime;
        this.command = command;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Integer getZ() {
        return z;
    }

    public String getCustomName() {
        return customname;
    }

    public String getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public Byte getRot() {
        return rot;
    }

    public Byte getSkullType() {
        return skulltype;
    }

    public Short getDelay() {
        return delay;
    }

    public Short getMaxNearbyEntities() {
        return maxnearbyentities;
    }

    public Short getMaxSpawnDelay() {
        return maxspawndelay;
    }

    public Short getMinSpawnDelay() {
        return minspawndelay;
    }

    public Short getRequiredPlayerRange() {
        return requiredplayerrange;
    }

    public Short getSpawnCount() {
        return spawncount;
    }

    public Short getSpawnRange() {
        return spawnrange;
    }

    public String getEntityId() {
        return entityid;
    }

    public Short getBurnTime() {
        return burntime;
    }

    public Short getCookTime() {
        return cooktime;
    }
    
    public String getText1() {
        return text1;
    }
    
    public String getText2() {
        return text2;
    }
    
    public String getText3() {
        return text3;
    }
    
    public String getText4() {
        return text4;
    }
    
    public Byte getNote() {
        return note;
    }
    
    public Integer getRecord() {
        return record;
    }
    
    public RecordItem getRecordItem() {
        return recorditem;
    }
    
    public Short getBrewTime() {
        return brewtime;
    }
    
    public String getCommand() {
        return command;
    }
    
    public String toString() {
        return "{" + this.getClass().getName() + 
                ": x=" + Sanitize(x) +
                ", y=" + Sanitize(y) +
                ", z=" + Sanitize(z) +
                ", customname=" + Sanitize(customname) +
                ", id=" + Sanitize(id) +
                ", items=" + Sanitize(items) + 
                ", rot=" + Sanitize(rot) +
                ", skulltype=" + Sanitize(skulltype) +
                ", delay=" + Sanitize(delay) +
                ", maxnearbyentities=" + Sanitize(maxnearbyentities) +
                ", maxspawndelay=" + Sanitize(maxspawndelay) +
                ", minspawndelay=" + Sanitize(minspawndelay) +
                ", requiredplayerrange=" + Sanitize(requiredplayerrange) +
                ", spawncount=" + Sanitize(spawncount) +
                ", spawnrange=" + Sanitize(spawnrange) +
                ", entityid=" + Sanitize(entityid) +
                ", burntime=" + Sanitize(burntime) +
                ", cooktime=" + Sanitize(cooktime) + 
                ", text1=" + Sanitize(text1) + 
                ", text2=" + Sanitize(text2) + 
                ", text3=" + Sanitize(text3) + 
                ", text4=" + Sanitize(text4) + 
                ", note=" + Sanitize(note) + 
                ", record=" + Sanitize(record) +
                ", recorditem=" + Sanitize(recorditem) + 
                ", brewtime=" + Sanitize(brewtime) + 
                ", command=" + Sanitize(command) + "}";
    }
}
