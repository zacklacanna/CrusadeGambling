package me.boyjamal.gambling.slots;

import java.util.Collection;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.boyjamal.gambling.utils.GuiItem;

public class RewardsEvent extends Event implements Cancellable {
	
	private Player p;
	private Inventory inv;
	private Collection<List<GuiItem>> items;
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	public RewardsEvent(Player p, Inventory inv, Collection<List<GuiItem>> items)
	{
		this.p = p;
		this.inv = inv;
		this.items = items;
	}
	
	public Player getPlayer()
	{
		return p;
	}
	
	public Inventory getInv()
	{
		return inv;
	}
	
	public Collection<List<GuiItem>> getWonItems()
	{
		return items;
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
	
	public Inventory updateInv()
	{
		for (List<GuiItem> eachRow : items)
		{
			for (GuiItem each : eachRow)
			{
				ItemStack slot = inv.getItem(each.getSlot());
				ItemMeta meta = slot.getItemMeta();

				if (slot.containsEnchantment(Enchantment.DURABILITY))
				{
					slot.removeEnchantment(Enchantment.DURABILITY);
				} else {
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					slot.setItemMeta(meta);
					slot.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
				}
				inv.setItem(each.getSlot(),slot);
			}	
		}
		p.updateInventory();
		return inv;
	}

}
