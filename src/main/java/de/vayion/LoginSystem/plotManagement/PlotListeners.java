package de.vayion.LoginSystem.plotManagement;

import java.util.ArrayList;

import de.vayion.LoginSystem.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.BlockInventoryHolder;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.commands.ToggleeditCmd;

public class PlotListeners implements Listener {

	Main main;
	ArrayList<Player> players;

	public PlotListeners(Main main) {
		this.main = main;
		players = new ArrayList<Player>();

		main.getCommand("toggleedit").setExecutor(new ToggleeditCmd(this));
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (players.contains(player)) {
			return;
		}
		if (player != null) {
			if (main.getIDMain().playerIsLoggedIn(player)) {
				if (player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
					if (!main.getIDMain().isAllowed(event.getBlock().getLocation(), event.getPlayer())) {
						event.setCancelled(true);
						player.sendMessage(
								ChatColor.RED + "Du musst auf deinem eigenen Grundstück stehen um dies zu tun.");
					}
				}
			} else {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (players.contains(player)) {
			return;
		}
		if (player != null) {
			if (main.getIDMain().playerIsLoggedIn(player)) {
				if (player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
					if (!main.getIDMain().isAllowed(event.getBlock().getLocation(), event.getPlayer())) {
						event.setCancelled(true);
						player.sendMessage(
								ChatColor.RED + "Du musst auf deinem eigenen Grundstück stehen um dies zu tun.");
					}
				}
			} else {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (event.getDrops().contains(main.getInterfaceMain().getMenu())) {
			event.getDrops().remove(main.getInterfaceMain().getMenu());
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (players.contains(player)) {
			return;
		}
		if ((main.getPlotManager().getPlotWorld() != null)
				&& (player.getWorld().equals(main.getPlotManager().getPlotWorld()))) {
			if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				Block block = event.getClickedBlock();
				switch (block.getType()) {
				// a dirty solution of just returning whenever its not the listed blocks, so
				// that I dont have to do some shit with a called function at the end
				case ENCHANTING_TABLE:
					break;
				case REPEATER:
					break;
				case COMPARATOR:
					break;

				case NOTE_BLOCK:
					Block blocky = block.getWorld().getBlockAt(block.getLocation().clone().add(0, -1, 0));
					if (blocky.getType().equals(Material.BARRIER)) {
						Plot plot = main.getPlotManager().getPlotByCornerBlock(event.getClickedBlock().getLocation());
						if (plot == null) {
							player.sendMessage(ChatColor.RED
									+ "Dieser Block gehört zu keinem Grundstück. Kontaktiere einen Erzieher falls dies nicht passieren sollte");
							return;
						}
						if (plot.isClaimed()) {
							if (main.getIDMain().isOwnPlot(plot, player)) {
								player.sendMessage(ChatColor.GREEN + "Dieses Grundstück gehört dir schon.");
							} else {
								player.sendMessage(ChatColor.RED + "Dieses Grundstück gehört schon "
										+ plot.getClaimer().getName() + ".");
							}
							return;
						}
						if (main.getIDMain().claimPlot(plot.getID(), player)) {
							player.sendMessage(ChatColor.GREEN + "Du hast erfolgreich dieses Grundstück gekauft.");
							return;
						} else {
							player.sendMessage(ChatColor.RED + "Du besitzt schon ein Grundstück.");
							return;
						}
					}
					break;

				default:
					if (!(block.getState() instanceof BlockInventoryHolder
							|| block.getBlockData() instanceof Openable)) {
						return;
					}
				}

				if (!main.getIDMain().isInBorders(block.getLocation(), player)) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED + "Das ist nicht deins.");
				}
			}
		}
	}

	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event) {
		World world = main.getPlotManager().getPlotWorld();
		if (world == null) {
			return;
		}
		if (event.getBlock().getLocation().getWorld().equals(world)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		World world = main.getPlotManager().getPlotWorld();
		if (world == null) {
			return;
		}
		if (event.getLocation().getWorld().equals(world)) {
			event.setCancelled(true);
		}
	}

	/**
	 * 
	 */
	public void onPlayerTakeLecternBookEvent(PlayerTakeLecternBookEvent event) {
		Player player = event.getPlayer();
		if (players.contains(player)) {
			return;
		}
		if (player != null) {
			if (main.getIDMain().playerIsLoggedIn(player)) {
				if (player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
					if (!main.getIDMain().isAllowed(event.getLectern().getLocation(), event.getPlayer())) {
						event.setCancelled(true);
						player.sendMessage(
								ChatColor.RED + "Du musst auf deinem eigenen Grundstück stehen um dies zu tun.");
					}
				}
			} else {
				event.setCancelled(true);
			}
		}
	}

	// not necessary due to being handled by Multiverse-Core
	/*
	 * @EventHandler public void onMobSpawn(CreatureSpawnEvent event) { World world
	 * = main.getPlotManager().getPlotWorld(); if(world==null) {return;}
	 * if(event.getLocation().getWorld().equals(world)) { event.setCancelled(true);
	 * } }
	 */

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			World world = main.getPlotManager().getPlotWorld();
			if (world == null) {
				return;
			}
			if (event.getEntity().getLocation().getWorld().equals(world)) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDamagesAnimal(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			return;
			/* Handled in onPlayerDamage() */}
		if (event.getDamager() instanceof Player && main.getPlotManager().getPlotWorld() != null
				&& event.getEntity().getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
			Entity damaged = event.getEntity();
			Player player = (Player) event.getDamager();
//			Debug: player.sendMessage(main.getIDMain().playerIsLoggedIn(player)?"You are logged in"+(main.getIDMain().getPlayerProfile(player).getPlot().isAllowed(damaged.getLocation())?" and are allowed to punch here":""):"");

			if (!main.getIDMain().isAllowed(damaged.getLocation(), player)) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Du musst auf deinem eigenen Grundstück stehen um dies zu tun.");
			}
		}
	}

	@EventHandler
	public void onStructureGrowth(StructureGrowEvent event) {
		if (main.getPlotManager().getPlotWorld() != null
				&& main.getPlotManager().getPlotWorld().equals(event.getLocation().getWorld())) {
			Plot plot = main.getPlotManager().getPlotByLoc(event.getLocation());
			if (plot == null) {
				event.setCancelled(true);
				return;
			}
			for (int i = 0; i < event.getBlocks().size(); i++) {
				if (!plot.isAllowed(event.getBlocks().get(i).getLocation())) {
					event.getBlocks().get(i).setType(Material.AIR);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerTrample(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (players.contains(player)) {
			return;
		}
		if (event.getAction() == Action.PHYSICAL && event.getClickedBlock() != null) {
			if (event.getClickedBlock().getType().equals(Material.FARMLAND)) {
				if (main.getIDMain().playerIsLoggedIn(player)) {
					if (player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
						if (!main.getIDMain().isAllowed(event.getClickedBlock().getLocation(), event.getPlayer())) {
							event.setCancelled(true);
						}
					}
				} else {
					event.setCancelled(true);
				}
			}

		}

	}

	@EventHandler
	public void onHangingRemove(HangingBreakByEntityEvent event) {
		if(event.getRemover() instanceof Player) {
			Player player = (Player)(event.getRemover());
			if (players.contains(player)) {
				return;
			}

			if (player != null) {
				if (main.getIDMain().playerIsLoggedIn(player)) {
					if (player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
						if (!main.getIDMain().isAllowed(event.getEntity().getLocation(), player)) {
							event.setCancelled(true);
							player.sendMessage(
									ChatColor.RED + "Du musst auf deinem eigenen Grundstück stehen um dies zu tun.");
						}
					}
				} else {
					event.setCancelled(true);
				}
			}
		}
	}

	
	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent event) {
		onBucketUse(event);
	}
	
	@EventHandler
	public void onBucketFill(PlayerBucketEmptyEvent event) {
		onBucketUse(event);
	}
	
	@EventHandler
	public void onBucketFish(PlayerBucketEntityEvent event) {
		Player player = event.getPlayer();
		if (players.contains(player)) {
			return;
		}
		if (player != null) {
			if (main.getIDMain().playerIsLoggedIn(player)) {
				if (player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
					if (!main.getIDMain().isAllowed(event.getEntity().getLocation(), event.getPlayer())) {
						event.setCancelled(true);
						player.sendMessage(
								ChatColor.RED + "Du musst auf deinem eigenen Grundstück stehen um dies zu tun.");
					}
				}
			} else {
				event.setCancelled(true);
			}
		}
	}
	
	private void onBucketUse(PlayerBucketEvent event) {
		Player player = event.getPlayer();
		if (players.contains(player)) {
			return;
		}
		if (player != null) {
			if (main.getIDMain().playerIsLoggedIn(player)) {
				if (player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
					if (!main.getIDMain().isAllowed(event.getBlock().getLocation(), event.getPlayer())) {
						event.setCancelled(true);
						player.sendMessage(
								ChatColor.RED + "Du musst auf deinem eigenen Grundstück stehen um dies zu tun.");
					}
				}
			} else {
				event.setCancelled(true);
			}
		}
	}
	 
	public boolean toggleEdit(Player player) {
		if (players.contains(player)) {
			players.remove(player);
			return false;
		} else {
			players.add(player);
			return true;
		}
	}

	//@EventHandler
	public void playerTestEvent(PlayerBedEnterEvent event) {
		if(!Utils.subtractItem(event.getPlayer(), Material.GOLD_NUGGET, 10)) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("Give the Toll!.");
		}
	}

	public void removeFromEdit(Player player) {
		players.remove(player);
	}
}
