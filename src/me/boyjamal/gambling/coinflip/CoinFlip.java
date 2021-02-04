package me.boyjamal.gambling.coinflip;

import org.bukkit.entity.Player;

public class CoinFlip {
	
	private Player one;
	private double flipCost;
	private boolean started;
	
	public CoinFlip(Player one, double flipCost, boolean started)
	{
		this.one = one;
		this.flipCost = flipCost;
		started = false;
	}
	
	public boolean getStarted()
	{
		return started;
	}
	
	public double getFlipCost()
	{
		return flipCost;
	}
	
	public void setStarted(boolean val)
	{
		started = val;
	}
	
	public Player getPlayerOne()
	{
		return one;
	}

}
