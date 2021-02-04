package me.boyjamal.gambling.coinflip;

import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;

import me.boyjamal.gambling.coinflip.ActiveCoinFlip;
import me.boyjamal.gambling.coinflip.CoinFlipManager;

public class FlipEvent extends Event implements Cancellable {
	
	private ActiveCoinFlip flip;
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private Inventory inv;
	private boolean start;
	
	public FlipEvent(ActiveCoinFlip flip,Inventory inv, boolean start)
	{
		this.flip = flip;
		this.inv = inv;
		this.start = start;
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
	
	public ActiveCoinFlip getFlip()
	{
		return flip;
	}
	
	public void setCancelled(boolean cancel)
	{
		this.cancelled = cancel;
	}
	
	public int getMaxCount()
	{
		Random r = new Random();
		return r.nextInt(105)+19;
	}
	
	public Inventory getInv()
	{
		return inv;
	}
	
	boolean enchant = true;
	public Inventory updateInv()
	{
			if (start)
			{
				for (int i = 0; i<= 26; i++)
				{
					if (i==13)
					{
						inv.setItem(13,CoinFlipManager.playerHead(flip.getPlayerOne()));
					} else {
						inv.setItem(i,CoinFlipManager.glassPanes(false));
					}
				}
				
				if (enchant)
				{
					enchant = false;
				} else {
					enchant = true;
				}
				start = false;
			} else {
				for (int i = 0; i<= 26; i++)
				{
					if (i==13)
					{
						inv.setItem(13,CoinFlipManager.playerHead(flip.getPlayerTwo()));
					} else {
						inv.setItem(i,CoinFlipManager.glassPanes(false));
					}
				}
				
				if (enchant)
				{
					enchant = false;
				} else {
					enchant = true;
				}
				start = true;
			}
			flip.getPlayerOne().playSound(flip.getPlayerOne().getLocation(),Sound.WOOD_CLICK,500,500);
			flip.getPlayerTwo().playSound(flip.getPlayerTwo().getLocation(),Sound.WOOD_CLICK,500,500);
		return inv;
	}

}
