package me.boyjamal.gambling.coinflip;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.boyjamal.gambling.coinflip.CoinRewards;

public class RewardsTask implements Runnable {
	
	private boolean win;
	private Player p;
	
	public RewardsTask(Player p, boolean win)
	{
		this.win = win;
		this.p = p;
	}
	
	int count = 0;
	public void run()
	{
		if (count == 7)
		{
			if (CoinRewards.rewardsTask.containsKey(p.getUniqueId().toString()))
			{
				int id = CoinRewards.rewardsTask.get(p.getUniqueId().toString());
				Bukkit.getScheduler().cancelTask(id);
				CoinRewards.rewardsTask.remove(p.getUniqueId().toString());
				return;
			}
		} else {
			if (win)
			{
				p.playSound(p.getLocation(),Sound.LEVEL_UP,500,500);
			} else {
				p.playSound(p.getLocation(),Sound.ANVIL_BREAK,500,500);
			}
			count++;
		}
	}
	
}
