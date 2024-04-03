package de.vayion.LoginSystem.shopManagement;

import de.vayion.LoginSystem.Utils;
import de.vayion.LoginSystem.idManagement.UserProfile;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ShopListeners implements Listener {
    private final ShopRegistry registry;
    public ShopListeners(ShopRegistry registry) {
        this.registry = registry;
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        //event.getPlayer().sendMessage("This shit event fired.");
        if(event.getClickedBlock()!=null && event.getClickedBlock().getState() instanceof Sign){

            // establish all necessary variables
            Sign sign = (Sign) (event.getClickedBlock().getState());
            ShopSign shopSign = registry.getSign(sign.getLocation());
            if(shopSign==null) {
                //event.getPlayer().sendMessage("This shit isnt a shop.");
                //no valid sign exists anyways
                return;
            }

            String hash = Utils.hashSign(sign.getLine(0)+sign.getLine(1)+sign.getLine(2), sign.getLocation());
            Player player = event.getPlayer();
            UserProfile prof = registry.getMain().getIDMain().getPlayerProfile(player);
            if(!hash.equals(shopSign.hash)) {
                player.sendMessage("Hash values don't match up, this is not a valid sign! Found Hash in Database: "+shopSign.hash+ " vs: " + hash+"!");
                return;
            }

            shopSign.executeTrade(player, registry.getMain());
        }
    }
}