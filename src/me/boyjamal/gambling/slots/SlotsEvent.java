package me.boyjamal.gambling.slots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.boyjamal.gambling.utils.GuiItem;
import me.boyjamal.gambling.utils.GuiManager;
import me.boyjamal.gambling.utils.MainUtils;
import me.boyjamal.gambling.utils.StorageManager;

public class SlotsEvent extends Event implements Cancellable {

	private Player p;
	private GuiManager inv;
	private Inventory open;
	private boolean cancelled = false;
	private double max;
	private static final HandlerList handlers = new HandlerList();
	
	public SlotsEvent(Player p, GuiManager inv, Inventory open, double max)
	{
		this.p = p;
		this.inv = inv;
		this.open = open;
		this.max = max;
	}
	
	public static HashMap<String,List<GuiItem>> currentItems = new HashMap<>();
	
	public double getMax()
	{
		return max;
	}
	
	public Inventory getInv()
	{
		return open;
	}
	
	private List<Integer> pos = new ArrayList<>();
	private int counter = 0;
	public Inventory openInventory()
	{
		if (!(currentItems.containsKey(p.getUniqueId().toString())))
		{
			List<GuiItem> slotItems = new ArrayList<>();
			for (GuiItem items : inv.getItems())
			{
				if (items.getType().equalsIgnoreCase("slot") || items.getType().equalsIgnoreCase("slots"))
				{
					if (StorageManager.getSlotSettings() != null)
					{
						ItemStack set = MainUtils.getSlotItem(StorageManager.getSlotSettings().getItems());
						if (set != null)
						{
							items.getItem().setType(set.getType());
							items.getItem().setDurability(set.getDurability());
							open.setItem(items.getSlot(),items.getItem());
							slotItems.add(new GuiItem(items.getActions(), items.getSlot(), items.getItem(), items.getType(), items.getRow(),items.getPosition()));
						}
					}
				} else {
					open.setItem(items.getSlot(),items.getItem());
				}
			}
			currentItems.put(p.getUniqueId().toString(),slotItems);
			counter++;
			return open;
		} else {
			List<GuiItem> oldItems = currentItems.get(p.getUniqueId().toString());
			List<GuiItem> newItems = new ArrayList<>();
			List<Integer> slots = new ArrayList<>();
			for (GuiItem each : oldItems)
			{
				if (each.getType().equalsIgnoreCase("slot") && pos.contains(each.getPosition()))
				{
					if (!(slots.contains(each.getSlot())))
					{
						newItems.add(each);
						slots.add(each.getSlot());
						continue;
					}
				}
				
				for (GuiItem rest : oldItems)
				{
					if (each.getType().equalsIgnoreCase("slot") && rest.getType().equalsIgnoreCase("slot"))
					{
						if (rest.getRow() - each.getRow() == 1 && rest.getRow() != 0)
						{
							if (rest.getSlot() - each.getSlot() == 9)
							{	
								if (max*10 - counter == 14)
								{
									pos.add(1);
									p.playSound(p.getLocation(),Sound.ORB_PICKUP,500,500);
								} else if (max*10-counter == 8) {
									pos.add(2);
									p.playSound(p.getLocation(),Sound.ORB_PICKUP,500,500);
								} else if (max*10 - counter == 2) {
									pos.add(3);
									p.playSound(p.getLocation(),Sound.ORB_PICKUP,500,500);
								}
								
								if (MainUtils.isFirstSlotItem(each))
								{
									ItemStack set = MainUtils.getSlotItem(StorageManager.getSlotSettings().getItems());
									if (set != null)
									{
										ItemStack newest = each.getItem().clone();
										newest.setType(set.getType());
										newest.setDurability(set.getDurability());
										GuiItem newItem = new GuiItem(each.getActions(),each.getSlot(),newest,each.getType(),each.getRow(),each.getPosition());
										newItems.add(newItem);
										open.setItem(newItem.getSlot(),newItem.getItem());
									}
								}
								GuiItem newItem = new GuiItem(each.getActions(),rest.getSlot(),each.getItem(),each.getType(),rest.getRow(),rest.getPosition());
								newItems.add(newItem);
								open.setItem(newItem.getSlot(),newItem.getItem());
							}
						}
					}
				}
			}
			String key = p.getUniqueId().toString();
			currentItems.replace(key,newItems);
			counter++;
			return open;
		}
	}
	
	public Player getPlayer()
	{
		return p;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	public void setCancelled(boolean cancel)
	{
		this.cancelled = cancel;
	}
	
	

}

