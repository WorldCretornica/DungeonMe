package com.worldcretornica.dungeonme;

import org.bukkit.block.Block;

public abstract class AbstractDungeonRoom {

    private Block[][][] _blocks;
    
    public Block getBlockAt(int x, int y, int z) {
        return _blocks[x][y][z];
    }
    
}
