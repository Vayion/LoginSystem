package de.vayion.LoginSystem;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Utils {
	public enum Mode{
		APLHA, ALPHANUMERIC, NUMERIC, SYMBOLIC, ALPHASYMBOLIC, NUMERICSYMBOLIC, APLHANUMERICSYMBOLIC;
	}
	
	public static String getString(int lenght, Mode mode){
		StringBuilder builder = new StringBuilder();
		String s = "";
		switch(mode){
		case APLHA:
			s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
			break;
		case ALPHANUMERIC:
			s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			break;
		case NUMERIC:
			s = "0123456789";
			break;
		case SYMBOLIC:
			s = "~,.:?;[]{}�`^!@#$%�&*()-_+=></ ";
			break;
		case ALPHASYMBOLIC:
			s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ~,.:?;[]{}�`^!@#$%�&*()-_+=></ ";
			break;
		case NUMERICSYMBOLIC:
			s = "0123456789~,.:?;[]{}�`^!@#$%�&*()-_+=></ ";
			break;
		case APLHANUMERICSYMBOLIC:
			s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789~,.:?;[]{}�`^!@#$%�&*()-_+=></ ";
			break;
		}
		for(int i = 0; i<lenght; i++){
			double index = Math.random() * s.length();
			builder.append(s.charAt((int)index));
		}
		return builder.toString();
	}
	
	@SuppressWarnings("deprecation")
    public static void changeName(String name, Player player) {
        player.setCustomName(player.getName());
        player.setPlayerListName(name);
        player.setDisplayName(name);
       
        try {
            Method getHandle = player.getClass().getMethod("getHandle");
            Object entityPlayer = getHandle.invoke(player);
            boolean gameProfileExists = false;
            try {
                Class.forName("net.minecraft.util.com.mojang.authlib.GameProfile");
                gameProfileExists = true;
            } catch (ClassNotFoundException ignored) {
            }
            try {
                Class.forName("com.mojang.authlib.GameProfile");
                gameProfileExists = true;
            } catch (ClassNotFoundException ignored) {
            }
            if (!gameProfileExists) {
                Field nameField = entityPlayer.getClass().getSuperclass().getDeclaredField("name");
                nameField.setAccessible(true);
                nameField.set(entityPlayer, name);
            } else {
                Object profile = entityPlayer.getClass().getMethod("getProfile").invoke(entityPlayer);
                Field ff = profile.getClass().getDeclaredField("name");
                ff.setAccessible(true);
                ff.set(profile, name);
            }
            if (Bukkit.class.getMethod("getOnlinePlayers", new Class<?>[0]).getReturnType() == Collection.class) {
                @SuppressWarnings("unchecked")
                Collection<? extends Player> players = (Collection<? extends Player>) Bukkit.class.getMethod("getOnlinePlayers").invoke(null);
                for (Player p : players) {
                    p.hidePlayer(player);
                    p.showPlayer(player);
                }
            } else {
                Player[] players = ((Player[]) Bukkit.class.getMethod("getOnlinePlayers").invoke(null));
                for (Player p : players) {
                    p.hidePlayer(player);
                    p.showPlayer(player);
                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
	
	public static boolean checkIfValidSpawn(Location location) {
		if(((location.getBlock().getType().equals(Material.LAVA))||(location.clone().add(0,1,0).getBlock().getType().equals(Material.LAVA)))||
			((location.getBlock().getType().isSolid())||(location.clone().add(0,1,0).getBlock().getType().isSolid()))) {
			return false;
		}
		for(int i = 1; i <=8 ;i++) {
			if((location.clone().subtract(0,i,0).getBlock().getType().equals(Material.LAVA))) {
				return false;
			}
			if(location.clone().subtract(0,i,0).getBlock().getType().isSolid()) {
				return true;
			}
		}
		return false;
	}
	
	public static void createPlotBorders(int minX, int maxX, int minZ, int maxZ, World world) {
		minX--; minZ--;
		maxX++; maxZ++;
		for(int i = 1; i < maxX-minX; i++) {
			try {
				Block block1 = world.getHighestBlockAt(i+minX, maxZ).getLocation().add(0,1,0).getBlock();
				if(!block1.getType().equals(Material.DARK_OAK_SLAB)) {
					block1.setType(Material.DARK_OAK_SLAB);
				}
				Block block2 = world.getHighestBlockAt(i+minX, minZ).getLocation().add(0,1,0).getBlock();
				if(!block2.getType().equals(Material.DARK_OAK_SLAB)) {
					block2.setType(Material.DARK_OAK_SLAB);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		for(int i = 1; i < maxZ-minZ; i++) {
			try {
				Block block1 = world.getHighestBlockAt(maxX, minZ+i).getLocation().add(0,1,0).getBlock();
				if(!block1.getType().equals(Material.DARK_OAK_SLAB)) {
					block1.setType(Material.DARK_OAK_SLAB);
				}
				Block block2 = world.getHighestBlockAt(minX, minZ+i).getLocation().add(0,1,0).getBlock();
				if(!block2.getType().equals(Material.DARK_OAK_SLAB)) {
					block2.setType(Material.DARK_OAK_SLAB);
				}	
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		Block corner1 = world.getHighestBlockAt(minX, minZ);
		try {
			if(!corner1.getType().equals(Material.NOTE_BLOCK)) {
				corner1.setType(Material.BARRIER);
				corner1.getLocation().add(0, 1, 0).getBlock().setType(Material.NOTE_BLOCK);
			}
		}
		catch (Exception e) {
			// we dont handle
		}
		Block corner2 = world.getHighestBlockAt(maxX, minZ);
		try {
			if(!corner2.getType().equals(Material.NOTE_BLOCK)) {
				corner2.setType(Material.BARRIER);
				corner2.getLocation().add(0, 1, 0).getBlock().setType(Material.NOTE_BLOCK);
			}
		}
		catch (Exception e) {
			// we dont handle
		}
		Block corner3 = world.getHighestBlockAt(minX, maxZ);
		try {
			if(!corner3.getType().equals(Material.NOTE_BLOCK)) {
				corner3.setType(Material.BARRIER);
				corner3.getLocation().add(0, 1, 0).getBlock().setType(Material.NOTE_BLOCK);
			}
		}
		catch (Exception e) {
			// we dont handle
		}
		Block corner4 = world.getHighestBlockAt(maxX, maxZ);
		try {
			if(!corner4.getType().equals(Material.NOTE_BLOCK)) {
				corner4.setType(Material.BARRIER);
				corner4.getLocation().add(0, 1, 0).getBlock().setType(Material.NOTE_BLOCK);
			}
		}
		catch (Exception e) {
			// we dont handle
		}
		
	}

	public static boolean subtractItem(Player player, Material item, int amount){
		// Pair<Integer, ItemStack> doesn't want to work, so I will use a Hashmap for the queue
		PlayerInventory inventory = player.getInventory();
		HashMap<Integer, ItemStack> queue = new HashMap<>();
		for (ItemStack stack : inventory) {

			if(stack!= null && stack.getType().equals(item)) {
				int min = Integer.min(amount, stack.getAmount());
				queue.put(min, stack);
				amount -= min;
			}

			if(amount <= 0) {
				break;
			}
		}
		if(amount <= 0) {
			queue.forEach((i,e)->e.setAmount(e.getAmount()-i));
			return true;
		}
		return false;
	}
	public static boolean addItem(Player player, ItemStack item){
		if(player.getInventory().firstEmpty()==-1){
			return false;
		}
		Map<Integer, ItemStack> map = player.getInventory().addItem(item);
		if(!map.isEmpty()){
			map.forEach((key, value) -> player.getWorld().dropItem(player.getLocation(), value));
		}
		return true;
	}


	public static String hashSign(String signText, Location signLoc) {
		signText = signText+ signLoc.toString();
		long hash = 7;
		for (int i = 0; i < signText.length(); i++) {
			hash = hash*31 + signText.charAt(i);
		}
		return Long.toHexString(hash);
	}

	public static String formatMoney(long balance) {
		return balance / 100 + "," + balance % 100+"€";
	}
}
