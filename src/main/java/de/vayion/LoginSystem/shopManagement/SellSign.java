package de.vayion.LoginSystem.shopManagement;

import de.vayion.LoginSystem.Main;
import org.bukkit.entity.Player;

public class SellSign extends ShopSign {

    public SellSign(){}
    @Override
    public boolean executeTrade(Player player, Main main) {
        return false;
    }
}
