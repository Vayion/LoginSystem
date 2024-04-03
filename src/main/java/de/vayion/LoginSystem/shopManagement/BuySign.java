package de.vayion.LoginSystem.shopManagement;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.Utils;
import de.vayion.LoginSystem.idManagement.UserProfile;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BuySign extends ShopSign {


    private ItemStack product;
    private long price;
    public BuySign(ItemStack product, long price, String hash){
        this.price = price;
        this.product = product;
        this.hash = hash;
        this.seller = true;
    }
    @Override
    public boolean executeTrade(Player player, Main main) {
        UserProfile prof = main.getIDMain().getPlayerProfile(player);
        if(prof != null && prof.getCurrency() >= price) {
            Utils.addItem(player, product);
            prof.subtractCurrency(price);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
            return true;
        }
        else {
            player.sendMessage("Du kannst dir das nicht leisten!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return false;
        }
    }
}
