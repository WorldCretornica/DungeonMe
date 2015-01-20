package com.worldcretornica.dungeonme;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import com.worldcretornica.dungeonme.populator.DoorPopulator;
import com.worldcretornica.dungeonme.populator.RoomPopulator;
import com.worldcretornica.dungeonme.populator.StairPopulator;
import com.worldcretornica.dungeonme.schematic.Size;

public class DungeonGenerator extends ChunkGenerator {

    private DungeonMe plugin;
    
    public DungeonGenerator(DungeonMe instance) {
        plugin = instance;
    }
    
    @Override
    public byte[][] generateBlockSections(World w, Random r, int roomX, int roomZ, BiomeGrid b) {
        final int maxY = w.getMaxHeight();
        
        byte[][] result = new byte[maxY << 4][];
        
        Random rand = null;
        long seed = w.getSeed();
        
        int roomX2 = roomX >> 2;
        int roomX1 = roomX >> 1;
        int roomZ2 = roomZ >> 2;
        int roomZ1 = roomZ >> 1;
        
        for (int roomY = 0; roomY < (maxY >> 3); roomY++) {
            //Check if it's a 4x4x4 room
            //rand = new Random(seed ^ (roomX2 << 32) ^ ((roomY >> 2) << 16) ^ roomZ2);
            rand = plugin.getRandom(seed, roomX2, roomY >> 2, roomZ2);
            if ((rand.nextInt(100) + 1) >= 99) {
                setBlock(result, 0, (int) ((roomY << 3) + 1), 0, (byte) 35);
            } else {
                //Check if it's a 4x4x2 room
                //rand = new Random(seed ^ (roomX2 << 32) ^ ((roomY >> 1) << 16) ^ roomZ2);
                rand = plugin.getRandom(seed, roomX2, roomY >> 1, roomZ2);
                if ((rand.nextInt(100) + 1) >= 97) {
                    setBlock(result, 0, (int) ((roomY << 3) + 1), 0, (byte) 17);
                } else {
                    //Check if it's a 2x2x2 room
                    //rand = new Random(seed ^ (roomX1 << 32) ^ ((roomY >> 1) << 16) ^ roomZ1);
                    rand = plugin.getRandom(seed, roomX1, roomY >> 1, roomZ1);
                    if ((rand.nextInt(100) + 1) >= 95) {
                        setBlock(result, 0, (int) ((roomY << 3) + 1), 0, (byte) 1);
                    } else {
                        //Check if it's a 2x2x1
                        //rand = new Random(seed ^ (roomX1 << 32) ^ ((roomY) << 16) ^ roomZ1);
                        rand = plugin.getRandom(seed, roomX1, roomY, roomZ1);
                        if ((rand.nextInt(100) + 1) >= 90) {
                            setBlock(result, 0, (int) ((roomY << 3) + 1), 0, (byte) 2);
                        } else {
                            //Else make 1x1x1 room
                            //rand = new Random(seed ^ (roomX << 32) ^ ((roomY) << 16) ^ roomZ);
                            rand = plugin.getRandom(seed, roomX, roomY, roomZ);
                            //makeSingleRoom(result, (int) roomY, plugin.getSchematicUtil().getNextSchematic(Size.OneXOneXOne, rand));
                            plugin.getSchematicUtil().generateNextSchematic(result, (int) roomY, Size.OneXOneXOne, rand);
                        }
                    }
                }
            }
        }
                
        return result;
    }

    /*public void makeSingleRoom(byte[][] result, int roomY, Schematic schematic) {
        int minY = roomY << 3;
        
        if (schematic == null) {
            plugin.getLogger().severe("Schematic is null");
        } else {
            
            int[] blocks = schematic.getBlocks();
            Short length = schematic.getLength();
            Short width = schematic.getWidth();
            Short height = schematic.getHeight();
            
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    for (int z = 0; z < length; ++z) {
                        int index = y * width * length + z * width + x;
                        setBlock(result, x, y + minY, z, (byte) blocks[index]);
                    }
                }
            }
        }
    }*/
    
    protected void setBlock(byte[][] result, int x, int y, int z, byte blockkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blockkid;
    }
    

    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 8, 65, 8);
    }
    
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return Arrays.asList(new RoomPopulator(plugin), new DoorPopulator(plugin), new StairPopulator(plugin));
    }
    
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }
}
