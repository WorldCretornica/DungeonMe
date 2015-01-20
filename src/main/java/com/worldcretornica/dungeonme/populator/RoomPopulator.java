package com.worldcretornica.dungeonme.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import com.worldcretornica.dungeonme.DungeonMe;
import com.worldcretornica.dungeonme.schematic.Size;

public class RoomPopulator extends BlockPopulator {

    private DungeonMe plugin;
    
    public RoomPopulator(DungeonMe instance) {
        this.plugin = instance;
    }
    
    @Override
    public void populate(World w, Random random, Chunk chunk) {

        Random rand = null;
        
		final long seed = w.getSeed();

		final int roomX = chunk.getX();
		final int roomZ = chunk.getZ();

		final int xx = (int) (roomX << 4);
		final int zz = (int) (roomZ << 4);

		final int maxY = w.getMaxHeight();
	
		int roomX2 = roomX >> 2;
		int roomX1 = roomX >> 1;
		int roomZ2 = roomZ >> 2;
		int roomZ1 = roomZ >> 1;
        
        for (int roomY = 0; roomY < (maxY >> 3); roomY++) {
            //Check if it's a 4x4x4 room
            rand = plugin.getRandom(seed, roomX2, roomY >> 2, roomZ2);
            if ((rand.nextInt(100) + 1) >= 99) {
                //setBlock(w, 0, (int) ((roomY << 3) + 1), 0, (byte) 35);
            } else {
                //Check if it's a 4x4x2 room
                rand = plugin.getRandom(seed, roomX2, roomY >> 1, roomZ2);
                if ((rand.nextInt(100) + 1) >= 97) {
                    //setBlock(w, 0, (int) ((roomY << 3) + 1), 0, (byte) 17);
                } else {
                    //Check if it's a 2x2x2 room
                    rand = plugin.getRandom(seed, roomX1, roomY >> 1, roomZ1);
                    if ((rand.nextInt(100) + 1) >= 95) {
                        //setBlock(w, 0, (int) ((roomY << 3) + 1), 0, (byte) 1);
                    } else {
                        //Check if it's a 2x2x1
                        rand = plugin.getRandom(seed, roomX1, roomY, roomZ1);
                        if ((rand.nextInt(100) + 1) >= 90) {
                            //setBlock(w, 0, (int) ((roomY << 3) + 1), 0, (byte) 2);
                        } else {
                            //Else make 1x1x1 room
                            rand = plugin.getRandom(seed, roomX, roomY, roomZ);
                            
                            int yy = roomY << 3;
                            
                            //makeSingleRoom(new Location(w, xx, yy,zz), plugin.getSchematicUtil().getNextSchematic(Size.OneXOneXOne, rand));
                            plugin.getSchematicUtil().populateNextSchematic(new Location(w, xx, yy,zz), Size.OneXOneXOne, rand);
                        }
                    }
                }
            }
        }
	}

    /*@SuppressWarnings("deprecation")
    private void setBlock(World w, int x, int y, int z, byte val, int id)
    {
        if(val != 0)
        {
            w.getBlockAt(x, y, z).setTypeIdAndData(id, val, false);
        }else{
            w.getBlockAt(x, y, z).setTypeId(id);
        }
    }*/
}
