package me.boyjamal.gambling.coinflip;

import org.bukkit.entity.Player;

public class ActiveCoinFlip {

	private Player one;
	private Player two;
	private double flipCost;
	private boolean started;
	private double prize;
	
	public ActiveCoinFlip(Player one, Player two, double flipCost, boolean started)
	{
		this.one = one;
		this.two = two;
		this.flipCost = flipCost;
		started = true;
		prize = flipCost*2;
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
	
	public Player getPlayerTwo()
	{
		return two;
	}
	
	public double getPrize()
	{
		return prize;
	}
	
}
