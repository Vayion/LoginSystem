package de.vayion.LoginSystem;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListeners implements Listener {
	
	Main main;
	
	public ConnectionListeners(Main main) {
		this.main = main;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		main.getIDMain().addPlayer(event.getPlayer());
		event.getPlayer().setOp(false);
		event.getPlayer().setGameMode(GameMode.ADVENTURE);
		main.getLocationManager().teleportToLobby(event.getPlayer());
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		main.getIDMain().logout(event.getPlayer());
		main.getIDMain().removePlayer(event.getPlayer());
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		main.getIDMain().logout(event.getPlayer());
		main.getIDMain().removePlayer(event.getPlayer());
	}
	
}
