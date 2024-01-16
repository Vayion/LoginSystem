package de.vayion.LoginSystem.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;

public class DeleteUser implements CommandExecutor {
	
	Main main;
	
	public DeleteUser(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if((sender instanceof Player)) {
			player = (Player) sender;
			
			if(!player.hasPermission("login.admin")) {
				player.sendMessage("Du kannst diesen Befehl nicht ausführen.");
				return false;
			}
			
		}
		if(args.length!=1) {
			sender.sendMessage("Wrong amount of args. Only an ID is needed.");
			return false;
		}

		String id = args[0];
		
		if(main.getIDMain().deleteUser(id)) {
			sender.sendMessage(ChatColor.GREEN+"Deleted user "+args[0]+".");
		}
		else {
			sender.sendMessage(ChatColor.GRAY+"This user doesn't exist.");
		}
		
		
		
		return false;
	}
}
