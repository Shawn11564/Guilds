package dev.mrshawn.guilds.files.regions;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.files.FileCreator;
import dev.mrshawn.guilds.guilds.regions.Region;
import dev.mrshawn.guilds.utils.Chat;
import dev.mrshawn.guilds.utils.Storage;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class RegionFileManager {

	private Guilds main;
	private File file;
	private FileConfiguration config;
	private FileCreator fc;

	public RegionFileManager(Guilds main) {
		this.main = main;
		file = new File(main.getDataFolder() + File.separator + "regions.yml");
		if (!file.exists()) {
			fc = new FileCreator();
			if (fc.make("regions")) {
				Chat.log("&aCreated &6regions.yml &4file!");
			} else {
				Chat.log("&4Unable to create &6regions.yml &4file!");
			}
		}
		config = YamlConfiguration.loadConfiguration(file);
		init();
	}

	public void init() {
		for (String s : config.getKeys(false)) {
			Location locOne, locTwo;
			World world;

			String[] info = config.getString(s).split(":");
			world = Bukkit.getServer().getWorld(info[0]);
			locOne = world.getBlockAt(Integer.parseInt(info[1]), Integer.parseInt(info[2]), Integer.parseInt(info[3])).getLocation();
			locTwo = world.getBlockAt(Integer.parseInt(info[4]), Integer.parseInt(info[5]), Integer.parseInt(info[6])).getLocation();
			Guilds.getInstance().getRegionManager().addRegion(s, locOne, locTwo);
		}
	}

	public void save() {
		for (Region r : Guilds.getInstance().getRegionManager().getRegions()) {
			config.set(r.getName(), Storage.format(r));
		}
		try {
			config.save(file);
		} catch (IOException e) {
			Chat.log("&4Unable to save &6regions.yml");
			e.printStackTrace();
		}
	}

}
