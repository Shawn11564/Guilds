package dev.mrshawn.guilds.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {

	public static boolean isWand(ItemStack item) {
		if (item.getType() == Material.GOLDEN_AXE && item.hasItemMeta()) {
			return ChatColor.stripColor(item.getItemMeta().getDisplayName()).equals("Guilds Region Claiming Wand");
		}
		return false;
	}

	public static void regionWand(Player player) {
		ItemStack item = new ItemStack(Material.GOLDEN_AXE);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(Chat.colorize("&aGuilds Region Claiming Wand"));
		item.setItemMeta(meta);

		player.getInventory().addItem(item);
	}

}
