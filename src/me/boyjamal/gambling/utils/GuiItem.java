package me.boyjamal.gambling.utils;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class GuiItem {
	
	private List<String> actions;
	private int slot;
	private ItemStack item;
	private String type;
	private int row;
	private int position;
	
	public GuiItem(List<String> actions, int slot, ItemStack item, String type)
	{
		this.actions = actions;
		this.slot = slot;
		this.item = item;
		this.type = type;
	}
	
	public GuiItem(List<String> actions, int slot, ItemStack item, String type, int row, int position)
	{
		this.actions = actions;
		this.slot = slot;
		this.item = item;
		this.type = type;
		this.row = row;
		this.position = position;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getPosition()
	{
		return position;
	}
	
	public List<String> getActions()
	{
		return actions;
	}
	
	public int getSlot()
	{
		return slot;
	}
	
	public void setSlot(int newSlot)
	{
		slot = newSlot;
	}
	
	public ItemStack getItem()
	{
		return item;
	}
	
	public String getType()
	{
		return type;
	}

}

