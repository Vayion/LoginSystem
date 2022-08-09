package de.vayion.LoginSystem.playerManagement;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.idManagement.UserProfile;
import de.vayion.LoginSystem.plotManagement.Plot;

public abstract class Element {
	public Element() {}
	public abstract Element addPlayer(Player player);
	public abstract Element removePlayer(Player player);

	public abstract boolean isLoggedIn(String id);
	public abstract boolean playerIsLoggedIn(Player player);
	public abstract boolean login(Player player, UserProfile userProfile);
	public abstract boolean logout(Player player);
	public abstract void logoutAll();
	
	public abstract boolean isAllowed(Location loc, Player player);
	public abstract boolean isInBorders(Location loc, Player player);
	public abstract boolean claimPlot(int id, Player player);
	
	public abstract boolean isOwnPlot(Plot plot, Player player);
	public abstract Player getPlayerByID(String id);
	
	public abstract void sendHome(Player player);
}
