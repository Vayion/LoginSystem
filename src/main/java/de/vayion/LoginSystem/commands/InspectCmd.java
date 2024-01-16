package de.vayion.LoginSystem.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.groups.Group;
import de.vayion.LoginSystem.idManagement.UserProfile;
import de.vayion.LoginSystem.plotManagement.Plot;

public class InspectCmd implements CommandExecutor {
	
	Main main;
	
	public InspectCmd(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!(player.hasPermission("LoginSystem.Inspect"))) {
				player.sendMessage(ChatColor.RED+"Du kannst das nicht tun.");
				return false;
			}
			
		}
		switch(args.length) {
		case 2:
			args[0] = args[0].toLowerCase();
			switch (args[0]) {
			case "plot":
				handlePlotInspect(args[1], sender, main);
				break;
			case "profile":
				handleProfileInspect(args[1], sender, main);
				break;
			case "user":
				handleProfileInspect(args[1], sender, main);
				break;
			default:
				sender.sendMessage(ChatColor.RED+"Invalid type. Only able to display plots or profiles/users.");
				return false;
			}
			break;
		default:
			sender.sendMessage(ChatColor.RED+"Wrong amount of arguments.");
			return false;	
		}
		return false;
	}
	
	public static void handlePlotInspect(String arg, CommandSender sender, Main main) {
		int id = 0;
		try {
			id = Integer.parseInt(arg);
		} catch (Exception e) {
			sender.sendMessage(ChatColor.RED+"Not a valid ID.");
			return;
		}
		
		Plot plot = main.getPlotManager().getPlotByID(id);
		if(plot == null) {
			sender.sendMessage(ChatColor.RED+"Specified Plot doesn't exist.");
			return;
		}
		
		sender.sendMessage(ChatColor.GREEN+"Information for Plot "+id+".");
		sender.sendMessage(ChatColor.YELLOW+"------------");
		sender.sendMessage("- ready: "+plot.isReady());
		if(plot.isClaimed()) {
			sender.sendMessage("- claimed by: "+plot.getClaimer().getName()+" - "+plot.getClaimer().getID()+".");
		} 
		else {
			sender.sendMessage("- not claimed.");
		}
		sender.sendMessage(ChatColor.YELLOW+"------------");
		sender.sendMessage("MinX: "+plot.getMinX());
		sender.sendMessage("MaxX: "+plot.getMaxX());
		sender.sendMessage("MinZ: "+plot.getMinZ());
		sender.sendMessage("MaxZ: "+plot.getMaxZ());
		sender.sendMessage(ChatColor.YELLOW+"------------");
		if(plot.getHome()!=null) {
			sender.sendMessage("Home: "+plot.getHome().toString());
		}else {
			sender.sendMessage("Home: not set.");
		}
		sender.sendMessage(ChatColor.YELLOW+"------------");
		
	}

	public static void handleProfileInspect(String arg, CommandSender sender, Main main) {
		UserProfile profile = main.getIDMain().getUser(arg);
		if(profile==null) {sender.sendMessage(ChatColor.RED+"Not a valid ID."); return;}
		ArrayList<Group> groups = profile.getGroups();
		sender.sendMessage(ChatColor.GREEN+"Information for Userprofile "+profile.getID()+":");
		sender.sendMessage(ChatColor.YELLOW+"------------");
		sender.sendMessage("- username: "+profile.getName());
		sender.sendMessage("- admin: "+profile.isAdmin());
		sender.sendMessage("- online: "+profile.isOccupied());
		sender.sendMessage(ChatColor.YELLOW+"------------"+
				ChatColor.RESET+
				(groups.isEmpty()?" \nUser is in no groups.":
				groups.stream().map(g->g.getName()+"\n").reduce("",String::concat)));
		sender.sendMessage(ChatColor.YELLOW+"------------");
		if(profile.isOccupied()) {
			Player player = main.getIDMain().getPlayerByID(profile.getID());
			if(player == null) {
				sender.sendMessage("If this ever happens I don't really know what went wrong. Player is logged in but not registered. Shut down the Server!");
			}
			else {
				String location = "unknown";
				Location loc = player.getLocation();
				Location farmLocation = main.getLocationManager().getFarm();
				World plotWorld = main.getPlotManager().getPlotWorld();
				if(plotWorld != null) {
					if(plotWorld.equals(loc.getWorld())) {
						location = "PlotWorld -> "+loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ()+".";
					}
				}
				else if(farmLocation!=null) {
					if(farmLocation.getWorld().equals(loc.getWorld())) {
						location = "Farm -> "+loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ()+".";
					}
				}
				sender.sendMessage("- Current Location: "+location);
				sender.sendMessage("- Health: "+profile.getHealth());
				sender.sendMessage("- Hunger: "+profile.getHunger());
			}
		}
		else {
			String location = "unknown";
			Location loc = profile.getLocation();
			if(loc!=null) {
				Location farmLocation = main.getLocationManager().getFarm();
				World plotWorld = main.getPlotManager().getPlotWorld();
				if(plotWorld != null) {
					if(plotWorld.equals(loc.getWorld())) {
						location = "PlotWorld -> "+loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ()+".";
					}
				}
				else if(farmLocation!=null) {
					if(farmLocation.getWorld().equals(loc.getWorld())) {
						location = "Farm -> "+loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ()+".";
					}
				}
			}
			sender.sendMessage("- Last Location: "+location);
			sender.sendMessage("- Health: "+profile.getHealth());
			sender.sendMessage("- Hunger: "+profile.getHunger());
		}
		
		sender.sendMessage(ChatColor.YELLOW+"------------");
		if(profile.getPlot().getID()==-1) {
			sender.sendMessage("Profile has no plot attached.");
		}else {
			sender.sendMessage("Profile claims plot number: "+profile.getPlot().getID());
		}
		sender.sendMessage(ChatColor.YELLOW+"------------");
	}
}
