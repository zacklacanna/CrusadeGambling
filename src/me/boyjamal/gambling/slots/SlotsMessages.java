package me.boyjamal.gambling.slots;

import java.util.List;

public class SlotsMessages {

	private List<String> funds;
	private List<String> win;
	private List<String> lose;
	private List<String> winT;
	private List<String> loseT;
	
	public SlotsMessages(List<String> funds, List<String> win, List<String> lose, List<String> winT, List<String> loseT)
	{
		this.funds = funds;
		this.win = win;
		this.lose = lose;
		this.winT = winT;
		this.loseT = loseT;
	}
	
	public List<String> getFunds()
	{
		return funds;
	}
	
	public List<String> getWinTitles()
	{
		return winT;
	}
	
	public List<String> getLoseTitles()
	{
		return loseT;
	}
	
	public List<String> getLose()
	{
		return lose;
	}
	
	public List<String> getWin()
	{
		return win;
	}
	
}
