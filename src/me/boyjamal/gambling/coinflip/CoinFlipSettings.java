package me.boyjamal.gambling.coinflip;

public class CoinFlipSettings {
	
	private double min;
	private double max;
	private CoinFlipMessages mes;
	
	public CoinFlipSettings(double min, double max, CoinFlipMessages mes)
	{
		this.min = min;
		this.max = max;
		this.mes = mes;
	}
	
	public double getMin()
	{
		return min;
	}
	
	public double getMax()
	{
		return max;
	}
	
	public CoinFlipMessages getMessages()
	{
		return mes;
	}

}
