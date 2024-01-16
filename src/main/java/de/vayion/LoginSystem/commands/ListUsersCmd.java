package de.vayion.LoginSystem.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.idManagement.UserProfile;

public class ListUsersCmd implements CommandExecutor {
	
	Main main;
	
	public ListUsersCmd(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if((sender instanceof Player)) {
			((Player) sender).sendMessage("You can only execute this command in the console.");
			return false;
		}

		ArrayList<UserProfile> users = main.getIDMain().getUsers();
		System.out.println(users.size()+" Nutzer:");
		users.forEach(user -> System.out.println(user.getName()+" - "+user.getID()+"||"));
		
		
		
		return false;
	}
}
