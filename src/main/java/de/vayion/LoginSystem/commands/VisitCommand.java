package de.vayion.LoginSystem.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;

public class VisitCommand implements CommandExecutor {
	
	public enum Target {CITY, FARM, LOBBY}
	
	Target target;
	
	Main main;
	
	public VisitCommand(Main main, Target target) {
		this.main = main;
		this.target = target;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(main.getIDMain().playerIsLoggedIn(player)) {
				switch(target) {
				case CITY:
					main.getLocationManager().teleportToCity(player);
					break;
				case FARM:
					main.getLocationManager().teleportToFarm(player);
					break;
				case LOBBY:
					main.getLocationManager().teleportToLobby(player);
					break;
				}
				return true;
			}
			player.sendMessage(ChatColor.RED+"Du musst eingeloggt sein um dies zu tun");
			
			return true;
		}
		System.out.println("You can't do that in 1942.");
		return false;
	}

}
