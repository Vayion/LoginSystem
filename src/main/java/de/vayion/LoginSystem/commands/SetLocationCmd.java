package de.vayion.LoginSystem.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.plotManagement.Plot;

public class SetLocationCmd implements CommandExecutor {
	
	Main main;
	
	public SetLocationCmd(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.hasPermission("Login.createPlot")) {
				player.sendMessage("Du kannst diesen Befehl nicht ausführen");
				return false;
			}
			if(args.length==0) {
				player.sendMessage("Missing Args");
				return false;
			}
			else if(args.length==1) {
				String argument = args[0].toLowerCase();
				
				switch(argument) {
				case "city":
					if(main.getLocationManager().setCity(player.getLocation())) {
						player.sendMessage(ChatColor.GREEN+"Location for City sucessfully set.");
						return true;
					}
					
					break;

				case "lobby":
					if(main.getLocationManager().setLobby(player.getLocation())) {
						player.sendMessage(ChatColor.GREEN+"Location for Lobby sucessfully set.");
						return true;
					}
					break;
				case "farm":
				if(main.getLocationManager().setFarm(player.getLocation())) {
					player.sendMessage(ChatColor.GREEN+"Location for Farm sucessfully set.");
					return true;
				}
					break;
					default: 
						player.sendMessage("Unknown Location. Available locations are");
						return false;
				}
				player.sendMessage(ChatColor.RED+"This Location already is set. Delete it in the config files and restart the server to continue.");
				return true;
			}
			
			else {
				player.sendMessage(ChatColor.RED+"Too many arguments");
			}
		}else {
			System.out.println("You can't do this in console");
		}
		return false;
	}
	
	

}
