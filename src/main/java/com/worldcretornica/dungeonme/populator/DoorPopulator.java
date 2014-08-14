package com.worldcretornica.dungeonme.populator;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import com.worldcretornica.dungeonme.Orientation;

public class DoorPopulator extends BlockPopulator {

    @Override
    public void populate(World w, Random rand, Chunk chunk) {

		final long seed = w.getSeed();

		final long roomx = chunk.getX();
		final long roomz = chunk.getZ();

		final int xx = (int) (roomx << 4);
		final int zz = (int) (roomz << 4);

		final int maxY = w.getMaxHeight();

		for (long roomy = 0; roomy < (maxY >> 3); roomy++) {
			int yy = (int) (roomy << 3);
			int door = 0;
			boolean locked = false;

			// Doors
			Random current = new Random(seed ^ (roomx << 32) ^ (roomy << 16) ^ roomz);

			// bottomZ door
			door = current.nextInt(100) + 1;
			locked = current.nextInt(25) + 1 == 4;

			if (door > 40) {
				if (door > 70) {
					if (locked) {
						setDoor(w, xx + 3, yy + 1, zz, true, Orientation.MinZ);
					} else {
						setDoor(w, xx + 3, yy + 1, zz);
					}
				} else {
					if (locked) {
						setDoor(w, xx + 12, yy + 1, zz, true, Orientation.MinZ);
					} else {
						setDoor(w, xx + 12, yy + 1, zz);
					}
				}
			}

			// bottomX door
			door = current.nextInt(100) + 1;
			locked = current.nextInt(25) + 1 == 4;

			if (door > 40) {
				if (door > 70) {
					if (locked) {
						setDoor(w, xx, yy + 1, zz + 3, true, Orientation.MinX);
					} else {
						setDoor(w, xx, yy + 1, zz + 3);
					}
				} else {
					if (locked) {
						setDoor(w, xx, yy + 1, zz + 12, true, Orientation.MinX);
					} else {
						setDoor(w, xx, yy + 1, zz + 12);
					}
				}
			}

			// Other doors
			current = new Random(seed ^ (roomx << 32) ^ (roomy << 16) ^ (roomz + 1));

			// topZ door
			door = current.nextInt(100) + 1;

			if (door > 40) {
				if (door > 70) {
					setDoor(w, xx + 3, yy + 1, zz + 15);
				} else {
					setDoor(w, xx + 12, yy + 1, zz + 15);
				}
			}

			// topX door
			current = new Random(seed ^ ((roomx + 1) << 32) ^ (roomy << 16)	^ roomz);

			door = current.nextInt(100) + 1;
			locked = current.nextInt(25) + 1 == 4;
			door = current.nextInt(100) + 1;

			if (door > 40) {
				if (door > 70) {
					setDoor(w, xx + 15, yy + 1, zz + 3);
				} else {
					setDoor(w, xx + 15, yy + 1, zz + 12);
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
