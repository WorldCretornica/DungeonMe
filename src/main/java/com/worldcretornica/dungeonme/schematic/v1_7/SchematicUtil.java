package com.worldcretornica.dungeonme.schematic.v1_7;

import java.io.*;
import java.util.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.util.Vector;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.worldcretornica.dungeonme.DungeonMe;
import com.worldcretornica.dungeonme.schematic.AbstractSchematicUtil;
import com.worldcretornica.dungeonme.schematic.Size;
import com.worldcretornica.dungeonme.schematic.jnbt.*;

public class SchematicUtil extends AbstractSchematicUtil {

    private Map<Size, List<Schematic>> loadedschematics;
    
    public SchematicUtil(DungeonMe instance) {
        super(instance);
    }

    private Schematic getNextSchematic(Size size, Random rand) {
        if (loadedschematics.containsKey(size)) {
            int id = rand.nextInt(loadedschematics.get(size).size());
            return getSchematic(size, id);
        } else {
            return null;
        }
    }
    
    private Schematic getSchematic(Size size, int id) {
        return loadedschematics.get(size).get(id);
    }

    @Override
    public void loadSchematics() {
        plugin.getLogger().info("Loading room schematics...");

        loadedschematics = new HashMap<>();
        
        for (final File fileEntry : new File(plugin.getDataFolder().getPath() + "/Rooms").listFiles()) {
            if (!fileEntry.isDirectory()) {
                try {
                    Schematic schem = loadSchematic(fileEntry);
                    
                    if (schem != null) {
                        Size size = schem.getSize();
                        
                        if (!loadedschematics.containsKey(size)) {
                            loadedschematics.put(size, new ArrayList<Schematic>());
                        }
                        loadedschematics.get(size).add(schem);
                    }
                } catch (IllegalArgumentException | IOException e) {
                    plugin.getLogger().severe("Error loading file " + fileEntry.getName());
                    e.printStackTrace();
                }
            }
        }
        
        int nbrooms = 0;
        for (Size size : loadedschematics.keySet()) {
            nbrooms += loadedschematics.get(size).size();
        }

        plugin.getLogger().info("" + nbrooms + " room schematics loaded.");
    }
    
    @Override
    public void pasteSchematic(Location loc, Size size, int id) {
        Schematic schem = getSchematic(size, id);
        pasteSchematic(loc, schem);
    }
    
    private void pasteSchematic(Location loc, Schematic schem) {
        pasteSchematicBlocks(loc, schem, true);
        pasteSchematicEntities(loc, schem);
    }
    
    @SuppressWarnings("deprecation")
    private void pasteSchematicBlocks(Location loc, Schematic schematic, boolean setBlock) {
        World world = loc.getWorld();
        int[] blocks = schematic.getBlocks();
        byte[] blockData = schematic.getData();

        Short length = schematic.getLength();
        Short width = schematic.getWidth();
        Short height = schematic.getHeight();
        
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    
                    if (blocks[index] != 0) {
                        Block block = world.getBlockAt(x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ());
                        
                        try {
                            if (setBlock)
                                block.setTypeIdAndData(blocks[index], blockData[index], false);
                            block.setData(blockData[index], false);
                        } catch (NullPointerException e) {
                            plugin.getLogger().info("Error pasting block : " + blocks[index] + " of data " + blockData[index]);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void generateNextSchematic(byte[][] blocks, int roomY, Size size, Random rand) {
        
        Schematic schematic = getNextSchematic(size, rand);
        
        int minY = roomY << 3;
        
        if (schematic == null) {
            plugin.getLogger().severe("Schematic is null");
        } else {
            
            int[] schemblocks = schematic.getBlocks();
            Short length = schematic.getLength();
            Short width = schematic.getWidth();
            Short height = schematic.getHeight();
            
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    for (int z = 0; z < length; ++z) {
                        int index = y * width * length + z * width + x;
                        setBlock(blocks, x, y + minY, z, (byte) schemblocks[index]);
                    }
                }
            }
        }
    }
    
    @Override
    public void populateNextSchematic(Location loc, Size size, Random rand) {
        Schematic schematic = getNextSchematic(size, rand);
        pasteSchematicBlocks(loc, schematic, true);
        pasteSchematicEntities(loc, schematic);
    }
    
    @SuppressWarnings("deprecation")
    private void pasteSchematicEntities(Location loc, Schematic schematic) {
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
            // Integer levels = te.getLevels();
            // Integer primary = te.getPrimary();
            // Integer secondary = te.getSecondary();

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
        ItemTag itemtag = item.getTag();

        if (itemtag != null) {
            setTag(is, itemtag);
        }
        
        return is;
    }

    @SuppressWarnings("deprecation")
    private void setTag(ItemStack is, ItemTag itemtag) {
        List<Ench> enchants = itemtag.getEnchants();
        //Integer repaircost = itemtag.getRepairCost();
        List<String> pages = itemtag.getPages();
        String author = itemtag.getAuthor();
        String title = itemtag.getTitle();
        Display display = itemtag.getDisplay();
        
        ItemMeta itemmeta = is.getItemMeta();
        
        if (display != null) {
            List<String> lores = display.getLore();
            String name = display.getName();
            
            itemmeta.setLore(lores);
            itemmeta.setDisplayName(name);
        }
        
        if (itemmeta instanceof BookMeta) {
            BookMeta bookmeta = (BookMeta) itemmeta;
            bookmeta.setAuthor(author);
            bookmeta.setTitle(title);
            bookmeta.setPages(pages);
        }
        
        if (itemmeta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta enchantmentstoragemeta = (EnchantmentStorageMeta) itemmeta;
            
            for (Ench enchant : enchants) {
                enchantmentstoragemeta.addEnchant(Enchantment.getById(enchant.getId()), enchant.getLvl(), true);
            }
        }
        
        is.setItemMeta(itemmeta);
    }

    @SuppressWarnings({ "deprecation", "unused" })
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
            Byte canpickuploot = e.getCanPickupLoot();
            Byte color = e.getColor();
            Byte customnamevisible = e.getCustomNameVisible();
            //Byte leashed = e.getLeashed();
            Byte persistencerequired = e.getPersistenceRequired();
            Byte sheared = e.getSheared();
            Byte skeletontype = e.getSkeletonType();
            Byte isbaby = e.getIsBaby();
            Byte itemrotation = e.getItemRotation();
            
            //Double pushx = e.getPushX();
            //Double pushz = e.getPushZ();
            
            Entity riding = e.getRiding();
            
            Float falldistance = e.getFallDistance();
            Float absorptionamount = e.getAbsorptionAmount();
            Float healf = e.getHealF();
            Float itemdropchance = e.getItemDropChance();
            
            Integer dimension = e.getDimension();
            Integer portalcooldown = e.getPortalCooldown();
            Integer tilex = e.getTileX();
            Integer tiley = e.getTileY();
            Integer tilez = e.getTileZ();
            Integer age = e.getAge();
            Integer inlove = e.getInLove();
            //Integer transfercooldown = e.getTransferCooldown();
            //Integer tntfuse = e.getTNTFuse();
            
            Item item = e.getItem();
            
            Leash leash = e.getLeash();
            
            Short air = e.getAir();
            Short fire = e.getFire();
            Short attacktime = e.getAttackTime();
            Short deathtime = e.getDeathTime();
            Short health = e.getHealth();
            Short hurttime = e.getHurtTime();
            //Short fuel = e.getFuel()
            
            String id = e.getId();
            String motive = e.getMotive();
            String customname = e.getCustomName();
            
            List<Double> motion = e.getMotion();
            List<Float> rotation = e.getRotation();
            List<Attribute> attributes = e.getAttributes();
            List<Float> dropchances = e.getDropChances();
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
                if (health != null)             livingentity.setHealth(health);
                if (healf != null)              livingentity.setMaxHealth(healf);
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
                
                if (equipments != null && !equipments.isEmpty()) {
                    for(Equipment equipitem : equipments) {
                        if (equipitem != null && equipitem.getId() != null && equipitem.getCount() != null) {
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

    private Schematic loadCompiledSchematic(String file, long checksum) {
        Schematic schem = null;
        String filename = plugin.getDataFolder().getAbsolutePath() + "\\.Buff\\" + file + ".room";
        File f = new File(filename);

        if (f.exists()) {
            try (ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                schem = (Schematic) input.readObject();
            } catch (ClassNotFoundException ex) {
                plugin.getLogger().severe("Cannot load '" + file + ".room' from cache, recreating cache");
            } catch (IOException ex) {
                plugin.getLogger().severe("Cannot read file '" + file + ".room', verify file permissions ");
            }

            if (schem != null && schem.getChecksum() != checksum) {
                return null;
            }
        }
        
        return schem;
    }
    
    private void saveCompiledSchematic(Schematic schem, String file) {
        String filename = plugin.getDataFolder().getAbsolutePath() + "\\.Buff\\" + file + ".room";
        
        try (ObjectOutput output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            output.writeObject(schem);
        } catch (IOException ex) {
            plugin.getLogger().severe("Could not save buffer " + filename + ".");
            ex.printStackTrace();
        }
    }
    
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
                                    transfercooldown, levels, primary, secondary));
                        }
                    }
                }

                schem = new Schematic(blocks, blockData, blockBiomes, materials, width, length, height, entities, tileentities, roomauthor, checksum, originx, originy, originz, size);

                saveCompiledSchematic(schem, file.getName());
            }
        }
        
        return schem;
    }
    
    private Entity getEntity(CompoundTag tag) {
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
        
        Item item = null;
        
        Leash leash = null;
        
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
                                  
        return new Entity(dir, direction, invulnerable, onground, air, fire, dimension, portalcooldown, tilex, tiley, tilez, falldistance, id, motive, motion, pos, rotation,
                canpickuploot, color, customnamevisible, leashed, persistencerequired, sheared, attacktime, deathtime, health, hurttime, age, inlove, absorptionamount,
                healf, customname, attributes, dropchances, equipments, skeletontype, riding, leash, item, isbaby, items, transfercooldown, fuel, pushx, pushz, tntfuse,
                itemrotation, itemdropchance);
    }
    
    private Leash getLeash(CompoundTag leashelement) {
        Map<String, Tag> leash = ((CompoundTag) leashelement).getValue();
        Integer x = getChildTag(leash, "X", IntTag.class, Integer.class);
        Integer y = getChildTag(leash, "Y", IntTag.class, Integer.class);
        Integer z = getChildTag(leash, "Z", IntTag.class, Integer.class);
        
        return new Leash(x, y, z);
    }
    
    private Modifier getModifier(CompoundTag modifierelement) {
        Map<String, Tag> modifier = ((CompoundTag) modifierelement).getValue();
        Integer operation = getChildTag(modifier, "Operation", IntTag.class, Integer.class);
        Double amount = getChildTag(modifier, "Amount", DoubleTag.class, Double.class);
        String name = getChildTag(modifier, "Name", StringTag.class, String.class);
        
        return new Modifier(operation, amount, name);
    }
    
    private List<Modifier> getModifiers(Map<String, Tag> attribute) {
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
    
    private Item getItem(CompoundTag itemElement) {
        Map<String, Tag> item = itemElement.getValue();
        Byte count = getChildTag(item, "Count", ByteTag.class, Byte.class);
        Byte slot = getChildTag(item, "Slot", ByteTag.class, Byte.class);
        Short damage = getChildTag(item, "Damage", ShortTag.class, Short.class);
        Short itemid = getChildTag(item, "id", ShortTag.class, Short.class);

        ItemTag tag = getItemTag(item);
        
        return new Item(count, slot, damage, itemid, tag);
    }
    
    private List<Item> getItems(Map<String, Tag> entity) {
        List<?> itemsList = getChildTag(entity, "Items", ListTag.class, List.class);

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
    
    private ItemTag getItemTag(Map<String, Tag> item) {
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
    
    private Display getDisplay(Map<String, Tag> itemtag) {
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
    
    private Ench getEnchant(CompoundTag enchantelement) {
        Map<String, Tag> enchant = enchantelement.getValue();
        Short id = getChildTag(enchant, "id", ShortTag.class, Short.class);
        Short lvl = getChildTag(enchant, "lvl", ShortTag.class, Short.class);
        return new Ench(id, lvl);
    }
    
    private List<Ench> getEnchant(Map<String, Tag> enchanttag) {
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
    
    private Equipment getEquipment(CompoundTag equipmentelement) {
        Map<String, Tag> equipment = equipmentelement.getValue();
        Byte count = getChildTag(equipment, "Count", ByteTag.class, Byte.class);
        Short damage = getChildTag(equipment, "Damage", ShortTag.class, Short.class);
        Short id = getChildTag(equipment, "id", ShortTag.class, Short.class);
        
        ItemTag itemtag = getItemTag(equipment);
        
        return new Equipment(count, damage, id, itemtag);
    }
    
    private List<Equipment> getEquipment(Map<String, Tag> entity) {
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
    
    private Attribute getAttribute(CompoundTag attributeelement) {
        Map<String, Tag> attribute = attributeelement.getValue();
        Double base = getChildTag(attribute, "Base", DoubleTag.class, Double.class);
        String name = getChildTag(attribute, "Name", StringTag.class, String.class);
        List<Modifier> modifiers = getModifiers(attribute);      
        return new Attribute(base, name, modifiers);
    }

    private List<Attribute> getAttributes(Map<String, Tag> entity) {
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
}
