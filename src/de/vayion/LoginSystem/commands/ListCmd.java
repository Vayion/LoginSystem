package de.vayion.LoginSystem.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;

public class ListCmd implements CommandExecutor {
	
	private Main main;
	
	public ListCmd(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			if(!(((Player)sender).hasPermission("LoginSystem.list"))) {
				((Player) sender).sendMessage(ChatColor.GRAY+"Du kannst dies nicht tun.");
				return false;
			}
		}
		int length = args.length;
		int page = 1;
		switch (length) {
		case 1:
			break;
		case 2:
			try {
				page = Integer.parseInt(args[1]);
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED+"Not a valid page number");
				return false;
			}
			if(page == 0) {
				page = 1;
			}
			break;
		default: 
			sender.sendMessage(ChatColor.RED+"Wrong amount of args.");
			return false;
		}
		
		ArrayList<String> list;
		
		args[0] = args[0].toLowerCase();
		switch (args[0]) {
		case "plots":
			list = main.getPlotManager().getAllPlots();
			break;
			
		case "profiles":
			list = main.getIDMain().getAllUsers(true);
			break;

		case "users":
			list = main.getIDMain().getAllUsers(false);
			break;
		default:
			sender.sendMessage(ChatColor.RED+"Not a valid list. Possible lists: Profiles/Users, Plots");
			return false;
		}
		
		if(page>(int)(list.size()/10)+1) {sender.sendMessage(ChatColor.RED+"Page number to high! "+(int)(list.size()/10)+1+" is the maximum."); return false;}
		sender.sendMessage(ChatColor.YELLOW+"List of "+args[0]+", Page "+page+":");
		sender.sendMessage(ChatColor.GRAY+"------");
		for(int i = 0; i < 10; i++) {
			if((i+((page-1)*10))<list.size()) {
				sender.sendMessage(list.get(i+((page-1)*10)));
			}
		}
		sender.sendMessage(ChatColor.GRAY+"------");
		return true;
	}
	
	public static void outputPage(CommandSender sender, String[] args, Main main) {
		int length = args.length;
		int page = 1;
		switch (length) {
		case 1:
			break;
		case 2:
			try {
				page = Integer.parseInt(args[1]);
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED+"Not a valid page number");
				return;
			}
			if(page == 0) {
				page = 1;
			}
			break;
		default: 
			sender.sendMessage(ChatColor.RED+"Wrong amount of args.");
			return;
		}
		
		ArrayList<String> list;
		
		args[0] = args[0].toLowerCase();
		switch (args[0]) {
		case "plots":
			list = main.getPlotManager().getAllPlots();
			break;
			
		case "profiles":
			list = main.getIDMain().getAllUsers(true);
			break;

		case "users":
			list = main.getIDMain().getAllUsers(false);
			break;
		default:
			sender.sendMessage(ChatColor.RED+"Not a valid list. Possible lists: Profiles/Users, Plots");
			return;
		}
		
		if(page>(int)(list.size()/10)+1) {sender.sendMessage(ChatColor.RED+"Page number to high! "+(int)(list.size()/10)+1+" is the maximum."); return;}
		sender.sendMessage(ChatColor.YELLOW+"List of "+args[0]+", Page "+page+":");
		sender.sendMessage(ChatColor.GRAY+"------");
		for(int i = 0; i < 10; i++) {
			if((i+((page-1)*10))<list.size()) {
				sender.sendMessage(list.get(i+((page-1)*10)));
			}
		}
		sender.sendMessage(ChatColor.GRAY+"------");
		return;
	}
}
