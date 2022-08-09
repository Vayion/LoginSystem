package de.vayion.LoginSystem.plotManagement;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.idManagement.UserProfile;

public class DummyPlot extends Plot {

	public DummyPlot(int id, PlotManager plotManager) {
		super(id, plotManager);
		// TODO Auto-generated constructor stub
	}
	
	public boolean addPosition(Boolean one, Location loc) {return false;}
	
	public void setClaimed(boolean bool) {}
	
	public void handle() {}
	
	public boolean isAllowed(Location loc) {
		return false;
	}
	
	public int getID() {
		return -1;
	}
	
	public void setID(int id) {}
	
	public Plot claimPlot(int id, UserProfile profile) {
		Plot plot = plotManager.getPlotByID(id);
		plot.setClaimed(true);
		plot.setClaimer(profile);
		Bukkit.broadcastMessage("Claimed Plot "+ id);
		return plot;
	}

	public boolean sendHome(Player player) {
		return false;
	}
}
