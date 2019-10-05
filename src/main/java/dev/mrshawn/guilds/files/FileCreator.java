package dev.mrshawn.guilds.files;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.utils.Chat;

import java.io.File;
import java.io.IOException;

public class FileCreator {

	public boolean make(String fileName, String folderName) {
		File directory = new File(Guilds.getInstance().getDataFolder() + File.separator + folderName);
		File file = new File(Guilds.getInstance().getDataFolder() + File.separator + folderName + File.separator + fileName + ".yml");
		if (file.exists()) {
			return false;
		}
		// Run this block if a subfolder is provided
		if (!directory.exists()) {
			if (directory.mkdir()) { // Create the folder if it doesn't exist already
				try {
					if (file.createNewFile()) { // Create the file
						Chat.log("&aSuccessfully created file: &6" + file.getName());
						return true;
					}
				} catch (IOException e) {
					Chat.log("&4Unable to create file: &6" + file.getName());
					return false;
				}
			} else {
				Chat.log("&4Unable to create folder: &6" + directory.getName());
				return false;
			}
		}
		return false;
	}

	public boolean make(String fileName) {
		File file = new File(Guilds.getInstance().getDataFolder() + File.separator + fileName + ".yml");
		if (file.exists()) {
			return false;
		}
		try {
			if (file.createNewFile()) {
				Chat.log("&aSuccessfully created file: &6" + file.getName());
				return true;
			}
		} catch (IOException e) {
			Chat.log("&4Unable to create file: &6" + file.getName());
			return false;
		}
		return false;
	}
}
