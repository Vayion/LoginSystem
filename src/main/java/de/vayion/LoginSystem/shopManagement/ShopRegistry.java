package de.vayion.LoginSystem.shopManagement;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.Utils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class ShopRegistry {

    private final Main main;
    private final HashMap<Block, ShopSign> signs = new HashMap<>();

    public ShopRegistry(Main main) {
        this.main = main;
        main.getCommand("shop").setExecutor(new ShopCommand(this));
    }

    public boolean createBuySign(Location loc, ItemStack product, long price){
        if(signs.containsKey(loc)){
            return false;
        }

        if(addBuySign(loc,product,price)){

            //TODO: implement saving of sign
            return true;
        }
        return false;
    }

    public ShopSign getSign(Location loc) {
        return signs.get(loc.getBlock());
    }

    public boolean addBuySign(Location loc, ItemStack product, long price) {
        if(loc==null||!(loc.getBlock().getState() instanceof Sign)) {
            //TODO: delete sign
            Bukkit.broadcastMessage("Not a sign");
            return false;
        }
        Sign sign = (Sign) loc.getBlock().getState();

        String line0, line1, line2;
        sign.setLine(0, line0 = product.getAmount()+ " "+"product.getItemMeta().getDisplayName()");
        sign.setLine(1, line1 = "für:");
        sign.setLine(2, line2 = (double)price/100D+"€");
        String hash = Utils.hashSign(line0+line1+line2, loc);
        //sign.setLine(3, hash);
        sign.update();

        signs.put(loc.getBlock(), new BuySign(product, price, hash));
        return true;
    }

    public Main getMain() {
        return main;
    }
}
