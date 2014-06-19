package com.blackace.itemtradechests;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.blackace.itemtradechests.listeners.BlockListener;
import com.blackace.itemtradechests.listeners.PlayerListener;
import com.blackace.itemtradechests.util.MyConfig;
import com.blackace.itemtradechests.util.MyConfigManager;
import com.blackace.itemtradechests.util.MyFile;

public class ItemTradeChests extends JavaPlugin {
	
	
	BlockListener blocklistener;
	PlayerListener playerlistener;
	
	public static MyFile file;
	
	//Configs
	MyConfig config;
	MyConfigManager manager;
	
	Logger log = Bukkit.getLogger();
	
	@Override
	public void onEnable()
	{
		this.blocklistener = new BlockListener(this);
		this.playerlistener = new PlayerListener(this);
		//config = manager.getNewConfig("config.yml", new String[]{"Configuration containing settings for all trade chest locations.", "Created by Black_Ace2004!"});
		
		ItemTradeChests.file = new MyFile("locations.dat", this);
		BlockListener.chestlist = MyFile.readFile(MyFile.file);
	}
	
	@Override
	public void onDisable()
	{
		log.info("Saving trade chests locations to file.");
		MyFile.writeFile(BlockListener.chestlist);
		log.info("Trade chests save finished.");
	}
	
	
	

}
