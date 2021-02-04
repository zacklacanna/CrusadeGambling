package me.boyjamal.gambling.coinflip;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.boyjamal.gambling.Main;
import me.boyjamal.gambling.coinflip.ActiveCoinFlip;
import me.boyjamal.gambling.coinflip.CoinFlip;
import me.boyjamal.gambling.coinflip.CoinFlipManager;
import me.boyjamal.gambling.utils.MainUtils;
import me.boyjamal.gambling.utils.StorageManager;

public class CoinFlipCMD implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player p = (Player)sender;
			if (StorageManager.getCoinSettings() == null)
			{
				p.sendMessage(MainUtils.chatColor(MainUtils.prefix + " &7&oCoinFlip currently &c&nDISABLED"));
				p.playSound(p.getLocation(),Sound.FIZZ,15,1);
				return true;
			}
			if (args.length == 0)
			{
				for (ActiveCoinFlip flip : CoinFlipManager.getStarted())
				{
					if (p.getName().equalsIgnoreCase(flip.getPlayerOne().getName()) ||
							p.getName().equalsIgnoreCase(flip.getPlayerTwo().getName()))
					{
						p.sendMessage(MainUtils.chatColor("&c&n&LERROR &7You are already in an Active CoinFlip!"));
						return true;
					}
				}
				
				if (CoinFlipManager.getQueued().size() == 0)
				{
					for (String line : StorageManager.getCoinSettings().getMessages().getEmpty())
					{
						p.sendMessage(MainUtils.chatColor(line));
					}
					p.playSound(p.getLocation(),Sound.FIZZ,15,1);
					return true;
				} else {
					p.openInventory(CoinFlipManager.queuedCoinFlip());
					Bukkit.getScheduler().runTaskLater(Main.getInstance(),new Runnable() {
						public void run()
						{
							p.updateInventory();
						}
					},2L);
					p.playSound(p.getLocation(),Sound.BAT_TAKEOFF,15,1);
					return true;
				}
			} else if (args.length == 1) {
				double amount;
				try {
					amount = Integer.parseInt(args[0]);
				} catch (Exception e) {
					for (String line : StorageManager.getCoinSettings().getMessages().invalidNum())
					{
						p.sendMessage(MainUtils.chatColor(line).replaceAll("%num%",args[0]));
					}
					p.playSound(p.getLocation(),Sound.FIZZ,15,1);
					return true;
				}
				
				if (amount > StorageManager.getCoinSettings().getMax() && StorageManager.getCoinSettings().getMax() != -1)
				{
					for (String line : StorageManager.getCoinSettings().getMessages().getOverMax())
					{
						p.sendMessage(MainUtils.chatColor(line).replaceAll("%num%",args[0]));
					}
					p.playSound(p.getLocation(),Sound.FIZZ,15,1);
					return true;
				} else if (amount < StorageManager.getCoinSettings().getMin() && StorageManager.getCoinSettings().getMin() != 1) {
					for (String line : StorageManager.getCoinSettings().getMessages().getUnderMin())
					{
						p.sendMessage(MainUtils.chatColor(line).replaceAll("%num%",args[0]));
					}
					p.playSound(p.getLocation(),Sound.FIZZ,15,1);
					return true;
				}
				
				if (amount <= 0)
				{
					for (String line : StorageManager.getCoinSettings().getMessages().getUnderMin())
					{
						p.sendMessage(MainUtils.chatColor(line));
					}
					p.playSound(p.getLocation(),Sound.FIZZ,15,1);
					return true;
				}
				
				if (CoinFlipManager.getQueued().size() >= 54)
				{
					for (String line : StorageManager.getCoinSettings().getMessages().getFull())
					{
						p.sendMessage(MainUtils.chatColor(line).replaceAll("%num%",args[0]));
					}
					p.playSound(p.getLocation(),Sound.FIZZ,15,1);
					return true;
				}
				
				if (!(Main.getInstance().getEco().withdrawPlayer(p,amount).transactionSuccess()))
				{
					for (String line : StorageManager.getCoinSettings().getMessages().funds())
					{
						p.sendMessage(MainUtils.chatColor(line).replaceAll("%num%",args[0]));
					}
					p.playSound(p.getLocation(),Sound.FIZZ,15,1);
					return true;
				} else {
					CoinFlip flip = new CoinFlip(p,amount,false);
					CoinFlipManager.getQueued().add(flip);
					p.openInventory(CoinFlipManager.queuedCoinFlip());
					p.updateInventory();
					Bukkit.getScheduler().runTaskLater(Main.getInstance(),new Runnable() {
						public void run()
						{
							p.updateInventory();
						}
					},2L);
					p.playSound(p.getLocation(),Sound.BAT_TAKEOFF,500,500);
					for (String line : StorageManager.getCoinSettings().getMessages().getAdded())
					{
						p.sendMessage(MainUtils.chatColor(line).replaceAll("%num%",args[0]));
					}
					p.playSound(p.getLocation(),Sound.FIZZ,15,1);
					return true;
				}
				
			}
		}
		return true;
	}
	
}
