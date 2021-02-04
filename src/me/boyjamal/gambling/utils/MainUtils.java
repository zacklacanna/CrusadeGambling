package me.boyjamal.gambling.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import me.boyjamal.gambling.slots.SlotItems;
import me.boyjamal.gambling.utils.GuiItem;
import me.boyjamal.gambling.utils.StorageManager;

public class MainUtils {
	
	public static String prefix = chatColor("&e&o&lCrusade&b&o&lMC");

	public static String chatColor(String message)
	{
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	public static List<String> listColor(List<String> messages)
	{
		List<String> newList = new ArrayList<>();
		for (String each : messages)
		{
			newList.add(chatColor(each));
		}
		return newList;
	}
	
	public static ItemStack getSlotItem(List<SlotItems> items)
	{
		double total = 0.0;
		double max = Integer.MIN_VALUE;
		double min = Integer.MAX_VALUE;
		for (SlotItems temp : items)
		{
			if (temp.getChance() > max)
			{
				max = temp.getChance();
			}
			
			if (temp.getChance() < min)
			{
				min = temp.getChance();
			}
			total += temp.getChance();
		}
		double rand = (int)(Math.random()*total) + 1;
		double current = 0.0;
		for (SlotItems temp : items)
		{
			current += temp.getChance();
			if (rand <= current)
			{
				return temp.getMaterial();
			}
		}
		return null;
	}
	
	public static boolean isFirstSlotItem(GuiItem item)
	{
		int min = Integer.MAX_VALUE;
		if (StorageManager.getSlotSettings() != null)
		{
			for (GuiItem items : StorageManager.getSlotSettings().getGui().getItems())
			{
				if (items.getRow() == item.getRow() && item.getRow() == 1)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static int getFirstSlot(GuiItem item)
	{
		if (StorageManager.getSlotSettings() != null)
		{
			for (GuiItem items : StorageManager.getSlotSettings().getGui().getItems())
			{
				if (item.getRow() == items.getRow())
				{
					return items.getSlot();
				}
			}
		}
		return -1;
	}
	
	public static boolean isBottomSlotItem(GuiItem item)
	{
		int max = Integer.MIN_VALUE;
		if (StorageManager.getSlotSettings() != null)
		{
			for (GuiItem items : StorageManager.getSlotSettings().getGui().getItems())
			{
				if (items.getRow() == item.getRow() && items.getRow() != 0)
				{
					if (items.getSlot() > max)
					{
						max = items.getSlot();
					}
				}
			}
		}
		if (max == item.getSlot())
		{
			return true;
		}
		return false;
	}
	
	public static int getMaxPositions(List<GuiItem> items)
	{
		int max = 0;
		for (GuiItem list : items)
		{
			if (list.getPosition() > max)
			{
				max = list.getPosition();
			}
		}
		return max;
	}
	
}
