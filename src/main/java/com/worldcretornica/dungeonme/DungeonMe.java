package com.worldcretornica.dungeonme;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
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
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtList;
import com.worldcretornica.dungeonme.schematic.Schematic;

public class DungeonMe extends JavaPlugin implements Listener {

    private SchematicUtil schematicutil;
    
    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        
        prepareFolders();
        
        schematicutil = new SchematicUtil(this);
        schematicutil.loadSchematics();
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldname, String id) {
        return new DungeonGenerator();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("dungeonme")) {
            if(args.length == 0) {
                if(sender instanceof Player) {
                    Player p = (Player) sender;
                    
                    ItemStack is = p.getItemInHand();
                    NbtCompound compound = NbtFactory.asCompound(NbtFactory.fromItemTag(is));
                    
                    
                    try{
                        getLogger().info(compound.toString());
                    }catch(Exception e) {}
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
                        
                        schematicutil.pasteSchematic(l, schemid);
                    }
                    
                } else {
                
                    getLogger().info("Loading schematic...");
                    try {
                        Schematic schem = schematicutil.loadSchematic(new File("C:\\Minecraft\\Schematic\\Test2.schematic"));
                        
                        if(sender instanceof Player) {
                            Player p = (Player) sender;
                            Location l = p.getLocation();
                            
                            schematicutil.pasteSchematic(l, schem);
                        }
                    } catch (IllegalArgumentException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            
            return true;
        }
        return false;
    }
        
 
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {

        ItemStack is = event.getItem();
        Block block = event.getClickedBlock();

        if (block != null && block.getType() == Material.IRON_DOOR_BLOCK && is != null) {

            Player p = event.getPlayer();
            NbtCompound tag = NbtFactory.asCompound(NbtFactory.fromItemTag(is));

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
            }
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
