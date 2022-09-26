package de.vayion.LoginSystem.commands;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.groups.Group;
import de.vayion.LoginSystem.groups.GroupMain;

public class GroupCmd implements CommandExecutor {
	
	private GroupMain groupMain;
	
	public GroupCmd(GroupMain groupMain) {
		this.groupMain = groupMain;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player player = null;
		boolean isPlayer = false;
		if(sender instanceof Player) {
			player = (Player) sender;
			isPlayer = true;
			
			if(!player.hasPermission("login.plotadmin")) {
				player.sendMessage(ChatColor.RED+"Du kannst das nicht tun.");
				return false;
			}
		}
		
		if(args.length==0) {
			sender.sendMessage(ChatColor.RED+"Missing args.");
			return false;
		}
		
		switch(args[0].toLowerCase()) {
		case "add":
			if(args.length == 2) {
				if(groupMain.addNewGroup(args[1])) {
					sender.sendMessage(ChatColor.YELLOW+"Successfully created group: '"+ChatColor.GREEN+args[1]+ChatColor.YELLOW+"'");
				}
				else {
					sender.sendMessage(ChatColor.RED+"Group with that name already exists.");
				}
			} else {
				sender.sendMessage(ChatColor.RED+"Correct use: /group add [name].");
			}
			break;

		case "list":
			sender.sendMessage(ChatColor.YELLOW+"Groups: ");
			sender.sendMessage(ChatColor.GRAY+"------");
			
			for (Map.Entry<String, Group> set :
	             groupMain.getGroups().entrySet()) {
				sender.sendMessage(set.getValue().getName());
	        }
			/*for(String key : groupMain.getGroups().keySet()) {
				player.sendMessage(groupMain.getGroups().get(key).getName());
			}*/
			sender.sendMessage(ChatColor.GRAY+"------");
			break;
		
		case "assign":
			if(args.length == 3) {
				if(groupMain.addUserToGroup(args[1], args[2])) {
					sender.sendMessage(
							ChatColor.YELLOW+"Sucessfully added '"+
							ChatColor.GREEN+groupMain.getMain().getIDMain().getUser(args[1]).getName()+
							ChatColor.YELLOW+"' to '"+ 
							ChatColor.GREEN+ args[2]+
							ChatColor.YELLOW+ "'.");
				}
				else {
					sender.sendMessage(ChatColor.RED+"Either Group or User not found. Have fun finding out which.");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED+"Correct use: /group assign [UserID] [GroupName].");
			}
			break;
			
		case "inspect":
			if(args.length == 2) {
				Group group = groupMain.getGroup(args[1].toLowerCase());
				if(group!=null) {
					
					sender.sendMessage(ChatColor.YELLOW+"Members of Group "+args[1]+": ");
					sender.sendMessage(ChatColor.GRAY+"------");
					group.getProfiles().forEach(user -> sender.sendMessage(user.getName()));
					sender.sendMessage(ChatColor.GRAY+"------");
				}
				else {
					sender.sendMessage(ChatColor.RED+"Group not found.");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED+"Correct use: /group inspect [GroupName].");
			}
			break;
		case "unassign":
			if(args.length == 3) {
				if(groupMain.removeUserFromGroup(args[1], args[2])) {
					sender.sendMessage(
							ChatColor.YELLOW+"Sucessfully removed '"+
							ChatColor.GREEN+groupMain.getMain().getIDMain().getUser(args[1]).getName()+
							ChatColor.YELLOW+"' from '"+ 
							ChatColor.GREEN+ args[2]+
							ChatColor.YELLOW+ "'.");
				}
				else {
					sender.sendMessage(ChatColor.RED+"Either Group or User not found or they have already been removed. Have fun finding out which.");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED+"Correct use: /group unassign [UserID] [GroupName].");
			}
			break;
		case "setposition":
			if(!isPlayer) {sender.sendMessage(ChatColor.RED+"You can't do that in console."); return false;}
			if(args.length == 3) {
				String id = args[1];
				Group group = groupMain.getGroup(id);
				if(group == null) {
					sender.sendMessage(ChatColor.RED+"Please enter a valid group.");
					return false;
				}
				int one;
				try {
					one = Integer.parseInt(args[2]);
				}catch(Exception e) {
					sender.sendMessage(ChatColor.RED+"Please enter a valid number.");
					return false;
				}
				
				//oops, I did it again, I did it again...
				boolean onE;
				
				if(one == 1) {onE = true;}
				else if(one == 2) {onE = false;}
				else {return false;}
				
				group.addPosition(onE, player.getLocation());
				
			}else {
				sender.sendMessage(ChatColor.GRAY+"Wrong amount of args. Usuage: /group setPosition [id] [1/2].");
			}
			break;
		
		default:
			sender.sendMessage(ChatColor.RED+"Known Subcommands: /group [add/unassign/assign/setPosition/list/inspect]");
			break;
		}
			
		return false;
	}

	
}
