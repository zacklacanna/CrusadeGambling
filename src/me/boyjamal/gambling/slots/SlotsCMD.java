package me.boyjamal.gambling.slots;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.boyjamal.gambling.Main;
import me.boyjamal.gambling.slots.SlotsEvent;
import me.boyjamal.gambling.slots.SlotsSettings;
import me.boyjamal.gambling.utils.MainUtils;
import me.boyjamal.gambling.utils.StorageManager;

public class SlotsCMD implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(MainUtils.chatColor(MainUtils.prefix + " &7&oPlayers can only utilize this command!"));
			return true;
		}
		Player p = (Player)sender;
		if (StorageManager.getSlotSettings() != null)
		{
			SlotsSettings mang = StorageManager.getSlotSettings();
			if (Main.getInstance().getEco().withdrawPlayer(p,mang.getCost()).transactionSuccess())
			{
				Inventory inv = StorageManager.getSlotSettings().getGui().openInventory();
				p.openInventory(inv);
				
				SlotsEvent event = new SlotsEvent(p,mang.getGui(),inv,mang.getLength()+1);
				Bukkit.getServer().getPluginManager().callEvent(event);
				return true;
			} else {
				if (StorageManager.getSlotSettings() != null)
				{
					for (String line : StorageManager.getSlotSettings().getMessages().getFunds())
					{
						p.sendMessage(MainUtils.chatColor(line)
								.replaceAll("%cost%",String.valueOf(mang.getCost()))
								.replaceAll("%player%",p.getName()));
					}
				}
				return true;
			}
		} else {
			p.sendMessage(MainUtils.chatColor(MainUtils.prefix + " &7&oSlots are currently disabled!"));
			return true;
		}
	}

}
