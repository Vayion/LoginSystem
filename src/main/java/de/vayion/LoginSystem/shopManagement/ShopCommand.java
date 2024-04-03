package de.vayion.LoginSystem.shopManagement;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

public class ShopCommand implements CommandExecutor {
    private ShopRegistry reg;
    public ShopCommand(ShopRegistry reg) {
        this.reg = reg;
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
                        if(player.getInventory().getItemInMainHand().getAmount() == 0) {
                            player.sendMessage("You are holding no item!");
                        }
                        else {
                            reg.addBuySign(player.getTargetBlock((HashSet<Material>) null,10).getLocation(), player.getInventory().getItemInMainHand(), 100);
                        }
                        break;
                }
            }
        }
        return true;
    }
}
