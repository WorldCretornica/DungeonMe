package com.worldcretornica.dungeonme;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class DungeonGenerator extends ChunkGenerator {

    @Override
    public byte[][] generateBlockSections(World w, Random r, int cx, int cz, BiomeGrid b) {
        final int maxY = w.getMaxHeight();
        
        byte[][] result = new byte[maxY << 4][];
        
        //Old ways
        /*for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < maxY; y++) {
                    if ((y + 1) % 8 == 1 || (y + 1) % 8 == 0 || x == 0 || x == 15 || z == 0 || z == 15) {
                        setBlock(result, x, y, z, (byte) 98);
                    }
                }
            }
        }*/
        
        
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
        }
        
        return result;
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
}
