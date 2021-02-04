package me.boyjamal.gambling.coinflip;

import java.util.List;

public class CoinFlipMessages {

	private List<String> help;
	private List<String> empty;
	private List<String> full;
	private List<String> invalidNum;
	private List<String> funds;
	private List<String> added;
	private List<String> min;
	private List<String> max;
	private List<String> tWin;
	private List<String> tLose;
	
	public CoinFlipMessages(List<String> help, List<String> empty, List<String> full, List<String> invalidNum,
			List<String> funds, List<String> added, List<String> min, List<String> max, List<String> tWin, List<String> tLose)
	{
		this.help = help;
		this.empty = empty;
		this.full = full;
		this.invalidNum = invalidNum;
		this.funds = funds;
		this.added = added;
		this.min = min;
		this.max = max;
		this.tWin = tWin;
		this.tLose = tLose;
	}
	
	public List<String> getTitleWin()
	{
		return tWin;
	}
	
	public List<String> getTitleLose()
	{
		return tLose;
	}
	
	public List<String> getHelp()
	{
		return help;
	}
	
	public List<String> getOverMax()
	{
		return max;
	}
	
	public List<String> getUnderMin()
	{
		return min;
	}
	
	public List<String> getEmpty()
	{
		return empty;
	}
	
	public List<String> getFull()
	{
		return full;
	}
	
	public List<String> invalidNum()
	{
		return invalidNum;
	}
	
	public List<String> funds()
	{
		return funds;
	}
	
	public List<String> getAdded()
	{
		return added;
	}
}

