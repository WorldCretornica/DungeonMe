package com.worldcretornica.dungeonme.schematic;

import java.util.List;

public class Entity extends AbstractSchematicElement {

    private static final long serialVersionUID = 3315103410018232693L;

    private Byte dir;
    private Byte direction;
    private Byte invulnerable;
    private Byte onground;
    private Byte canpickuploot;
    private Byte color;
    private Byte customnamevisible;
    private Byte leashed;
    private Byte persistencerequired;
    private Byte sheared;
    private Byte skeletontype;
    private Byte isbaby;
    private Byte itemrotation;

    private Double pushx;
    private Double pushz;

    private Entity riding;

    private Float falldistance;
    private Float absorptionamount;
    private Float healf;
    private Float itemdropchance;

    private Integer dimension;
    private Integer portalcooldown;
    private Integer tilex;
    private Integer tiley;
    private Integer tilez;
    private Integer transfercooldown;
    private Integer age;
    private Integer inlove;
    private Integer tntfuse;

    private Item item;

    private Leash leash;

    private Short attacktime;
    private Short deathtime;
    private Short health;
    private Short hurttime;
    private Short air;
    private Short fire;
    private Short fuel;

    private String id;
    private String motive;
    private String customname;

    private List<Double> motion;
    private List<Double> pos;
    private List<Float> rotation;
    private List<Attribute> attributes;
    private List<Float> dropchances;
    private List<Equipment> equipments;
    private List<Item> items;

    public Entity(Byte dir, Byte direction, Byte invulnerable, Byte onground, Short air, Short fire, Integer dimension, Integer portalcooldown, Integer tilex, 
            Integer tiley, Integer tilez, Float falldistance, String id, String motive, List<Double> motion, List<Double> pos, List<Float> rotation,
            Byte canpickuploot, Byte color, Byte customnamevisible, Byte leashed, Byte persistencerequired, Byte sheared, Short attacktime, Short deathtime, 
            Short health, Short hurttime, Integer age, Integer inlove, Float absorptionamount, Float healf, String customname, List<Attribute> attributes, 
            List<Float> dropchances, List<Equipment> equipments, Byte skeletontype, Entity riding, Leash leash, Item item, Byte isbaby,
            List<Item> items, Integer transfercooldown, Short fuel, Double pushx, Double pushz, Integer tntfuse, Byte itemrotation,
            Float itemdropchance) {
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
        this.item = item;
        this.isbaby = isbaby;
        this.items = items;
        this.transfercooldown = transfercooldown;
        this.fuel = fuel;
        this.pushx = pushx;
        this.pushz = pushz;
        this.tntfuse = tntfuse;
        this.itemrotation = itemrotation;
        this.itemdropchance = itemdropchance;
    }

    public Byte getDir() { return dir; }
    public Byte getDirection() { return direction; }
    public Byte getInvulnerable() { return invulnerable; }
    public Byte getOnGround() { return onground; }
    public Byte getCanPickupLoot() { return canpickuploot; }
    public Byte getColor() { return color; }
    public Byte getCustomNameVisible() { return customnamevisible; }
    public Byte getLeashed() { return leashed; }
    public Byte getPersistenceRequired() { return persistencerequired; }
    public Byte getSheared() { return sheared; }
    public Byte getSkeletonType() { return skeletontype; }
    public Byte getIsBaby() { return isbaby; }
    public Byte getItemRotation() { return itemrotation; }

    public Double getPushX() { return pushx; }
    public Double getPushZ() { return pushz; }

    public Entity getRiding() { return riding; }

    public Float getFallDistance() { return falldistance; }
    public Float getAbsorptionAmount() { return absorptionamount; }
    public Float getHealF() { return healf; }
    public Float getItemDropChance() { return itemdropchance; }

    public Integer getDimension() { return dimension; }
    public Integer getPortalCooldown() { return portalcooldown; }
    public Integer getTileX() { return tilex; }
    public Integer getTileY() { return tiley; }
    public Integer getTileZ() { return tilez; }
    public Integer getAge() { return age; }
    public Integer getInLove() { return inlove; }
    public Integer getTransferCooldown() { return transfercooldown; }
    public Integer getTNTFuse() { return tntfuse; }

    public Item getItem() { return item; }

    public Leash getLeash() { return leash; }

    public Short getAir() { return air; }
    public Short getFire() { return fire; }
    public Short getAttackTime() { return attacktime; }
    public Short getDeathTime() { return deathtime; }
    public Short getHealth() { return health; }
    public Short getHurtTime() { return hurttime; }
    public Short getFuel() { return fuel; }

    public String getId() { return id; }
    public String getMotive() { return motive; }
    public String getCustomName() { return customname; }

    public List<Double> getMotion() { return motion; }
    public List<Double> getPos() { return pos; }
    public List<Float> getRotation() { return rotation; }
    public List<Attribute> getAttributes() { return attributes; }
    public List<Float> getDropChances() { return dropchances; }
    public List<Equipment> getEquipments() { return equipments; }
    public List<Item> getItems() { return items; }

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
                ", leash=" + Sanitize(leash) + 
                ", item=" + Sanitize(item) + 
                ", isbaby=" + Sanitize(isbaby) + 
                ", items=" + Sanitize(items) + 
                ", transfercooldown=" + Sanitize(transfercooldown) + 
                ", fuel=" + Sanitize(fuel) + 
                ", pushx=" + Sanitize(pushx) + 
                ", pushz=" + Sanitize(pushz) + 
                ", tntfuse=" + Sanitize(tntfuse) + 
                ", itemrotation=" + Sanitize(itemrotation) + 
                ", itemdropchance=" + Sanitize(itemdropchance) + "}";
    }
}
