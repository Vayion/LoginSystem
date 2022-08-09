package de.vayion.LoginSystem.playerManagement;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Utils;
import de.vayion.LoginSystem.idManagement.UserProfile;
import de.vayion.LoginSystem.plotManagement.Plot;

public class Reference extends Element {
	
	private String originalName;
	private Element next;
	private Player player;
	private UserProfile userProfile;
	private boolean loggedIn;
	
	public Reference(Player player) {
		super();
		this.player = player;
		next = new End();
		originalName = player.getName();
	}

	public Element addPlayer(Player player) {
		next = next.addPlayer(player);
		return this;
	}

	public Element removePlayer(Player player) {
		if(player.equals(this.player)) {
			return next;
		}
		next = next.removePlayer(player);
		return this;
	}

	public boolean isLoggedIn(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean login(Player player, UserProfile userProfile) {
		if(player.equals(this.player)) {
			if((!loggedIn)&&(userProfile.login(player))) {
				loggedIn = true;
				this.userProfile = userProfile;
				if(userProfile.getName().contains(" ")) {
					String name[] = userProfile.getName().split(" ", 2);
					Utils.changeName(name[0], player);
				}
				return true;
			}
			return false;
		}
		else {
			return next.login(player, userProfile);
		}
	}

	public boolean logout(Player player) {
		if(player.equals(this.player)) {
			if(loggedIn) {
				loggedIn = false;
				userProfile.getiDMain().getMain().getPlotListeners().removeFromEdit(player);
				userProfile.logout(player);
				this.userProfile = null;
				Utils.changeName(originalName, player);
				return true;
			}
			return false;
		}
		else {
			return next.logout(player);
		}
	}
	
	public void logoutAll() {
		next.logoutAll();
		if(userProfile!=null) {
			userProfile.logout(player);
		}
	}
	
	public boolean isAllowed(Location loc, Player player) {
		if(player.equals(this.player)) {
			if(!loggedIn) {return false;}
			return userProfile.getPlot().isAllowed(loc);
		}
		return next.isAllowed(loc, player);
	}
	
	public boolean isInBorders(Location loc, Player player) {
		if(player.equals(this.player)) {
			if(!loggedIn) {return false;}
			return userProfile.getPlot().getClaimBlockPlot(loc);
		}
		return next.isInBorders(loc, player);
	}
	
	public boolean claimPlot(int id, Player player) {
		if(this.player.equals(player)) {
			return userProfile.claimPlot(id);
		}
		return next.claimPlot(id, player);
	}

	public boolean playerIsLoggedIn(Player player) {
		if(this.player.equals(player)) {
			return loggedIn;
		}
		return next.playerIsLoggedIn(player);
	}
	
	public boolean isOwnPlot(Plot plot, Player player) {
		if(player.equals(player)) {
			if(userProfile == null) {return false;}
			return plot.equals(userProfile.getPlot());
		}
		return next.isOwnPlot(plot, player);
	}
	
	public Player getPlayerByID(String id) {
		if(loggedIn) {
			if(userProfile.getID().equals(id)) {
				return player;
			}
			return next.getPlayerByID(id);
		}
		else {
			return next.getPlayerByID(id);
		}
	}

	@Override
	public void sendHome(Player player) {
		if(player.equals(this.player)) {
			if(loggedIn) {
				userProfile.sendHome(player);
			}
		}
		else {
			next.sendHome(player);
		}
	}

}
