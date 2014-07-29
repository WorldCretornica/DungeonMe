package com.worldcretornica.dungeonme;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

public class DungeonGenerator extends ChunkGenerator {

    @Override
    public byte[][] generateBlockSections(World world, Random random, int cx, int cz, BiomeGrid biomes) {
        
        final int maxY = world.getMaxHeight();
        final long seed = world.getSeed();
        
        byte[][] result = new byte[maxY / 16][];
        
        //int oneToTwenty = new Random(""+seed+x+y+z).nextInt(20) + 1
        
        return result;
    }
    
}
