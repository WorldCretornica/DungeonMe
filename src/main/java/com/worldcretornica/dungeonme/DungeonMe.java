package com.worldcretornica.dungeonme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.worldcretornica.dungeonme.map.DungeonFont;
import com.worldcretornica.dungeonme.map.MapListener;
import com.worldcretornica.dungeonme.schematic.AbstractSchematicUtil;
import com.worldcretornica.dungeonme.schematic.Size;

public class DungeonMe extends JavaPlugin implements Listener {

    private AbstractSchematicUtil schematicutil;
    
    private final HashFunction hf = Hashing.md5();
    
    @Override
    public void onDisable() {
        
    }

    @Override
    public void onEnable() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(new MapListener(this), this);
        
        prepareFolders();
        
        if (Bukkit.getVersion().contains("1.7")) {
            schematicutil = new com.worldcretornica.dungeonme.schematic.v1_7.SchematicUtil(this);
        } else if (Bukkit.getVersion().contains("1.8")) {
            schematicutil = new com.worldcretornica.dungeonme.schematic.v1_8.SchematicUtil(this);
        } else {
            getLogger().warning("This MC version is not supported yet, trying latest version!");
            schematicutil = new com.worldcretornica.dungeonme.schematic.v1_8.SchematicUtil(this);
        }
        
        schematicutil.loadSchematics();
        
        new DungeonFont();
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldname, String id) {
        return new DungeonGenerator(this);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("dungeonme")) {
            if(args.length == 0) {
                if(sender instanceof Player) {
                   
                }
            }else if(args[0].equalsIgnoreCase("paste")) {
                
                if (args.length > 1) {
                    
                    if(sender instanceof Player) {
                        Player p = (Player) sender;
                        Location l = p.getLocation();
                        
                        int schemid = 0;
                        
                        try {
                            schemid = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            
                        }
                        
                        schematicutil.pasteSchematic(l, Size.OneXOneXOne, schemid);
                    }
                    
                } else {
                
                    /*getLogger().info("Loading schematic...");
                    try {
                        Schematic schem = schematicutil.loadSchematic(new File("C:\\Minecraft\\Schematic\\Test2.schematic"));
                        
                        if(sender instanceof Player) {
                            Player p = (Player) sender;
                            Location l = p.getLocation();
                            
                            schematicutil.pasteSchematic(l, schem);
                        }
                        
                        schematicutil.pasteSchematic(l, size, id);
                        
                    } catch (IllegalArgumentException | IOException e) {
                        e.printStackTrace();
                    }*/
                }
            }else if(args[0].equalsIgnoreCase("test")) {
                
                //getServer().getServerId()
               
                
                
                
                
            }
            
            return true;
        }
        return false;
    }
        
    public AbstractSchematicUtil getSchematicUtil() {
        return this.schematicutil;
    }
    
    public Random getRandom(long seed, int roomX, int roomY, int roomZ) {
        return new Random(hf.newHasher().putLong(seed).putLong(roomX).putInt(roomY).putLong(roomZ).hash().asLong());
    }
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {

        ItemStack is = event.getItem();
        Block block = event.getClickedBlock();

        if (block != null && block.getType() == Material.IRON_DOOR_BLOCK && is != null) {

            //Player p = event.getPlayer();
            //TODO
            /*NbtCompound tag = NbtFactory.asCompound(NbtFactory.fromItemTag(is));

            if (tag != null && tag.containsKey("display")) {
                NbtCompound display = tag.getCompoundOrDefault("display");

                if (display != null && display.containsKey("Lore")) {
                    NbtList<String> lores = display.getList("Lore");

                    for (String lore : lores) {
                        if (lore.equalsIgnoreCase("§6Unlocks iron doors")) {
                            block.setType(Material.AIR);
                            p.sendMessage("door unlocked");
                        }
                    }
                }
            }*/
        }
    }
    
    
    private void prepareFolders() {
        File root = this.getDataFolder();
        if (!root.exists()) {
            root.mkdir();
        }
            
        File rooms = new File(root.getPath() + "/Rooms");
        
        if (!rooms.exists()) {
            rooms.mkdir();
        }
        
        File buff = new File(root.getPath() + "/.Buff");
        
        if (!buff.exists()) {
            buff.mkdir();
        }
        
        String resourcepath = "Rooms";

        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        if (jarFile.isFile()) { // Run with JAR file
            JarFile jar = null;
            try {
                jar = new JarFile(jarFile);

                final Enumeration<JarEntry> entries = jar.entries(); 
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if (name.startsWith(resourcepath + "/") && !entry.isDirectory()) {
                        String filename = name.substring(name.lastIndexOf("/"));
                        
                        InputStream input = jar.getInputStream(entry);
                        
                        File of = new File(rooms.getPath() + "\\" + filename);
                        
                        if(!of.exists()) {
                            OutputStream output = new FileOutputStream(of);
            
                            byte[] buffer = new byte[1024];
    
                            int bytesRead;
    
                            while ((bytesRead = input.read(buffer)) > 0) {
                                output.write(buffer, 0, bytesRead);
                            }
                            
                            output.close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    jar.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
