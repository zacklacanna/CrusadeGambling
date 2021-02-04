package me.boyjamal.gambling.coinflip;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.connorlinfoot.titleapi.TitleAPI;

import me.boyjamal.gambling.Main;
import me.boyjamal.gambling.coinflip.ActiveCoinFlip;
import me.boyjamal.gambling.coinflip.CoinFlip;
import me.boyjamal.gambling.utils.MainUtils;
import me.boyjamal.gambling.utils.StorageManager;

public class CoinRewards {
	
	private CoinFlip flip;
	private Inventory inv;
	
	public static HashMap<String,Integer> rewardsTask = new HashMap<>();
	
	public CoinRewards(CoinFlip flip, Inventory inv)
	{
		this.flip = flip;
		this.inv = inv;
	}
	
	public static void winningAnimation(Player p, ActiveCoinFlip flip)
	{
		if (StorageManager.getCoinSettings().getMessages() != null)
		{
			List<String> wTitle = StorageManager.getCoinSettings().getMessages().getTitleWin();
			TitleAPI.sendTitle(p,5,60,5,MainUtils.chatColor(wTitle.get(0)).replaceAll("%money%", String.valueOf(flip.getPrize())),
										MainUtils.chatColor(wTitle.get(1)).replaceAll("%money%", String.valueOf(flip.getPrize())));
		}
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 500, 500);
		Main.getInstance().getEco().depositPlayer(p, flip.getPrize());
	}
	
	public static void losingAnimation(Player p, ActiveCoinFlip flip)
	{
		if (StorageManager.getCoinSettings().getMessages() != null)
		{
			List<String> wTitle = StorageManager.getCoinSettings().getMessages().getTitleLose();
			TitleAPI.sendTitle(p,5,60,5,MainUtils.chatColor(wTitle.get(0)).replaceAll("%money%", String.valueOf(flip.getPrize())),
										MainUtils.chatColor(wTitle.get(1)).replaceAll("%money%", String.valueOf(flip.getPrize())));
		}
		p.playSound(p.getLocation(), Sound.ANVIL_LAND, 500, 500);
	}
	
	public static void checkRewards(ActiveCoinFlip flip, Inventory inv)
	{
		ItemStack item = inv.getItem(13);
		if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName())
		{
			String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
			if (name.equalsIgnoreCase(flip.getPlayerOne().getName()))
			{
				winningAnimation(flip.getPlayerOne(),flip);
				losingAnimation(flip.getPlayerTwo(),flip);
			} else if (name.equalsIgnoreCase(flip.getPlayerTwo().getName())) {
				winningAnimation(flip.getPlayerTwo(),flip);
				losingAnimation(flip.getPlayerOne(),flip);
			} else {
				return;
			}
		}
	}

}
