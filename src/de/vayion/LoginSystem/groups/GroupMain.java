package de.vayion.LoginSystem.groups;

import java.util.HashMap;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.commands.GroupCmd;

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
			groups.put(name.toLowerCase(), new Group(name));
			return true;
		}
	}
	
	private void loadGroups() {}
	
	public boolean addUserToGroup(String userID, String groupName) {
		if(groups.containsKey(groupName.toLowerCase())) {
			groups.get(groupName.toLowerCase()).addProfile(main.getIDMain().getUser(userID));
			return true;
		} 
		else {
			return false;
		}
	}
	
	public HashMap<String, Group> getGroups() {
		return groups;
	}
	
}
