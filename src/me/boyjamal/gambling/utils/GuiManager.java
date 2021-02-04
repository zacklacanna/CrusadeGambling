package me.boyjamal.gambling.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import me.boyjamal.gambling.utils.GuiItem;
import me.boyjamal.gambling.utils.MainUtils;

public class GuiManager {
	
	private String name;
	private int slots;
	private List<GuiItem> items;
	
	public GuiManager(String name, int slots, List<GuiItem> items)
	{
		this.name = name;
		this.slots = slots;
		this.items = items;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getSlots()
	{
		return slots;
	}
	
	public List<GuiItem> getItems()
	{
		return items;
	}
	
	public Inventory openInventory()
	{
		return Bukkit.createInventory(null,slots,MainUtils.chatColor(name));
	}
	
	public Inventory openItems()
	{
		Inventory inv = Bukkit.createInventory(null,slots,MainUtils.chatColor(name));
		for (GuiItem each : items)
		{
			inv.setItem(each.getSlot(),each.getItem());
		}
		return inv;
	}
}

