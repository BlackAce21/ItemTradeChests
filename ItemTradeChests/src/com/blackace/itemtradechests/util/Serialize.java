package com.blackace.itemtradechests.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Serialize {
	
	
	
	 @SuppressWarnings("deprecation")
	public static String InventoryToString (Inventory invInventory)
	    {
	        String serialization = invInventory.getSize() + ";";
	        for (int i = 0; i < invInventory.getSize(); i++)
	        {
	            ItemStack is = invInventory.getItem(i);
	            if (is != null)
	            {
	                String serializedItemStack = new String();
	               
	                String isType = String.valueOf(is.getType().getId());
	                serializedItemStack += "t@" + isType;
	               
	                if (is.getDurability() != 0)
	                {
	                    String isDurability = String.valueOf(is.getDurability());
	                    serializedItemStack += ":d@" + isDurability;
	                }
	               
	                if (is.getAmount() != 1)
	                {
	                    String isAmount = String.valueOf(is.getAmount());
	                    serializedItemStack += ":a@" + isAmount;
	                }
	               
	                Map<Enchantment,Integer> isEnch = is.getEnchantments();
	                if (isEnch.size() > 0)
	                {
	                    for (Entry<Enchantment,Integer> ench : isEnch.entrySet())
	                    {
	                        serializedItemStack += ":e@" + ench.getKey().getId() + "@" + ench.getValue();
	                    }
	                }
	               
	                serialization += i + "#" + serializedItemStack + ";";
	            }
	        }
	        return serialization;
	    }
	   
	    @SuppressWarnings("deprecation")
		public static Inventory StringToInventory (String invString)
	    {
	        String[] serializedBlocks = invString.split(";");
	        String invInfo = serializedBlocks[0];
	        Inventory deserializedInventory = Bukkit.getServer().createInventory(null, Integer.valueOf(invInfo));
	       
	        for (int i = 1; i < serializedBlocks.length; i++)
	        {
	            String[] serializedBlock = serializedBlocks[i].split("#");
	            int stackPosition = Integer.valueOf(serializedBlock[0]);
	           
	            if (stackPosition >= deserializedInventory.getSize())
	            {
	                continue;
	            }
	           
	            ItemStack is = null;
	            Boolean createdItemStack = false;
	           
	            String[] serializedItemStack = serializedBlock[1].split(":");
	            for (String itemInfo : serializedItemStack)
	            {
	                String[] itemAttribute = itemInfo.split("@");
	                if (itemAttribute[0].equals("t"))
	                {
	                    is = new ItemStack(Material.getMaterial(Integer.valueOf(itemAttribute[1])));
	                    createdItemStack = true;
	                }
	                else if (itemAttribute[0].equals("d") && createdItemStack)
	                {
	                    is.setDurability(Short.valueOf(itemAttribute[1]));
	                }
	                else if (itemAttribute[0].equals("a") && createdItemStack)
	                {
	                    is.setAmount(Integer.valueOf(itemAttribute[1]));
	                }
	                else if (itemAttribute[0].equals("e") && createdItemStack)
	                {
	                    is.addEnchantment(Enchantment.getById(Integer.valueOf(itemAttribute[1])), Integer.valueOf(itemAttribute[2]));
	                }
	            }
	            deserializedInventory.setItem(stackPosition, is);
	        }
	       
	        return deserializedInventory;
	    }
	    
	    public static String[] blockToString(Block b)
	    {
	    	Location l = b.getLocation();
	    	
	    	String[] locString = new String[4];
	    	
	    	locString[0] = l.getWorld().getName();
	    	locString[1] = String.valueOf(l.getBlockX());
	    	locString[2] = String.valueOf(l.getBlockY());
	    	locString[3] = String.valueOf(l.getBlockZ());
	    	
	    	return locString;
	    	
	    }
	    
	    
		public static Block stringToBlock(String[] s)
	    {
	    	int x = Integer.parseInt(s[1]);
	    	int y = Integer.parseInt(s[2]);
	    	int z = Integer.parseInt(s[3]);
	    	
	    	Block b = Bukkit.getServer().getWorld(s[0]).getBlockAt(x, y, z);
	    	return b;
	    	
	    }
	    
	    public static HashMap<String[], String[]> blockToLocHash(HashMap<Block, Block> theMap)
	    {
	    	HashMap<String[], String[]> output = new HashMap<String[], String[]>();
	    	Set<Block> keys = theMap.keySet();
	    	
	    	for(Block s : keys)
	    	{
	    		Block c = theMap.get(s);
		    	String[] signs = blockToString(s);
		    	String[] chests = blockToString(c);
	    		
	    		output.put(signs, chests);
	    	}
	    	
	    	
	    	return output;
	    	
	    }
	    
	    public static HashMap<Block, Block> locToBlockHash(HashMap<String[], String[]> theMap, File file)
	    {
	    	HashMap<Block, Block> output = new HashMap<Block, Block>();
	    	Set<String[]> keys = theMap.keySet();
	   
	    	for(String[] signs : keys)
	    	{
	    		String[] chests = theMap.get(signs);
	    		
	    		Block s = stringToBlock(signs);
	    		
	    		Block c = stringToBlock(chests);
	    		
	    		if(s.getState() instanceof Sign && c.getState() instanceof Chest)
	    		{
	    		output.put(s, c);
	    		}
	    	}
	    	
	    	
	    	return output;
	    }
	    
	    
	    
}
