package de.vayion.LoginSystem.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.plotManagement.Plot;

public class FindCmd  implements CommandExecutor{

	Main main;
	
	public FindCmd(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!(main.getIDMain().playerIsLoggedIn(player))) {player.sendMessage(ChatColor.RED+"Du bist nicht eingeloggt."); return false;}
			Plot plot =main.getPlotManager().getRandomUnclaimedPlot();
			if(plot!=null) {
				player.teleport(plot.getHome());
				player.sendMessage(ChatColor.GREEN+"Du wurdest zu einem zufälligen freien Grundstück geschickt.");
			}
			else {
				player.sendMessage(ChatColor.RED+"Es scheint keine freien Grundstücke mehr zu geben. Kontaktiere einen Erzieher.");
			}
			
		}
		return false;
	}

}

