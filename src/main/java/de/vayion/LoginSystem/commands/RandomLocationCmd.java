package de.vayion.LoginSystem.commands;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;

public class RandomLocationCmd implements CommandExecutor {

	private Main main;
	
	public RandomLocationCmd(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!main.getIDMain().playerIsLoggedIn(player)) {
				sender.sendMessage(ChatColor.GRAY + "Du musst hierfür eingeloggt sein.");
				return false;
			}
			/*if(player.getWorld().equals(main.getLocationManager().getFarm().getWorld())) {
				int x,y;
				Location loc;
				do {
					Random randomiser = new Random();
					x = randomiser.nextInt(2000)-1000;
					y = randomiser.nextInt(2000)-1000;
					loc = player.getWorld().getHighestBlockAt(x, y).getLocation().add(0,1,0);
				}while(!loc.getBlock().getType().isSolid());
				player.teleport(loc);

				player.sendMessage("Du wurdest zu x: "+x+ " , y: "+y+ " teleportiert.");
			}*/
		}
		return false;
	}

}
