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
        
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < maxY; y++) {
                    if ((y + 1) % 8 == 1 || (y + 1) % 8 == 0 || x == 0 || x == 15 || z == 0 || z == 15) {
                        setBlock(result, x, y, z, (byte) 98);
                    }
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
