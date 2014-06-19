package com.blackace.itemtradechests.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.blackace.itemtradechests.ItemTradeChests;

public class PlayerListener implements Listener{


	ItemTradeChests plugin;
	Logger log = Bukkit.getLogger();
	
	
	public PlayerListener(ItemTradeChests instance){
		plugin = instance;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e)
	{
		//Handle Chest Permissions
		if(!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
		Block cB = e.getClickedBlock();
		BlockState cBS = cB.getState();
		Player p = e.getPlayer();
		if(cBS instanceof Chest)
		{
			if(BlockListener.chestlist.containsValue(cB))
			{
				if(p.isOp())
				{
					return;
				}
				else
				{
					e.setCancelled(true);
					p.closeInventory();
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to access this trade chest.");
				}	
			}
		}

		//Handle Sign Interactions
		if(cBS instanceof Sign)
		{
			
			if(BlockListener.chestlist.containsKey(cB))
			{
				Block chest = BlockListener.chestlist.get(cB);
				if(chest.getType() == Material.CHEST)
				{
					handleTradeEvent(p, chest, cB);
				}
				else
				{
					p.sendMessage(ChatColor.DARK_RED + "The chest appears to be broken or missing. Contact an admin if this is in error.");
					return;
				}
			}
		}



	}

	@SuppressWarnings("deprecation")
	private void handleTradeEvent(Player p, Block c, Block s)
	{
		//Assemble all variables
		Chest chest = (Chest) c.getState();
		Inventory i = chest.getInventory();
		Inventory pI = p.getInventory();
		ItemStack[] contentsArray = i.getContents();
		ArrayList<ItemStack> chestContents = itemTransfer(contentsArray);

		//Ensure chest has contents for trade
		if(chestContents.size() <= 1)
		{
			p.sendMessage(ChatColor.DARK_RED + "There is nothing to trade for in this chest.");
			return;
		}

		//Get item to be traded and set it to a variable
		int lastSlot = i.getSize() - 1;
		ItemStack itemToTrade = i.getItem(lastSlot);
		
		if(itemToTrade == null)
		{
			p.sendMessage(ChatColor.DARK_RED + "Chest does not have an item to trade or the chest isn't configured properly.");
			return;
		}

	
		//Set ArrayList to be just the items needed to trade
		chestContents.remove(itemToTrade);

		//Check for Items in players Inventory
		//ArrayList<ItemStack> playerArray = itemTransfer(pI.getContents()); <- might not need?
		HashMap<ItemStack, Boolean> test = new HashMap<ItemStack, Boolean>();
		for(ItemStack item : chestContents)
		{
			if(!(item == null))
			{
				boolean theTest = pI.containsAtLeast(item, item.getAmount());
				test.put(item, theTest);
			}
		}

		//Handle player not having an item
		if(test.containsValue(false))
		{
			p.sendMessage(ChatColor.RED + "You do not have all the required items. Here is a list, what you need is in red:");
			for(ItemStack item : chestContents)
			{
				if(!(item == null))
				{
					boolean pointer = test.get(item);
					if(pointer == true)
					{
						p.sendMessage(ChatColor.GREEN + "+" + ChatColor.AQUA + "" + item.getAmount() + "x " + ChatColor.GREEN + item.getTypeId() + ":" + item.getDurability());
					}
					if(pointer == false)
					{
						p.sendMessage(ChatColor.RED + "-" + ChatColor.AQUA + "" + item.getAmount() + "x " + ChatColor.RED + item.getTypeId() + ":" + item.getDurability());
					}
				}
			}
			return;
		}

		//Handle player having all items
		if(!test.containsValue(false))
		{
			p.sendMessage(ChatColor.GREEN + "You have the proper items and have recieved a:");
			for(ItemStack item : chestContents)
			{
				if(!(item == null))
				{
				pI.removeItem(item);
				}
			}
			p.sendMessage(ChatColor.GREEN + "+" + ChatColor.AQUA + "" + itemToTrade.getAmount() + "x " + ChatColor.GOLD + itemToTrade.getTypeId() + ":" + itemToTrade.getDurability());
			HashMap<Integer, ItemStack> results = pI.addItem(itemToTrade);

			//Handle if item is not added to inventory
			if(results.size() > 0)
			{
				p.getLocation().getWorld().dropItem(p.getLocation(), itemToTrade);
			}
		}





	}


	private ArrayList<ItemStack> itemTransfer(ItemStack[] input)
	{
		ArrayList<ItemStack> output = new ArrayList<ItemStack>();
		for(ItemStack item : input)
		{
			output.add(item);
		}
		return output;

	}
}
