package me.boyjamal.gambling.coinflip;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import me.boyjamal.gambling.Main;
import me.boyjamal.gambling.coinflip.ActiveCoinFlip;
import me.boyjamal.gambling.coinflip.CoinFlip;
import me.boyjamal.gambling.coinflip.CoinFlipManager;
import me.boyjamal.gambling.coinflip.CoinRunnable;
import me.boyjamal.gambling.coinflip.FlipEvent;
import me.boyjamal.gambling.utils.FundsEvent;
import me.boyjamal.gambling.utils.MainUtils;

public class CoinFlipListener implements Listener {
	
	@EventHandler
	public void onFlip(FlipEvent e)
	{
		int id = Bukkit.getScheduler().runTaskTimer(Main.getInstance(),new CoinRunnable(e),0L,3L).getTaskId();
		CoinFlipManager.getActiveCoinFlip().put(e.getFlip().getPlayerTwo().getUniqueId().toString(),id);
		return;
	}
	
	@EventHandler
	public void onActiveClick(InventoryClickEvent e)
	{
		if (MainUtils.chatColor(e.getInventory().getName()).equalsIgnoreCase(MainUtils.chatColor(CoinFlipManager.activeName)))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e)
	{
		if (MainUtils.chatColor(e.getInventory().getName()).equalsIgnoreCase(MainUtils.chatColor(CoinFlipManager.activeName)))
		{
			for (ActiveCoinFlip each : CoinFlipManager.getStarted())
			{
				if (each.getPlayerOne().getName().equalsIgnoreCase(e.getPlayer().getName()))
				{
					Bukkit.getScheduler().runTaskLater(Main.getInstance(),new Runnable() {
						public void run()
						{
							each.getPlayerOne().openInventory(e.getInventory());
						}
					},1L);
					return;
				} else if (each.getPlayerTwo().getName().equalsIgnoreCase(e.getPlayer().getName())) {
					Bukkit.getScheduler().runTaskLater(Main.getInstance(),new Runnable() {
						public void run()
						{
							each.getPlayerTwo().openInventory(e.getInventory());
						}
					},1L);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e)
	{
		if (MainUtils.chatColor(e.getInventory().getName()).equalsIgnoreCase(MainUtils.chatColor(CoinFlipManager.queueName)))
		{
			Player p = (Player)e.getWhoClicked();
			e.setCancelled(true);
			for (CoinFlip each : CoinFlipManager.getQueued())
			{
				ItemStack item = e.getCurrentItem();
				if (item != null && p != null && item.hasItemMeta())
				{
					ItemMeta im = item.getItemMeta();
					if (im.hasDisplayName() && im.hasLore() && each.getPlayerOne() != null)
					{
						String name = ChatColor.stripColor(im.getDisplayName());
						if (name.equalsIgnoreCase(each.getPlayerOne().getName()))
						{
							List<String> lore = im.getLore();
							if (lore.size() >= 1)
							{
								String val = ChatColor.stripColor(lore.get(0));
								val = val.replaceAll("Wager: ","");
								val = val.replace("$", "");
								double amount;
								try {
									amount = Double.parseDouble(val);
								} catch (Exception exc) {
									System.out.println(lore.get(0));
									System.out.println(val);
									return;
								}
								
								if (name.equalsIgnoreCase(p.getName()))
								{
									ItemStack error = new ItemStack(Material.REDSTONE_BLOCK);
									ItemMeta eIM = error.getItemMeta();
									eIM.setDisplayName(MainUtils.chatColor("&7      &c&nError"));
									eIM.setLore(Arrays.asList(MainUtils.chatColor("&7&oYou can not"),
											MainUtils.chatColor("&7&ocoinflip yourself!")));
									error.setItemMeta(eIM);
									
									int id = Bukkit.getScheduler().runTaskTimer(Main.getInstance(),new FundsEvent(e.getInventory(),error,item,e.getSlot(),3,p,amount),0L,5L).getTaskId();
									Bukkit.getScheduler().runTaskLater(Main.getInstance(),new Runnable()
									{
										public void run()
										{
											Bukkit.getScheduler().cancelTask(id);
										}
									},70L);
									return;
								}
								
								if (Main.getInstance().getEco().withdrawPlayer(p,amount).transactionSuccess())
								{
									ActiveCoinFlip active = new ActiveCoinFlip(each.getPlayerOne(),p,amount,true);
									if (!(p.getOpenInventory() instanceof PlayerInventory))
									{
										p.closeInventory();
									}
									if (!(each.getPlayerOne().getOpenInventory() instanceof PlayerInventory))
									{
										each.getPlayerOne().closeInventory();
									}
									CoinFlipManager.getQueued().remove(each);
									CoinFlipManager.getStarted().add(active);
									
									Inventory inv = CoinFlipManager.mainInv(active);
									boolean first = CoinFlipManager.flipInv(active,inv);
									FlipEvent evt = new FlipEvent(active,inv,first);
									Bukkit.getPluginManager().callEvent(evt);
									p.playSound(p.getLocation(),Sound.BAT_TAKEOFF,500,500);
									each.getPlayerOne().playSound(p.getLocation(),Sound.BAT_TAKEOFF,500,500);
									break;
								} else {
									ItemStack error = new ItemStack(Material.REDSTONE_BLOCK);
									ItemMeta eIM = error.getItemMeta();
									eIM.setDisplayName(MainUtils.chatColor("&7         &c&nError"));
									eIM.setLore(Arrays.asList(MainUtils.chatColor("&7&oYou do not have"),
											MainUtils.chatColor("&7&oenough funds to do this!"),"",
											MainUtils.chatColor("&c&nCost:&7&o $" + amount)));
									error.setItemMeta(eIM);
									
									int id = Bukkit.getScheduler().runTaskTimer(Main.getInstance(),new FundsEvent(e.getInventory(),error,item,e.getSlot(),3,p,amount),0L,5L).getTaskId();
									Bukkit.getScheduler().runTaskLater(Main.getInstance(),new Runnable()
									{
										public void run()
										{
											Bukkit.getScheduler().cancelTask(id);
										}
									},70L);
									return;
								}
							}
						}
					}
				}
			}		
		}
	}

}
