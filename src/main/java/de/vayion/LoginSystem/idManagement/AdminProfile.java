package de.vayion.LoginSystem.idManagement;

import org.bukkit.entity.Player;

public class AdminProfile extends UserProfile {

	public AdminProfile(String name, IDMain iDMain) {
		super(name, iDMain);
		// TODO Auto-generated constructor stub
	}
	

	public AdminProfile(String name, String id, IDMain iDMain) {
		super(name, id, iDMain);
	}
	
	public boolean login(Player player) {
		player.setOp(true);
		return super.login(player);
	}
	
	public void logout(Player player) {
		player.setOp(false);
		super.logout(player);
	}
	
	
	@Override
	public boolean isAdmin() {
		return true;
	}


}
