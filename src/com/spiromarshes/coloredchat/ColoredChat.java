package com.spiromarshes.coloredchat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class ColoredChat extends JavaPlugin implements Listener{
	
	String setMessage;
	String resetMessage;
	String usageMessage;
	
	HashMap<String, String> colors;
		
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable()
	{
		PluginDescriptionFile pdfFile = getDescription();	
		Logger logger = getLogger();
		logger.info(pdfFile.getName() + " has been enabled (V." + pdfFile.getVersion() + ")");
		
		File directory = getDataFolder();
		
		if(!directory.exists())
			if(!directory.mkdir())
				System.out.println("Could not create directory for plugin: " + getDescription().getName());
		
		colors = (HashMap<String, String>) load(new File(getDataFolder(), "colors.dat"));
		
		if(colors == null)
			colors = new HashMap<String, String>();
		
		getServer().getPluginManager().registerEvents(this, this);
		getCommand("color").setExecutor(this);
		getCommand("reset").setExecutor(this);
		
		registerConfig();
		
		setMessage = getConfig().getString("Color Set");
		resetMessage = getConfig().getString("Color Reset");
		usageMessage = getConfig().getString("Usage Message");		
	}
	
	public void onDisable()
	{
		File file = new File(getDataFolder(), "colors.dat");
		save(colors, file);
		
		saveDefaultConfig();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(!(sender instanceof Player))
		{
			sender.sendMessage("You need to be a player to use this command!");
			return true;
		}
		
		String player = sender.getName();
		
		if(command.getName().equalsIgnoreCase("reset") && Bukkit.getPlayer(player).hasPermission("color.set"))
		{
			if(colors.containsKey(player))
				colors.remove(player);
			Bukkit.getPlayer(player).sendMessage(resetMessage.replaceAll("&", "§"));
			return true;
		}
		
		if(command.getName().equalsIgnoreCase("color") && args.length == 0 && Bukkit.getPlayer(player).hasPermission("color.set"))
		{		
			Bukkit.getPlayer(player).sendMessage(usageMessage.replaceAll("&", "§"));
			return true;
		}
		
		if(command.getName().equalsIgnoreCase("color") && args.length == 1 && Bukkit.getPlayer(player).hasPermission("color.set"))
		{		
			colors.put(player, args[0]);
			Bukkit.getPlayer(player).sendMessage("§" + colors.get(player) + setMessage);
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerchat(AsyncPlayerChatEvent event)
	{
		String message = event.getMessage();
		
		String player = event.getPlayer().getName();
		
		if(colors.containsKey(player))
		{
			String color = colors.get(player);
			String toChat = "";
			
			switch (color)
			{
			case "0": 
				toChat = "§0";
				break;
			case "1":
				toChat = "§1";
				break;
			case "2":
				toChat = "§2";
				break;		  
			case "3":
				toChat = "§3";
				break;
			case "4":
				toChat = "§4";
				break;
			case "5":
				toChat = "§5";
				break;
			case "6":
				toChat = "§6";
				break;
			case "7":
				toChat = "§7";
				break;
			case "8":
				toChat = "§8";
				break;
			case "9":
				toChat = "§9";
				break;
			case "a":
				toChat = "§a";
				break;
			case "b":
				toChat = "§b";
				break;
			case "c":
				toChat = "§c";
				break;
			case "d":
				toChat = "§d";
				break;
			case "e":
				toChat = "§e";
				break;
			case "f":
				toChat = "§f";
				break;
			}
			event.setMessage(toChat + message);
		}
	}

	private void registerConfig()
	{
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
	}
	
	public void save(Object obj, File file)
	{
		try{
			if(!file.exists())
				file.createNewFile();
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file));
			output.writeObject(obj);
			output.flush();
			output.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Object load(File file)
	{
		try{
			ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
			
			Object result = input.readObject();
			input.close();
			
			return result;
		}catch(Exception e)
		{
			return null;
		}
	}
}
