package me.boyjamal.gambling.slots;

import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.boyjamal.gambling.slots.RewardsEvent;
import me.boyjamal.gambling.slots.SlotsEvent;
import me.boyjamal.gambling.slots.SlotsListener;
import me.boyjamal.gambling.utils.GuiItem;

public class RewardsTask implements Runnable {
	
	private Inventory inv;
	private Collection<List<GuiItem>> items;
	private Player p;
	private RewardsEvent e;
	
	public RewardsTask(RewardsEvent e)
	{
		this.e = e;
		this.inv = e.getInv();
		this.items = e.getWonItems();
		this.p = e.getPlayer();
	}
	
	int count = 0;
	public void run()
	{
		if (count >= 10)
		{
			String key = p.getUniqueId().toString();
			if (SlotsListener.activeSlots.containsKey(key))
			{
				int id = SlotsListener.activeSlots.get(key);
				Bukkit.getScheduler().cancelTask(id);
				SlotsListener.activeSlots.remove(key);
				SlotsListener.timeLeft.remove(key);
				SlotsEvent.currentItems.remove(key);
				p.closeInventory();
				return;
			}
		} else {
			e.updateInv();
			p.updateInventory();
			p.playSound(p.getLocation(),Sound.LEVEL_UP,15,1);
			count++;
		}
	}
}

