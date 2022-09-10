package de.vayion.LoginSystem.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
					sender.sendMessage(ChatColor.RED+"Group with that name already exists.");
				}
				else {
					sender.sendMessage(ChatColor.YELLOW+"Successfully created group: '"+ChatColor.GREEN+args[1]+ChatColor.YELLOW+".");
				}
			}
			break;

		case "list":
			sender.sendMessage(ChatColor.YELLOW+"Groups: ");
			sender.sendMessage(ChatColor.GRAY+"------");
			for(String key : groupMain.getGroups().keySet()) {
				player.sendMessage(groupMain.getGroups().get(key).getName());
			}
			sender.sendMessage(ChatColor.GRAY+"------");
			break;
		}
			
		return false;
	}

	
}
