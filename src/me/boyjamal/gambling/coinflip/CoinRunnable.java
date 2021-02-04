package me.boyjamal.gambling.coinflip;

import org.bukkit.Bukkit;

import me.boyjamal.gambling.Main;
import me.boyjamal.gambling.coinflip.CoinFlipManager;
import me.boyjamal.gambling.coinflip.CoinRewards;
import me.boyjamal.gambling.coinflip.FlipEvent;

public class CoinRunnable implements Runnable {
	
	private FlipEvent e;
	
	public CoinRunnable(FlipEvent e)
	{
		this.e = e;
	}
	
	int count = 0;
	public void run()
	{
		if (count >= e.getMaxCount())
		{	
			String key = e.getFlip().getPlayerTwo().getUniqueId().toString();
			if (CoinFlipManager.getActiveCoinFlip().containsKey(key))
			{
				int id = CoinFlipManager.getActiveCoinFlip().get(key);
				Bukkit.getScheduler().cancelTask(id);
			}
			
			if (CoinFlipManager.getStarted().contains(e.getFlip()))
			{
				CoinFlipManager.getStarted().remove(e.getFlip());
			}
			
			Bukkit.getScheduler().runTaskLater(Main.getInstance(),new Runnable() {
				public void run()
				{
					e.getFlip().getPlayerOne().closeInventory();
					e.getFlip().getPlayerTwo().closeInventory();
					CoinRewards.checkRewards(e.getFlip(),e.getInv());
					return;
				}
			},10L);
		} else {
			e.updateInv();
			Bukkit.getServer().getScheduler().runTaskLater(Main.getInstance(),new Runnable() {
				public void run()
				{
					e.getFlip().getPlayerOne().updateInventory();
					e.getFlip().getPlayerTwo().updateInventory();
				}
			},1L);
			count++;
		}
	}

}
