package com.worldcretornica.dungeonme.schematic;

import java.util.List;

public class TileEntity {

    private Integer x;
    private Integer y;
    private Integer z;
    private String customname;
    private String id;
    private List<Item> items;
    
    public TileEntity(Integer x, Integer y, Integer z, String customname, String id, List<Item> items)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.customname = customname;
        this.id = id;
        this.items = items;
    }
    
    public Integer getX()
    {
        return x;
    }
    
    public Integer getY()
    {
        return y;
    }
    
    public Integer getZ()
    {
        return z;
    }
    
    public String getCustomName()
    {
        return customname;
    }
    
    public String getId()
    {
        return id;
    }
    
    public List<Item> getItems()
    {
        return items;
    }
}
