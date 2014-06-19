package com.blackace.itemtradechests.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;

import com.blackace.itemtradechests.ItemTradeChests;

public class MyFile {

	ItemTradeChests plugin;
	public static File file;
	Logger log = Bukkit.getLogger();
	
	public MyFile(String filename, ItemTradeChests instance)
	{
		plugin = instance;
		MyFile.file = new File(plugin.getDataFolder(), filename);
			try {
				MyFile.file.getParentFile().mkdirs();
				if(MyFile.file.createNewFile())
				{
					log.info("New File created. Trade Chest Location file did not exist.");
				}
			} catch (IOException e) {
				log.severe("Error occured during file creation.");
				e.printStackTrace();
			}
		
	}

	public static void writeFile(HashMap<Block,Block> hashed)
	{
		FileOutputStream fileOs;
		try {
			fileOs = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fileOs);

			HashMap<String[], String[]> serializedMap = Serialize.blockToLocHash(hashed);
			oos.writeObject(serializedMap);
			oos.flush();
			oos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}



	}

	@SuppressWarnings({ "unchecked", "resource" })
	public static HashMap<Block,Block> readFile(File file)
	{
		if(file.exists() == false) return new HashMap<Block, Block>();
		try {
			FileInputStream fileIs = new FileInputStream(file);
			ObjectInputStream is = new ObjectInputStream(fileIs);
			HashMap<String[], String[]> input = (HashMap<String[], String[]>) is.readObject(); 
			HashMap<Block, Block> output = Serialize.locToBlockHash(input, file);
			return output;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Logger log = Bukkit.getLogger();
			log.severe("File failed to load properly, returned new HashMap with no values.");
			return new HashMap<Block,Block>();
		}


	}


}
