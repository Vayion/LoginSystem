package de.vayion.LoginSystem.groups;

import java.util.ArrayList;

import de.vayion.LoginSystem.idManagement.UserProfile;

public class Group {
	
	private ArrayList<UserProfile> profiles;
	
	private String name;
	private boolean noHunger, noDamage, noNether;
	
	public Group(String name) {
		this.name = name;
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
	
	
}
