package com.worldcretornica.dungeonme;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import com.worldcretornica.dungeonme.jnbt.SchematicUtil;
import com.worldcretornica.dungeonme.schematic.Schematic;

public class DungeonMe extends JavaPlugin {

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    	try {
    		
    		getLogger().info("Loading schematic...");
			Schematic schem = SchematicUtil.loadSchematic(new File("C:\\Minecraft\\Schematic\\ZachBoraTest1.schematic"));
			getLogger().info("Finished loading");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

}
