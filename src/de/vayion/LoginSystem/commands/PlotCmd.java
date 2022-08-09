package de.vayion.LoginSystem.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.plotManagement.Plot;

public class PlotCmd implements CommandExecutor {
	
	Main main;

	public PlotCmd(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		Boolean isPlayer = false;
		Player player = null;
		if(sender instanceof Player) {
			player = (Player) sender;
			isPlayer = true;
			if(!player.hasPermission("login.plotadmin")) {
				player.sendMessage("Du kannst diesen Befehl nicht ausführen");
				return false;
			}
		}
		if(args.length==0) {
			player.sendMessage("Missing Args");
			return false;
		}
		switch (args[0]) {
		
		case "list":
			if(args.length == 1) {
				String[] list = new String[2];
				list[0] = "plots";
				list[1] = "1";
				ListCmd.outputPage(sender, list, main);
			}
			else if(args.length == 2) {
				args[0] = "plots";
				ListCmd.outputPage(sender, args, main);
			}
			else {
				sender.sendMessage(ChatColor.GRAY+"Wrong amount of args. Usuage: /plot list [page].");
			}
			break;
			
		case "add":
			if(args.length == 1) {
				sender.sendMessage(ChatColor.GREEN+"Sucessfully added new Plot with ID: "+main.getPlotManager().addPlot()+".");
			}
			else {
				sender.sendMessage(ChatColor.GRAY+"Wrong amount of args. Usuage: /plot add.");
			}
			break;
			
		case "get":
			if(!isPlayer) {sender.sendMessage(ChatColor.RED+"You can't do that in console."); return false;}
			if(args.length == 1) {
				Plot plot = main.getPlotManager().getPlotByLoc(player.getLocation());
				if(plot == null) {
					player.sendMessage("You are standing on no plot");
				}else {
					player.sendMessage("You are standing on plot number "+plot.getID());
				}
			}
			else {
				sender.sendMessage(ChatColor.GRAY+"Wrong amount of args. Usuage: /plot get.");
			}
			break;
			
		case "setworld":
			if(!isPlayer) {sender.sendMessage(ChatColor.RED+"You can't do that in console."); return false;}
			if(args.length == 1) {
				if(main.getPlotManager().setPlotWorld(player.getWorld())) {
					player.sendMessage(ChatColor.GREEN+"Sucessfully set plot world");
				}else {
					player.sendMessage(ChatColor.RED+"The Plot World is already set. Doing this could cause fatal errors. Please shut down the server, delete the World out of the yml file and then start the server again if you are sure.");
				}
			}
			else {
				sender.sendMessage(ChatColor.GRAY+"Wrong amount of args. Usuage: /plot setworld.");
			}
			break;
			
		case "inspect":
			if(args.length == 2) {
				InspectCmd.handlePlotInspect(args[1], sender, main);
			}
			else {
				sender.sendMessage(ChatColor.GRAY+"Wrong amount of args. Usuage: /plot inspect [id].");
			}
			break;
			
		case "delete":
			if(args.length == 2) {
				try {
					int i = Integer.parseInt(args[1]);
					if(main.getPlotManager().removePlot(i)) {
						sender.sendMessage(ChatColor.GREEN+"Removed Plot: "+i+".");
					}
					else {
						sender.sendMessage(ChatColor.GRAY+"This plot doesn't exist.");
					}
				}
				catch (Exception e) {
					sender.sendMessage(ChatColor.RED+"Please enter an ID.");
				}
			}
			else {
				sender.sendMessage(ChatColor.GRAY+"Wrong amount of args. Usuage: /plot delete [id].");
			}
			break;
			
		case "set":
			if(!isPlayer) {sender.sendMessage(ChatColor.RED+"You can't do that in console."); return false;}
			if(args.length == 3) {
				int id;
				int one;
				try {
					id = Integer.parseInt(args[1]);
					one = Integer.parseInt(args[2]);
				}catch(Exception e) {
					player.sendMessage(ChatColor.RED+"Please enter valid numbers.");
					return false;
				}
				
				//what have I done, oh god, oh god, oh god
				boolean onE;
				
				if(one == 1) {onE = true;}
				else if(one == 2) {onE = false;}
				else {return false;}
				if(main.getPlotManager().getPlotsSize()>id) {
					if(main.getPlotManager().setPlotLocation(player.getLocation(), id, onE)) {
						sender.sendMessage(ChatColor.GREEN+"Sucessfully added new Location "+one+" to Plot: "+id+".");
						}
					else {
						sender.sendMessage(ChatColor.RED+"Plot world is not set yet. Set it with "+ChatColor.YELLOW+"/plot setworld"+ChatColor.RED+".");
						}
				}else {
					sender.sendMessage(ChatColor.RED+"Plot doesn't exist.");
				}
			}else {
				sender.sendMessage(ChatColor.GRAY+"Wrong amount of args. Usuage: /plot set [id] [1/2].");
			}
			break;
		case "random":
			if(!isPlayer) {sender.sendMessage(ChatColor.RED+"You can't do that in console."); return false;}
			Plot plot =main.getPlotManager().getRandomUnclaimedPlot();
			if(plot!=null) {player.teleport(plot.getHome());}
			
			break;
		case "sethome":
			if(!isPlayer) {sender.sendMessage(ChatColor.RED+"You can't do that in console."); return false;}
			if(args.length==2) {
				int i = 0;
				try {
					i = Integer.parseInt(args[1]);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.GRAY+"Not a valid id.");
					return false;
				}
				World world = main.getPlotManager().getPlotWorld();
				if((world != null)&&(player.getLocation().getWorld().equals(world))) {
					if(main.getPlotManager().getPlotsSize()>i) {
						if(main.getPlotManager().getPlotByID(i).setHome(player.getLocation(), false)) {
							sender.sendMessage(ChatColor.GREEN+"Sucessfully set Home of Plot Nr."+i+".");
						}
						else {
							sender.sendMessage(ChatColor.GRAY+"Home location is not part of the specified plot.");
						}
						
					}
					else {
						sender.sendMessage(ChatColor.GRAY+"This plot doesn't exist.");
					}
				}
			}
			else {
				sender.sendMessage(ChatColor.GRAY+"Wrong amount of args. Usuage: /plot sethome [id].");
			}
			break;
			
		default:
			sender.sendMessage(ChatColor.GRAY+"Unknown Command. Known subcommands are: ");
			sender.sendMessage(ChatColor.GRAY+"set/add/delete/inspect/list/get/setworld/sethome");
			return false;
		}
		return true;
	}
	
	/*
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		Boolean isPlayer = false;
		Player player = null;
		if(sender instanceof Player) {
			player = (Player) sender;
			isPlayer = true;
			if(!player.hasPermission("login.plotadmin")) {
				player.sendMessage("Du kannst diesen Befehl nicht ausführen");
				return false;
			}
		}
		if(args.length==0) {
			player.sendMessage("Missing Args");
			return false;
		}
		else if(args.length==1) {
			String argument = args[0].toLowerCase();
			
			switch(argument) {
			case "add":
				sender.sendMessage(ChatColor.GREEN+"Sucessfully added new Plot with ID: "+main.getPlotManager().addPlot()+".");
				break;

			case "get":
				if(!isPlayer) {sender.sendMessage(ChatColor.RED+"You can't do that in console."); return false;}
				Plot plot = main.getPlotManager().getPlotByLoc(player.getLocation());
				if(plot == null) {
					player.sendMessage("You are standing on no plot");
				}else {
					player.sendMessage("You are standing on plot number "+plot.getID());
				}
				
				
				break;
			case "setworld":
				if(!isPlayer) {sender.sendMessage(ChatColor.RED+"You can't do that in console."); return false;}
				if(main.getPlotManager().setPlotWorld(player.getWorld())) {
					player.sendMessage(ChatColor.GREEN+"Sucessfully set plot world");
				}else {
					player.sendMessage(ChatColor.RED+"The Plot World is already set. Doing this could cause fatal errors. Please shut down the server, delete the World out of the yml file and then start the server again if you are sure.");
				}
				
				break;
			case "list":
				args[0] = "plots";
				ListCmd.outputPage(sender, args, main);
				break;
			default: 
				sender.sendMessage(ChatColor.GRAY+"Unknown Command");
				return false;
			}
			return true;
		}
		
		else if(args.length==2) {
			String argument = args[0];
			switch(argument) {
			case "inspect":
				InspectCmd.handlePlotInspect(args[1], sender, main);
				break;
			case "list":
				args[0] = "plots";
				ListCmd.outputPage(sender, args, main);
				break;
			case "delete":
				try {
					int i = Integer.parseInt(args[1]);
					if(main.getPlotManager().removePlot(i)) {
						sender.sendMessage(ChatColor.GREEN+"Removed Plot: "+i+".");
					}
					else {
						sender.sendMessage(ChatColor.GRAY+"This plot doesn't exist.");
					}
				}
				catch (Exception e) {
					sender.sendMessage(ChatColor.RED+"Please enter an ID.");
				}
				break;
			default:
				sender.sendMessage(ChatColor.GRAY+"Unknown Command");
				return false;
			}
		}
		
		else if(args.length==3) {
			String argument = args[0];
			
			switch(argument) {
			case "set":
				if(!isPlayer) {sender.sendMessage(ChatColor.RED+"You can't do that in console."); return false;}
				int id;
				int one;
				try {
					id = Integer.parseInt(args[1]);
					one = Integer.parseInt(args[2]);
				}catch(Exception e) {
					player.sendMessage(ChatColor.RED+"Please enter valid numbers.");
					return false;
				}
				
				//what have I done, oh god, oh god, oh god
				boolean onE;
				
				if(one == 1) {onE = true;}
				else if(one == 2) {onE = false;}
				else {return false;}
				if(main.getPlotManager().getPlotsSize()>id) {
					if(main.getPlotManager().setPlotLocation(player.getLocation(), id, onE)) {
						sender.sendMessage(ChatColor.RED+"Plot world is not set yet. Set it with "+ChatColor.YELLOW+"/plot setworld"+ChatColor.RED+".");
					}
					else {
							sender.sendMessage(ChatColor.GREEN+"Sucessfully added new Location "+one+" to Plot: "+id+".");
						}
				}else {
					sender.sendMessage(ChatColor.RED+"Plot doesn't exist.");
				}
					
				break;
				
			default: 
				sender.sendMessage("Unknown Command");
				return false;
			}
			return true;
		}
		else {
			sender.sendMessage(ChatColor.RED+"Too many arguments");
		}
	
		return false;
	}
	*/
	
	/*TODO: Add Tab Completer
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		return null;
	}
	*/

}
