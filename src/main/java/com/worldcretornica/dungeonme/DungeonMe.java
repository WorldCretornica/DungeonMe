package com.worldcretornica.dungeonme;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
import com.worldcretornica.dungeonme.jnbt.SchematicUtil;
import com.worldcretornica.dungeonme.schematic.Schematic;

public class DungeonMe extends JavaPlugin implements Listener {

    
    private static Logger logger;
    
    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
    	/*try {
    		
    		getLogger().info("Loading schematic...");
			Schematic schem = SchematicUtil.loadSchematic(new File("C:\\Minecraft\\Schematic\\ZachBoraTest1.schematic"));
			//getLogger().info("Finished loading. Outputting :");
			//getLogger().info(schem.toString());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
        
        logger = getLogger();
        
        Bukkit.getPluginManager().registerEvents(this, this);
    	
        for(World w : Bukkit.getWorlds()) {
            if(w.getGenerator() instanceof DungeonGenerator) {
                w.setSpawnLocation(8, 65, 8);
            }
        }
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
                
                getLogger().info("Loading schematic...");
                try {
                    Schematic schem = SchematicUtil.loadSchematic(new File("C:\\Minecraft\\Schematic\\Test2.schematic"));
                    
                    if(sender instanceof Player) {
                        Player p = (Player) sender;
                        Location l = p.getLocation();
                        
                        SchematicUtil.pasteSchematic(l, schem);
                    }
                } catch (IllegalArgumentException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
    public static void log(String text) {
        logger.info(text);
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
}
