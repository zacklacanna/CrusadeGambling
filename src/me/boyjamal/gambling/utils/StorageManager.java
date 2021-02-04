package me.boyjamal.gambling.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.boyjamal.gambling.coinflip.CoinFlipManager;
import me.boyjamal.gambling.coinflip.CoinFlipMessages;
import me.boyjamal.gambling.coinflip.CoinFlipSettings;
import me.boyjamal.gambling.utils.GuiItem;
import me.boyjamal.gambling.utils.GuiManager;
import me.boyjamal.gambling.utils.MainUtils;
import me.boyjamal.gambling.slots.SlotsSettings;
import me.boyjamal.gambling.slots.SlotItems;
import me.boyjamal.gambling.slots.SlotsMessages;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.boyjamal.gambling.Main;

public class StorageManager {

	private static File coinFile = new File(Main.getInstance().getDataFolder() + File.separator + "guis" + File.separator + "coinflip.yml");
	private static YamlConfiguration coinYML;
	private static CoinFlipSettings coinSettings;
	
	private static File slotsFile = new File(Main.getInstance().getDataFolder() + File.separator + "guis" + File.separator + "slots.yml");
	private static YamlConfiguration slotsYML;
	private static SlotsSettings slotSettings;
	
	public static void loadFiles()
	{
		System.out.println("+=======================================+");
		Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + " &7&oStarting to load files!"));
		System.out.println(" ");
		
		Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + " &7&oStarting to load CoinFlip"));
		if (!(coinFile.exists()))
		{
			if (!(coinFile.getParentFile().exists()))
			{
				try {
					coinFile.getParentFile().mkdirs();
				} catch (Exception e) {
					Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + " &c&lERROR &7&oCould not load CoinFlip Files!"));
					e.printStackTrace();
				}
			}
			try {
				coinFile.createNewFile();
				copy(Main.getInstance().getResource("guis/coinflip.yml"),coinFile);
				coinYML = YamlConfiguration.loadConfiguration(coinFile);
				loadCoinflip();
				Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + " &7Successfully loaded CoinFlip!"));
			} catch (Exception e) {
				Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + " &c&lERROR &7&oCould not load CoinFlip Files!"));
				e.printStackTrace();
			}
		} else {
			try {
				coinFile.createNewFile();
				copy(Main.getInstance().getResource("guis/coinflip.yml"),coinFile);
				coinYML = YamlConfiguration.loadConfiguration(coinFile);
				loadCoinflip();
				Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + " &7Successfully loaded CoinFlip!"));
			} catch (Exception e) {
				Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + " &c&lERROR &7&oCould not load CoinFlip Files!"));
				e.printStackTrace();
			}
		}
		
		if (!(slotsFile.exists()))
		{
			try {
				slotsFile.createNewFile();
				copy(Main.getInstance().getResource("guis/slots.yml"),slotsFile);
				slotsYML = YamlConfiguration.loadConfiguration(slotsFile);
				loadSlots();
				Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + " Successfully loaded slots.yml"));
			} catch (Exception e) {
				for (int i = 0; i <= 5; i++)
				{
					Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + " Could not create slots.yml"));
				}
				Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + " Disabling slots.yml"));
			}
		} else {
			slotsYML = YamlConfiguration.loadConfiguration(slotsFile);
			loadSlots();
			Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + " Successfully loaded slots.yml"));
		}
		System.out.println("");
		System.out.println("+=======================================+");
	}
	
	private static void loadCoinflip()
	{
		double min;
		double max;
		ConfigurationSection mainSettings = coinYML.getConfigurationSection("settings");
		try {
			min = mainSettings.getDouble("min");
			max = mainSettings.getDouble("max");
		} catch (Exception e) {
			System.out.println(MainUtils.chatColor(MainUtils.prefix + "&7&o Invalid CoinFlip Setttings"));
			System.out.println(MainUtils.chatColor("&c&nDisabling CoinFlip"));
			return;
		}
			
		ConfigurationSection messages = coinYML.getConfigurationSection("messages");
		List<String> help = messages.getStringList("helpMessage");
		List<String> empty = messages.getStringList("emptyQueue");
		List<String> full = messages.getStringList("fullQueue");
		List<String> num = messages.getStringList("invalidNumber");
		List<String> funds = messages.getStringList("notEnoughFunds");
		List<String> add = messages.getStringList("addedQueue");
		List<String> overMin = messages.getStringList("underMin");
		List<String> overMax = messages.getStringList("overMax");
		List<String> titleWin = coinYML.getStringList("titles.winner");
		List<String> titleLoss = coinYML.getStringList("titles.loser");
			
		if (titleWin == null || titleWin.size() != 2)
		{
			System.out.println(MainUtils.chatColor(MainUtils.prefix + "&7&o Invalid Winner Title. Inputting Default!"));
			titleWin = Arrays.asList("&a&lWINNER","&7&oYou won $%money%");
		}
			
		if (titleLoss == null || titleLoss.size() != 2)
		{
			System.out.println(MainUtils.chatColor(MainUtils.prefix + "&7&o Invalid Loser Title. Inputting Default!"));
			titleLoss = Arrays.asList("&c&lLOSS","&7&oYou lost $%money%. Play again with /cf");
		}
		CoinFlipMessages mess = new CoinFlipMessages(help,empty,full,num,funds,add,overMin,overMax,titleWin,titleLoss);
		coinSettings = new CoinFlipSettings(min,max,mess);
		
		CoinFlipManager.loadCoinFlips();
	}
	
	
	private static void loadSlots()
	{
		if (slotsYML != null)
		{
			ConfigurationSection gui = slotsYML.getConfigurationSection("gui");
			if (gui != null)
			{
				List<GuiItem> items = new ArrayList<>();
				for (String indvSlot : gui.getKeys(false))
				{
					boolean head = gui.getBoolean(indvSlot + ".head");
					ItemStack item;
					if (head)
					{
						HeadDatabaseAPI api = new HeadDatabaseAPI();
						try {
							item = api.getItemHead(gui.getString(indvSlot + ".headID"));
						} catch (Exception e) {
							for (int i = 0; i <=4;i++)
							{
								Bukkit.getLogger().severe(MainUtils.prefix + "&7&o Error loading in item for slot " + indvSlot + ".");
							}
							Bukkit.getLogger().severe("Skipping Item!");
							continue;
						}
					} else {
						try {
							item = new ItemStack(Material.getMaterial(gui.getInt(indvSlot + ".itemId")),1,Byte.valueOf(gui.getString(indvSlot + ".itemData")));
						} catch (Exception e) {
							for (int i = 0; i <=4;i++)
							{
								Bukkit.getLogger().severe(MainUtils.prefix + "&7&o Error loading in item for slot " + indvSlot + ".");
							}
							Bukkit.getLogger().severe("Skipping Item!");
							continue;
						}
					}
					
					ItemMeta im = item.getItemMeta();
					im.setDisplayName(MainUtils.chatColor(gui.getString(indvSlot + ".name")));
					im.setLore(MainUtils.listColor(gui.getStringList(indvSlot + ".lore")));
					boolean glow = gui.getBoolean(indvSlot + ".glow");
					int row = gui.getInt(indvSlot + ".row");
					int pos = gui.getInt(indvSlot + ".position");
					if (glow)
					{
						im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
						item.setItemMeta(im);
						item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
					}
					item.setItemMeta(im);
					items.add(new GuiItem(gui.getStringList(indvSlot + ".actions"),(Integer.valueOf(indvSlot)-1),item,gui.getString(indvSlot + ".type"),row,pos));
				}
				
				System.out.println(MainUtils.chatColor(MainUtils.prefix + "&7&o Successfully loaded SlotsGUI"));
				GuiManager slotsGUI = new GuiManager(slotsYML.getString("settings.name"),slotsYML.getInt("settings.slots"),items);
				
				List<String> newSlotItems = slotsYML.getStringList("settings.slotItems");
				List<SlotItems> slotItems = new ArrayList<SlotItems>();
				for (String each : newSlotItems)
				{
					
					String[] values = each.split(";");
					double chance;
					ItemStack mat;
					double reward;
					if (values.length == 3)
					{
						try {
							chance = Double.valueOf(values[2]);
							reward = Double.valueOf(values[1]);
							String[] total = values[0].split("_");
							mat = new ItemStack(Material.getMaterial(Integer.valueOf(total[0])),1,Byte.valueOf(total[1]));
						} catch (Exception e) {
							Bukkit.getLogger().severe(MainUtils.prefix + "&7&o Invalid SlotItem (" + each + ")");
							continue;
						}
						slotItems.add(new SlotItems(reward,mat,chance));
					} else {
						Bukkit.getLogger().severe(MainUtils.prefix + "&7&o Invalid SlotItem (" + each + ")");
						continue;
					}
				}
				
				ConfigurationSection messages = slotsYML.getConfigurationSection("messages");
				if (messages == null)
				{
					Bukkit.getLogger().severe(MainUtils.prefix + "&7&o Could not load Messages");
					return;
				}
				
				List<String> funds = messages.getStringList("funds");
				List<String> win = messages.getStringList("win");
				List<String> lose = messages.getStringList("lose");
				List<String> winT = messages.getStringList("winTitle");
				List<String> loseT = messages.getStringList("loseTitle");
				if (winT.size() != 2)
				{
					Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + "&7&o Invalid Win Title. Inputing Default"));
					List<String> newTitles = new ArrayList<>();
					newTitles.add("&a&lCongradulations");
					newTitles.add("&7&oYou won $%money%");
					winT = newTitles;
				}
				
				if (loseT.size() != 2)
				{
					Bukkit.getLogger().severe(MainUtils.chatColor(MainUtils.prefix + "&7&o Invalid Loss Title. Inputing Default"));
					List<String> newTitles = new ArrayList<>();
					newTitles.add("&c&lLoss");
					newTitles.add("&7&oYou did not win. Play again with /slots");
					loseT = newTitles;
				}
				SlotsMessages slotMes = new SlotsMessages(funds,win,lose,winT,loseT);
				
				ConfigurationSection settings = slotsYML.getConfigurationSection("settings");
				if (settings != null)
				{
					double cost = settings.getDouble("cost");
					double length = settings.getDouble("length");
					boolean animation = settings.getBoolean("animation");
					slotSettings = new SlotsSettings(slotMes,slotsGUI,slotItems,cost,length,animation);
				}
			}
		}
	}
	
	private static void copy(InputStream in, File file)
	{
		try {
			OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf)) > 0)
            {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static CoinFlipSettings getCoinSettings()
	{
		return coinSettings;
	}
	
	public static SlotsSettings getSlotSettings()
	{
		return slotSettings;
	}
	
}
