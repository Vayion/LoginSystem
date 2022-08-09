package de.vayion.LoginSystem;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
}
