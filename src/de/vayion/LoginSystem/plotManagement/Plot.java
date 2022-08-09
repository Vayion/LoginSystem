package de.vayion.LoginSystem.plotManagement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.idManagement.UserProfile;

public class Plot {
	
	
	private Location pos1, pos2, home, playersetHome;
	private int minX, maxX, minZ, maxZ;
	
	private UserProfile claimer;
	
	private int id;
	protected PlotManager plotManager;
	private boolean ready;
	
	private boolean claimed;
	
	public Plot (int id, PlotManager plotManager) {
		this.id = id;
		this.plotManager = plotManager;
		ready = false;
		claimed = false;
	}
	
	public boolean addPosition(Boolean one, Location loc) {
		if(loc.getWorld() == plotManager.getPlotWorld()) {
			
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
	
	/**
	 * Set the home location of a plot. Playerset is (a yet to be added) function for the player to create his own home.
	 * It is stored seperatly to the original one in case there is no space near the original spawn
	 * @param home a position in the matching world. it is assumed that this position has already been checked if its in the right world
	 * @param playerset if the location is the position set by the admin or by the user
	 * @return returns if the position was on the plot
	 */
	public boolean setHome(Location home, boolean playerset) {
		if(getClaimBlockPlot(home)) {
			if(playerset) {
				//TODO: add playerset homes
			}else {
				this.home = home;
			}
			handle();
			return true;
		}
		else {
			return false;
		}
	}
	
	public void setClaimed(boolean bool) {
		claimed = bool;
	}
	
	public void setClaimer(UserProfile profile) {
		this.claimer = profile;
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
			if(home==null) {ready = false;}
			Bukkit.broadcastMessage(ChatColor.GREEN+"Plot "+id+" is ready.");
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
	
	public int getID() {
		return id;
	}
	
	public void setID(int id) {
		this.id = id;
	}
	
	public Location getPos1() {
		return pos1;
	}
	public Location getPos2() {
		return pos2;
	}
	
	public Plot claimPlot(int id, UserProfile profile) {
		return this;
	}
	
	public boolean isClaimed() {
		return claimed;
	}
	
	public boolean getClaimBlockPlot(Location loc) {
		if(pos1!=null&&pos2!=null) {
			int x = loc.getBlockX();
			int z = loc.getBlockZ();
			
			if((x>=minX-1)&&(x <= maxX+1)&&(z>=minZ-1)&&(z <= maxZ+1)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean sendHome(Player player) {
		if(ready) {
			player.teleport(home);
		}
		return ready;
	}
	
	
	public UserProfile getClaimer() {
		return claimer;
	}
	public int getMaxX() {
		return maxX;
	}
	public int getMaxZ() {
		return maxZ;
	}
	public int getMinX() {
		return minX;
	}
	public int getMinZ() {
		return minZ;
	}
	public boolean isReady() {
		return ready;
	}
	
	public Location getHome() {
		return home;
	}
	
}
