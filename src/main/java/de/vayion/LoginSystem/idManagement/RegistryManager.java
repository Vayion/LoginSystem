package de.vayion.LoginSystem.idManagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.vayion.LoginSystem.Main;

public class RegistryManager {

	private Main main;
	
	private File file;
	private FileConfiguration config;
	
	public RegistryManager(Main main) {
		this.main = main;
		
		file = new File(main.getDataFolder(), "playerregister.yml");
		config = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
		if(file.exists()) {
			loadProfiles();
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
	
	public void loadProfiles() {
		int max = 0;
		if(config.get("amountUsers")!=null) {
			max = (int) config.get("amountUsers");
		}
		System.out.println("Found "+max+" users.");
		for(int i = 0; i<max; i++) {
			if(config.getBoolean("userprofile."+i+".isAdmin")) {
				System.out.println("Loading userprofile Nr: "+i+".");
				main.getIDMain().addAdmin(((String) config.get("userprofile."+i+".name")), (String) config.get("userprofile."+i+".ID"));
			}
			else {
				System.out.println("Loading userprofile Nr: "+i+".");
				main.getIDMain().addUser(((String) config.get("userprofile."+i+".name")), (String) config.get("userprofile."+i+".ID"));
			}
		}
		
		
	}
	
	public void saveProfileRegister(ArrayList<UserProfile> register) {
		config.set("amountUsers", register.size());
		for(int i = 0; i < register.size(); i++) {
			config.set("userprofile."+i+".name", register.get(i).getName());
			config.set("userprofile."+i+".ID", register.get(i).getID());
			config.set("userprofile."+i+".isAdmin", register.get(i).isAdmin());
			System.out.println("Saving userprofile of "+register.get(i).getName()+".");
		}
		save();
	}
	
	public void save() {
		try {
			System.out.println("Saving to file.");
			config.save(file);
		}catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to save to file.");
		}
	}

	public Main getMain() {
		return main;
	}
}
