package de.vayion.LoginSystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.vayion.LoginSystem.Main;

public class AddUser implements CommandExecutor {
	
	Main main;
	
	public AddUser(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if((sender instanceof Player)) {
			player = (Player) sender;
			
			if(!player.hasPermission("login.admin")) {
				player.sendMessage("Du kannst diesen Befehl nicht ausführen.");
				return false;
			}
			
		}
		if(args.length==0) {
			if(player == null) {
				System.out.println("Du musst einen Namen angeben.");
			}
			else {
				player.sendMessage("Du musst einen Namen angeben.");
			}
			return false;
		}

		String name = args[0];
		if(args.length>1) {
			for(int i = 1; i< args.length;i++) {
				name = name +" "+args[i];
			}
		}
		
		main.getIDMain().addNewUser(name);
		
		//System.out.println(name);
		//System.out.println(Utils.getString(6, Mode.ALPHANUMERIC));
		
		
		
		return false;
	}
}
