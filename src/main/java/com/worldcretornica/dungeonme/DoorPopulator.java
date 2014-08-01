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
        
        int cx = chunk.getX() << 4;
        int cz = chunk.getZ() << 4;
        
        int maxY = w.getMaxHeight();
                
        for (int roomy = 0; roomy <= (maxY >> 3); roomy++) 
        {
            int yy = roomy << 3;
            int chance = 0;

            // Doors
            /*
             * 0 = no doors
             * 1 = bottomz
             * 2 = bottomX
             * 4 = bottomzlocked
             * 8 = bottomxlocked
             * 16 = MinimumDoor
             * 31 = total
           */
            
            
            // bottomZ door
            chance = new Random(seed ^ (roomx << 32) ^ (roomy << 16) ^ roomz).nextInt(32);
            
            if ((chance & 1) == 1) {
                if ((chance & 16) == 16) {
                    if ((chance & 4) == 4) {
                        setDoor(w, cx + 3, yy + 1, cz, true, Orientation.MinZ);
                    } else {
                        setDoor(w, cx + 3, yy + 1, cz);
                    }
                } else {
                    if ((chance & 4) == 4) {
                        setDoor(w, cx + 12, yy + 1, cz, true, Orientation.MinZ);
                    } else {
                        setDoor(w, cx + 12, yy + 1, cz);
                    }
                }
            }

            // bottomX door
            if ((chance & 2) == 2) {
                if ((chance & 16) == 16) {
                    if ((chance & 8) == 8) {
                        setDoor(w, cx, yy + 1, cz + 3, true, Orientation.MinX);
                    } else {
                        setDoor(w, cx, yy + 1, cz + 3);
                    }
                } else {
                    if ((chance & 8) == 8) {
                        setDoor(w, cx, yy + 1, cz + 12, true, Orientation.MinX);
                    } else {
                        setDoor(w, cx, yy + 1, cz + 12);
                    }
                }
            }
            
            // topZ door
            chance = new Random(seed ^ (roomx << 32) ^ (roomy << 16) ^ (roomz + 1)).nextInt(32);
            
            if ((chance & 1) == 1) {
                if ((chance & 16) == 16) {
                    setDoor(w, cx + 3, yy + 1, cz + 15);
                } else {
                    setDoor(w, cx + 12, yy + 1, cz + 15);
                }
            }

            // topX door
            if ((chance & 2) == 2) {
                if ((chance & 16) == 16) {
                    setDoor(w, cx + 15, yy + 1, cz + 3);
                } else {
                    setDoor(w, cx + 15, yy + 1, cz + 12);
                }
            }
        }
    }
    
    private void setDoor(World w, int x, int y, int z) { 
        setDoor(w, x, y, z, false, Orientation.MaxX);
    }
    
    private void setDoor(World w, int x, int y, int z, boolean locked, Orientation orient) {
        if (locked) {
            byte data1;
            byte data2 = 8;
            
            switch(orient) {
            case MinZ:
                data1 = 1;
                break;
            case MinX:
                data1 = 0;
                break;
            default:
                data1 = 0;
                break;
            }
            
            setBlock(w, x, y, z, data1, 71);
            setBlock(w, x, y + 1, z, data2, 71);
        } else {
            setBlock(w, x, y, z, (byte) 0, 0);
            setBlock(w, x, y + 1, z, (byte) 0, 0);
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
