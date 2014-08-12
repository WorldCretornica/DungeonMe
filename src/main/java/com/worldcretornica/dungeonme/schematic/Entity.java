package com.worldcretornica.dungeonme.schematic;

import java.util.List;

public class Entity extends AbstractSchematicElement {

    private static final long serialVersionUID = 3315103410018232693L;
    private Byte dir;
    private Byte direction;
    private Byte invulnerable;
    private Byte onground;
    private Short air;
    private Short fire;
    private Integer dimension;
    private Integer portalcooldown;
    private Integer tilex;
    private Integer tiley;
    private Integer tilez;
    private Float falldistance;
    private String id;
    private String motive;
    private List<Double> motion;
    private List<Double> pos;
    private List<Float> rotation;

    public Entity(Byte dir, Byte direction, Byte invulnerable, Byte onground, Short air, Short fire, Integer dimension, Integer portalcooldown, Integer tilex, Integer tiley, Integer tilez, Float falldistance, String id, String motive, List<Double> motion, List<Double> pos, List<Float> rotation) {
        this.dir = dir;
        this.direction = direction;
        this.invulnerable = invulnerable;
        this.onground = onground;
        this.air = air;
        this.fire = fire;
        this.dimension = dimension;
        this.portalcooldown = portalcooldown;
        this.tilex = tilex;
        this.tiley = tiley;
        this.tilez = tilez;
        this.falldistance = falldistance;
        this.id = id;
        this.motive = motive;
        this.motion = motion;
        this.pos = pos;
        this.rotation = rotation;
    }

    public Byte getDir() {
        return dir;
    }

    public Byte getDirection() {
        return direction;
    }

    public Byte getInvulnerable() {
        return invulnerable;
    }

    public Byte getOnGround() {
        return onground;
    }

    public Short getAir() {
        return air;
    }

    public Short getFire() {
        return fire;
    }

    public Integer getDimension() {
        return dimension;
    }

    public Integer getPortalCooldown() {
        return portalcooldown;
    }

    public Integer getTileX() {
        return tilex;
    }

    public Integer getTileY() {
        return tiley;
    }

    public Integer getTileZ() {
        return tilez;
    }

    public Float getFallDistance() {
        return falldistance;
    }

    public String getId() {
        return id;
    }

    public String getMotive() {
        return motive;
    }

    public List<Double> getMotion() {
        return motion;
    }

    public List<Double> getPos() {
        return pos;
    }

    public List<Float> getRotation() {
        return rotation;
    }
    
    public String toString()
    {
    	return "{" + this.getClass().getName() + 
    	        ": dir=" + Sanitize(dir) + 
    	        ", direction=" + Sanitize(direction) + 
    	        ", invulnerable=" + Sanitize(invulnerable) +
    			", onground=" + Sanitize(onground) + 
    			", air=" + Sanitize(air) + 
    			", fire=" + Sanitize(fire) + 
    			", dimension=" + Sanitize(dimension) + 
    			", portalcooldown=" + Sanitize(portalcooldown) +
    			", tilex=" + Sanitize(tilex) + 
    			", tiley=" + Sanitize(tiley) + 
    			", tilez=" + Sanitize(tilez) + 
    			", falldistance=" + Sanitize(falldistance) + 
    			", id=" + Sanitize(id) +
    			", motive=" + Sanitize(motive) + 
    			", motion=" + Sanitize(motion) + 
    			", pos=" + Sanitize(pos) + 
    			", rotation=" + Sanitize(rotation) + "}";
    }
}
