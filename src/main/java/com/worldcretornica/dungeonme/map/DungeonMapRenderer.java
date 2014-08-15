package com.worldcretornica.dungeonme.map;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursor;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapCursor.Type;
import org.bukkit.map.MapCursorCollection;

import com.worldcretornica.dungeonme.DungeonMe;

public class DungeonMapRenderer extends MapRenderer {

    @SuppressWarnings("unused")
    private DungeonMe plugin;
    
    private static DungeonMapRenderer renderer = null;
    
    private DungeonMapRenderer() {
    }
    
    private DungeonMapRenderer(DungeonMe instance) {
        plugin = instance;
    }
    
    public static DungeonMapRenderer getInstance(DungeonMe instance) {
        if (DungeonMapRenderer.renderer == null)
            DungeonMapRenderer.renderer = new DungeonMapRenderer(instance);
        
        return DungeonMapRenderer.renderer;
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void render(MapView mv, MapCanvas mc, Player p) {
        
        Location ploc = p.getPlayer().getLocation();
        World w = p.getWorld();
        
        int lv = (int) Math.ceil(((double) ploc.getBlockY()) / 8);
        
        int centerX = (int) Math.ceil(((double) ploc.getBlockX()) / 16);
        int centerZ = (int) Math.ceil(((double) ploc.getBlockZ()) / 16);
        int y = lv * 8 - 7;
        
        mv.setCenterX(centerX);
        mv.setCenterZ(centerZ);
        
        Location center = new Location(w, (centerX << 4) - 8, y + 2, (centerZ << 4) - 8);
                
        MapCursorCollection cursors = new MapCursorCollection();
        
        List<Entity> entities = new ArrayList<>();
        entities.addAll(p.getNearbyEntities(16, 8, 16));
                
        //Add player cursor first
        MapCursor playercursor = getCursor(p, y, center, Type.WHITE_POINTER.getValue());
        
        if (playercursor != null) {
            cursors.addCursor(playercursor);
        }
        
        //Add mobs 2nd
        cursors = addCursors(entities, cursors, y, center, Monster.class, Type.RED_POINTER.getValue());
        //Add Ghast
        cursors = addCursors(entities, cursors, y, center, Ghast.class, Type.RED_POINTER.getValue());
        //Add players 3rd
        cursors = addCursors(entities, cursors, y, center, HumanEntity.class, Type.BLUE_POINTER.getValue());
        //Add animals 4th
        cursors = addCursors(entities, cursors, y, center, Animals.class, Type.GREEN_POINTER.getValue());
        
        mc.setCursors(cursors);
        
        for(int x = 0; x < 128; x++) {
            for(int z = 0; z < 128; z++) {
                
                Block b = w.getBlockAt((centerX << 4) + x - 64 - 8, y, (centerZ << 4) + z - 64 - 8);
                
                switch(b.getType()) {
                case AIR:
                case RAILS:
                case ACTIVATOR_RAIL:
                case POWERED_RAIL:
                case TORCH:
                case LEVER:
                case WOOD_BUTTON:
                case STONE_BUTTON:
                case WOOD_PLATE:
                case IRON_PLATE:
                    mc.setPixel(x, z, MapPalette.TRANSPARENT);
                    break;
                case CHEST:
                    mc.setPixel(x, z, MapPalette.matchColor(Color.yellow));
                    break;
                case LAVA:
                    mc.setPixel(x, z, MapPalette.RED);
                case WATER:
                    mc.setPixel(x, z, MapPalette.PALE_BLUE);
                    break;
                case IRON_DOOR_BLOCK:
                    mc.setPixel(x, z, MapPalette.WHITE);
                    break;
                case ACACIA_STAIRS:
                case BIRCH_WOOD_STAIRS:
                case BRICK_STAIRS:
                case COBBLESTONE_STAIRS:
                case DARK_OAK_STAIRS:
                case WOOD_STAIRS:
                case SMOOTH_STAIRS:
                case NETHER_BRICK_STAIRS:
                case SANDSTONE_STAIRS:
                case SPRUCE_WOOD_STAIRS:
                case JUNGLE_WOOD_STAIRS:
                case QUARTZ_STAIRS:
                    mc.setPixel(x, z, MapPalette.LIGHT_GREEN);
                    break;
                default:
                    mc.setPixel(x, z, MapPalette.GRAY_1);
                    break;
                }
            }
        }
        
        mc.drawText(3, 3, DungeonFont.Font, "F" + lv);
    }

    @SuppressWarnings("deprecation")
    public MapCursor getCursor(Entity entity, int y, Location center, byte type) {
        Location eloc = entity.getLocation();
        
        if (eloc.getBlockY() >= (y - 1) && eloc.getBlockY() <= (y + 7)) {
            byte direction = 0;
            float yaw = eloc.getYaw();
            
            while (yaw < 0) yaw = yaw + 360;
            
            direction = (byte) (((yaw + 11.25f) / 22.5f));
            
            if (direction < 0 || direction > 15) direction = 0;
            
            byte eX = (byte) ((eloc.getX() - center.getX()) * 2);
            byte eY = (byte) ((eloc.getZ() - center.getZ()) * 2);
            
            return new MapCursor(eX, eY, direction, type, true);
        } else {
            return null;
        }
    }
    
    public MapCursorCollection addCursors(List<Entity> entities, MapCursorCollection cursors, int y, Location center, Class<?> clazz, byte type) {
        for(Entity entity : entities) {
            if (clazz.isInstance(entity)) {               
                MapCursor cursor = getCursor(entity, y, center, type);
                
                if (cursor != null) {
                    cursors.addCursor(cursor);
                }
                
                if (cursors.size() >= 4) {
                    break;
                }
            }
        }
        
        return cursors;
    }
}
