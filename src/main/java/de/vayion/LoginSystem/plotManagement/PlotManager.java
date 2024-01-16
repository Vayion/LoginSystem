package de.vayion.LoginSystem.plotManagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.vayion.LoginSystem.Main;

public class PlotManager {
	
	private World plotWorld;
	private Main main;
	private ArrayList<Plot> plots;

	private File file;
	private FileConfiguration config;
	
	public PlotManager(Main main) {
		this.main = main;
		plots = new ArrayList<Plot>();
		
		file = new File(main.getDataFolder(), "plotmanager.yml");
		config = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if(file.exists()) {
			loadPlots();
		}
		else {
			try {
				file.createNewFile();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	public boolean setPlotWorld(World world) {
		if(plotWorld == null) {
			this.plotWorld = world;
			return true;
		}else {
			return false;
		}
		
	}
	
	public World getPlotWorld() {
		return plotWorld;
	}
	
	public int addPlot() {
		int i = plots.size();
		plots.add(new Plot(i, this));
		return i;
	}
	
	public boolean removePlot(int id) {
		if(id < plots.size()) {
			plots.get(id).setID(-2);
			if(id == plots.size()-1) {
				plots.remove(id);
			}
			else {
				plots.set(id, plots.get(plots.size()-1));
				plots.get(id).setID(id);
				plots.remove(plots.size()-1);
			}
			main.getIDMain().refreshPlotIDs();
			return true;
		}
		else {return false;}
	}
	
	public boolean setPlotLocation(Location loc, int id, boolean bool) {
		if(id < plots.size()) {
			return plots.get(id).addPosition(bool, loc);
		}
		return false;
	}
	
	public Plot getPlotByLoc(Location loc) {
		for(int i = 0; i<plots.size(); i++) {
			if(plots.get(i).isAllowed(loc)) {
				return plots.get(i);
			}
		}
		return null;
	}
	
	public Plot getPlotByID(int id) {
		if(id < plots.size()) {
			return plots.get(id);
		}else {
			return null;
		}
	}
	
	public Plot getPlotByCornerBlock(Location loc) {
		for(int i = 0; i< plots.size(); i++) {
			if(plots.get(i).getClaimBlockPlot(loc)) {
				return plots.get(i);
			}
		}
		return null;
	}
	
	public void savePlots() {
		if(plotWorld!=null) {
			config.set("plotWorld", plotWorld.getName());
		}
		config.set("amountPlots", plots.size());
		for(int i = 0; i < plots.size(); i++) {
			config.set("plot."+i+".pos1", plots.get(i).getPos1());
			config.set("plot."+i+".pos2", plots.get(i).getPos2());
			config.set("plot."+i+".home", plots.get(i).getHome());
			config.set("plot."+i+".id", plots.get(i).getID());
			

			config.set("plot."+i+".claimed", plots.get(i).isClaimed());
		}
		save();
	}

	public void loadPlots() {
		if(config.get("plotWorld")!=null) {
			plotWorld = Bukkit.getWorld(config.getString("plotWorld"));	
		}
		
		int max = 0;
		if(config.get("amountPlots")!=null) {
			max = config.getInt("amountPlots");
		}
		System.out.println("Max: "+max);
		for(int i = 0; i < max; i++) {
			System.out.println("Loaded Plot "+i);
			plots.add(new Plot(config.getInt("plot."+i+".id"), this));
			if(config.contains("plot."+i+".pos1")) {plots.get(i).addPosition(true, (Location) config.get("plot."+i+".pos1"));}
			if(config.contains("plot."+i+".pos2")) {plots.get(i).addPosition(false, (Location) config.get("plot."+i+".pos2"));}
			if(config.contains("plot."+i+".home")) {plots.get(i).setHome((Location) config.get("plot."+i+".home"), false);}
		}
	}
	
	public void save() {
		try {
			config.save(file);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getPlotsSize() {
		return plots.size();
	}
	
	
	/**
	 * returns an Array list with all the relevant data
	 * @return ArrayList<String> with all users
	 */
	public ArrayList<String> getAllPlots(){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i<plots.size(); i++) {
			Plot plot = plots.get(i);
			if(plot.isReady()) {
				list.add(ChatColor.GREEN+"Plot "+i+" is ready.");
			}
			else {
				list.add(ChatColor.RED+"Plot "+i+" is not ready.");
			}
		}
		return list;
	}
	
	/**
	 * Getter for Plots Array List
	 * @return plots array list
	 */
	public ArrayList<Plot> getPlots() {
		return plots;
	}
	
	public ArrayList<Plot> getUnclaimedPlots(){
		ArrayList<Plot> unclaimedPlots = new ArrayList<Plot>();
		
		for(int i = 0; i<plots.size();i++) {
			if(!(plots.get(i).isClaimed())) {
				unclaimedPlots.add(plots.get(i));
			}
		}
		
		return unclaimedPlots;
	}
	
	public Plot getRandomUnclaimedPlot() {
		ArrayList<Plot> unclaimedPlots = getUnclaimedPlots();
		if(unclaimedPlots.size()==0) {
			return null;
		}
		double index = Math.random() * unclaimedPlots.size();
		return (unclaimedPlots.get((int)index));
	}
}
