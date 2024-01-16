package de.vayion.LoginSystem;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class LocationManager {
	
	private Location city, lobby, farm;
	private Main main;
	
	private File file;
	private FileConfiguration config;
	
	public LocationManager(Main main) {
		this.main = main;
		
		System.out.println("Debug");
		
		file = new File(main.getDataFolder(), "warpLocations.yml");
		config = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if(file.exists()) {
			loadLocations();
		}
		else {
			try {
				file.createNewFile();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	public boolean setCity(Location loc) {
		if(city==null) {
			city = loc; return true;
		}
		return false;
	}

	public boolean setFarm(Location loc) {
		if(farm==null) {
			farm = loc; return true;
		}
		return false;
	}

	public boolean setLobby(Location loc) {
		if(lobby==null) {
			lobby = loc; return true;
		}
		return false;
	}
	
	public Location getCity() {
		return city;
	}
	public Location getFarm() {
		return farm;
	}
	public Location getLobby() {
		return lobby;
	}
	
	
	public void teleportToCity(Player player) {
		if(city != null) {
			player.teleport(city);
			player.sendMessage(ChatColor.GREEN+"Du besuchst nun die Stadt.");
		}
		else {
			player.sendMessage(ChatColor.RED+"Dies funktioniert noch nicht. Versuche es nachher nochmal oder wende dich an einen der Erzieher.");
		}
	}

	public void teleportToFarm(Player player) {
		if(farm != null) {
			player.teleport(farm);
			player.sendMessage(ChatColor.GREEN+"Du besuchst nun die Farmwelt.");
		}
		else {
			player.sendMessage(ChatColor.RED+"Dies funktioniert noch nicht. Versuche es nachher nochmal oder wende dich an einen der Erzieher.");
		}
	}

	public void teleportToLobby(Player player) {
		if(lobby != null) {
			player.teleport(lobby);
			player.sendMessage(ChatColor.GREEN+"Du bist nun in der Lobby.");
		}
		else {
			player.sendMessage(ChatColor.RED+"Dies funktioniert noch nicht. Versuche es nachher nochmal oder wende dich an einen der Erzieher.");
		}
	}
	
	private void loadLocations() {
		if(config.get("farm")!=null) {
			farm = (Location) config.get("farm");
		}
		if(config.get("city")!=null) {
			city = (Location) config.get("city"); 
		}
		if(config.get("lobby")!=null) {
			lobby = (Location) config.get("lobby");
		}
		
	}
	
	public void saveLocations() {
		config.set("farm", farm);
		config.set("lobby", lobby);
		config.set("city", city);
		save();
	}
	
	public void save() {
		try {
			config.save(file);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
