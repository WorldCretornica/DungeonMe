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
    
    private Byte canpickuploot;
    private Byte color;
    private Byte customnamevisible;
    private Byte leashed;
    private Byte persistencerequired;
    private Byte sheared;
    private Short attacktime;
    private Short deathtime;
    private Short health;
    private Short hurttime;
    private Integer age;
    private Integer inlove;
    private Float absorptionamount;
    private Float healf;
    private String customname;
    private List<Attribute> attributes;
    private List<Float> dropchances;
    private List<Equipment> equipments;
    
    private Byte skeletontype;
    private Entity riding;
    private Leash leash;

    public Entity(Byte dir, Byte direction, Byte invulnerable, Byte onground, Short air, Short fire, Integer dimension, Integer portalcooldown, Integer tilex, 
            Integer tiley, Integer tilez, Float falldistance, String id, String motive, List<Double> motion, List<Double> pos, List<Float> rotation,
            Byte canpickuploot, Byte color, Byte customnamevisible, Byte leashed, Byte persistencerequired, Byte sheared, Short attacktime, Short deathtime, 
            Short health, Short hurttime, Integer age, Integer inlove, Float absorptionamount, Float healf, String customname, List<Attribute> attributes, 
            List<Float> dropchances, List<Equipment> equipments, Byte skeletontype, Entity riding, Leash leash) {
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
        this.canpickuploot = canpickuploot;
        this.color = color;
        this.customnamevisible = customnamevisible;
        this.leashed = leashed;
        this.persistencerequired = persistencerequired;
        this.sheared = sheared;
        this.attacktime = attacktime;
        this.deathtime = deathtime;
        this.health = health;
        this.hurttime = hurttime;
        this.age = age;
        this.inlove = inlove;
        this.absorptionamount = absorptionamount;
        this.healf = healf;
        this.customname = customname;
        this.attributes = attributes;
        this.dropchances = dropchances;
        this.equipments = equipments;
        this.skeletontype = skeletontype;
        this.riding = riding;
        this.leash = leash;
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

    public Byte getCanPickupLoot() {
        return canpickuploot;
    }

    public Byte getColor() {
        return color;
    }

    public Byte getCustomNameVisible() {
        return customnamevisible;
    }

    public Byte getLeashed() {
        return leashed;
    }

    public Byte getPersistenceRequired() {
        return persistencerequired;
    }

    public Byte getSheared() {
        return sheared;
    }

    public Short getAttackTime() {
        return attacktime;
    }

    public Short getDeathTime() {
        return deathtime;
    }

    public Short getHealth() {
        return health;
    }

    public Short getHurtTime() {
        return hurttime;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getInLove() {
        return inlove;
    }

    public Float getAbsorptionAmount() {
        return absorptionamount;
    }

    public Float getHealF() {
        return healf;
    }

    public String getCustomName() {
        return customname;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<Float> getDropChances() {
        return dropchances;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }
    
    public Byte getSkeletonType() {
        return skeletontype;
    }
    
    public Entity getRiding() {
        return riding;
    }
    
    public Leash getLeash() {
        return leash;
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
    			", rotation=" + Sanitize(rotation) + 
                ", canpickuploot=" + Sanitize(canpickuploot) + 
                ", color=" + Sanitize(color) + 
                ", customnamevisible=" + Sanitize(customnamevisible) + 
                ", leashed=" + Sanitize(leashed) + 
                ", persistencerequired=" + Sanitize(persistencerequired) + 
                ", sheared=" + Sanitize(sheared) + 
                ", attacktime=" + Sanitize(attacktime) + 
                ", deathtime=" + Sanitize(deathtime) + 
                ", health=" + Sanitize(health) + 
                ", hurttime=" + Sanitize(hurttime) + 
                ", age=" + Sanitize(age) + 
                ", inlove=" + Sanitize(inlove) + 
                ", absorptionamount=" + Sanitize(absorptionamount) + 
                ", healf=" + Sanitize(healf) + 
                ", customname=" + Sanitize(customname) + 
                ", attributes=" + Sanitize(attributes) + 
                ", dropchances=" + Sanitize(dropchances) + 
                ", equipments=" + Sanitize(equipments) + 
                ", skeletontype=" + Sanitize(skeletontype) +
                ", riding=" + Sanitize(riding) + 
                ", leash=" + Sanitize(leash) + "}";
    }
}
