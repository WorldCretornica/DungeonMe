package com.worldcretornica.dungeonme.jnbt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtList;
import com.worldcretornica.dungeonme.DungeonMe;
import com.worldcretornica.dungeonme.schematic.Display;
import com.worldcretornica.dungeonme.schematic.Ench;
import com.worldcretornica.dungeonme.schematic.Entity;
import com.worldcretornica.dungeonme.schematic.Item;
import com.worldcretornica.dungeonme.schematic.ItemTag;
import com.worldcretornica.dungeonme.schematic.Schematic;
import com.worldcretornica.dungeonme.schematic.TileEntity;

public class SchematicUtil {

    @SuppressWarnings("deprecation")
    public static void pasteSchematic(Location loc, Schematic schematic) {
        World world = loc.getWorld();
        byte[] blocks = schematic.getBlocks();
        byte[] blockData = schematic.getData();

        Short length = schematic.getLength();
        Short width = schematic.getWidth();
        Short height = schematic.getHeight();
        List<Entity> entities = schematic.getEntities();
        List<TileEntity> tileentities = schematic.getTileEntities();

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    Block block = new Location(world, x + loc.getBlockX(), y + loc.getBlockY(), z + loc.getBlockZ()).getBlock();
                    block.setTypeIdAndData(blocks[index], blockData[index], true);
                    block.setData(blockData[index]);
                }
            }
        }

        try {
            for (Entity e : entities) {
                EntityType et = EntityType.fromName(e.getId());

                if (et != null) {
                    //world.spawnEntity(new Location(world, e.getTileX() + loc.getX(), e.getTileY() + loc.getY(), e.getTileZ() + loc.getZ()), et);
                }
            }
        } catch (Exception e) {
            DungeonMe.log("err:" + e.getMessage());
        }

        for (TileEntity te : tileentities) {

            Location teloc = new Location(world, te.getX() + loc.getBlockX(), te.getY() + loc.getBlockY(), te.getZ() + loc.getBlockZ());
            DungeonMe.log("x:" + (te.getX() + loc.getBlockX()) + " y:" + (te.getY() + loc.getBlockY()) + " z:" + (te.getZ() + loc.getBlockZ()));
            Block block = teloc.getBlock();
            List<Item> items = te.getItems();

            if (block.getState() instanceof InventoryHolder && items != null && items.size() > 0) {

                InventoryHolder ih = (InventoryHolder) block.getState();
                Inventory inventory = ih.getInventory();
                // inventory.

                for (Item item : items) {

                    ItemStack is = new ItemStack(item.getId(), item.getCount());
                    is = MinecraftReflection.getBukkitItemStack(is);
                    ItemTag itemtag = item.getTag();

                    if (itemtag != null) {
                        
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
                    
                    inventory.setItem(item.getSlot(), is);
                }
            }
        }
    }

    public static Schematic loadSchematic(File file) throws IOException, IllegalArgumentException {
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

            String materials = getChildTag(schematic, "Materials", StringTag.class, String.class);
            if (!materials.equals("Alpha")) {
                throw new IllegalArgumentException("Schematic file is not an Alpha schematic");
            }

            byte[] blocks = getChildTag(schematic, "Blocks", ByteArrayTag.class, byte[].class);
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
                        Map<String, Tag> entity = ((CompoundTag) tag).getValue();
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

                        entities.add(new Entity(dir, direction, invulnerable, onground, air, fire, dimension, portalcooldown, tilex, tiley, tilez, falldistance, id, motive, motion, pos, rotation));
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

                        tileentities.add(new TileEntity(x, y, z, customname, id, items));
                    }
                }
            }

            return new Schematic(blocks, blockData, blockBiomes, materials, width, length, height, entities, tileentities);
        }
    }

    /**
     * Get child tag of a NBT structure.
     * 
     * @param <K>
     *
     * @param items
     *            The parent tag map
     * @param key
     *            The name of the tag to get
     * @param expected
     *            The expected type of the tag
     * @return child tag casted to the expected type
     * @throws DataException
     *             if the tag does not exist or the tag is not of the expected
     *             type
     */
    private static <T extends Tag, K> K getChildTag(Map<String, Tag> items, String key, Class<T> expected, Class<K> result) {
        if (!items.containsKey(key)) {
            return null;
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(tag.getName() + " tag is not of tag type " + expected.getName());
        }
        Object obj = expected.cast(tag).getValue();
        if (!result.isInstance(obj)) {
            return null;
        }
        return result.cast(obj);
    }

    private static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) {
        if (!items.containsKey(key)) {
            return null;
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(tag.getName() + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }

    private static <T> T convert(Object obj, Class<T> expected) {
        if (!(obj instanceof Tag)) {
            return null;
        } else {

            Tag tag = (Tag) obj;

            if (!expected.isInstance(tag.getValue())) {
                throw new IllegalArgumentException(tag.getName() + " tag is not of tag type " + expected.getName());
            }

            return expected.cast(tag.getValue());
        }
    }

    private static <T> List<T> convert(List<?> tagList, Class<T> expected) {
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

    private static List<Item> getItems(Map<String, Tag> tileentity) {
        List<?> itemsList = getChildTag(tileentity, "Items", ListTag.class, List.class);

        if (itemsList != null) {
            List<Item> items = new ArrayList<Item>();

            for (Object itemElement : itemsList) {
                if (itemElement instanceof CompoundTag) {
                    Map<String, Tag> item = ((CompoundTag) itemElement).getValue();
                    Byte count = getChildTag(item, "Count", ByteTag.class, Byte.class);
                    Byte slot = getChildTag(item, "Slot", ByteTag.class, Byte.class);
                    Short damage = getChildTag(item, "Damage", ShortTag.class, Short.class);
                    Short itemid = getChildTag(item, "id", ShortTag.class, Short.class);

                    ItemTag tag = getItemTag(item);

                    items.add(new Item(count, slot, damage, itemid, tag));
                }
            }

            return items;
        } else {
            return null;
        }
    }

    private static ItemTag getItemTag(Map<String, Tag> item) {
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

    private static Display getDisplay(Map<String, Tag> itemtag) {
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

    private static List<Ench> getEnchant(Map<String, Tag> enchanttag) {
        List<?> enchantList = getChildTag(enchanttag, "ench", ListTag.class, List.class);

        if (enchantList != null) {
            List<Ench> enchants = new ArrayList<Ench>();

            for (Object enchantelement : enchantList) {
                if (enchantelement instanceof CompoundTag) {
                    Map<String, Tag> enchant = ((CompoundTag) enchantelement).getValue();
                    Short id = getChildTag(enchant, "id", ShortTag.class, Short.class);
                    Short lvl = getChildTag(enchant, "lvl", ShortTag.class, Short.class);

                    enchants.add(new Ench(id, lvl));
                }
            }

            return enchants;
        } else {
            return null;
        }
    }
}
