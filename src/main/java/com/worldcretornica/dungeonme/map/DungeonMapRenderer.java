package com.worldcretornica.dungeonme.map;

import java.awt.Color;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;
import org.bukkit.map.MapCursor.Type;
import org.bukkit.map.MapCursorCollection;

import com.worldcretornica.dungeonme.DungeonMe;

public class DungeonMapRenderer extends MapRenderer {

    @SuppressWarnings("unused")
    private DungeonMe plugin;
    
    public DungeonMapRenderer(DungeonMe instance) {
        plugin = instance;
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
        
        Location center = new Location(w, centerX * 16 - 8, y + 4, centerZ * 16 - 8);
        Arrow arrow = w.spawn(center, Arrow.class);
        List<Entity> entities = arrow.getNearbyEntities(16, 4, 16);
        
        arrow.remove();
        
        //plugin.getLogger().info("Arrow " + center.toString());
        
        MapCursorCollection cursors = new MapCursorCollection();
        
        for(Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                
                byte type = Type.RED_POINTER.getValue();
                
                if (entity.equals((Entity) p)) {
                    type = Type.WHITE_POINTER.getValue();
                } else if (entity instanceof Player) {
                    type = Type.BLUE_POINTER.getValue();
                } else if(entity instanceof Animals) {
                    type = Type.GREEN_POINTER.getValue();
                }
                
                Location eloc = entity.getLocation();
                
                byte direction = 0;
                float yaw = eloc.getYaw();
                
                direction = (byte) (((yaw + 11.25f) / 22.5f) - 1);
                
                if (direction < 0 || direction > 15) direction = 0;
                
                byte eX = (byte) ((eloc.getX() - center.getX()) * 2);
                byte eY = (byte) ((eloc.getZ() - center.getZ()) * 2);
                
                cursors.addCursor(eX, eY, direction, type, true);
            }
        }
        
        mc.setCursors(cursors);
        
        for(int x = 0; x < 128; x++) {
            for(int z = 0; z < 128; z++) {
                
                Block b = w.getBlockAt((centerX * 16) + x - 64 - 8, y, (centerZ * 16) + z - 64 - 8);
                
                
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
        
        mc.drawText(2, 2, MinecraftFont.Font, "F" + lv);
    }

}
