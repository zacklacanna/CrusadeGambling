package me.boyjamal.gambling.coinflip;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.boyjamal.gambling.Main;
import me.boyjamal.gambling.coinflip.ActiveCoinFlip;
import me.boyjamal.gambling.coinflip.CoinFlip;
import me.boyjamal.gambling.utils.HeadAPI;
import me.boyjamal.gambling.utils.MainUtils;

public class CoinFlipManager {

	private static HashMap<String,Integer> activeCoinFlip = new HashMap<>();
	private static List<ActiveCoinFlip> activeFlips = new ArrayList<>();
	private static List<CoinFlip> queueCoinFlip = new ArrayList<>();
	public static String queueName = "&7            &e&nCoinFlip Menu";
	public static String activeName = "&7           &e&nActive CoinFlip";
	
	private static File coinData = new File(Main.getInstance().getDataFolder() + File.separator + "data" + File.separator + "coinflip.yml");
	
	public static Inventory queuedCoinFlip()
	{
		Inventory inv = Bukkit.createInventory(null,getSlots(queueCoinFlip.size()),MainUtils.chatColor(queueName));
		int slot = 0;
		for (CoinFlip each : queueCoinFlip)	
		{
			if (each.getPlayerOne() != null)
			{
				ItemStack item = playerHead(each.getPlayerOne());
				ItemMeta im = item.getItemMeta();
				im.setLore(Arrays.asList(MainUtils.chatColor("&7&oWager: &e&o" + each.getFlipCost())));
				item.setItemMeta(im);
				inv.setItem(slot, item);
				slot++;
			}
		}
		return inv;
	}
	
	public static void saveCoinFlips()
	{
		FileConfiguration config;
		if (!(coinData.exists()))
		{
			if (!(coinData.getParentFile().exists()))
			{
				coinData.getParentFile().mkdirs();
			}
			
			try {
				coinData.createNewFile();
				config = YamlConfiguration.loadConfiguration(coinData);
			} catch (Exception e) {
				Bukkit.getLogger().severe(MainUtils.chatColor("&e&lEraGambling &7&o>> &c&lERROR &7&oCould not create CoinFlip Data File!"));
				return;
			}
			
		} else {
			try {
				config = YamlConfiguration.loadConfiguration(coinData);
			} catch (Exception e) {
				Bukkit.getLogger().severe(MainUtils.chatColor("&e&lEraGambling &7&o>> &c&lERROR &7&oCould not create CoinFlip Data File!"));
				return;
			}
		}
		
		ConfigurationSection data = config.createSection("data");
		int id = 1;
		for (CoinFlip flip : getQueued())
		{
			try {
				data.set(id + ".playerName", flip.getPlayerOne().getUniqueId().toString());
				data.set(id + ".cost", flip.getFlipCost());
				id++;
			} catch (Exception e) {
				Bukkit.getLogger().severe(MainUtils.chatColor("&e&lEraGambling &7&o>> &c&lERROR &7&oCould not save CoinFlip Data File! (" + id+")"));
				continue;
			}
		}
		
		try {
			config.save(coinData);
		} catch (Exception e) {
			Bukkit.getLogger().severe(MainUtils.chatColor("&e&lEraGambling &7&o>> &c&lERROR &7&oCould not save CoinFlip Data File!"));
			return;
		}
	}
	
	public static void loadCoinFlips()
	{
		if (coinData.exists())
		{
			FileConfiguration config;
			try {
				config = YamlConfiguration.loadConfiguration(coinData);
			} catch (Exception e) {
				Bukkit.getLogger().severe(MainUtils.chatColor("&e&lEraGambling &7&o &c&lERROR &7&oCould not load CoinFlip Data File!"));
				return;
			}
			
			ConfigurationSection section = config.getConfigurationSection("data");
			if (section != null)
			{
				List<CoinFlip> each = new ArrayList<>();
				for (String keys : section.getKeys(false))
				{
					double amount;
					Player name;
					try {
						name = Bukkit.getPlayer(UUID.fromString(section.getString(keys + ".playerName")));
						amount = section.getDouble(keys + ".cost");
					} catch (Exception e) {
						Bukkit.getLogger().severe(MainUtils.chatColor("&e&lEraGambling &7&o>> &c&lERROR &7&oCould not load CoinFlip Data File! (" + keys + ")"));
						continue;
					}
					section.set(keys, null);
					each.add(new CoinFlip(name,amount,false));
				}
				queueCoinFlip = each;
				try {
					config.save(coinData);
				} catch (Exception e) {
					Bukkit.getLogger().severe(MainUtils.chatColor("&e&lEraGambling &7&o>> &c&lERROR &7&oCould not save CoinFlip Data File!"));
					return;
				}
			}
		}
	}
	
	private static int getSlots(int max)
	{
		int size = 9;
		if (max >= 9 && max <= 18)
		{
			size = 18;
		} else if (max >= 18 && max <= 27)
		{
			size = 27;
		} else if (max >= 27 && max <= 36)
		{
			size = 36;
		} else if (max >= 36 && max <= 45)
		{
			size = 45;
		} else if (max >= 45 && max <= 54)
		{
			size = 54;
		} else {
			size = 9;
		}
		return size;
	}
	
	public static List<CoinFlip> getQueued()
	{
		return queueCoinFlip;
	}
	
	public static Inventory mainInv(ActiveCoinFlip flip)
	{
		return Bukkit.createInventory(null, 27,MainUtils.chatColor(activeName));
	}
	
	public static boolean flipInv(ActiveCoinFlip flip, Inventory inv)
	{
		for (int i = 0; i <= 26; i++)
		{
			inv.setItem(i, glassPanes(false));
		}
		
		Random r = new Random();
		int next = r.nextInt(3) + 1;
		if (next == 1)
		{
			inv.setItem(13, playerHead(flip.getPlayerOne()));
			flip.getPlayerOne().openInventory(inv);
			flip.getPlayerTwo().openInventory(inv);
			
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				public void run()
				{
					flip.getPlayerOne().updateInventory();
					flip.getPlayerTwo().updateInventory();
				}
			}, 1L);
				
			return true;
		} else {
			inv.setItem(13, playerHead(flip.getPlayerTwo()));
			
			flip.getPlayerOne().openInventory(inv);
			flip.getPlayerTwo().openInventory(inv);
			
			Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				public void run()
				{
					flip.getPlayerOne().updateInventory();
					flip.getPlayerTwo().updateInventory();
				}
			}, 1L);
			
			return false;
		}
	}
	
	public static ItemStack glassPanes(boolean type)
	{
		ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE,1,(short)15);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(" ");
		item.setItemMeta(im);
		if (type)
		{
			im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(im);
			item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		}
		return item;
	}
	
	public static HashMap<String,Integer> getActiveCoinFlip()
	{
		return activeCoinFlip;
	}
	
	public static List<ActiveCoinFlip> getStarted()
	{
		return activeFlips;
	}
	
	public static ItemStack playerHead(Player p)
	{
		String id = HeadAPI.addPlayerHead(p.getName(), p.getUniqueId());
		ItemStack item = HeadAPI.getHead(id);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(MainUtils.chatColor("&e&n" + p.getName()));
		item.setItemMeta(meta);
		return item;
	}
	
}
