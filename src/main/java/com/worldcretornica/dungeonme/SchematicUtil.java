package com.worldcretornica.dungeonme;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Furnace;
import org.bukkit.block.Jukebox;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.*;
import com.google.common.io.Files;
import com.worldcretornica.dungeonme.jnbt.*;
import com.worldcretornica.dungeonme.schematic.*;

public class SchematicUtil {

    private DungeonMe plugin;
    
    public SchematicUtil(DungeonMe instance) {
        plugin = instance;
    }
    
    private List<Schematic> loadedschematics;

    public Schematic getSchematic(int id) {
        return loadedschematics.get(id);
    }

    public void loadSchematics() {
        plugin.getLogger().info("Loading room schematics...");

        loadedschematics = new ArrayList<>();
        
        for (final File fileEntry : new File(plugin.getDataFolder().getPath() + "/Rooms").listFiles()) {
            if (!fileEntry.isDirectory()) {
                try {
                    Schematic schem = loadSchematic(fileEntry);
                    
                    if (schem != null) {
                        loadedschematics.add(schem);
                    }
                } catch (IllegalArgumentException | IOException e) {
                    plugin.getLogger().severe("Error loading file " + fileEntry.getName());
                    e.printStackTrace();
                }
            }
        }

        plugin.getLogger().info("" + loadedschematics.size() + " room schematics loaded.");
    }
    
    public void pasteSchematic(Location loc, int id) {
        pasteSchematic(loc, getSchematic(id));
    }
    
    @SuppressWarnings("deprecation")
    public void pasteSchematic(Location loc, Schematic schematic) {
        World world = loc.getWorld();
        int[] blocks = schematic.getBlocks();
        byte[] blockData = schematic.getData();

        Short length = schematic.getLength();
        Short width = schematic.getWidth();
        Short height = schematic.getHeight();
        List<Entity> entities = schematic.getEntities();
        List<TileEntity> tileentities = schematic.getTileEntities();
        Integer originX = schematic.getOriginX();
        Integer originY = schematic.getOriginY();
        Integer originZ = schematic.getOriginZ();
        
        if (originX == null) originX = 0;
        if (originY == null) originY = 0;
        if (originZ == null) originZ = 0;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    Block block = new Location(world, x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ()).getBlock();
                    
                    try {
                        block.setTypeIdAndData(blocks[index], blockData[index], false);
                        block.setData(blockData[index], false);
                    } catch (NullPointerException e) {
                        plugin.getLogger().info("Error pasting block : " + blocks[index] + " of data " + blockData[index]);
                    }
                }
            }
        }
        
        try {
            for (Entity e : entities) {
                createEntity(e, loc, originX, originY, originZ);
            }
        } catch (Exception e) {
            //plugin.getLogger().warning("err:" + e.getMessage());
            e.printStackTrace();
        }

        
        for (TileEntity te : tileentities) {

            Location teloc = new Location(world, te.getX() + loc.getBlockX(), te.getY() + loc.getBlockY(), te.getZ() + loc.getBlockZ());

            Block block = teloc.getBlock();
            List<Item> items = te.getItems();

            // Short maxnearbyentities = te.getMaxNearbyEntities();
            // Short maxspawndelay = te.getMaxSpawnDelay();
            // Short minspawndelay = te.getMinSpawnDelay();
            // Short requiredplayerrange = te.getRequiredPlayerRange();
            // Short spawncount = te.getSpawnCount();
            // Short spawnrange = te.getSpawnRange();
            // String customname = te.getCustomName();

            BlockState bs = block.getState();

            if (bs instanceof Skull) {
                Skull skull = (Skull) bs;

                BlockFace bf = null;
                switch (te.getRot()) {
                case 0:
                    bf = BlockFace.NORTH;
                    break;
                case 1:
                    bf = BlockFace.NORTH_NORTH_EAST;
                    break;
                case 2:
                    bf = BlockFace.NORTH_EAST;
                    break;
                case 3:
                    bf = BlockFace.EAST_NORTH_EAST;
                    break;
                case 4:
                    bf = BlockFace.EAST;
                    break;
                case 5:
                    bf = BlockFace.EAST_SOUTH_EAST;
                    break;
                case 6:
                    bf = BlockFace.SOUTH_EAST;
                    break;
                case 7:
                    bf = BlockFace.SOUTH_SOUTH_EAST;
                    break;
                case 8:
                    bf = BlockFace.SOUTH;
                    break;
                case 9:
                    bf = BlockFace.SOUTH_SOUTH_WEST;
                    break;
                case 10:
                    bf = BlockFace.SOUTH_WEST;
                    break;
                case 11:
                    bf = BlockFace.WEST_SOUTH_WEST;
                    break;
                case 12:
                    bf = BlockFace.WEST;
                    break;
                case 13:
                    bf = BlockFace.WEST_NORTH_WEST;
                    break;
                case 14:
                    bf = BlockFace.NORTH_WEST;
                    break;
                case 15:
                    bf = BlockFace.NORTH_NORTH_WEST;
                    break;
                }

                skull.setSkullType(SkullType.values()[te.getSkullType()]);
                skull.setRotation(bf);
                skull.update(true, false);
            }
            
            if (bs instanceof CreatureSpawner) {
                CreatureSpawner spawner = (CreatureSpawner) bs;
                spawner.setCreatureTypeByName(te.getEntityId());
                spawner.setDelay(te.getDelay());
                spawner.update(true, false);
            }
            
            if (bs instanceof Furnace) {
                Furnace furnace = (Furnace) bs;
                furnace.setBurnTime(te.getBurnTime());
                furnace.setCookTime(te.getCookTime());
                furnace.update(true, false);
            }

            if (bs instanceof Sign) {
                Sign sign = (Sign) bs;
                sign.setLine(0, te.getText1());
                sign.setLine(1, te.getText2());
                sign.setLine(2, te.getText3());
                sign.setLine(3, te.getText4());
                sign.update(true, false);
            }

            if (bs instanceof CommandBlock) {
                CommandBlock cb = (CommandBlock) bs;
                cb.setCommand(te.getCommand());
                cb.update(true, false);
            }

            if (bs instanceof BrewingStand) {
                BrewingStand brew = (BrewingStand) bs;
                brew.setBrewingTime(te.getBrewTime());
                brew.update(true, false);
            }

            if (bs instanceof Jukebox) {
                Jukebox jb = (Jukebox) bs;
                jb.setPlaying(Material.getMaterial(te.getRecord()));
                jb.update(true, false);
            }

            if (bs instanceof NoteBlock) {
                NoteBlock nb = (NoteBlock) bs;
                nb.setRawNote(te.getNote());
                nb.update(true, false);
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

    @SuppressWarnings("deprecation")
    private ItemStack getItemStack(Item item) {
        ItemStack is = new ItemStack(item.getId(), item.getCount());
        is = MinecraftReflection.getBukkitItemStack(is);
        ItemTag itemtag = item.getTag();

        if (itemtag != null) {
            setTag(is, itemtag);
        }
        
        return is;
    }

    private void setTag(ItemStack is, ItemTag itemtag) {
        List<Ench> enchants = itemtag.getEnchants();
        Integer repaircost = itemtag.getRepairCost();
        List<String> pages = itemtag.getPages();
        String author = itemtag.getAuthor();
        String title = itemtag.getTitle();
        NbtCompound compound = NbtFactory.ofCompound("null");

        // Display
        if (itemtag.getDisplay() != null) {

            Display display = itemtag.getDisplay();
            List<String> lores = display.getLore();
            String name = display.getName();

            NbtCompound displaycompound = NbtFactory.ofCompound("display");

            if (name != null) {
                NbtBase<String> namecompound = NbtFactory.of("Name", name);
                displaycompound.put(namecompound);
            }

            if (lores != null) {
                displaycompound.put("Lore", NbtFactory.ofList("Lore", lores));
            }

            compound.put(displaycompound);
        }

        // Enchants
        if (enchants != null) {
            @SuppressWarnings("unchecked")
            NbtList<Map<String, NbtBase<?>>> nbtL = NbtFactory.ofList("ench");

            for (Ench enchant : enchants) {
                NbtCompound enchantcompound = NbtFactory.ofCompound("");
                enchantcompound.put("id", enchant.getId());
                enchantcompound.put("lvl", enchant.getLvl());
                nbtL.add(enchantcompound);
            }

            compound.put("ench", nbtL);
        }

        // Pages
        if (pages != null) {
            compound.put("pages", NbtFactory.ofList("pages", pages));
        }

        // RepairCost
        if (repaircost != null) {
            compound.put(NbtFactory.of("RepairCost", repaircost));
        }

        // Author
        if (author != null) {
            compound.put(NbtFactory.of("author", author));
        }

        // Title
        if (title != null) {
            compound.put(NbtFactory.of("title", title));
        }

        NbtFactory.setItemTag(is, compound);
    }

    @SuppressWarnings("deprecation")
    private org.bukkit.entity.Entity createEntity(Entity e, Location loc, int originX, int originY, int originZ) {
        EntityType entitytype = EntityType.fromName(e.getId());
        World world = loc.getWorld();

        org.bukkit.entity.Entity ent = null;
        
        if (entitytype != null && e.getPos() != null && e.getPos().size() == 3) {
            List<Double> positions = e.getPos();
            
            double x = positions.get(0) - originX;
            double y = positions.get(1) - originY;
            double z = positions.get(2) - originZ;
            
            //Set properties
            Byte dir = e.getDir();
            Byte direction = e.getDirection();
            Byte invulnerable = e.getInvulnerable();
            Byte onground = e.getOnGround();
            Short air = e.getAir();
            Short fire = e.getFire();
            Integer dimension = e.getDimension();
            Integer portalcooldown = e.getPortalCooldown();
            Integer tilex = e.getTileX();
            Integer tiley = e.getTileY();
            Integer tilez = e.getTileZ();
            Float falldistance = e.getFallDistance();
            String id = e.getId();
            String motive = e.getMotive();
            List<Double> motion = e.getMotion();
            List<Float> rotation = e.getRotation();
            Byte canpickuploot = e.getCanPickupLoot();
            Byte color = e.getColor();
            Byte customnamevisible = e.getCustomNameVisible();
            //Byte leashed = e.getLeashed();
            Byte persistencerequired = e.getPersistenceRequired();
            Byte sheared = e.getSheared();
            Short attacktime = e.getAttackTime();
            Short deathtime = e.getDeathTime();
            Short health = e.getHealth();
            Short hurttime = e.getHurtTime();
            Integer age = e.getAge();
            Integer inlove = e.getInLove();
            Float absorptionamount = e.getAbsorptionAmount();
            Float healf = e.getHealF();
            String customname = e.getCustomName();
            List<Attribute> attributes = e.getAttributes();
            List<Float> dropchances = e.getDropChances();
            List<Equipment> equipments = e.getEquipments();
            Byte skeletontype = e.getSkeletonType();
            Entity riding = e.getRiding();
            Leash leash = e.getLeash();
            Item item = e.getItem();
            Byte isbaby = e.getIsBaby();
            
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
                    ItemStack is = new ItemStack(item.getId(), item.getCount());
                    is = MinecraftReflection.getBukkitItemStack(is);
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
            
            if (ent instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) ent;
                
                if (canpickuploot != null)      livingentity.setCanPickupItems(canpickuploot != 0);
                if (customname != null)         livingentity.setCustomName(customname);
                if (customnamevisible != null)  livingentity.setCustomNameVisible(customnamevisible != 0);
                if (health != null)             livingentity.setHealth(health);
                //TODO figure out setMaxHealth()
                if (air != null)                livingentity.setRemainingAir(air);
                if (persistencerequired != null) livingentity.setRemoveWhenFarAway(persistencerequired == 0);
                if (leash != null) {
                    org.bukkit.entity.Entity leashentity = getLeash(leash, loc, originX, originY, originZ);
                    if (leashentity != null) {
                        livingentity.setLeashHolder(leashentity);
                    }
                }

                EntityEquipment entityequipment = livingentity.getEquipment();
                Set<ItemStack> newitems = new HashSet<>();
                
                if (equipments != null && equipments.size() > 0) {
                    for(Equipment equipitem : equipments) {
                        if (equipitem != null && equipitem.getId() != null && equipitem.getCount() != null) {
                            ItemStack is = new ItemStack(equipitem.getId(), equipitem.getCount());
                            is = MinecraftReflection.getBukkitItemStack(is);
                            ItemTag itemtag = equipitem.getItemTag();
    
                            if (itemtag != null) {
                                setTag(is, itemtag);
                            }
                            
                            newitems.add(is);
                        }
                    }
                }
                
                entityequipment.setArmorContents(newitems.toArray(new ItemStack[5]));
               
                
                if (livingentity instanceof Skeleton && skeletontype != null) {
                    Skeleton skeleton = (Skeleton) livingentity;
                    SkeletonType st = SkeletonType.getType(skeletontype);
                    skeleton.setSkeletonType(st);
                }                
            }
        }
        
        if (ent == null) {
            plugin.getLogger().info("null entity");
        }
        
        return ent;
    }
    
    
    private org.bukkit.entity.Entity getLeash(Leash leash, Location loc, int originX, int originY, int originZ) {
        org.bukkit.entity.Entity ent = null;
        World world = loc.getWorld();
        
        int x = leash.getX() - originX;
        int y = leash.getY() - originY;
        int z = leash.getZ() - originZ;
        
        Location etloc = new Location(world, x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ());
        
        Block block = world.getBlockAt(etloc);
        
        if (block.getType() == Material.FENCE || block.getType() == Material.NETHER_FENCE) {
            etloc.setX(Math.floor(etloc.getX()));
            etloc.setY(Math.floor(etloc.getY()));
            etloc.setZ(Math.floor(etloc.getZ()));
            
            ent = world.spawnEntity(etloc, EntityType.LEASH_HITCH);
            
            List<org.bukkit.entity.Entity> nearbyentities = ent.getNearbyEntities(1, 1, 1);
            
            for(org.bukkit.entity.Entity nearby : nearbyentities) {
                if (nearby instanceof LeashHitch) {
                    if (nearby.getLocation().distance(ent.getLocation()) == 0) {
                        ent.remove();
                        return nearby;
                    }
                }
            }
        }
        
        return ent;
    }
    
    
    
    

    public Schematic loadCompiledSchematic(String file, long checksum) {
        Schematic schem = null;
        String filename = plugin.getDataFolder().getAbsolutePath() + "\\.Buff\\" + file + ".room";
        File f = new File(filename);

        if (f.exists()) {
            try (ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                schem = (Schematic) input.readObject();
            } catch (ClassNotFoundException ex) {
                plugin.getLogger().severe("Cannot perform input. Class not found.");
            } catch (IOException ex) {
                plugin.getLogger().severe("Cannot perform input.");
            }

            if (schem != null && schem.getChecksum() != checksum) {
                return null;
            }
        }
        
        return schem;
    }
    
    public void saveCompiledSchematic(Schematic schem, String file) {
        String filename = plugin.getDataFolder().getAbsolutePath() + "\\.Buff\\" + file + ".room";
        
        try (ObjectOutput output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            output.writeObject(schem);
        } catch (IOException ex) {
            plugin.getLogger().severe("Could not save buffer " + filename + ".");
            ex.printStackTrace();
        }
    }
    
    public Schematic loadSchematic(File file) throws IOException, IllegalArgumentException {
        
        long checksum = Files.getChecksum(file , new java.util.zip.CRC32());
        
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
                    int blockid = rawblocks[ctr];
                    
                    if(blockid < 0) 
                        blockid = 256 + blockid;
                    
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
                            Integer x = getChildTag(tileentity, "x", IntTag.class, Integer.class);
                            Integer y = getChildTag(tileentity, "y", IntTag.class, Integer.class);
                            Integer z = getChildTag(tileentity, "z", IntTag.class, Integer.class);
                            String customname = getChildTag(tileentity, "CustomName", StringTag.class, String.class);
                            String id = getChildTag(tileentity, "id", StringTag.class, String.class);
                            List<Item> items = getItems(tileentity);

                            Byte rot = getChildTag(tileentity, "Rot", ByteTag.class, Byte.class);
                            Byte skulltype = getChildTag(tileentity, "SkullType", ByteTag.class, Byte.class);
                            
                            Short delay = getChildTag(tileentity, "Delay", ShortTag.class, Short.class);
                            Short maxnearbyentities = getChildTag(tileentity, "MaxNearbyEntities", ShortTag.class, Short.class);
                            Short maxspawndelay = getChildTag(tileentity, "MaxSpawnDelay", ShortTag.class, Short.class);
                            Short minspawndelay = getChildTag(tileentity, "MinSpawnDelay", ShortTag.class, Short.class);
                            Short requiredplayerrange = getChildTag(tileentity, "RequiredPlayerRange", ShortTag.class, Short.class);
                            Short spawncount = getChildTag(tileentity, "SpawnCount", ShortTag.class, Short.class);
                            Short spawnrange = getChildTag(tileentity, "SpawnRange", ShortTag.class, Short.class);
                            String entityid = getChildTag(tileentity, "EntityId", StringTag.class, String.class);

                            Short burntime = getChildTag(tileentity, "BurnTime", ShortTag.class, Short.class);
                            Short cooktime = getChildTag(tileentity, "CookTime", ShortTag.class, Short.class);

                            String text1 = getChildTag(tileentity, "Text1", StringTag.class, String.class);
                            String text2 = getChildTag(tileentity, "Text2", StringTag.class, String.class);
                            String text3 = getChildTag(tileentity, "Text3", StringTag.class, String.class);
                            String text4 = getChildTag(tileentity, "Text4", StringTag.class, String.class);

                            Byte note = getChildTag(tileentity, "note", ByteTag.class, Byte.class);
                            Integer record = getChildTag(tileentity, "Record", IntTag.class, Integer.class);

                            RecordItem recorditem = null;
                            if (tileentity.containsKey("RecordItem")) {
                                Map<String, Tag> recorditemtag = getChildTag(tileentity, "RecordItem", CompoundTag.class).getValue();
                                Byte count = getChildTag(recorditemtag, "Count", ByteTag.class, Byte.class);
                                Short damage = getChildTag(recorditemtag, "Damage", ShortTag.class, Short.class);
                                Short recorditemid = getChildTag(recorditemtag, "id", ShortTag.class, Short.class);
                                recorditem = new RecordItem(count, damage, recorditemid);
                            }

                            Short brewtime = getChildTag(tileentity, "BrewTime", ShortTag.class, Short.class);
                            String command = getChildTag(tileentity, "Command", StringTag.class, String.class); 

                            tileentities.add(new TileEntity(x, y, z, customname, id, items, rot, skulltype, delay, maxnearbyentities, maxspawndelay, minspawndelay, requiredplayerrange, spawncount, spawnrange, entityid, burntime, cooktime, text1, text2, text3, text4, note, record, recorditem, brewtime, command));
                        }
                    }
                }

                schem = new Schematic(blocks, blockData, blockBiomes, materials, width, length, height, entities, tileentities, roomauthor, checksum, originx, originy, originz);

                saveCompiledSchematic(schem, file.getName());
            }
        }
        
        return schem;
    }
    
    public Entity getEntity(CompoundTag tag) {
        Map<String, Tag> entity = tag.getValue();
        Byte dir = getChildTag(entity, "Dir", ByteTag.class, Byte.class);
        Byte direction = getChildTag(entity, "Direction", ByteTag.class, Byte.class);
        Byte invulnerable = getChildTag(entity, "Invulnerable", ByteTag.class, Byte.class);
        Byte onground = getChildTag(entity, "OnGround", ByteTag.class, Byte.class);
        Short air = getChildTag(entity, "Air", ShortTag.class, Short.class);
        Short fire = getChildTag(entity, "Fire", ShortTag.class, Short.class);
        Integer dimension = getChildTag(entity, "Dimension", IntTag.class, Integer.class);
        Integer portalcooldown = getChildTag(entity, "PortalCooldown", IntTag.class, Integer.class);
        Integer tilex = getChildTag(entity, "TileX", IntTag.class, Integer.class);
        Integer tiley = getChildTag(entity, "TileY", IntTag.class, Integer.class);
        Integer tilez = getChildTag(entity, "TileZ", IntTag.class, Integer.class);
        Float falldistance = getChildTag(entity, "FallDistance", FloatTag.class, Float.class);
        String id = getChildTag(entity, "id", StringTag.class, String.class);
        String motive = getChildTag(entity, "Motive", StringTag.class, String.class);
        List<Double> motion = convert(getChildTag(entity, "Motion", ListTag.class, List.class), Double.class);
        List<Double> pos = convert(getChildTag(entity, "Pos", ListTag.class, List.class), Double.class);
        List<Float> rotation = convert(getChildTag(entity, "Rotation", ListTag.class, List.class), Float.class);

        Byte canpickuploot = getChildTag(entity, "CanPickUpLoot", ByteTag.class, Byte.class);
        Byte color = getChildTag(entity, "Color", ByteTag.class, Byte.class);
        Byte customnamevisible = getChildTag(entity, "CustomNameVisible", ByteTag.class, Byte.class);
        Byte leashed = getChildTag(entity, "Leashed", ByteTag.class, Byte.class);
        Byte persistencerequired = getChildTag(entity, "PersistenceRequired", ByteTag.class, Byte.class);
        Byte sheared = getChildTag(entity, "Sheared", ByteTag.class, Byte.class);
        Short attacktime = getChildTag(entity, "AttachTime", ShortTag.class, Short.class);
        Short deathtime = getChildTag(entity, "DeathTime", ShortTag.class, Short.class);
        Short health = getChildTag(entity, "Health", ShortTag.class, Short.class);
        Short hurttime = getChildTag(entity, "HurtTime", ShortTag.class, Short.class);
        Integer age = null;
        
        try {
            age = getChildTag(entity, "Age", IntTag.class, Integer.class);
        }catch(IllegalArgumentException e) {
            Short shortAge = getChildTag(entity, "Age", ShortTag.class, Short.class);
            
            if (shortAge != null)
            age = shortAge.intValue();
        }
        
        Integer inlove = getChildTag(entity, "InLove", IntTag.class, Integer.class);
        Float absorptionamount= getChildTag(entity, "AbsorptionAmount", FloatTag.class, Float.class);
        Float healf= getChildTag(entity, "HealF", FloatTag.class, Float.class);
        String customname = getChildTag(entity, "CustomName", StringTag.class, String.class);
        List<Attribute> attributes = getAttributes(entity);
        List<Float> dropchances = convert(getChildTag(entity, "DropChances", ListTag.class, List.class), Float.class);
        List<Equipment> equipments = getEquipment(entity);
        
        Byte skeletontype = getChildTag(entity, "SkeletonType", ByteTag.class, Byte.class);
        
        Item item = null;
        CompoundTag itemtag = getChildTag(entity, "Item", CompoundTag.class);
        if (itemtag != null) {
            item = getItem(itemtag);
        }
        
        Byte isbaby = getChildTag(entity, "IsBaby", ByteTag.class, Byte.class);
        
        Entity riding = null;
        
        if (entity.containsKey("Riding")) {
            riding = getEntity(getChildTag(entity, "Riding", CompoundTag.class));
        }
        
        Leash leash = null;
        
        if (entity.containsKey("Leash")) {
            leash = getLeash(getChildTag(entity, "Leash", CompoundTag.class));
        }
                                  
        return new Entity(dir, direction, invulnerable, onground, air, fire, dimension, portalcooldown, tilex, tiley, tilez, falldistance, id, motive, motion, pos, rotation,
                canpickuploot, color, customnamevisible, leashed, persistencerequired, sheared, attacktime, deathtime, health, hurttime, age, inlove, absorptionamount,
                healf, customname, attributes, dropchances, equipments, skeletontype, riding, leash, item, isbaby);
    }
    
    public Leash getLeash(CompoundTag leashelement) {
        Map<String, Tag> leash = ((CompoundTag) leashelement).getValue();
        Integer x = getChildTag(leash, "X", IntTag.class, Integer.class);
        Integer y = getChildTag(leash, "Y", IntTag.class, Integer.class);
        Integer z = getChildTag(leash, "Z", IntTag.class, Integer.class);
        
        return new Leash(x, y, z);
    }
    
    public Modifier getModifier(CompoundTag modifierelement) {
        Map<String, Tag> modifier = ((CompoundTag) modifierelement).getValue();
        Integer operation = getChildTag(modifier, "Operation", IntTag.class, Integer.class);
        Double amount = getChildTag(modifier, "Amount", DoubleTag.class, Double.class);
        String name = getChildTag(modifier, "Name", StringTag.class, String.class);
        
        return new Modifier(operation, amount, name);
    }
    
    public List<Modifier> getModifiers(Map<String, Tag> attribute) {
        List<?> modifierlist = getChildTag(attribute, "Modifiers", ListTag.class, List.class);

        if (modifierlist != null) {
            List<Modifier> modifiers = new ArrayList<Modifier>();

            for (Object modifierelement : modifierlist) {
                if (modifierelement instanceof CompoundTag) {                    
                    modifiers.add(getModifier((CompoundTag) modifierelement));
                }
            }

            return modifiers;
        } else {
            return null;
        }
    }
    
    public Item getItem(CompoundTag itemElement) {
        Map<String, Tag> item = itemElement.getValue();
        Byte count = getChildTag(item, "Count", ByteTag.class, Byte.class);
        Byte slot = getChildTag(item, "Slot", ByteTag.class, Byte.class);
        Short damage = getChildTag(item, "Damage", ShortTag.class, Short.class);
        Short itemid = getChildTag(item, "id", ShortTag.class, Short.class);

        ItemTag tag = getItemTag(item);
        
        return new Item(count, slot, damage, itemid, tag);
    }
    
    public List<Item> getItems(Map<String, Tag> tileentity) {
        List<?> itemsList = getChildTag(tileentity, "Items", ListTag.class, List.class);

        if (itemsList != null) {
            List<Item> items = new ArrayList<Item>();

            for (Object itemElement : itemsList) {
                if (itemElement instanceof CompoundTag) {
                    items.add(getItem((CompoundTag) itemElement));
                }
            }

            return items;
        } else {
            return null;
        }
    }
    
    public ItemTag getItemTag(Map<String, Tag> item) {
        CompoundTag itemtagElement = getChildTag(item, "tag", CompoundTag.class);

        if (itemtagElement != null) {
            Map<String, Tag> itemtag = itemtagElement.getValue();
            Integer repaircost = getChildTag(itemtag, "RepairCost", IntTag.class, Integer.class);
            String author = getChildTag(itemtag, "author", StringTag.class, String.class);
            String title = getChildTag(itemtag, "title", StringTag.class, String.class);
            List<String> pages = convert(getChildTag(itemtag, "pages", ListTag.class, List.class), String.class);
            Display display = getDisplay(itemtag);
            List<Ench> enchants = getEnchant(itemtag);

            return new ItemTag(repaircost, enchants, display, author, title, pages);
        } else {
            return null;
        }
    }
    
    public Display getDisplay(Map<String, Tag> itemtag) {
        CompoundTag displayElement = getChildTag(itemtag, "display", CompoundTag.class);

        if (displayElement != null) {
            Map<String, Tag> display = displayElement.getValue();
            String name = getChildTag(display, "Name", StringTag.class, String.class);
            List<String> lore = convert(getChildTag(display, "Lore", ListTag.class, List.class), String.class);

            return new Display(name, lore);
        } else {
            return null;
        }
    }
    
    public Ench getEnchant(CompoundTag enchantelement) {
        Map<String, Tag> enchant = enchantelement.getValue();
        Short id = getChildTag(enchant, "id", ShortTag.class, Short.class);
        Short lvl = getChildTag(enchant, "lvl", ShortTag.class, Short.class);
        return new Ench(id, lvl);
    }
    
    public List<Ench> getEnchant(Map<String, Tag> enchanttag) {
        List<?> enchantList = getChildTag(enchanttag, "ench", ListTag.class, List.class);

        if (enchantList != null) {
            List<Ench> enchants = new ArrayList<Ench>();

            for (Object enchantelement : enchantList) {
                if (enchantelement instanceof CompoundTag) {
                    enchants.add(getEnchant((CompoundTag) enchantelement));
                }
            }

            return enchants;
        } else {
            return null;
        }
    }
    
    public Equipment getEquipment(CompoundTag equipmentelement) {
        Map<String, Tag> equipment = equipmentelement.getValue();
        Byte count = getChildTag(equipment, "Count", ByteTag.class, Byte.class);
        Short damage = getChildTag(equipment, "Damage", ShortTag.class, Short.class);
        Short id = getChildTag(equipment, "id", ShortTag.class, Short.class);
        
        ItemTag itemtag = getItemTag(equipment);
        
        return new Equipment(count, damage, id, itemtag);
    }
    
    public List<Equipment> getEquipment(Map<String, Tag> entity) {
        List<?> equipmentlist = getChildTag(entity, "Equipment", ListTag.class, List.class);

        if (equipmentlist != null) {
            List<Equipment> equipments = new ArrayList<Equipment>();

            for (Object equipmentelement : equipmentlist) {
                if (equipmentelement instanceof CompoundTag) {
                    equipments.add(getEquipment((CompoundTag) equipmentelement));
                }
            }

            return equipments;
        } else {
            return null;
        }
    }
    
    public Attribute getAttribute(CompoundTag attributeelement) {
        Map<String, Tag> attribute = attributeelement.getValue();
        Double base = getChildTag(attribute, "Base", DoubleTag.class, Double.class);
        String name = getChildTag(attribute, "Name", StringTag.class, String.class);
        List<Modifier> modifiers = getModifiers(attribute);      
        return new Attribute(base, name, modifiers);
    }

    public List<Attribute> getAttributes(Map<String, Tag> entity) {
        List<?> attributelist = getChildTag(entity, "Attributes", ListTag.class, List.class);

        if (attributelist != null) {
            List<Attribute> attributes = new ArrayList<Attribute>();

            for (Object attributeelement : attributelist) {
                if (attributeelement instanceof CompoundTag) {
                    attributes.add(getAttribute((CompoundTag) attributeelement));
                }
            }

            return attributes;
        } else {
            return null;
        }
    }
    
    public <T extends Tag, K> K getChildTag(Map<String, Tag> items, String key, Class<T> expected, Class<K> result) {
        if (!items.containsKey(key)) {
            return null;
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(tag.getName() + " tag is not of tag type " + expected.getName() + System.lineSeparator() + "tag is: " + tag.toString());
        }
        Object obj = expected.cast(tag).getValue();
        if (!result.isInstance(obj)) {
            return null;
        }
        return result.cast(obj);
    }
    
    private <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) {
        if (!items.containsKey(key)) {
            return null;
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(tag.getName() + " tag is not of tag type " + expected.getName() + System.lineSeparator() + "tag is: " + tag.toString());
        }
        return expected.cast(tag);
    }
    
    private <T> T convert(Object obj, Class<T> expected) {
        if (!(obj instanceof Tag)) {
            return null;
        } else {

            Tag tag = (Tag) obj;

            if (!expected.isInstance(tag.getValue())) {
                throw new IllegalArgumentException(tag.getName() + " tag is not of tag type " + expected.getName() + System.lineSeparator() + "tag is: " + tag.toString());
            }

            return expected.cast(tag.getValue());
        }
    }
    
    private <T> List<T> convert(List<?> tagList, Class<T> expected) {
        if (tagList != null) {
            List<T> newlist = new ArrayList<T>();
            for (Object tag : tagList) {
                newlist.add(convert(tag, expected));
            }
            return newlist;
        } else {
            return null;
        }
    }
}
