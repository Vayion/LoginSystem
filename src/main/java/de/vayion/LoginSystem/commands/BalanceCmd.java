package de.vayion.LoginSystem.commands;

import de.vayion.LoginSystem.Main;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class BalanceCmd implements CommandExecutor {

    private Main main;

    public BalanceCmd(Main main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(!sender.hasPermission("login.economy")){
                player.sendMessage("Missing perms");
                return false;
            }
            if(args.length!=0){
                switch(args[0]){
                    case "add":
                        if(args.length == 2) {
                            try{
                                int added = Integer.parseUnsignedInt(args[1]);
                                if(main.getIDMain().playerIsLoggedIn(player)) {
                                    main.getIDMain().getPlayerProfile(player).addCurrency(added);
                                }
                            }
                            catch (NumberFormatException exception) {
                                player.sendMessage(args[1]+" is not a valid number!");
                            }
                        }
                        break;
                }
            }
        }
        return true;
    }
}
