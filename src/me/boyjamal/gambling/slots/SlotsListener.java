package me.boyjamal.gambling.slots;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.boyjamal.gambling.Main;
import me.boyjamal.gambling.slots.SlotsEvent;
import me.boyjamal.gambling.slots.SlotsRunnable;
import me.boyjamal.gambling.utils.MainUtils;
import me.boyjamal.gambling.utils.StorageManager;

public class SlotsListener implements Listener {
	
	public static HashMap<String,Integer> timeLeft = new HashMap<>();
	
	@EventHandler
	public void onClick(InventoryClickEvent e)
	{
		Player p = (Player)e.getWhoClicked();
		if (p == null)
		{
			return;
		}
		if (StorageManager.getSlotSettings() != null)
		{
			String name = MainUtils.chatColor(StorageManager.getSlotSettings().getGui().getName());
			if (e.getInventory().getName().equalsIgnoreCase(name))
			{
				e.setCancelled(true);
			}
		}
	}
	
	public static HashMap<String,Integer> activeSlots = new HashMap<>();
	
	@EventHandler
	public void playerOpenGui(SlotsEvent e)
	{
		String key = e.getPlayer().getUniqueId().toString();
		int id = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new SlotsRunnable(e), 0, (long) (2.5)).getTaskId();
		activeSlots.put(e.getPlayer().getUniqueId().toString(),id);
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e)
	{
		Player p = (Player)e.getPlayer();
		if (p == null)
		{
			return;
		}
		if (StorageManager.getSlotSettings() != null)
		{
			String name = MainUtils.chatColor(StorageManager.getSlotSettings().getGui().getName());
			if (e.getInventory().getName().equalsIgnoreCase(name))
			{
				if (timeLeft.containsKey(p.getUniqueId().toString()))
				{
					Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
						  @Override
						  public void run() {
						    p.openInventory(e.getInventory());						  
						  }
						}, 1);
					return;
				}
			}
		}
	}
	
	public static HashMap<String,Integer> getActiveSlots()
	{
		return activeSlots;
	}

}

