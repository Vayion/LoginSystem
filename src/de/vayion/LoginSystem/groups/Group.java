package de.vayion.LoginSystem.groups;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.vayion.LoginSystem.idManagement.UserProfile;

public class Group {
	
	private ArrayList<UserProfile> profiles;
	
	private String name;
	private boolean noHunger, noDamage, noNether, ready;
	private Location pos1, pos2;
	private int minX, maxX, minZ, maxZ;
	private GroupMain groupMain;
	

	protected File file;
	protected FileConfiguration config;
	
	public Group(String name, GroupMain groupMain) {
		ready = false;
		this.name = name;
		this.groupMain = groupMain;
		profiles = new ArrayList<UserProfile>();
		registerFile();
	}
	
	public void registerFile() {
		file = new File(groupMain.getDir().getPath()+System.getProperty("file.separator")+name.toLowerCase()+".yml");
		config = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if(file.exists()) {
			loadGroup();
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
	
	public void loadGroup() {
		int max = config.getInt("size");
		for (int i = 0; i<max; i++) {
			String id = config.getString("user."+i);
			if(id != null) {
				UserProfile user = groupMain.getMain().getIDMain().getUser(id);
				if(user!= null) {
					addProfile(user);
				}
			}
		}
		if(config.contains("pos1")) {addPosition(true, (Location)(config.get("pos1")));}
		if(config.contains("pos2")) {addPosition(false, (Location)(config.get("pos2")));}
	}
	
	public void saveGroup() {
		config.set("size", profiles.size());
		for (int i = 0; i < profiles.size(); i++) {
			config.set("user."+i, profiles.get(i).getID());
		}
		
		config.set("pos1", pos1);
		config.set("pos2", pos2);
		save();	
	}
	
	public void save() {
		try {
			config.save(file);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addProfile(UserProfile userprofile) {
		userprofile.setGroup(this);
		profiles.add(userprofile);
	}
	
	public boolean removeProfile(UserProfile userprofile) {
		userprofile.setGroup(null);
		return profiles.remove(userprofile);
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<UserProfile> getProfiles() {
		return profiles;
	}
	
	public boolean addPosition(Boolean one, Location loc) {
		if(loc.getWorld() == groupMain.getMain().getPlotManager().getPlotWorld()) {
			
			if(one) {
				pos1 = loc;
			} else {
				pos2 = loc;
			}
			handle();
			return true;
			
		}else {
			return false;
		}
	}
	
	public void handle() {
		if((pos1 != null)&&(pos2 != null)){
			
			if(pos1.getBlockX()>=pos2.getBlockX()) {
				minX = pos2.getBlockX();
				maxX = pos1.getBlockX();
			}else {
				minX = pos1.getBlockX();
				maxX = pos2.getBlockX();
			}
			
			if(pos1.getBlockZ()>=pos2.getBlockZ()) {
				minZ = pos2.getBlockZ();
				maxZ = pos1.getBlockZ();
			}else {
				minZ = pos1.getBlockZ();
				maxZ = pos2.getBlockZ();
			}
			ready = true;
			Bukkit.broadcastMessage(ChatColor.GREEN+"Plot of Group '"+name+"' is ready.");
		}
		else {
			ready = false;
		}
	}
	
	public boolean isAllowed(Location loc) {
		if(ready) {
			int x = loc.getBlockX();
			int z = loc.getBlockZ();
			
			if((x>=minX)&&(x <= maxX)&&(z>=minZ)&&(z <= maxZ)) {
				return true;
			}
		}
		return false;
	}
}
