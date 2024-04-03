package de.vayion.LoginSystem.shopManagement;


import de.vayion.LoginSystem.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class ShopSign {
    public String hash;
    public boolean seller;
    public abstract boolean executeTrade(Player player, Main main);


}
