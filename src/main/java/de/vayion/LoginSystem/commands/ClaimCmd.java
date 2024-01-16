package de.vayion.LoginSystem.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.plotManagement.Plot;

public class ClaimCmd implements CommandExecutor{

	Main main;
	
	public ClaimCmd(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!(main.getIDMain().playerIsLoggedIn(player))) {player.sendMessage(ChatColor.RED+"Du bist nicht eingeloggt."); return false;}
			Plot plot = main.getPlotManager().getPlotByLoc(player.getLocation());
			if(plot==null) {player.sendMessage(ChatColor.RED+"Du stehst auf keinem Grundstück"); return false;}
			if(plot.isClaimed()) {
				if(main.getIDMain().isOwnPlot(plot, player)) {player.sendMessage(ChatColor.GREEN+"Dieses Grundstück gehört dir schon.");}
				else {
					player.sendMessage(ChatColor.RED+"Dieses Grundstück gehört schon jemanden.");
				}
				return false;
			}
			if(main.getIDMain().claimPlot(plot.getID(), player)) {
				player.sendMessage(ChatColor.GREEN+"Du hast erfolgreich dieses Grundstück gekauft."); return true;
			}
			else {
				player.sendMessage(ChatColor.RED+"Du besitzt schon ein Grundstück."); return false;
			}
			
		}
		return false;
	}

}
