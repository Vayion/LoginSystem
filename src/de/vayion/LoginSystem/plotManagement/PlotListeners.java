package de.vayion.LoginSystem.plotManagement;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.event.world.StructureGrowEvent;

import de.vayion.LoginSystem.Main;
import de.vayion.LoginSystem.commands.ToggleeditCmd;

public class PlotListeners implements Listener{
	
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
		if(players.contains(player)) {return;}
		if(player!=null) {
			if(main.getIDMain().playerIsLoggedIn(player)) {
				if(player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
					if(!main.getIDMain().isAllowed(event.getBlock().getLocation(), event.getPlayer())) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED+"Du musst auf deinem eigenen Grundstück stehen um dies zu tun.");
					}	
				}
			}
			else {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if(players.contains(player)) {return;}
		if(player!=null) {
			if(main.getIDMain().playerIsLoggedIn(player)) {
				if(player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
					if(!main.getIDMain().isAllowed(event.getBlock().getLocation(), event.getPlayer())) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED+"Du musst auf deinem eigenen Grundstück stehen um dies zu tun.");
					}	
				}
			}
			else {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if(event.getDrops().contains(main.getInterfaceMain().getMenu())) {
			event.getDrops().remove(main.getInterfaceMain().getMenu());
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(players.contains(player)) {return;}
		if((main.getPlotManager().getPlotWorld()!=null)&&(player.getWorld().equals(main.getPlotManager().getPlotWorld()))) {
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				Block block = event.getClickedBlock();
				switch(block.getType()) {
				//a dirty solution of just returning whenever its not the listed blocks, so that I dont have to do some shit with a called function at the end
				case CHEST:break;
				case FURNACE:break;
				case ANVIL:break;
				case TRAPPED_CHEST:break;
				case BREWING_STAND:break;
				case ENCHANTING_TABLE:break;
				case BARREL: break;
				case REPEATER: break;
				case COMPARATOR: break;
				
				case NOTE_BLOCK:
					Block blocky = block.getWorld().getBlockAt(block.getLocation().clone().add(0, -1, 0));
					if(blocky.getType().equals(Material.BARRIER)) {
						Plot plot = main.getPlotManager().getPlotByCornerBlock(event.getClickedBlock().getLocation());
						if(plot==null) {player.sendMessage(ChatColor.RED+"Dieser Block gehört zu keinem Grundstück. Kontaktiere einen Erzieher falls dies nicht passieren sollte"); return;}
						if(plot.isClaimed()) {
							if(main.getIDMain().isOwnPlot(plot, player)) {player.sendMessage(ChatColor.GREEN+"Dieses Grundstück gehört dir schon.");}
							else {
								player.sendMessage(ChatColor.RED+"Dieses Grundstück gehört schon jemanden.");
							}
							return;
						}
						if(main.getIDMain().claimPlot(plot.getID(), player)) {
							player.sendMessage(ChatColor.GREEN+"Du hast erfolgreich dieses Grundstück gekauft."); return;
						}
						else {
							player.sendMessage(ChatColor.RED+"Du besitzt schon ein Grundstück."); return;
						}
					}
					break;
				
				default:
					if(block.getState()instanceof ShulkerBox) {
						
					}else {
						return;	
					}
				}
				
				if(!main.getIDMain().isInBorders(block.getLocation(), player)) {
					event.setCancelled(true);
					player.sendMessage(ChatColor.RED+"Das ist nicht deins.");
				}
			}
		}
		/*
		if((player!=null)&&(event.getClickedBlock()!=null)) {
			if(main.getIDMain().playerIsLoggedIn(player)) {
				if(player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
					if(!main.getIDMain().isAllowed(event.getClickedBlock().getLocation(), event.getPlayer())) {
						event.setCancelled(true);
					}
					Block block1 = event.getClickedBlock();
					Block block2 = block1.getWorld().getBlockAt(block1.getLocation().clone().add(0, -1, 0));
					if(block1.getType().equals(Material.NOTE_BLOCK)) {
						if(block2.getType().equals(Material.BARRIER)) {
							Plot plot = main.getPlotManager().getPlotByCornerBlock(event.getClickedBlock().getLocation());
							if(plot==null) {player.sendMessage(ChatColor.RED+"Dieser Block gehört zu keinem Grundstück. Kontaktiere einen Erzieher falls dies nicht passieren sollte"); return;}
							if(plot.isClaimed()) {
								if(main.getIDMain().isOwnPlot(plot, player)) {player.sendMessage(ChatColor.GREEN+"Dieses Grundstück gehört dir schon.");}
								else {
									player.sendMessage(ChatColor.RED+"Dieses Grundstück gehört schon jemanden.");
								}
								return;
							}
							if(main.getIDMain().claimPlot(plot.getID(), player)) {
								player.sendMessage(ChatColor.GREEN+"Du hast erfolgreich dieses Grundstück gekauft."); return;
							}
							else {
								player.sendMessage(ChatColor.RED+"Du besitzt schon ein Grundstück."); return;
							}
						}
					}
				}
			}
			else {
				event.setCancelled(true);
			}
		}
		*/
	}
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent event) {
		World world = main.getPlotManager().getPlotWorld();
		if(world==null) {return;}
		if(event.getBlock().getLocation().getWorld().equals(world)) {
			event.setCancelled(true);
		}	
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		World world = main.getPlotManager().getPlotWorld();
		if(world==null) {return;}
		if(event.getLocation().getWorld().equals(world)) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * 
	 */
	public void onPlayerTakeLecternBookEvent(PlayerTakeLecternBookEvent event) {
		Player player = event.getPlayer();
		if(players.contains(player)) {return;}
		if(player!=null) {
			if(main.getIDMain().playerIsLoggedIn(player)) {
				if(player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
					if(!main.getIDMain().isAllowed(event.getLectern().getLocation(), event.getPlayer())) {
						event.setCancelled(true);
						player.sendMessage(ChatColor.RED+"Du musst auf deinem eigenen Grundstück stehen um dies zu tun.");
					}	
				}
			}
			else {
				event.setCancelled(true);
			}
		}
	}
	
	
	//not necessary due to being handled by Multiverse-Core
	/*@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event) {
		World world = main.getPlotManager().getPlotWorld();
		if(world==null) {return;}
		if(event.getLocation().getWorld().equals(world)) {
			event.setCancelled(true);
		}
	}*/
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			World world = main.getPlotManager().getPlotWorld();
			if(world==null) {return;}
			if(event.getEntity().getLocation().getWorld().equals(world)) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onStructureGrowth(StructureGrowEvent event) {
		if(main.getPlotManager().getPlotWorld()!=null&&main.getPlotManager().getPlotWorld().equals(event.getLocation().getWorld())) {
			Plot plot = main.getPlotManager().getPlotByLoc(event.getLocation());
			if(plot==null) {return;}
			for (int i = 0; i<event.getBlocks().size();i++) {
				if(!plot.isAllowed(event.getBlocks().get(i).getLocation())) {
					event.getBlocks().get(i).setType(Material.AIR);
				}
			}
		}
	}
	
	 @EventHandler
	 public void onPlayerTrample(PlayerInteractEvent event) {
		 Player player = event.getPlayer();
			if(players.contains(player)) {return;}
			if(event.getAction()==Action.PHYSICAL && event.getClickedBlock()!=null) {
				 if(event.getClickedBlock().getType().equals(Material.FARMLAND)){
						if(main.getIDMain().playerIsLoggedIn(player)) {
							if(player.getLocation().getWorld().equals(main.getPlotManager().getPlotWorld())) {
								if(!main.getIDMain().isAllowed(event.getClickedBlock().getLocation(), event.getPlayer())) {
									event.setCancelled(true);
								}	
							}
						}
						else {
							event.setCancelled(true);
						}
					}
					 
			 }
			
		 
	 }
	
	public boolean toggleEdit(Player player) {
		if(players.contains(player)) {
			players.remove(player);
			return false;
		}else {
			players.add(player);
			return true;
		}
	}
	
	public void removeFromEdit(Player player) {
		players.remove(player);
	}
}
