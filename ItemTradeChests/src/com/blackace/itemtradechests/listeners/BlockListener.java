package com.blackace.itemtradechests.listeners;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;

import com.blackace.itemtradechests.ItemTradeChests;

public class BlockListener implements Listener{

	ItemTradeChests plugin;
	
	public BlockListener(ItemTradeChests instance)
	{
		plugin = instance;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public static HashMap<Block,Block> chestlist;

	@EventHandler
	public void onSignChangeEvent(SignChangeEvent e)
	{
		if(!(e.getLine(0).equalsIgnoreCase("[TradeChest]"))) return;
		if(e.getPlayer().isOp())
		{
			Block b = e.getBlock();
			Block c = checkForChest(b);
			if(c == null) {
				e.getPlayer().sendMessage(ChatColor.DARK_RED + "No chest found adajacent to this sign! Chests can not be placed above or below the sign.");
				return;
			}
			e.setLine(0, "[TradeChest]");
			e.setLine(1, "Right click");
			e.setLine(2, "for item list");
			String line3 = e.getLine(3);
			e.setLine(3, line3);
			chestlist.put(b, c);

		}
		else
		{
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.DARK_RED + "You don't have permission to make this sign type!");
			return;
		}

	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent e)
	{
		//Remove Signs from list
		if(e.getBlock().getState() instanceof Sign) 
		{
			Block b = e.getBlock();
			if(chestlist.containsKey(b))
			{
				if(e.getPlayer().isOp())
				{
					chestlist.remove(b);
				}
				else 
				{
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.DARK_RED + "You don't have permission to remove this block!");
					return;
				}
			}
		}

		//Remove Chests from list
		if(e.getBlock().getState() instanceof Chest)
		{
			Block c = e.getBlock();
			Collection<Block> chests = chestlist.values();
			
			if(chests.contains(c))
			{
				if(e.getPlayer().isOp())
				{
					Collection<Block> chestSet = chestlist.values();
					chestSet.remove(c);
				}
				else 
				{
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.DARK_RED + "You don't have permission to remove this block!");
					return;
				}
			}

		}
	}

	private Block checkForChest(Block b)
	{
		Block n = b.getRelative(BlockFace.NORTH);
		Block w = b.getRelative(BlockFace.WEST);
		Block e = b.getRelative(BlockFace.EAST);
		Block s = b.getRelative(BlockFace.SOUTH);

		if(n.getType() == Material.CHEST)
		{
			return n;
		}
		if(w.getType() == Material.CHEST)
		{
			return w;
		}
		if(e.getType() == Material.CHEST)
		{
			return e;
		}
		if(s.getType() == Material.CHEST)
		{
			return s;
		}

		return null;
	}


}
