package com.worldcretornica.dungeonme.schematic.v1_8;

import java.io.*;
import java.util.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.*;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.*;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.worldcretornica.dungeonme.DungeonMe;
import com.worldcretornica.dungeonme.schematic.Attribute;
import com.worldcretornica.dungeonme.schematic.Entity;
import com.worldcretornica.dungeonme.schematic.Equipment;
import com.worldcretornica.dungeonme.schematic.Item;
import com.worldcretornica.dungeonme.schematic.ItemTag;
import com.worldcretornica.dungeonme.schematic.Leash;
import com.worldcretornica.dungeonme.schematic.Pattern;
import com.worldcretornica.dungeonme.schematic.Pose;
import com.worldcretornica.dungeonme.schematic.RecordItem;
import com.worldcretornica.dungeonme.schematic.Schematic;
import com.worldcretornica.dungeonme.schematic.Size;
import com.worldcretornica.dungeonme.schematic.TileEntity;
import com.worldcretornica.dungeonme.schematic.jnbt.*;

public class SchematicUtil extends com.worldcretornica.dungeonme.schematic.v1_7.SchematicUtil {
    
    public SchematicUtil(DungeonMe instance) {
        super(instance);
    }
    
    @Override
    public void pasteSchematic(Location loc, Size size, int id) {
        Schematic schem = getSchematic(size, id);
        pasteSchematic(loc, schem);
    }
    
    @Override
    public Schematic loadSchematic(File file) throws IOException, IllegalArgumentException {
        
        long checksum = Files.hash(file, Hashing.md5()).asLong();
        
        Schematic schem = loadCompiledSchematic(file.getName(), checksum);

        if (schem == null) {

            try (NBTInputStream nbtStream = new NBTInputStream(new FileInputStream(file))) {

                CompoundTag schematicTag = (CompoundTag) nbtStream.readTag();
                if (!schematicTag.getName().equals("Schematic")) {
                    throw new IllegalArgumentException("Tag \"Schematic\" does not exist or is not first");
                }

                Map<String, Tag> schematic = schematicTag.getValue();
                if (!schematic.containsKey("Blocks")) {
                    throw new IllegalArgumentException("Schematic file is missing a \"Blocks\" tag");
                }

                short width = getChildTag(schematic, "Width", ShortTag.class, Short.class);
                short length = getChildTag(schematic, "Length", ShortTag.class, Short.class);
                short height = getChildTag(schematic, "Height", ShortTag.class, Short.class);
                String roomauthor = getChildTag(schematic, "RoomAuthor", StringTag.class, String.class);
                
                Size size = null;
                               
                if (width == 16 * 4 && length == 16 * 4) {
                    if (height == 8 * 4) {
                        size = Size.FourXFourXFour;
                    } else {
                        size = Size.FourXFourXTwo;
                    }
                } else if (width == 16 * 2 && length == 16 * 2) {
                    size = Size.TwoXTwoXTwo;
                } else if (width == 16 * 2 || length == 16 * 2) {
                    size = Size.TwoXTwoXOne;
                } else {
                    size = Size.OneXOneXOne;
                }
                
                Integer originx = getChildTag(schematic, "WEOriginX", IntTag.class, Integer.class);
                Integer originy = getChildTag(schematic, "WEOriginY", IntTag.class, Integer.class);
                Integer originz = getChildTag(schematic, "WEOriginZ", IntTag.class, Integer.class);

                String materials = getChildTag(schematic, "Materials", StringTag.class, String.class);
                if (!materials.equals("Alpha")) {
                    throw new IllegalArgumentException("Schematic file is not an Alpha schematic");
                }

                byte[] rawblocks = getChildTag(schematic, "Blocks", ByteArrayTag.class, byte[].class);
                int[] blocks = new int[rawblocks.length];
                
                for(int ctr = 0; ctr < rawblocks.length; ctr++) {
                    int blockid = rawblocks[ctr] & 0xff;
                    
                    //if(blockid < 0) blockid = 256 + blockid;
                    
                    blocks[ctr] = blockid;
                }
                
                
                byte[] blockData = getChildTag(schematic, "Data", ByteArrayTag.class, byte[].class);
                byte[] blockBiomes = getChildTag(schematic, "Biomes", ByteArrayTag.class, byte[].class);

                List<Entity> entities = null;
                List<TileEntity> tileentities = null;

                // Load Entities
                List<?> entitiesList = getChildTag(schematic, "Entities", ListTag.class, List.class);

                if (entitiesList != null) {
                    entities = new ArrayList<Entity>();

                    for (Object tag : entitiesList) {
                        if (tag instanceof CompoundTag) {                                          
                            entities.add(getEntity((CompoundTag) tag));
                        }
                    }
                }

                // Load TileEntities
                List<?> tileentitiesList = getChildTag(schematic, "TileEntities", ListTag.class, List.class);

                if (tileentitiesList != null) {
                    tileentities = new ArrayList<TileEntity>();

                    for (Object entityElement : tileentitiesList) {
                        if (entityElement instanceof CompoundTag) {
                            Map<String, Tag> tileentity = ((CompoundTag) entityElement).getValue();

                            Byte rot = getChildTag(tileentity, "Rot", ByteTag.class, Byte.class);
                            Byte skulltype = getChildTag(tileentity, "SkullType", ByteTag.class, Byte.class);
                            Byte note = getChildTag(tileentity, "note", ByteTag.class, Byte.class);

                            Integer x = getChildTag(tileentity, "x", IntTag.class, Integer.class);
                            Integer y = getChildTag(tileentity, "y", IntTag.class, Integer.class);
                            Integer z = getChildTag(tileentity, "z", IntTag.class, Integer.class);
                            Integer record = getChildTag(tileentity, "Record", IntTag.class, Integer.class);
                            Integer outputsignal = getChildTag(tileentity, "OutputSignal", IntTag.class, Integer.class);
                            Integer transfercooldown = getChildTag(tileentity, "TransferCooldown", IntTag.class, Integer.class);
                            Integer levels = getChildTag(tileentity, "Levels", IntTag.class, Integer.class);
                            Integer primary = getChildTag(tileentity, "Primary", IntTag.class, Integer.class);
                            Integer secondary = getChildTag(tileentity, "Secondary", IntTag.class, Integer.class);

                            RecordItem recorditem = null;

                            Short delay = getChildTag(tileentity, "Delay", ShortTag.class, Short.class);
                            Short maxnearbyentities = getChildTag(tileentity, "MaxNearbyEntities", ShortTag.class, Short.class);
                            Short maxspawndelay = getChildTag(tileentity, "MaxSpawnDelay", ShortTag.class, Short.class);
                            Short minspawndelay = getChildTag(tileentity, "MinSpawnDelay", ShortTag.class, Short.class);
                            Short requiredplayerrange = getChildTag(tileentity, "RequiredPlayerRange", ShortTag.class, Short.class);
                            Short spawncount = getChildTag(tileentity, "SpawnCount", ShortTag.class, Short.class);
                            Short spawnrange = getChildTag(tileentity, "SpawnRange", ShortTag.class, Short.class);
                            Short burntime = getChildTag(tileentity, "BurnTime", ShortTag.class, Short.class);
                            Short cooktime = getChildTag(tileentity, "CookTime", ShortTag.class, Short.class);
                            Short brewtime = getChildTag(tileentity, "BrewTime", ShortTag.class, Short.class);

                            String entityid = getChildTag(tileentity, "EntityId", StringTag.class, String.class);
                            String customname = getChildTag(tileentity, "CustomName", StringTag.class, String.class);
                            String id = getChildTag(tileentity, "id", StringTag.class, String.class);
                            String text1 = getChildTag(tileentity, "Text1", StringTag.class, String.class);
                            String text2 = getChildTag(tileentity, "Text2", StringTag.class, String.class);
                            String text3 = getChildTag(tileentity, "Text3", StringTag.class, String.class);
                            String text4 = getChildTag(tileentity, "Text4", StringTag.class, String.class);
                            String command = getChildTag(tileentity, "Command", StringTag.class, String.class);

                            List<Item> items = getItems(tileentity);
                            List<Pattern> patterns = getPatterns(tileentity);

                            if (tileentity.containsKey("RecordItem")) {
                                Map<String, Tag> recorditemtag = getChildTag(tileentity, "RecordItem", CompoundTag.class).getValue();
                                Byte count = getChildTag(recorditemtag, "Count", ByteTag.class, Byte.class);
                                Short damage = getChildTag(recorditemtag, "Damage", ShortTag.class, Short.class);
                                Short recorditemid = getChildTag(recorditemtag, "id", ShortTag.class, Short.class);
                                recorditem = new RecordItem(count, damage, recorditemid);
                            }

                            tileentities.add(new TileEntity(x, y, z, customname, id, items, rot, skulltype, delay, maxnearbyentities, 
                                    maxspawndelay, minspawndelay, requiredplayerrange, spawncount, spawnrange, entityid, burntime, cooktime, 
                                    text1, text2, text3, text4, note, record, recorditem, brewtime, command, outputsignal,
                                    transfercooldown, levels, primary, secondary, patterns));
                        }
                    }
                }

                schem = new Schematic(blocks, blockData, blockBiomes, materials, width, length, height, entities, tileentities, roomauthor, checksum, originx, originy, originz, size);

                saveCompiledSchematic(schem, file.getName());
            }
        }
        
        return schem;
    }
        
    @SuppressWarnings("deprecation")
    @Override
    protected void pasteSchematicEntities(Location loc, Schematic schematic) {
        
        World world = loc.getWorld();
        
        List<Entity> entities = schematic.getEntities();
        List<TileEntity> tileentities = schematic.getTileEntities();
        Integer originX = schematic.getOriginX();
        Integer originY = schematic.getOriginY();
        Integer originZ = schematic.getOriginZ();
        
        if (originX == null) originX = 0;
        if (originY == null) originY = 0;
        if (originZ == null) originZ = 0;

        try {
            for (Entity e : entities) {
                createEntity(e, loc, originX, originY, originZ);
            }
        } catch (Exception e) {
            //plugin.getLogger().warning("err:" + e.getMessage());
            e.printStackTrace();
        }

        
        for (TileEntity te : tileentities) {

            Block block = world.getBlockAt(te.getX() + loc.getBlockX(), te.getY() + loc.getBlockY(), te.getZ() + loc.getBlockZ());
            List<Item> items = te.getItems();

            // Commented are unused
            
            // Short maxnearbyentities = te.getMaxNearbyEntities();
            // Short maxspawndelay = te.getMaxSpawnDelay();
            // Short minspawndelay = te.getMinSpawnDelay();
            // Short requiredplayerrange = te.getRequiredPlayerRange();
            // Short spawncount = te.getSpawnCount();
            // Short spawnrange = te.getSpawnRange();
            // String customname = te.getCustomName();
            
            // Comparator
            // Integer outputsignal = te.getOutputSignal();
            
            // Hopper
            // Integer transfercooldown = te.getTransferCooldown();
            
            // Beacon
            //Integer levels = te.getLevels();
            //Integer primary = te.getPrimary();
            //Integer secondary = te.getSecondary();

            BlockState bs = block.getState();

            if (bs instanceof Skull) {
                Skull skull = (Skull) bs;

                BlockFace bf = BlockFace.NORTH;
                Byte rot = te.getRot();
                if (rot == 0) bf = BlockFace.NORTH;
                else if (rot == 1) bf = BlockFace.NORTH_NORTH_EAST;
                else if (rot == 2) bf = BlockFace.NORTH_EAST;
                else if (rot == 3) bf = BlockFace.EAST_NORTH_EAST;
                else if (rot == 4) bf = BlockFace.EAST;
                else if (rot == 5) bf = BlockFace.EAST_SOUTH_EAST;
                else if (rot == 6) bf = BlockFace.SOUTH_EAST;
                else if (rot == 7) bf = BlockFace.SOUTH_SOUTH_EAST;
                else if (rot == 8) bf = BlockFace.SOUTH;
                else if (rot == 9) bf = BlockFace.SOUTH_SOUTH_WEST;
                else if (rot == 10) bf = BlockFace.SOUTH_WEST;
                else if (rot == 11) bf = BlockFace.WEST_SOUTH_WEST;
                else if (rot == 12) bf = BlockFace.WEST;
                else if (rot == 13) bf = BlockFace.WEST_NORTH_WEST;
                else if (rot == 14) bf = BlockFace.NORTH_WEST;
                else if (rot == 15) bf = BlockFace.NORTH_NORTH_WEST;

                skull.setSkullType(SkullType.values()[te.getSkullType()]);
                skull.setRotation(bf);
                skull.update(true, false);
            } else if (bs instanceof CreatureSpawner) {
                CreatureSpawner spawner = (CreatureSpawner) bs;
                spawner.setCreatureTypeByName(te.getEntityId());
                spawner.setDelay(te.getDelay());
                spawner.update(true, false);
            } else if (bs instanceof Furnace) {
                Furnace furnace = (Furnace) bs;
                furnace.setBurnTime(te.getBurnTime());
                furnace.setCookTime(te.getCookTime());
                furnace.update(true, false);
            } else if (bs instanceof Sign) {
                Sign sign = (Sign) bs;
                sign.setLine(0, te.getText1());
                sign.setLine(1, te.getText2());
                sign.setLine(2, te.getText3());
                sign.setLine(3, te.getText4());
                sign.update(true, false);
            } else if (bs instanceof CommandBlock) {
                CommandBlock cb = (CommandBlock) bs;
                cb.setCommand(te.getCommand());
                cb.update(true, false);
            } else if (bs instanceof BrewingStand) {
                BrewingStand brew = (BrewingStand) bs;
                brew.setBrewingTime(te.getBrewTime());
                brew.update(true, false);
            } else if (bs instanceof Jukebox) {
                Jukebox jb = (Jukebox) bs;
                jb.setPlaying(Material.getMaterial(te.getRecord()));
                jb.update(true, false);
            } else if (bs instanceof NoteBlock) {
                NoteBlock nb = (NoteBlock) bs;
                nb.setRawNote(te.getNote());
                nb.update(true, false);
            } else if (bs instanceof Banner) {
                Banner banner = (Banner) bs;
                for (Pattern pattern : te.getPatterns()) {
                    DyeColor dc = DyeColor.getByDyeData((pattern.getColor()).byteValue());
                    PatternType pt = PatternType.getByIdentifier(pattern.getPattern());
                    org.bukkit.block.banner.Pattern pat = new org.bukkit.block.banner.Pattern(dc, pt);
                    banner.addPattern(pat);
                }
            }

            if (bs instanceof InventoryHolder && items != null && items.size() > 0) {
                
                InventoryHolder ih = (InventoryHolder) bs;
                Inventory inventory = ih.getInventory();

                for (Item item : items) {

                    ItemStack is = getItemStack(item);

                    inventory.setItem(item.getSlot(), is);
                }
            }
        }
    }

    @Override
    protected org.bukkit.entity.Entity createEntity(Entity e, Location loc, int originX, int originY, int originZ) {
        @SuppressWarnings("deprecation")
        EntityType entitytype = EntityType.fromName(e.getId());
        World world = loc.getWorld();

        org.bukkit.entity.Entity ent = null;
        
        if (entitytype != null && e.getPos() != null && e.getPos().size() == 3) {
            List<Double> positions = e.getPos();
            
            double x = positions.get(0) - originX;
            double y = positions.get(1) - originY;
            double z = positions.get(2) - originZ;
            
            //Set properties, unused are commented out
            
            //Byte dir = e.getDir();
            //Byte direction = e.getDirection();
            //Byte invulnerable = e.getInvulnerable();
            //Byte onground = e.getOnGround();
            Byte canpickuploot = e.getCanPickupLoot();
            Byte color = e.getColor();
            Byte customnamevisible = e.getCustomNameVisible();
            //Byte leashed = e.getLeashed();
            Byte persistencerequired = e.getPersistenceRequired();
            Byte sheared = e.getSheared();
            Byte skeletontype = e.getSkeletonType();
            Byte isbaby = e.getIsBaby();
            Byte itemrotation = e.getItemRotation();
            Byte agelocked = e.getAgeLocked();
            Byte invisible = e.getInvisible();
            Byte nobaseplate = e.getNoBasePlate();
            Byte nogravity = e.getNoGravity();
            Byte showarms = e.getShowArms();
            //Byte silent = e.getSilent();
            Byte small = e.getSmall();
            Byte elder = e.getElder();
            
            //Double pushx = e.getPushX();
            //Double pushz = e.getPushZ();
            
            Entity riding = e.getRiding();
            
            Float falldistance = e.getFallDistance();
            //Float absorptionamount = e.getAbsorptionAmount();
            Float healf = e.getHealF();
            //Float itemdropchance = e.getItemDropChance();
            
            //Integer dimension = e.getDimension();
            //Integer portalcooldown = e.getPortalCooldown();
            //Integer tilex = e.getTileX();
            //Integer tiley = e.getTileY();
            //Integer tilez = e.getTileZ();
            Integer age = e.getAge();
            //Integer inlove = e.getInLove();
            //Integer transfercooldown = e.getTransferCooldown();
            //Integer tntfuse = e.getTNTFuse();
            //Integer forcedage = e.getForcedAge();
            Integer hurtbytimestamp = e.getHurtByTimestamp();
            //Integer morecarrotsticks = e.getMoreCarrotSticks();
            Integer rabbittype = e.getRabbitType();
            //Integer disabledslots = e.getDisabledSlots();
            
            Item item = e.getItem();
            
            Leash leash = e.getLeash();
            
            Pose pose = e.getPose();
            
            Short air = e.getAir();
            Short fire = e.getFire();
            //Short attacktime = e.getAttackTime();
            //Short deathtime = e.getDeathTime();
            //Short health = e.getHealth();
            //Short hurttime = e.getHurtTime();
            //Short fuel = e.getFuel()
            
            //String id = e.getId();
            //String motive = e.getMotive();
            String customname = e.getCustomName();
            
            List<Double> motion = e.getMotion();
            //List<Float> rotation = e.getRotation();
            //List<Attribute> attributes = e.getAttributes();
            //List<Float> dropchances = e.getDropChances();
            List<Equipment> equipments = e.getEquipments();
            List<Item> items = e.getItems();
            
            Location etloc = new Location(world, x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ());
            
            if (entitytype == EntityType.ITEM_FRAME) {
                etloc.setX(Math.floor(etloc.getX()));
                etloc.setY(Math.floor(etloc.getY()));
                etloc.setZ(Math.floor(etloc.getZ()));
                ent = world.spawnEntity(etloc, entitytype);
            } else if (entitytype == EntityType.PAINTING) {
                etloc.setX(Math.floor(etloc.getX()));
                etloc.setY(Math.floor(etloc.getY()));
                etloc.setZ(Math.floor(etloc.getZ()));
                
                ent = world.spawnEntity(etloc, entitytype);
            } else if (entitytype == EntityType.LEASH_HITCH) {                        
                /*etloc.setX(Math.floor(etloc.getX()));
                etloc.setY(Math.floor(etloc.getY()));
                etloc.setZ(Math.floor(etloc.getZ()));
                
                ent = world.spawnEntity(etloc, entitytype);*/
                return null;
            } else if (entitytype == EntityType.DROPPED_ITEM) {
                if (item == null) {
                    return null;
                } else {
                    @SuppressWarnings("deprecation")
                    ItemStack is = new ItemStack(item.getId(), item.getCount());
                    ItemTag itemtag = item.getTag();

                    if (itemtag != null) {
                        setTag(is, itemtag);
                    }
                    
                    ent = world.dropItem(etloc, is);
                }
            } else {
                ent = world.spawnEntity(etloc, entitytype);
            }
            
            
            if (riding != null)             ent.setPassenger(createEntity(riding, loc, originX, originY, originZ));
            if (falldistance != null)       ent.setFallDistance(falldistance);
            if (fire != null)               ent.setFireTicks(fire);
            if (age != null && age >= 1)    ent.setTicksLived(age);
            
            if (motion != null && motion.size() == 3) {
                Vector velocity = new Vector(motion.get(0), motion.get(1), motion.get(2));
                ent.setVelocity(velocity);
            }
            
            if (ent instanceof InventoryHolder) {
                InventoryHolder ih = (InventoryHolder) ent;
                
                Set<ItemStack> newitems = new HashSet<>();
                
                if (items != null && !items.isEmpty()) {
                    for (Item newitem : items) {
                        @SuppressWarnings("deprecation")
                        ItemStack is = new ItemStack(newitem.getId(), newitem.getCount());
                        ItemTag itemtag = newitem.getTag();

                        if (itemtag != null) {
                            setTag(is, itemtag);
                        }
                        
                        newitems.add(is);
                    }
                }
                
                ih.getInventory().setContents(newitems.toArray(new ItemStack[newitems.size()]));
            }
            
            if (ent instanceof ItemFrame) {
                ItemFrame itemframe = (ItemFrame) ent;
                itemframe.setRotation(Rotation.values()[itemrotation]);
                
                @SuppressWarnings("deprecation")
                ItemStack is = new ItemStack(item.getId(), item.getCount());
                ItemTag itemtag = item.getTag();

                if (itemtag != null) {
                    setTag(is, itemtag);
                }
                
                itemframe.setItem(is);
            }
            
            if (ent instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) ent;
                
                if (canpickuploot != null)      livingentity.setCanPickupItems(canpickuploot != 0);
                if (customname != null)         livingentity.setCustomName(customname);
                if (customnamevisible != null)  livingentity.setCustomNameVisible(customnamevisible != 0);
                if (healf != null)             livingentity.setHealth(healf);
                if (air != null)                livingentity.setRemainingAir(air);
                if (persistencerequired != null) livingentity.setRemoveWhenFarAway(persistencerequired == 0);
                if (leash != null) {
                    org.bukkit.entity.Entity leashentity = getLeash(leash, loc, originX, originY, originZ);
                    if (leashentity != null) {
                        livingentity.setLeashHolder(leashentity);
                    }
                }
                if (hurtbytimestamp != null) livingentity.setNoDamageTicks(hurtbytimestamp);

                EntityEquipment entityequipment = livingentity.getEquipment();
                Set<ItemStack> newitems = new HashSet<>();
                
                if (equipments != null && !equipments.isEmpty()) {
                    for(Equipment equipitem : equipments) {
                        if (equipitem != null && equipitem.getId() != null && equipitem.getCount() != null) {
                            @SuppressWarnings("deprecation")
                            ItemStack is = new ItemStack(equipitem.getId(), equipitem.getCount());
                            ItemTag itemtag = equipitem.getTag();
    
                            if (itemtag != null) {
                                setTag(is, itemtag);
                            }
                            
                            newitems.add(is);
                        }
                    }
                }

                entityequipment.setArmorContents(newitems.toArray(new ItemStack[5]));
                
                if (livingentity instanceof Ageable) {
                    Ageable ageable = (Ageable) livingentity;
                    if (age != null)        ageable.setAge(age);
                    if (agelocked != null)  ageable.setAgeLock(agelocked != 0);
                    if (isbaby != null) {
                        if (isbaby == 0)
                            ageable.setBaby();
                        else
                            ageable.setAdult();
                    }
                }        

                if (livingentity instanceof Skeleton && skeletontype != null) {
                    Skeleton skeleton = (Skeleton) livingentity;
                    @SuppressWarnings("deprecation")
                    SkeletonType st = SkeletonType.getType(skeletontype);
                    skeleton.setSkeletonType(st);
                } else if (livingentity instanceof Rabbit && rabbittype != null) {
                    Rabbit rabbit = (Rabbit) livingentity;
                    
                    switch (rabbittype) {
                    case 0:
                        rabbit.setRabbitType(Type.BROWN);
                        break;
                    case 1:
                        rabbit.setRabbitType(Type.WHITE);
                        break;
                    case 2:
                        rabbit.setRabbitType(Type.BLACK);
                        break;
                    case 3:
                        rabbit.setRabbitType(Type.BLACK_AND_WHITE);
                        break;
                    case 4:
                        rabbit.setRabbitType(Type.GOLD);
                        break;
                    case 5:
                        rabbit.setRabbitType(Type.SALT_AND_PEPPER);
                        break;
                    case 99:
                        rabbit.setRabbitType(Type.THE_KILLER_BUNNY);
                        break;
                    }
                } else if (livingentity instanceof ArmorStand) {
                    ArmorStand armorstand = (ArmorStand) livingentity;
                    if (showarms != null) armorstand.setArms(showarms != 0);
                    if (nobaseplate != null) armorstand.setBasePlate(nobaseplate == 0);
                    if (invisible != null) armorstand.setVisible(invisible == 0);
                    if (nogravity != null) armorstand.setGravity(nogravity == 0);
                    if (small != null) armorstand.setSmall(small != 0);
                    
                    if (pose != null) {
                        List<Float> body = pose.getBody();
                        List<Float> head = pose.getHead();
                        List<Float> leftarm = pose.getLeftArm();
                        List<Float> rightarm = pose.getRightArm();
                        List<Float> leftleg = pose.getLeftLeg();
                        List<Float> rightleg = pose.getRightLeg();
                        
                        if (body != null && body.size() == 3) {
                            armorstand.setBodyPose(new EulerAngle(body.get(0), body.get(1), body.get(2)));
                        }
                        if (head != null && head.size() == 3) {
                            armorstand.setHeadPose(new EulerAngle(head.get(0), head.get(1), head.get(2)));
                        }
                        if (leftarm != null && leftarm.size() == 3) {
                            armorstand.setLeftArmPose(new EulerAngle(leftarm.get(0), leftarm.get(1), leftarm.get(2)));
                        }
                        if (rightarm != null && rightarm.size() == 3) {
                            armorstand.setRightArmPose(new EulerAngle(rightarm.get(0), rightarm.get(1), rightarm.get(2)));
                        }
                        if (leftleg != null && leftleg.size() == 3) {
                            armorstand.setLeftLegPose(new EulerAngle(leftleg.get(0), leftleg.get(1), leftleg.get(2)));
                        }
                        if (rightleg != null && rightleg.size() == 3) {
                            armorstand.setRightLegPose(new EulerAngle(rightleg.get(0), rightleg.get(1), rightleg.get(2)));
                        }
                    }
                } else if (livingentity instanceof Guardian) {
                    Guardian guardian = (Guardian) livingentity;
                    if (elder != null) guardian.setElder(elder != 0);
                } else if (livingentity instanceof Sheep) {
                    Sheep sheep = (Sheep) livingentity;
                    if (sheared != null) sheep.setSheared(sheared != 0);
                    if (color != null) {
                        @SuppressWarnings("deprecation")
                        DyeColor dyecolor = DyeColor.getByWoolData(color);
                        if (dyecolor != null) sheep.setColor(dyecolor);
                    }
                }
            }
        }
        
        if (ent == null) {
            plugin.getLogger().info("null entity");
        }
        
        return ent;
    }
    
    @Override
    protected Entity getEntity(CompoundTag tag) {
        Map<String, Tag> entity = tag.getValue();
        
        Byte dir = getChildTag(entity, "Dir", ByteTag.class, Byte.class);
        Byte direction = getChildTag(entity, "Direction", ByteTag.class, Byte.class);
        Byte invulnerable = getChildTag(entity, "Invulnerable", ByteTag.class, Byte.class);
        Byte onground = getChildTag(entity, "OnGround", ByteTag.class, Byte.class);
        Byte canpickuploot = getChildTag(entity, "CanPickUpLoot", ByteTag.class, Byte.class);
        Byte color = getChildTag(entity, "Color", ByteTag.class, Byte.class);
        Byte customnamevisible = getChildTag(entity, "CustomNameVisible", ByteTag.class, Byte.class);
        Byte leashed = getChildTag(entity, "Leashed", ByteTag.class, Byte.class);
        Byte persistencerequired = getChildTag(entity, "PersistenceRequired", ByteTag.class, Byte.class);
        Byte sheared = getChildTag(entity, "Sheared", ByteTag.class, Byte.class);
        Byte skeletontype = getChildTag(entity, "SkeletonType", ByteTag.class, Byte.class);
        Byte isbaby = getChildTag(entity, "IsBaby", ByteTag.class, Byte.class);
        Byte itemrotation = getChildTag(entity, "ItemRotation", ByteTag.class, Byte.class);
        Byte agelocked = getChildTag(entity, "AgeLocked", ByteTag.class, Byte.class);
        Byte invisible = getChildTag(entity, "Invisible", ByteTag.class, Byte.class);
        Byte nobaseplate = getChildTag(entity, "NoBasePlate", ByteTag.class, Byte.class);
        Byte nogravity = getChildTag(entity, "NoGravity", ByteTag.class, Byte.class);
        Byte showarms = getChildTag(entity, "ShowArms", ByteTag.class, Byte.class);
        Byte silent = getChildTag(entity, "Silent", ByteTag.class, Byte.class);
        Byte small = getChildTag(entity, "Small", ByteTag.class, Byte.class);
        Byte elder = getChildTag(entity, "Elder", ByteTag.class, Byte.class);
        
        Double pushx = getChildTag(entity, "PushX", DoubleTag.class, Double.class);
        Double pushz = getChildTag(entity, "PushZ", DoubleTag.class, Double.class);
        
        Entity riding = null;
        
        Float falldistance = getChildTag(entity, "FallDistance", FloatTag.class, Float.class);
        Float absorptionamount = getChildTag(entity, "AbsorptionAmount", FloatTag.class, Float.class);
        Float healf = getChildTag(entity, "HealF", FloatTag.class, Float.class);
        Float itemdropchance = getChildTag(entity, "ItemDropChance", FloatTag.class, Float.class);
        
        Integer dimension = getChildTag(entity, "Dimension", IntTag.class, Integer.class);
        Integer portalcooldown = getChildTag(entity, "PortalCooldown", IntTag.class, Integer.class);
        Integer tilex = getChildTag(entity, "TileX", IntTag.class, Integer.class);
        Integer tiley = getChildTag(entity, "TileY", IntTag.class, Integer.class);
        Integer tilez = getChildTag(entity, "TileZ", IntTag.class, Integer.class);
        Integer age = null; //Handled lower
        Integer inlove = getChildTag(entity, "InLove", IntTag.class, Integer.class);
        Integer transfercooldown = getChildTag(entity, "TransferCooldown", IntTag.class, Integer.class);
        Integer tntfuse = getChildTag(entity, "TNTFuse", IntTag.class, Integer.class);
        Integer forcedage = getChildTag(entity, "ForcedAge", IntTag.class, Integer.class);
        Integer hurtbytimestamp = getChildTag(entity, "HurtByTimestamp", IntTag.class, Integer.class);
        Integer morecarrotsticks = getChildTag(entity, "MoreCarrotTicks", IntTag.class, Integer.class);
        Integer rabbittype = getChildTag(entity, "RabbitType", IntTag.class, Integer.class);
        Integer disabledslots = getChildTag(entity, "DisabledSlots", IntTag.class, Integer.class);
        
        Item item = null;
        
        Leash leash = null;
        
        Pose pose = null;
        
        Short air = getChildTag(entity, "Air", ShortTag.class, Short.class);
        Short fire = getChildTag(entity, "Fire", ShortTag.class, Short.class);
        Short attacktime = getChildTag(entity, "AttachTime", ShortTag.class, Short.class);
        Short deathtime = getChildTag(entity, "DeathTime", ShortTag.class, Short.class);
        Short health = getChildTag(entity, "Health", ShortTag.class, Short.class);
        Short hurttime = getChildTag(entity, "HurtTime", ShortTag.class, Short.class);
        Short fuel = getChildTag(entity, "Fuel", ShortTag.class, Short.class);
        
        String id = getChildTag(entity, "id", StringTag.class, String.class);
        String motive = getChildTag(entity, "Motive", StringTag.class, String.class);
        String customname = getChildTag(entity, "CustomName", StringTag.class, String.class);
        
        List<Double> motion = convert(getChildTag(entity, "Motion", ListTag.class, List.class), Double.class);
        List<Double> pos = convert(getChildTag(entity, "Pos", ListTag.class, List.class), Double.class);
        List<Float> rotation = convert(getChildTag(entity, "Rotation", ListTag.class, List.class), Float.class);
        List<Attribute> attributes = getAttributes(entity);
        List<Float> dropchances = convert(getChildTag(entity, "DropChances", ListTag.class, List.class), Float.class);
        List<Equipment> equipments = getEquipment(entity);
        List<Item> items = getItems(entity);

        try {
            age = getChildTag(entity, "Age", IntTag.class, Integer.class);
        }catch(IllegalArgumentException e) {
            Short shortAge = getChildTag(entity, "Age", ShortTag.class, Short.class);
            
            if (shortAge != null)
            age = shortAge.intValue();
        }

        CompoundTag itemtag = getChildTag(entity, "Item", CompoundTag.class);
        if (itemtag != null) {
            item = getItem(itemtag);
        }
        
        if (entity.containsKey("Riding")) {
            riding = getEntity(getChildTag(entity, "Riding", CompoundTag.class));
        }

        if (entity.containsKey("Leash")) {
            leash = getLeash(getChildTag(entity, "Leash", CompoundTag.class));
        }
        
        if (entity.containsKey("Pose")) {
            pose = getPose(getChildTag(entity, "Pose", CompoundTag.class));
        }
                                  
        return new Entity(dir, direction, invulnerable, onground, air, fire, dimension, portalcooldown, tilex, tiley, tilez, falldistance, id, motive, motion, pos, rotation,
                canpickuploot, color, customnamevisible, leashed, persistencerequired, sheared, attacktime, deathtime, health, hurttime, age, inlove, absorptionamount,
                healf, customname, attributes, dropchances, equipments, skeletontype, riding, leash, item, isbaby, items, transfercooldown, fuel, pushx, pushz, tntfuse,
                itemrotation, itemdropchance, agelocked, invisible, nobaseplate, nogravity, showarms, silent, small, elder, forcedage, hurtbytimestamp,
                morecarrotsticks, rabbittype, disabledslots, pose);
    }
    
    protected Pose getPose(CompoundTag poseelement) {
        Map<String, Tag> pose = ((CompoundTag) poseelement).getValue();
        List<Float> body = convert(getChildTag(pose, "body", ListTag.class, List.class), Float.class);
        List<Float> head = convert(getChildTag(pose, "body", ListTag.class, List.class), Float.class);
        List<Float> leftarm = convert(getChildTag(pose, "body", ListTag.class, List.class), Float.class);
        List<Float> rightarm = convert(getChildTag(pose, "body", ListTag.class, List.class), Float.class);
        List<Float> leftleg = convert(getChildTag(pose, "body", ListTag.class, List.class), Float.class);
        List<Float> rightleg = convert(getChildTag(pose, "body", ListTag.class, List.class), Float.class);
        
        return new Pose(body, head, leftarm, rightarm, leftleg, rightleg);
    }
    
    protected List<Pattern> getPatterns(Map<String, Tag> entity) {
        List<?> patternsList = getChildTag(entity, "Patterns", ListTag.class, List.class);
        
        if (patternsList != null) {
            List<Pattern> patterns = new ArrayList<Pattern>();
            
            for (Object patternElement : patternsList) {
                if (patternElement instanceof CompoundTag) {
                    patterns.add(getPattern((CompoundTag) patternElement));
                }
            }
            
            return patterns;
        } else {
            return null;
        }
    }
    
    protected Pattern getPattern(CompoundTag patternElement) {
        Map<String, Tag> patternmap = patternElement.getValue();
        Integer color = getChildTag(patternmap, "Color", IntTag.class, Integer.class);
        String pattern = getChildTag(patternmap, "Pattern", StringTag.class, String.class);
        
        return new Pattern(color, pattern);
    }
}
