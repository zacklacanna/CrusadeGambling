package me.boyjamal.gambling.slots;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.boyjamal.gambling.slots.SlotsEvent;
import me.boyjamal.gambling.slots.SlotsListener;
import me.boyjamal.gambling.slots.SlotsRewards;

public class SlotsRunnable implements Runnable {
	
	private SlotsEvent e;
	private double increase;
	
	public SlotsRunnable(SlotsEvent e)
	{
		this.e = e;
	}
	
	public void run() 
	{
		Player p = e.getPlayer();
		String key = p.getUniqueId().toString();
		if (!(SlotsListener.timeLeft.containsKey(key)))
		{
			SlotsListener.timeLeft.put(key,0);
		}
		
		int current = SlotsListener.timeLeft.get(key);
		
		if (current >= e.getMax()*10)
		{
			if (SlotsRewards.checkRewards(e))
			{
				e.setCancelled(true);
				return;
			} else {
				SlotsListener.timeLeft.remove(key);
				if (SlotsListener.activeSlots.containsKey(key))
				{
					SlotsEvent.currentItems.remove(key);
					e.setCancelled(true);
					int cancel = SlotsListener.activeSlots.get(key);
					Bukkit.getScheduler().cancelTask(cancel);
					SlotsListener.activeSlots.remove(key);
					return;
				}
			}
		} else {
			e.openInventory();
			p.updateInventory();
			p.playSound(p.getLocation(),Sound.NOTE_PLING,12,1);
			SlotsListener.timeLeft.replace(key,current+1);
		}
	}

}
