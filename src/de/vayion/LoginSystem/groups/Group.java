package de.vayion.LoginSystem.groups;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import de.vayion.LoginSystem.idManagement.UserProfile;

public class Group {
	
	private ArrayList<UserProfile> profiles;
	
	private String name;
	private boolean noHunger, noDamage, noNether, ready;
	private Location pos1, pos2;
	private int minX, maxX, minZ, maxZ;
	private GroupMain groupMain;
	
	
	public Group(String name, GroupMain groupMain) {
		ready = false;
		this.name = name;
		profiles = new ArrayList<UserProfile>();
	}
	
	public void addProfile(UserProfile userprofile) {
		profiles.add(userprofile);
	}
	
	public boolean removeProfile(UserProfile userprofile) {
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
