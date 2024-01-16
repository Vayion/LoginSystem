package de.vayion.LoginSystem.groups;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class GroupRegister {

	private GroupMain groupMain;
		
	private File file;
	private FileConfiguration config;
	
	public GroupRegister(GroupMain groupMain) {
		this.groupMain = groupMain;
		
		file = new File(groupMain.getMain().getDataFolder(), "groupregister.yml");
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
		int max = config.getInt("size");
		for(int i = 0; i < max; i++) {
			String name = config.getString("group"+i+".name");
			if(name != null) {
				groupMain.addNewGroup(name);
			}	
		}
	}
		
	public void saveProfileRegister(HashMap<String , Group> register) {
		config.set("size", register.size());
		int i = 0;
		for (Map.Entry<String, Group> set :
            groupMain.getGroups().entrySet()) {
			config.set("group"+(i++)+".name",set.getValue().getName());
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
