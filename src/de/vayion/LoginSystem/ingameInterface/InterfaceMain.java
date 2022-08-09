package de.vayion.LoginSystem.ingameInterface;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.vayion.LoginSystem.Main;

public class InterfaceMain {
	
	private ItemStack menu, close, plot, home, farm, sethome, grayGlass, city;
	private Main main;
	private String inventoryName = "Menü";
	
	public InterfaceMain(Main main) {
		this.main = main;
		
		main.getServer().getPluginManager().registerEvents(new InterfaceListeners(this), main);
		
		menu = generateMenu();
		close = generateClose();
		plot = generatePlot();
		home = generateHome();
		sethome = generateSetHome();
		farm = generateFarm();
		grayGlass = generateMenuGlass();
		city = generateCity();
	}
	
	public void openMenuInterface(Player player) {
		Inventory inv = Bukkit.createInventory(player, 5*9, inventoryName);
		
		for (int i = 0; i < inv.getSize(); i++) {
			inv.setItem(i, grayGlass.clone());
		}
		
		inv.setItem(19, plot);
		inv.setItem(21, home);
		inv.setItem(23, farm);
		inv.setItem(25, city);
		inv.setItem(40, close);
		
		player.openInventory(inv);
		player.updateInventory();
	}
	
	/*
	 * Generators for Menu Items
	 */
	
	/**
	 * generates menu glass
	 * @return menu glass
	 */
	public static ItemStack generateMenuGlass() {
		ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
		ItemMeta grayM = grayGlass.getItemMeta();
		grayM.setDisplayName(ChatColor.WHITE+"");
		grayGlass.setItemMeta(grayM);
		return grayGlass;
	}
	/**
	 * generates Menu Item
	 * @return Menu Item
	 */
	public static ItemStack generateMenu() {
		ItemStack item;
		item = new ItemStack(Material.NETHER_STAR);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN+"Menü");
		ArrayList<String> list = new ArrayList<String>();
		list.add(ChatColor.YELLOW+"Nutze dies um das Menü zu öffnen.");
		meta.setLore(list);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * generates Menu Item
	 * @return Menu Item
	 */
	public static ItemStack generateCity() {
		ItemStack item;
		item = new ItemStack(Material.BEACON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN+"Stadt");
		ArrayList<String> list = new ArrayList<String>();
		list.add(ChatColor.YELLOW+"Nutze dies um zur Stadt zu gelangen.");
		meta.setLore(list);
		item.setItemMeta(meta);
		return item;
	}
	/**
	 * generates Menu Close Item
	 * @return Menu Close Item
	 */
	public static ItemStack generateClose() {
		ItemStack item;
		item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN+"Schließen");
		ArrayList<String> list = new ArrayList<String>();
		list.add(ChatColor.YELLOW+"Klicke hier um das Menü zu schließen.");
		meta.setLore(list);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * generates Plot Menu Item
	 * @return Plot Menu Item
	 */
	public static ItemStack generatePlot() {
		ItemStack item;
		item = new ItemStack(Material.WOODEN_HOE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN+"Dein Grundstück");
		ArrayList<String> list = new ArrayList<String>();
		list.add(ChatColor.YELLOW+"Klicke hier um dein Grundstück zu verwalten.");
		meta.setLore(list);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * generates Sethome Menu Item
	 * @return Sethome Menu Item
	 */
	public static ItemStack generateSetHome() {
		ItemStack item;
		item = new ItemStack(Material.REDSTONE_TORCH);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN+"Zuhause setzen");
		ArrayList<String> list = new ArrayList<String>();
		list.add(ChatColor.YELLOW+"Klicke hier um die Position deines Zuhauses zu setzen.");
		meta.setLore(list);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * generates Home Menu Item
	 * @return Home Menu Item
	 */
	public static ItemStack generateHome() {
		ItemStack item;
		item = new ItemStack(Material.RED_BED);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN+"Nach Hause");
		ArrayList<String> list = new ArrayList<String>();
		list.add(ChatColor.YELLOW+"Klicke hier um nach Hause zu kommen.");
		meta.setLore(list);
		item.setItemMeta(meta);
		return item;
	}

	/**
	 * generates Farm Menu Item
	 * @return Farm Menu Item
	 */
	public static ItemStack generateFarm() {
		ItemStack item;
		item = new ItemStack(Material.GRASS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN+"Farmwelt");
		ArrayList<String> list = new ArrayList<String>();
		list.add(ChatColor.YELLOW+"Klicke hier um zur Farmwelt zu kommen.");
		meta.setLore(list);
		item.setItemMeta(meta);
		return item;
	}
	
	/**
	 * handles the player clicking an item. called by EventListeners
	 * @param item item that needs to be handled
	 * @param player player that clicked an item
	 * @return returns if the event shall be cancelled
	 */
	public boolean handleItemStack(String inventoryName, ItemStack item, Player player) {
		if(item.equals(menu)) {
			openMenuInterface(player);
			return true;
		}
		if((inventoryName==null)||(!inventoryName.equals(this.inventoryName))) {
			return false;
		}
		else {	
			if(item.equals(close)) {
				player.closeInventory();
			}
			else if(item.equals(plot)) {
				player.closeInventory();
				player.sendMessage(ChatColor.GRAY+"Dies ist noch nicht erhältlich.");
			}
			else if(item.equals(home)) {
				player.closeInventory();
				main.getIDMain().sendHome(player);
			}
			else if(item.equals(sethome)) {
				player.closeInventory();
				player.sendMessage(ChatColor.GRAY+"Dies ist noch nicht erhältlich.");
			}
			else if(item.equals(farm)) {
				main.getLocationManager().teleportToFarm(player);
				player.closeInventory();
			}
			else if(item.equals(city)) {
				main.getLocationManager().teleportToCity(player);
			}
			else {
				//nothing happens
			}
			return true;
		}
	}
	
	public String getInventoryName() {
		return inventoryName;
	}
	
	public ItemStack getCity() {
		return city;
	}
	
	public ItemStack getMenu() {
		return menu;
	}
	
	public ItemStack getClose() {
		return close;
	}
	
	public ItemStack getFarm() {
		return farm;
	}
	
	public ItemStack getHome() {
		return home;
	}
	
	public Main getMain() {
		return main;
	}
	
	public ItemStack getPlot() {
		return plot;
	}
	
	public ItemStack getSethome() {
		return sethome;
	}
}
