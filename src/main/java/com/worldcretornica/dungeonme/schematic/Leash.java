package com.worldcretornica.dungeonme.schematic;

public class Leash extends AbstractSchematicElement {

    private static final long serialVersionUID = 5212830225976155819L;
    private Integer x;
    private Integer y;
    private Integer z;
    
    public Leash(Integer x, Integer y, Integer z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Integer getX(){
        return x;
    }
    
    public Integer getY(){
        return y;
    }
    
    public Integer getZ(){
        return z;
    }

    @Override
    public String toString() {
        return "{" + this.getClass().getName() + 
                ": x=" + Sanitize(x) + 
                ", y=" + Sanitize(y) +
                ", z=" + Sanitize(z) + "}";
    }
}
