package com.worldcretornica.dungeonme;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class DungeonMe extends JavaPlugin {

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
}
