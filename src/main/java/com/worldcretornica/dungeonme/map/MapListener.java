package com.worldcretornica.dungeonme.map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.worldcretornica.dungeonme.DungeonMe;

public class MapListener implements Listener {

    private DungeonMe plugin;
    
    public MapListener(DungeonMe instance) {
        plugin = instance;
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onMapInitialize(final MapInitializeEvent event) {
        MapView mv = event.getMap();
        
        for(MapRenderer r : mv.getRenderers()) {
            mv.removeRenderer(r);
        }
        
        mv.addRenderer(new DungeonMapRenderer(plugin));
    }
    
    
}
