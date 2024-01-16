package de.vayion.LoginSystem.idManagement;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.playerManagement.Element;
import de.vayion.LoginSystem.playerManagement.End;
import de.vayion.LoginSystem.plotManagement.Plot;

public class IDMain {
	
	private Main main;
	private ArrayList<UserProfile> users;
	private Element first;
	
	private File dir;
	
	public IDMain(Main main) {
		this.main = main;
		users = new ArrayList<UserProfile>();
		first = new End();
		
		dir = new File(main.getDataFolder().getPath()+System.getProperty("file.separator")+"users");
		dir.mkdir();
		
	}
	
	public void addPlayer(Player player) {
		first = first.addPlayer(player);
	}
	
	public void removePlayer(Player player) {
		first = first.removePlayer(player);
	}
	
	public void addNewUser(String name) {
		users.add(new UserProfile(name, this));
	}

	public void addNewAdmin(String name) {
		users.add(new AdminProfile(name, this));
	}
	
	public boolean checkIfAvailable(String id) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getID().equals(id)) {
				return false;
			}
		}
		return true;
	}

	
	/**
	 * Returns the UserProfile of the given ID
	 * @param id User ID of the required UserProfile
	 * @return required UserProfile
	 */
	public UserProfile getUser(String id) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getID().equals(id)) {
				return users.get(i);
			}
		}
		return null;
	}
	
	public void logoutAll() {
		first.logoutAll();
	}
	
	/**
	 * called by File Manager to add already existing users
	 * @param name Name of User
	 * @param 6 digit ID of user
	 */
	public void addUser(String name, String id) {
		users.add(new UserProfile(name, id, this));
	}
	
	/**
	 * called by File Manager to add already existing Admins
	 * @param name Name of Admin
	 * @param 6 digit ID of Admin
	 */
	public void addAdmin(String name, String id) {
		users.add(new AdminProfile(name, id, this));
	}
	
	/**
	 * Method for players to log in via certain id
	 * @param id id of the profile the player wants to log in
	 * @param player the player wanting to log in (I hate myself for explaining all of this, it's painfully obvious. I am so sorry)
	 */
	public void login(String id, Player player) {
		UserProfile profile = null;
		for(int i = 0; i<users.size(); i++) {
			if(users.get(i).getID().equals(id)) {
				profile = users.get(i);
			}
		}
		if(profile != null) {
			if(first.login(player, profile)) {
				player.sendMessage(ChatColor.GREEN+"Du hast dich erfolgreich als "+profile.getName()+" eingeloggt.");
			}
		} else {
			player.sendMessage(ChatColor.GRAY+"Dieser Account existiert nicht.");
		}
	}

	
	/**
	 * Logs out the given player
	 * @param player player profile that is supposed to be logged out
	 */
	public void logout(Player player) {
		if(first.logout(player)) {
			player.sendMessage(ChatColor.GREEN+"Du hast dich erfolgreich ausgeloggt.");
		} else {
			player.sendMessage(ChatColor.GRAY+"Du musst dich einloggen um dies zu tun.");
		}
	}
	
	public boolean isAllowed(Location loc, Player player) {
		return first.isAllowed(loc, player);
	}
	public boolean isInBorders(Location loc, Player player) {
		return first.isInBorders(loc, player);
	}
	
	public void saveRegistry() {
		main.getRegistryManager().saveProfileRegister(users);
	}
	
	public ArrayList<UserProfile> getUsers() {
		return users;
	}
	
	public Main getMain() {
		return main;
	}
	public File getDir() {
		return dir;
	}
	
	/**
	 * Function for a player to claim a plot. I know this would be smarter to do via userprofile but shut up nerd
	 * @param id id of the plot to be claimed
	 * @param player player that wants to claim the plot
	 * @return false if player already owns a plot
	 */
	public boolean claimPlot(int id, Player player) {
		return first.claimPlot(id, player);
	}
	
	
	/**
	 * returns if given player is logged into a userprofile
	 * @param player the player you're checking
	 * @return true if the player is logged in
	 */
	public boolean playerIsLoggedIn(Player player) {
		return first.playerIsLoggedIn(player);
	}
	
	
	/**
	 * Returns if given Plot belongs to given Player
	 * @param plot given Plot
	 * @param player given Player
	 * @return returns true if both match
	 */
	public boolean isOwnPlot(Plot plot, Player player) {
		return first.isOwnPlot(plot, player);
	}
	
	
	/**
	 * returns a player who is logged in via given IP. Used for the inspect command
	 * @param id the ID from the logged in player
	 * @return the player who logged in with that ID.
	 */
	public Player getPlayerByID(String id) {
		return first.getPlayerByID(id);
	}
	
	/**
	 * returns an Array list with all the relevant data
	 * @return ArrayList<String> with all users
	 */
	public ArrayList<String> getAllUsers(boolean includeAdmins){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i<users.size(); i++) {
			UserProfile user = users.get(i);
			if((includeAdmins)||(!user.isAdmin()&&!includeAdmins)) {
				list.add(user.getName() + " - "+ user.getID());
			}
		}
		
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		return list;
	}
	
	/**
	 * returns an Array list with User names sorted after a name
	 * @return ArrayList<String> with listed users
	 */
	public ArrayList<String> searchUsers(String string){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i<users.size(); i++) {
			if(new String(users.get(i).getName()).toLowerCase().contains(new String(string).toLowerCase()))
			list.add(users.get(i).getName() + " - "+ users.get(i).getID());
		}
		
		Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
		return list;
	}
	
	public boolean deleteUser(String id) {
		if(getUser(id)!=null) {
			for(int i = 0; i<users.size(); i++) {
				if(users.get(i).getID().equals(id)) {
					users.remove(i);
					return true;
				}
			}
		}
		return false;
	}
	public void refreshPlotIDs() {
		for (int i = 0; i<users.size(); i++) {
			users.get(i).refreshPlotIDs();
		}
	}
	
	public void sendHome(Player player) {
		first.sendHome(player);
	}
	
	public UserProfile getPlayerProfile(Player player) {
		return first.getPlayerProfile(player);
	}
}
