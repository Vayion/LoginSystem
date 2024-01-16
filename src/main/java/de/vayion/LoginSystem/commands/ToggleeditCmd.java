package de.vayion.LoginSystem.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.plotManagement.PlotListeners;

public class ToggleeditCmd implements CommandExecutor{
	PlotListeners plotListeners;
	
	public ToggleeditCmd(PlotListeners plotListeners) {
		this.plotListeners = plotListeners;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("login.edit")) {
				if(plotListeners.toggleEdit(player)) {
					player.sendMessage(ChatColor.GREEN+"Entered Edit Mode.");
				}else {
					player.sendMessage(ChatColor.YELLOW+"Exited Edit Mode.");
				}
			}else {
				player.sendMessage(ChatColor.GRAY+"Du kannst dies nicht tun.");
			}
		}
		else {sender.sendMessage("You can't do this in console");}
		return false;
	}
	
}
