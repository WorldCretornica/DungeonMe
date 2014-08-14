package com.worldcretornica.dungeonme.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class StairPopulator extends BlockPopulator {

    @Override
    public void populate(World w, Random rand, Chunk chunk) {
        
        final long seed = w.getSeed();
        
        final long roomx = chunk.getX();
        final long roomz = chunk.getZ();
        
        final int xx = (int) (roomx << 4);
        final int zz = (int) (roomz << 4);
        int yy;
        
        int maxY = w.getMaxHeight();
        int chance;
                
        for (long roomy = 0; roomy < (maxY >> 3) - 1; roomy++) {
            yy = (int) (roomy << 3);

            // Stairs
            // Chance of stairs in this room
            chance = new Random(seed ^ (roomx << 32) ^ (roomy << 16) ^ roomz).nextInt(100) + 1;
            
            if (chance >= 95) {
                chance = new Random(seed ^ (roomx << 32) ^ (roomy << 16) ^ roomz).nextInt(4) + 1;
                
                switch (chance) {
                case 1:
                    // bottomZ
                    for(int c = 1; c <= 8; c++) {
                        setBlock(w, xx + 3 + c, yy + c, zz + 1, (byte) 0, 109);
                        
                        if(c <= 6) {
                            setBlock(w, xx + 4 + c, yy + c, zz + 1, (byte) 5, 109);
                        }
                    }                       
                    
                    setBlock(w, xx + 7, yy + 8, zz + 1, (byte) 5, 109);
                    setBlock(w, xx + 8, yy + 8, zz + 1, (byte) 0, 0);
                    setBlock(w, xx + 9, yy + 8, zz + 1, (byte) 0, 0);
                    setBlock(w, xx + 10, yy + 8, zz + 1, (byte) 0, 0);
                    
                    setBlock(w, xx + 6, yy + 7, zz + 1, (byte) 5, 109);
                    setBlock(w, xx + 7, yy + 7, zz + 1, (byte) 0, 0);
                    setBlock(w, xx + 8, yy + 7, zz + 1, (byte) 0, 0);
                    setBlock(w, xx + 9, yy + 7, zz + 1, (byte) 0, 0);
                    
                    break;
                case 2:
                    // topZ                        
                    for(int c = 1; c <= 8; c++) {
                        setBlock(w, xx + 12 - c, yy + c, zz + 14, (byte) 1, 109);
                        
                        if(c <= 6) {
                            setBlock(w, xx + 11 - c, yy + c, zz + 14, (byte) 4, 109);
                        }
                    }

                    setBlock(w, xx + 9, yy + 7, zz + 14, (byte) 4, 109);
                    setBlock(w, xx + 8, yy + 7, zz + 14, (byte) 0, 0);
                    setBlock(w, xx + 7, yy + 7, zz + 14, (byte) 0, 0);
                    setBlock(w, xx + 6, yy + 7, zz + 14, (byte) 0, 0);
                    
                    setBlock(w, xx + 8, yy + 8, zz + 14, (byte) 4, 109);
                    setBlock(w, xx + 7, yy + 8, zz + 14, (byte) 0, 0);
                    setBlock(w, xx + 6, yy + 8, zz + 14, (byte) 0, 0);
                    setBlock(w, xx + 5, yy + 8, zz + 14, (byte) 0, 0);
                    
                    break;
                case 3:
                    // bottomX
                    for(int c = 1; c <= 8; c++) {
                        setBlock(w, xx + 1, yy + c, zz + 12 - c, (byte) 3, 109);
                        
                        if(c <= 6) {
                            setBlock(w, xx + 1, yy + c, zz + 11 - c, (byte) 6, 109);
                        }
                    }                       
                    
                    setBlock(w, xx + 1, yy + 7, zz + 9, (byte) 6, 109);
                    setBlock(w, xx + 1, yy + 7, zz + 8, (byte) 0, 0);
                    setBlock(w, xx + 1, yy + 7, zz + 7, (byte) 0, 0);
                    setBlock(w, xx + 1, yy + 7, zz + 6, (byte) 0, 0);
                    
                    setBlock(w, xx + 1, yy + 8, zz + 8, (byte) 6, 109);
                    setBlock(w, xx + 1, yy + 8, zz + 7, (byte) 0, 0);
                    setBlock(w, xx + 1, yy + 8, zz + 6, (byte) 0, 0);
                    setBlock(w, xx + 1, yy + 8, zz + 5, (byte) 0, 0);
                    
                    break;
                case 4:
                    // topX
                    for(int c = 1; c <= 8; c++) {
                        setBlock(w, xx + 14, yy + c, zz + 3 + c, (byte) 2, 109);
                        
                        if(c <= 6) {
                            setBlock(w, xx + 14, yy + c, zz + 4 + c, (byte) 7, 109);
                        }
                    }

                    setBlock(w, xx + 14, yy + 8, zz + 7, (byte) 7, 109);
                    setBlock(w, xx + 14, yy + 8, zz + 8, (byte) 0, 0);
                    setBlock(w, xx + 14, yy + 8, zz + 9, (byte) 0, 0);
                    setBlock(w, xx + 14, yy + 8, zz + 10, (byte) 0, 0);
                    
                    setBlock(w, xx + 14, yy + 7, zz + 6, (byte) 7, 109);
                    setBlock(w, xx + 14, yy + 7, zz + 7, (byte) 0, 0);
                    setBlock(w, xx + 14, yy + 7, zz + 8, (byte) 0, 0);
                    setBlock(w, xx + 14, yy + 7, zz + 9, (byte) 0, 0);
                    
                    break;
                }
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
