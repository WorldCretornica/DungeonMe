package com.worldcretornica.dungeonme.schematic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;

import com.worldcretornica.dungeonme.DungeonMe;
import com.worldcretornica.dungeonme.schematic.jnbt.Tag;

public abstract class AbstractSchematicUtil {

    protected DungeonMe plugin;
    
    public AbstractSchematicUtil(DungeonMe instance) {
        plugin = instance;
    }
    
    public abstract void generateNextSchematic(byte[][] blocks, int roomY, Size size, Random rand);
    
    public abstract void populateNextSchematic(Location loc, Size size, Random rand);

    public abstract void loadSchematics();

    public abstract void pasteSchematic(Location loc, Size size, int id);

    protected <T extends Tag, K> K getChildTag(Map<String, Tag> items, String key, Class<T> expected, Class<K> result) {
        if (!items.containsKey(key)) {
            return null;
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(tag.getName() + " tag is not of tag type " + expected.getName() + System.lineSeparator() + "tag is: " + tag.toString());
        }
        Object obj = expected.cast(tag).getValue();
        if (!result.isInstance(obj)) {
            return null;
        }
        return result.cast(obj);
    }
    
    protected <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) {
        if (!items.containsKey(key)) {
            return null;
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(tag.getName() + " tag is not of tag type " + expected.getName() + System.lineSeparator() + "tag is: " + tag.toString());
        }
        return expected.cast(tag);
    }
    
    protected <T> T convert(Object obj, Class<T> expected) {
        if (!(obj instanceof Tag)) {
            return null;
        } else {

            Tag tag = (Tag) obj;

            if (!expected.isInstance(tag.getValue())) {
                throw new IllegalArgumentException(tag.getName() + " tag is not of tag type " + expected.getName() + System.lineSeparator() + "tag is: " + tag.toString());
            }

            return expected.cast(tag.getValue());
        }
    }
    
    protected <T> List<T> convert(List<?> tagList, Class<T> expected) {
        if (tagList != null) {
            List<T> newlist = new ArrayList<T>();
            for (Object tag : tagList) {
                newlist.add(convert(tag, expected));
            }
            return newlist;
        } else {
            return null;
        }
    }
    
    protected void setBlock(byte[][] result, int x, int y, int z, byte blockkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blockkid;
    }
}