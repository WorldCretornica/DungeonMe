package com.worldcretornica.dungeonme;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import com.worldcretornica.dungeonme.populator.DoorPopulator;
import com.worldcretornica.dungeonme.populator.StairPopulator;

public class DungeonGenerator extends ChunkGenerator {

    @SuppressWarnings("unused")
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
        
        
        long roomX2 = ((long) roomX) >> 2;
        long roomX1 = ((long) roomX) >> 1;
        long roomZ2 = ((long) roomZ) >> 2;
        long roomZ1 = ((long) roomZ) >> 1;
        
        for (long roomY = 0; roomY < (maxY >> 3); roomY++) {
            //Check if it's a 4x4x4 room
            rand = new Random(seed ^ (roomX2 << 32) ^ ((roomY >> 2) << 16) ^ roomZ2);
            if ((rand.nextInt(100) + 1) >= 95) {
                setBlock(result, 0, (int) ((roomY << 3) + 1), 0, (byte) 35);
            } else {
                //Check if it's a 4x4x2 room
                rand = new Random(seed ^ (roomX2 << 32) ^ ((roomY >> 1) << 16) ^ roomZ2);
                if ((rand.nextInt(100) + 1) >= 95) {
                    setBlock(result, 0, (int) ((roomY << 3) + 1), 0, (byte) 17);
                } else {
                    //Check if it's a 2x2x2 room
                    rand = new Random(seed ^ (roomX1 << 32) ^ ((roomY >> 1) << 16) ^ roomZ1);
                    if ((rand.nextInt(100) + 1) >= 95) {
                        setBlock(result, 0, (int) ((roomY << 3) + 1), 0, (byte) 1);
                    } else {
                        //Check if it's a 2x2x1
                        rand = new Random(seed ^ (roomX1 << 32) ^ ((roomY) << 16) ^ roomZ1);
                        if ((rand.nextInt(100) + 1) >= 95) {
                            setBlock(result, 0, (int) ((roomY << 3) + 1), 0, (byte) 2);
                        } else {
                            //Else make 1x1 room
                            makeSingleRoom(result, (int) roomY + 1);
                        }
                    }
                }
            }
        }
        
        /*
        //Single room
        for (int x = 0; x < 16; x++) {
        	for (int y = 0; y < maxY; y++) {
        		setBlock(result, x, y, 0, (byte) 98);
        		setBlock(result, x, y, 15, (byte) 98);
        	}        	
        }
        
        for (int z = 1; z < 15; z++) {
        	for (int y = 0; y < maxY; y++) {
        		setBlock(result, 0, y, z, (byte) 98);
        		setBlock(result, 15, y, z, (byte) 98);
        	}
        }
        
        for (int y = 0; y < maxY; y += 8) {
        	for (int x = 1; x < 15; x++) {
        		for (int z = 1; z < 15; z++) {
        			setBlock(result, x, y, z, (byte) 98);
        			setBlock(result, x, y + 7, z, (byte) 98);
        		}
        	}
        }*/
        
        return result;
    }
    
    
    public void makeSingleRoom(byte[][] result, int roomY) {
        int maxY = (roomY << 3) - 1;
        int minY = maxY - 7;
        
        for (int y = minY; y <= maxY; y++) {
            for (int x = 0; x < 16; x++) {

                setBlock(result, x, y, 0, (byte) 98);
                setBlock(result, x, y, 15, (byte) 98);
            }

            for (int z = 1; z < 15; z++) {
                setBlock(result, 0, y, z, (byte) 98);
                setBlock(result, 15, y, z, (byte) 98);
            }
        }

        for (int x = 1; x < 15; x++) {
            for (int z = 1; z < 15; z++) {
                setBlock(result, x, minY, z, (byte) 98);
                setBlock(result, x, maxY, z, (byte) 98);
            }
        }
    }
    
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
        return Arrays.asList(new DoorPopulator(), new StairPopulator());
    }
    
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }
}
