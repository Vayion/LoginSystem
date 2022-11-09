package de.vayion.LoginSystem.playerManagement;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.idManagement.UserProfile;
import de.vayion.LoginSystem.plotManagement.Plot;

public class End extends Element {

	@Override
	public Element addPlayer(Player player) {
		return new Reference(player);
	}

	@Override
	public Element removePlayer(Player player) {
		return this;
	}

	@Override
	public boolean isLoggedIn(String id) {
		return false;
	}

	@Override
	public boolean login(Player player, UserProfile userProfile) {
		return false;
	}

	@Override
	public boolean logout(Player player) {
		return false;
	}
	
	public void logoutAll() {}
	
	public boolean isAllowed(Location loc, Player player) {
		return false;
	}
	
	public boolean claimPlot(int id, Player player) {
		return false;
	}

	public boolean playerIsLoggedIn(Player player) {
		return false;
	}
	
	public boolean isOwnPlot(Plot plot, Player player) {
		return false;
	}
	
	public Player getPlayerByID(String id) {
		return null;
	}

	@Override
	public boolean isInBorders(Location loc, Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void sendHome(Player player) {}

	@Override
	public UserProfile getPlayerProfile(Player player) {
		return null;
	}
}
