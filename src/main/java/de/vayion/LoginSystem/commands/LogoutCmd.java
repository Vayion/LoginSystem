package de.vayion.LoginSystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;

public class LogoutCmd implements CommandExecutor {
	
	Main main;
	
	public LogoutCmd(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			System.out.println("You can't execute this command in the console.");
			return false;
		}
		Player player = (Player) sender;
		
		main.getIDMain().logout(player);
		
		
		
		return false;
	}
}
