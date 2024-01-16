package de.vayion.LoginSystem.idManagement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.vayion.LoginSystem.Main;

public class IDStorageManager {

	Main main;
	
	private File file;
	private FileConfiguration config;
	
	public IDStorageManager(Main main) {
		this.main = main;
		
		file = new File(main.getDataFolder(), "overview.yml");
		config = (FileConfiguration)YamlConfiguration.loadConfiguration(file);
	
		try {
			file.createNewFile();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void saveProfileRegister(ArrayList<String> register) {
		for(int i = 0; i < register.size(); i++) {
			config.set("user"+i, register.get(i));
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
}