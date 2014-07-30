package com.worldcretornica.dungeonme.elements;

public abstract class AbstractStair extends AbstractElement {

    private byte blockid;
    
    public AbstractStair(double chance, byte blockid) {
        super(chance);
        this.blockid = blockid;
    }
    
    public byte getBlockId() {
        return blockid;
    }
}
