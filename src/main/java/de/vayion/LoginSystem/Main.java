package de.vayion.LoginSystem;

import de.vayion.LoginSystem.commands.*;
import de.vayion.LoginSystem.shopManagement.ShopListeners;
import de.vayion.LoginSystem.shopManagement.ShopRegistry;
import org.bukkit.plugin.java.JavaPlugin;

import de.vayion.LoginSystem.commands.VisitCommand.Target;
import de.vayion.LoginSystem.groups.GroupMain;
import de.vayion.LoginSystem.idManagement.IDMain;
import de.vayion.LoginSystem.idManagement.IDStorageManager;
import de.vayion.LoginSystem.idManagement.RegistryManager;
import de.vayion.LoginSystem.ingameInterface.InterfaceMain;
import de.vayion.LoginSystem.plotManagement.PlotListeners;
import de.vayion.LoginSystem.plotManagement.PlotManager;

public class Main extends JavaPlugin {
	
	private IDMain iDMain;
	private RegistryManager registryManager;
	private PlotManager plotManager;
	private LocationManager locationManager;
	private InterfaceMain interfaceMain;
	private PlotListeners plotListeners;
	private IDStorageManager idStorageManager;
	private GroupMain groupMain;

	private ShopRegistry shopRegistry;
	
	@Override
	public void onEnable() {
		if(!getDataFolder().exists()) {
			getDataFolder().mkdirs();
		}
		
		this.getCommand("finden").setExecutor(new FindCmd(this));
		this.getCommand("login").setExecutor(new LoginCmd(this));
		this.getCommand("logout").setExecutor(new LogoutCmd(this));
		//this.getCommand("deleteUser").setExecutor(new DeleteUser(this));
		//this.getCommand("addUser").setExecutor(new AddUser(this));
		//this.getCommand("addAdmin").setExecutor(new AddAdmin(this));
		this.getCommand("listUsers").setExecutor(new ListUsersCmd(this));
		this.getCommand("plot").setExecutor(new PlotCmd(this));
		this.getCommand("kaufen").setExecutor(new ClaimCmd(this));
		this.getCommand("user").setExecutor(new UserCmd(this));
		this.getCommand("farmwelt").setExecutor(new VisitCommand(this, Target.FARM));
		this.getCommand("stadt").setExecutor(new VisitCommand(this, Target.CITY));
		this.getCommand("setLocation").setExecutor(new SetLocationCmd(this));
		this.getCommand("logList").setExecutor(new ListCmd(this));
		this.getCommand("inspect").setExecutor(new InspectCmd(this));
		this.getCommand("zufall").setExecutor(new RandomLocationCmd(this));
		this.getCommand("balance").setExecutor(new BalanceCmd(this));
		
		this.getServer().getPluginManager().registerEvents(new ConnectionListeners(this), this);
		this.getServer().getPluginManager().registerEvents(plotListeners = new PlotListeners(this), this);

		shopRegistry = new ShopRegistry(this);
		locationManager = new LocationManager(this);
		iDMain = new IDMain(this);
		plotManager = new PlotManager(this);
		registryManager = new RegistryManager(this);
		interfaceMain = new InterfaceMain(this);
		idStorageManager = new IDStorageManager(this);
		groupMain = new GroupMain(this);

		this.getServer().getPluginManager().registerEvents(new ShopListeners(shopRegistry), this);
	}

	@Override
	public void onDisable() {
		plotManager.savePlots();
		locationManager.saveLocations();
		iDMain.logoutAll();
		iDMain.saveRegistry();
		idStorageManager.saveProfileRegister(iDMain.getAllUsers(false));
		groupMain.save();
	}
	
	
	public IDMain getIDMain() {
		return iDMain;
	}
	
	public RegistryManager getRegistryManager() {
		return registryManager;
	}
	
	public PlotManager getPlotManager() {
		return plotManager;
	}
	public LocationManager getLocationManager() {
		return locationManager;
	}
	
	public PlotListeners getPlotListeners() {
		return plotListeners;
	}
	
	public InterfaceMain getInterfaceMain() {
		return interfaceMain;
	}
	
	public GroupMain getGroupMain() {
		return groupMain;
	}
	
	

}
