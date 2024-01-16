package de.vayion.LoginSystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;

public class LoginCmd implements CommandExecutor {
	
	Main main;
	
	public LoginCmd(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			System.out.println("You can't execute this command in the console.");
			return false;
		}
		Player player = (Player) sender;
		if(args.length==0) {
			player.sendMessage("Du musst deine ID angeben.");
			return false;
		}

		if(args.length>1) {
			player.sendMessage("Du musst nur deine ID angeben.");
			return false;
		}

		main.getIDMain().login(args[0], player);
		
		
		
		return false;
	}
}
