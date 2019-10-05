package dev.mrshawn.guilds.utils;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.guilds.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Chat {

	private static Guilds instance = Guilds.getInstance();

	public static void log(String... messages) {
		for (final String message : messages)
			log(message);
	}

	public static void log(String messages) {
		tell(Bukkit.getConsoleSender(), "[" + instance.getName() + "] " + messages);
	}

	public static void tell(CommandSender toWhom, String... messages) {
		for (final String message : messages)
			tell(toWhom, message);
	}

	public static void tell(CommandSender toWhom, List<String> messages) {
		for (final String message : messages)
			tell(toWhom, message);
	}

	public static void tell(CommandSender toWhom, String message) {
		toWhom.sendMessage(colorize(message));
	}

	public static String colorize(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static void regionList(CommandSender toWhom) {
		for (Region r : instance.getRegionManager().getRegions()) {
			tell(toWhom, "&7- &b" + r.getName());
		}
	}

}
