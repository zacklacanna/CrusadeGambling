package me.boyjamal.gambling.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FundsEvent implements Runnable {
	
	private Inventory inv;
	private ItemStack item;
	private ItemStack change;
	private int slot;
	private int time;
	private Player p;
	private double cost;
	
	public FundsEvent(Inventory inv, ItemStack change, ItemStack item, int slot, int time, Player p, double cost)
	{
		this.inv = inv;
		this.item = item;
		this.slot = slot;
		this.change = change;
		this.time = time;
		this.p = p;
		this.cost = cost;
	}
	
	public Double getCost()
	{
		return cost;
	}
	
	public Player getPlayer()
	{
		return p;
	}
	
	public Inventory getInv()
	{
		return inv;
	}
	
	public int getTime()
	{
		return time;
	}
	
	public ItemStack getItem()
	{
		return item;
	}
	
	public int getSlot()
	{
		return slot;
	}
	
	int count = 0;
	boolean on = true;
	public void run()
	{
		if (count >= time*4)
		{
			inv.setItem(slot,item);
			p.updateInventory();
		} else {
			inv.setItem(slot,change);
			p.updateInventory();
			if (on)
			{
				ItemMeta im = change.getItemMeta();
				im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				change.setItemMeta(im);
				change.addUnsafeEnchantment(Enchantment.DURABILITY,1);
				
				on = false;
			} else {
				if (change.containsEnchantment(Enchantment.DURABILITY))
				{
					change.removeEnchantment(Enchantment.DURABILITY);
				}
				on = true;
			}
			count++;
		}
	}

}
