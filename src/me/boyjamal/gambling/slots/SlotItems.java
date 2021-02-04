package me.boyjamal.gambling.slots;

import org.bukkit.inventory.ItemStack;

import me.boyjamal.gambling.slots.SlotItems;
import me.boyjamal.gambling.utils.StorageManager;

public class SlotItems {
	
	private double reward;
	private ItemStack mat;
	private double chance;
	
	public SlotItems(double reward, ItemStack mat, double chance)
	{
		this.reward = reward;
		this.mat = mat;
		this.chance = chance;
	}
	
	public double getReward()
	{
		return reward;
	}
	
	public ItemStack getMaterial()
	{
		return mat;
	}
	
	public double getChance()
	{
		return chance;
	}
	
	public static SlotItems getSlotItem(ItemStack mat)
	{
		for (SlotItems each : StorageManager.getSlotSettings().getItems())
		{
			if (each.getMaterial().getType() == mat.getType())
			{
				return each;
			}
		}
		return null;
	}

}
