package de.vayion.LoginSystem.groups;

import java.util.HashMap;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.commands.GroupCmd;
import de.vayion.LoginSystem.idManagement.UserProfile;

public class GroupMain {
	
	private HashMap<String, Group> groups;
	private Main main;
	
	public GroupMain(Main main) {
		this.main = main;
		groups = new HashMap<String,Group>();
		main.getCommand("group").setExecutor(new GroupCmd(this));
	}
	
	
	public boolean addNewGroup(String name) {
		if(groups.containsKey(name.toLowerCase())) {
			return false;
		}
		else {
			groups.put(name.toLowerCase(), new Group(name, this));
			return true;
		}
	}
	
	private void loadGroups() {}
	
	public boolean removeUserFromGroup(String userID, String groupName) {
		if(groups.containsKey(groupName.toLowerCase())) {
			UserProfile user = main.getIDMain().getUser(userID);
			if(user == null) {
				return false;
			}
			else {
				return groups.get(groupName.toLowerCase()).removeProfile(user);
			}
		} 
		else {
			return false;
		}
	}
	
	public boolean addUserToGroup(String userID, String groupName) {
		if(groups.containsKey(groupName.toLowerCase())) {
			UserProfile user = main.getIDMain().getUser(userID);
			if(user == null) {
				return false;
			}
			else {
				groups.get(groupName.toLowerCase()).addProfile(user);
				return true;
			}
		} 
		else {
			return false;
		}
	}
	
	public HashMap<String, Group> getGroups() {
		return groups;
	}
	
	public Main getMain() {
		return main;
	}
	
	public Group getGroup(String name) {
		if(groups.containsKey(name)) {
			return groups.get(name);
		}
		else {
			return null;
		}
	}
}
