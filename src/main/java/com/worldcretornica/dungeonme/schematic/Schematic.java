package com.worldcretornica.dungeonme.schematic;

import java.io.Serializable;
import java.util.List;

public class Schematic extends AbstractSchematicElement implements Serializable {
 
    private static final long serialVersionUID = 8966082365181590943L;
    
    private byte[] blocks;
    private byte[] data;
    private byte[] biomes;
    private String materials;
    private Short width;
    private Short length;
    private Short height;
    private List<Entity> entities;
    private List<TileEntity> tileentities;
    private String roomauthor;
    private Long checksum;
 
    public Schematic(byte[] blocks, byte[] data, byte[] biomes, String materials, Short width, Short length, Short height, List<Entity> entities, List<TileEntity> tileentities, String roomauthor, Long checksum) {
        this.blocks = blocks;
        this.data = data;
        this.biomes = biomes;
        this.materials = materials;
        this.width = width;
        this.length = length;
        this.height = height;
        this.entities = entities;
        this.tileentities = tileentities;
        this.roomauthor = roomauthor;
        this.checksum = checksum;
    }

    public byte[] getBlocks() {
        return blocks;
    }

    public byte[] getData() {
        return data;
    }

    public byte[] getBiomes() {
        return biomes;
    }

    public String getMaterials() {
        return materials;
    }

    public Short getWidth() {
        return width;
    }

    public Short getLength() {
        return length;
    }

    public Short getHeight() {
        return height;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public List<TileEntity> getTileEntities() {
        return tileentities;
    }
    
    public String getRoomAuthor() {
        return roomauthor;
    }
    
    public Long getChecksum() {
        return checksum;
    }
    
    public String toString() {
        return "{" + this.getClass().getName() + 
                ": blocks=" + Sanitize(blocks) +
                ", data=" + Sanitize(data) +
                ", biomes=" + Sanitize(biomes) +
                ", materials=" + Sanitize(materials) +
                ", width=" + Sanitize(width) + 
                ", length=" + Sanitize(length) +
                ", height=" + Sanitize(height) +
                ", entities=" + Sanitize(entities) + 
                ", tileentities=" + Sanitize(tileentities) + 
                ", roomauthor=" + Sanitize(roomauthor) +
                ", checksum=" + Sanitize(checksum) + "}";
    }
}
