package me.boyjamal.gambling.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.arcaniax.hdb.enums.CategoryEnum;

public class HeadAPI implements Listener {

	private static final Map<UUID, String> playerHeadCache = new HashMap<>();
	private static HeadDatabaseAPI API;
	
    @EventHandler
    public void onDatabaseConnect(DatabaseLoadEvent event) {
        API = new HeadDatabaseAPI();
    }
 
    public static boolean isReady() {
        return API != null;
    }
 
    public static ItemStack getHead(String id) {
        if (isReady()) {
            try {
                return API.getItemHead(id);
            } catch (NullPointerException e) {
                e.printStackTrace();
                return null; // Soft exception landing
            }
        } else {
            return null;
        }
    }
 
    public static String addPlayerHead(String playerName, UUID uuid) {
        if (playerHeadCache.containsKey(uuid)) {
            return playerHeadCache.get(uuid);
        }
 
        String id = API.addHead(CategoryEnum.CUSTOM, playerName, uuid);
        playerHeadCache.put(uuid, id);
        return id;
    }
 
    public static void clearCache() {
        for (String id : playerHeadCache.values()) {
            API.removeHead(id);
        }
 
        playerHeadCache.clear();
    }
	
}
