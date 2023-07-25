package de.vayion.LoginSystem.idManagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.vayion.LoginSystem.Utils;
import de.vayion.LoginSystem.Utils.Mode;
import de.vayion.LoginSystem.groups.Group;
import de.vayion.LoginSystem.ingameInterface.InterfaceMain;
import de.vayion.LoginSystem.plotManagement.DummyPlot;
import de.vayion.LoginSystem.plotManagement.Plot;

public class UserProfile {
	
	protected String name;
	protected String id;
	protected IDMain iDMain;
	

	protected File file;
	protected FileConfiguration config;
	
	protected int xpLevel;
	protected float progress;
	
	/*
	 * relevant player informatino
	 */
	protected boolean occupied;
	protected Location location;
	protected double health = -1;
	protected int hunger = -1;
	protected ItemStack[] inventoryContents;
	protected Location spawnpoint;
	
	protected Plot plot;
	protected ArrayList<Group> groups;
	
	
	/**
	 * This is just used in the dummyProfile class
	 */
	public UserProfile() {}
	
	public UserProfile(String name, IDMain iDMain) {
		this.iDMain = iDMain;
		this.name = name;
		id = Utils.getString(6, Mode.ALPHANUMERIC);
		while(!iDMain.checkIfAvailable(id)) {
			id = Utils.getString(6, Mode.ALPHANUMERIC);
		}
		occupied = false;
		
		groups = new ArrayList<>();
		plot = new DummyPlot(-1, iDMain.getMain().getPlotManager());
		registerFile();
	}

	public UserProfile(String name, String id, IDMain iDMain) {
		this.iDMain = iDMain;
		this.name = name;
		this.id = id;
		System.out.println("Imported: "+name);
		System.out.println("Imported: "+id);
		occupied = false;
		
		groups = new ArrayList<>();
		registerFile();
	}
	
	public void registerFile() {
		file = new File(iDMain.getDir().getPath()+System.getProperty("file.separator")+name+" - "+id+".yml");
		config = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if(file.exists()) {
			loadProfile();
			health = 20D;
			hunger = 20;
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
	
	public boolean login(Player player) {
		if (occupied) {return false;}

		if(location != null) {
			if(Utils.checkIfValidSpawn(location)) {
				player.teleport(location);
			}
			else if(iDMain.getMain().getLocationManager().getCity()!=null) {
				player.teleport(iDMain.getMain().getLocationManager().getCity());
				player.sendMessage(ChatColor.YELLOW+"Deine letzte Position war nicht sicher. Du wurdest zum Anfang der Stadt gebracht.");
			}
		}else if(iDMain.getMain().getLocationManager().getCity()!=null) {
			player.teleport(iDMain.getMain().getLocationManager().getCity());
		}
		player.setVelocity(new Vector(0,0,0));
		if(spawnpoint != null) {player.setBedSpawnLocation(spawnpoint);}
		if(health>=0) {player.setHealth(health);}
		if(hunger>=0) {player.setFoodLevel(hunger);}
		player.setLevel(xpLevel);
		player.setExp(progress);
		if(inventoryContents!=null) {
			player.getInventory().setContents(inventoryContents);
			player.updateInventory();
		}
		player.getInventory().setItem(8, InterfaceMain.generateMenu());
		player.setGameMode(GameMode.SURVIVAL);
		
		occupied = true;
		return true;
	}
	
	public void logout(Player player) {
		xpLevel= player.getLevel();
		progress = player.getExp();
		location = player.getLocation();
		spawnpoint = player.getBedSpawnLocation();
		health = player.getHealth();
		hunger = player.getFoodLevel();
		inventoryContents = player.getInventory().getContents().clone();
		
		for(int i = 0; i<inventoryContents.length;i++) {
			if(player.getInventory().getContents()[i]!=null) {
				inventoryContents[i] = player.getInventory().getContents()[i].clone();
			}
		}
		player.getInventory().clear();
		player.setGameMode(GameMode.ADVENTURE);
		player.setLevel(0);
		player.setExp(0f);
		
		iDMain.getMain().getLocationManager().teleportToLobby(player);
		player.setVelocity(new Vector(0,0,0));
		occupied = false;
		saveData();
	}

	public String getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public IDMain getiDMain() {
		return iDMain;
	}
	

	public void saveData() {
		config.set("progress", (double)progress);
		config.set("xpLevel", xpLevel);
		config.set("location", location);
		config.set("health", health);
		config.set("hunger", hunger);
		config.set("spawnpoint", spawnpoint);
		int size = inventoryContents.length;
		config.set("inventory.size", size);
		for (int i = 0; i < size; i++) {
			config.set("inventory."+i, inventoryContents[i]);
		}
		config.set("plot", plot.getID()+1);
		save();
		
	}
	
	public void loadProfile() {
		//System.out.println("Loading Data");
		location = (Location) config.get("location");
		health = config.getDouble("health");
		hunger = config.getInt("hunger");
		xpLevel = config.getInt("xpLevel");
		progress = (float)(config.getDouble("progress"));
		spawnpoint = (Location) config.get("spawnpoint");
	
		int size = config.getInt("inventory.size");
		inventoryContents = new ItemStack[size];
		for (int i = 0; i < size; i++) {
			if(config.contains("inventory."+i)) {
				inventoryContents[i] = (ItemStack) config.get("inventory."+i);
			}
		}
		
		int temp = config.getInt("plot");
		if(temp == 0) {
			plot = new DummyPlot(-1, iDMain.getMain().getPlotManager());
		}else {
			plot = new DummyPlot(-1, iDMain.getMain().getPlotManager());
			claimPlot(temp -1);
		}
	}
	
	public boolean isAdmin() {
		return false;
	}
	
	public Plot getPlot() {
		return plot;
	}
	
	public void save() {
		try {
			config.save(file);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean claimPlot(int id) {
		int i = plot.getID();
		plot = plot.claimPlot(id, this);
		if(i == plot.getID()) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public double getHealth() {
		return health;
	}
	public int getHunger() {
		return hunger;
	}
	public Location getLocation() {
		return location;
	}
	public boolean isOccupied() {
		return occupied;
	}
	
	
	public void delete() {
		plot.setClaimed(false);
		try {
			file.delete();
		}catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public void sendHome(Player player) {
		if(plot.getID()==-1) {
			player.sendMessage(ChatColor.RED+"Du besitzt kein Grundstück.");
		}
		else if(plot.sendHome(player)) {
			player.sendMessage(ChatColor.GREEN+"Sende nach Hause.");
		}
		else {
			player.sendMessage(ChatColor.RED+"Dein Zuhause konnte nicht gefunden werden.");
		}
	}
	
	public void refreshPlotIDs() {
		if(plot.getID() == -2) {
			plot = new DummyPlot(-1, iDMain.getMain().getPlotManager());
			saveData();
			return;
		}
		if(config.getInt("plot") != plot.getID()) {
			config.set("plot", plot.getID());
		}
		saveData();
	}
	
	public ArrayList<Group> getGroups() {
		return groups;
	}
//	public void setGroup(ArrayList<Group> groups) {
//		this.groups = groups;
//	}
}
