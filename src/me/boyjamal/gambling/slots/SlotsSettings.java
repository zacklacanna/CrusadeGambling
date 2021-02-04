package me.boyjamal.gambling.slots;

import java.util.List;

import me.boyjamal.gambling.slots.SlotItems;
import me.boyjamal.gambling.slots.SlotsMessages;
import me.boyjamal.gambling.utils.GuiManager;

public class SlotsSettings {
	
	private SlotsMessages msgs;
	private GuiManager gui;
	private List<SlotItems> items;
	private double cost;
	private double length;
	private boolean animation;
	
	public SlotsSettings(SlotsMessages msgs, GuiManager gui, List<SlotItems> items, double cost, double length, boolean animation)
	{
		this.msgs = msgs;
		this.gui = gui;
		this.items = items;
		this.cost = cost;
		this.length = length;
		this.animation = animation;
	}
	
	public SlotsMessages getMessages()
	{
		return msgs;
	}
	
	public GuiManager getGui()
	{
		return gui;
	}
	
	public List<SlotItems> getItems()
	{
		return items;
	}
	
	public double getCost()
	{
		return cost;
	}
	
	public double getLength()
	{
		return length;
	}
	
	public boolean hasAnimation()
	{
		return animation;
	}

}
