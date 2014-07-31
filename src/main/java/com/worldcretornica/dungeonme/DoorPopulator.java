package com.worldcretornica.dungeonme;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class DoorPopulator extends BlockPopulator {

    @Override
    public void populate(World w, Random rand, Chunk chunk) {
        
        final long seed = w.getSeed();
        
        long roomx = chunk.getX();
        long roomz = chunk.getZ();
        
        int xx = chunk.getX() << 4;
        int zz = chunk.getZ() << 4;
        
        int maxY = w.getMaxHeight();
                
        for (int roomy = 0; roomy <= (maxY / 8); roomy++) 
        {
            int yy = roomy << 3;
            int chance = 0;

            // Doors
            // bottomZ door
            chance = new Random(seed ^ (roomx << 32) ^ (roomy << 16) ^ roomz).nextInt(100) + 1;

            if (chance >= 50 && chance < 75) {
                setBlock(w, xx + 3, yy + 1, zz, (byte) 0, 0);
                setBlock(w, xx + 3, yy + 2, zz, (byte) 0, 0);
            } else if (chance >= 75) {
                setBlock(w, xx + 12, yy + 1, zz, (byte) 0, 0);
                setBlock(w, xx + 12, yy + 2, zz, (byte) 0, 0);
            }

            // topZ door
            chance = new Random(seed ^ (roomx << 32) ^ (roomy << 16) ^ (roomz + 1)).nextInt(100) + 1; 
            
            if (chance >= 50 && chance < 75) {
                setBlock(w, xx + 3, yy + 1, zz + 15, (byte) 0, 0);
                setBlock(w, xx + 3, yy + 2, zz + 15, (byte) 0, 0);
            } else if (chance >= 75) {
                setBlock(w, xx + 12, yy + 1, zz + 15, (byte) 0, 0);
                setBlock(w, xx + 12, yy + 2, zz + 15, (byte) 0, 0);
            }

            // bottomX door
            chance = new Random(seed ^ (roomx << 32) ^ (roomy << 16) ^ roomz).nextInt(100) + 1;
            
            if (chance >= 50 && chance < 75) {
                setBlock(w, xx, yy + 1, zz + 3, (byte) 0, 0);
                setBlock(w, xx, yy + 2, zz + 3, (byte) 0, 0);
            } else if (chance >= 75) {
                setBlock(w, xx, yy + 1, zz + 12, (byte) 0, 0);
                setBlock(w, xx, yy + 2, zz + 12, (byte) 0, 0);
            }

            // topX door
            chance = new Random(seed ^ ((roomx + 1) << 32) ^ (roomy << 16) ^ roomz).nextInt(100) + 1;
            
            if (chance >= 50 && chance < 75) {
                setBlock(w, xx + 15, yy + 1, zz + 3, (byte) 0, 0);
                setBlock(w, xx + 15, yy + 2, zz + 3, (byte) 0, 0);
            } else if (chance >= 75) {
                setBlock(w, xx + 15, yy + 1, zz + 12, (byte) 0, 0);
                setBlock(w, xx + 15, yy + 2, zz + 12, (byte) 0, 0);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void setBlock(World w, int x, int y, int z, byte val, int id)
    {
        if(val != 0)
        {
            w.getBlockAt(x, y, z).setTypeIdAndData(id, val, false);
        }else{
            w.getBlockAt(x, y, z).setTypeId(id);
        }
    }
}
