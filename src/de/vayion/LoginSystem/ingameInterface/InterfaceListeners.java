package de.vayion.LoginSystem.ingameInterface;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class InterfaceListeners implements Listener {

	private InterfaceMain interfaceMain;
	
	public InterfaceListeners(InterfaceMain interfaceMain) {
		this.interfaceMain = interfaceMain;
	}
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getItem()!=null) {
			if(event.getItem().equals(interfaceMain.getMenu())) {
				interfaceMain.openMenuInterface(event.getPlayer());
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onOffhandSwap(PlayerSwapHandItemsEvent event) {
		if(event.getMainHandItem()!=null&&!(event.getMainHandItem().equals(interfaceMain.getMenu()))) {event.setCancelled(true);}
		if(event.getOffHandItem()!=null&&!(event.getOffHandItem().equals(interfaceMain.getMenu()))) {event.setCancelled(true);}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if(event.getClickedInventory()!=null&&event.getView()!=null) {
			if(interfaceMain.handleItemStack(event.getView().getTitle(), event.getCurrentItem(), (Player) (event.getWhoClicked()))) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		if(interfaceMain.getMain().getIDMain().playerIsLoggedIn(event.getPlayer())) {
			event.getPlayer().getInventory().setItem(8, InterfaceMain.generateMenu());
		}
	}
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if(event.getItemDrop().getItemStack().equals(interfaceMain.getMenu())) {
			event.setCancelled(true);
		}
	}
}
