package de.vayion.LoginSystem.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.idManagement.UserProfile;

public class UserCmd implements CommandExecutor{
	Main main;
	public UserCmd(Main main) {
		this.main = main;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.hasPermission("login.admin")) {
				player.sendMessage("Du kannst diesen Befehl nicht ausführen");
				return false;
			}
		}
		
		if(args.length==0) {
			sender.sendMessage("Missing Args");
			return false;
		}
		else if(args.length>=2) {
			String argument = args[0].toLowerCase();
			
			switch(argument) {
			case "add":
				String name = args[1];
				if(args.length>2) {
					for (int i = 2; i<args.length;i++) {
						name = name+" "+args[i];
					}
				}
				main.getIDMain().addNewUser(name);
				sender.sendMessage(ChatColor.YELLOW+"Created new Profile:");
				sender.sendMessage(ChatColor.YELLOW+"------------");
				sender.sendMessage(main.getIDMain().getUsers().get(main.getIDMain().getUsers().size()-1).getName());
				sender.sendMessage(main.getIDMain().getUsers().get(main.getIDMain().getUsers().size()-1).getID());
				sender.sendMessage(ChatColor.YELLOW+"------------");
				break;

			case "addadmin":
				String name2 = args[1];
				if(args.length>2) {
					for (int i = 2; i<args.length;i++) {
						name2 = name2+" "+args[i];
					}
				}
				main.getIDMain().addNewAdmin(name2);
				break;
			case "list":
				if(args.length>2) {
					sender.sendMessage(ChatColor.RED+"Wrong amount of args. Please only enter an ID.");
					return false;
				}
				args[0] = "profiles";
				ListCmd.outputPage(sender, args, main);
				break;
			case "inspect":
				if(args.length>2) {
					sender.sendMessage(ChatColor.RED+"Wrong amount of args. Please only enter an ID.");
					return false;
				}
				InspectCmd.handleProfileInspect(args[1], sender, main);
				break;
			case "delete":
				if(args.length>2) {
					sender.sendMessage(ChatColor.RED+"Wrong amount of args. Please only enter an ID.");
					return false;
				}
				else {
					String id = args[1];
					
					if(main.getIDMain().deleteUser(id)) {
						sender.sendMessage(ChatColor.GREEN+"Deleted user "+id+".");
					}
					else {
						sender.sendMessage(ChatColor.GRAY+"This user doesn't exist.");
					}
				}
				break;
			case "search":
				String search = "";
				if(args.length==1) {
					sender.sendMessage(ChatColor.RED+"Wrong amount of args. Please enter a search keyword.");
					return false;
				}
				search = args[1];
				if(args.length > 2) {
					for(int i = 2; i < args.length; i++) {
						search = search + " "+args[i];
					}
				}
				
				ArrayList<String> users = main.getIDMain().searchUsers(search);
				sender.sendMessage(ChatColor.GREEN+"Listing all profiles containing: "+ChatColor.WHITE+search+ChatColor.YELLOW+".");
				sender.sendMessage(ChatColor.YELLOW+"------------");
				users.forEach(user -> sender.sendMessage(user));
				sender.sendMessage(ChatColor.YELLOW+"------------");
				break;
			default: 
				sender.sendMessage("Unknown Command");
				return false;
			}
			return true;
		}
		else {
			sender.sendMessage(ChatColor.RED+"Wrong amount of arguments");
		}
		return false;
	}
}
