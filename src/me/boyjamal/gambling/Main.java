package me.boyjamal.gambling;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.boyjamal.gambling.utils.StorageManager;
import net.milkbowl.vault.economy.Economy;
import me.boyjamal.gambling.coinflip.CoinFlipManager;
import me.boyjamal.gambling.utils.MainUtils;
import me.boyjamal.gambling.utils.HeadAPI;
import me.boyjamal.gambling.coinflip.CoinFlipListener;
import me.boyjamal.gambling.coinflip.CoinFlipCMD;
import me.boyjamal.gambling.slots.SlotsListener;
import me.boyjamal.gambling.slots.SlotsRewards;
import me.boyjamal.gambling.slots.SlotsCMD;

public class Main extends JavaPlugin {
	
	private static Main instance;
	private Economy econ = null;
	
	public void onEnable()
	{
		instance = this;
		
		if (!setupEconomy() ) {
            Bukkit.getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
		StorageManager.loadFiles();
		registerCommands();
		registerListeners();
		System.out.println(MainUtils.prefix + MainUtils.chatColor(" Successfully enabled Plugin"));
	}
	
	public void onDisable()
	{
		CoinFlipManager.saveCoinFlips();
	}
	
	public void registerCommands()
	{
		getCommand("coinflip").setExecutor(new CoinFlipCMD());
		getCommand("slots").setExecutor(new SlotsCMD());
	}
	
	public void registerListeners()
	{
		Bukkit.getPluginManager().registerEvents(new CoinFlipListener(),this);
		Bukkit.getPluginManager().registerEvents(new HeadAPI(),this);
		Bukkit.getPluginManager().registerEvents(new SlotsListener(),this);
		Bukkit.getPluginManager().registerEvents(new SlotsRewards(),this);
		//slots rewards
	}
	
	public static Main getInstance()
	{
		return instance;
	}
	
	public Economy getEco()
	{
		return econ;
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

}
