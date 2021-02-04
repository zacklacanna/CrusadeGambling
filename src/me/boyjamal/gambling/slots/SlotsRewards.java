package me.boyjamal.gambling.slots;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import com.connorlinfoot.titleapi.TitleAPI;

import me.boyjamal.gambling.Main;
import me.boyjamal.gambling.slots.RewardsEvent;
import me.boyjamal.gambling.slots.SlotsEvent;
import me.boyjamal.gambling.slots.SlotsListener;
import me.boyjamal.gambling.slots.RewardsTask;
import me.boyjamal.gambling.slots.SlotItems;
import me.boyjamal.gambling.utils.GuiItem;
import me.boyjamal.gambling.utils.MainUtils;
import me.boyjamal.gambling.utils.StorageManager;

public class SlotsRewards implements Listener {

	public static boolean checkRewards(SlotsEvent e)
	{
		String key = e.getPlayer().getUniqueId().toString();
		if (SlotsEvent.currentItems.get(key) != null)
		{
			List<GuiItem> items = SlotsEvent.currentItems.get(key);
			HashMap<Integer,List<GuiItem>> winners = new HashMap<>();
			int maxPos = MainUtils.getMaxPositions(items);
			
			int currentRow = 1;
			for (GuiItem row : items)
			{
				boolean finished = false;
				int counter = 0;
				List<GuiItem> eachRow = new ArrayList<>();
				for (GuiItem pos : items)
				{
					if (row.getRow() == pos.getRow() && row.getRow() == currentRow)
					{
						if (row.getItem().getType() == pos.getItem().getType())
						{
							if (finished)
							{
								eachRow.add(pos);
								winners.put(currentRow,eachRow);
								break;
							} else {
								if (counter >= maxPos-2)
								{
									finished = true;
									eachRow.add(pos);
									continue;
								} else {
									counter++;
									eachRow.add(pos);
									continue;
								}
							}
						} else {
							break;
						}
					}
				}
				currentRow++;
			}
			
			if (winners.size() != 0)
			{
				if (SlotsListener.activeSlots.containsKey(key))
				{
					System.out.println("cancel");
					int cancel = SlotsListener.activeSlots.get(key);
					Bukkit.getScheduler().cancelTask(cancel);
					SlotsListener.activeSlots.remove(key);
				}
				
				Bukkit.getScheduler().runTaskLater(Main.getInstance(),new Runnable() {
					public void run()
					{
						rewardAnimation(e.getPlayer(),e.getInv(),winners.values());
					}
				},5L);
				
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					public void run()
					{
						double total = 0.0;
						for (List<GuiItem> each : winners.values())
						{
							for (GuiItem both : each)
							{
								SlotItems val = SlotItems.getSlotItem(both.getItem());
								Main.getInstance().getEco().depositPlayer(e.getPlayer(), val.getReward());
								total += val.getReward();
							}
						}
						
						for (String message : StorageManager.getSlotSettings().getMessages().getWin())
						{
							e.getPlayer().sendMessage(MainUtils.chatColor(message
									.replaceAll("%money%", String.valueOf(total))
									.replaceAll("%player%", e.getPlayer().getName())));
						}
						
						List<String> winTitle = StorageManager.getSlotSettings().getMessages().getWinTitles();
						TitleAPI.sendFullTitle(e.getPlayer(), 5, 20, 5,
								MainUtils.chatColor(winTitle.get(0)), MainUtils.chatColor(winTitle.get(1)
										.replaceAll("%money%",String.valueOf(total))
										.replaceAll("%player%",e.getPlayer().getName())));
						e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.PISTON_EXTEND,15,1);
						Bukkit.getScheduler().runTaskLater(Main.getInstance(),new Runnable() {
							public void run()
							{
								e.getPlayer().playSound(e.getPlayer().getLocation(),Sound.PISTON_RETRACT,15,1);
							}
						},5L);
					}
				}, 101L);
				return true;
			} else {
				e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.FIZZ, 15, 1);
				Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
					public void run()
					{
						e.getPlayer().closeInventory();
						for (String message : StorageManager.getSlotSettings().getMessages().getLose())
						{
							e.getPlayer().sendMessage(MainUtils.chatColor(message
									.replaceAll("%player%", e.getPlayer().getName())));
						}
						
						List<String> lossTitle = StorageManager.getSlotSettings().getMessages().getLoseTitles();
						TitleAPI.sendFullTitle(e.getPlayer(), 5, 20, 5,
								MainUtils.chatColor(lossTitle.get(0)), MainUtils.chatColor(lossTitle.get(1)
										.replaceAll("%player%",e.getPlayer().getName())));
					}
				}, 5L);
				
			}
		}
		return false;
	}
	
	@EventHandler
	public void onSlotsReward(RewardsEvent e)
	{
		e.getPlayer().updateInventory();
		int id = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), new RewardsTask(e), 0, (long)10).getTaskId();
		SlotsListener.activeSlots.put(e.getPlayer().getUniqueId().toString(),id);
	}
	
	public static void rewardAnimation(Player p, Inventory inv, Collection<List<GuiItem>> items)
	{
		RewardsEvent evt = new RewardsEvent(p,inv,items);
		Bukkit.getPluginManager().callEvent(evt);
	}
	
}

