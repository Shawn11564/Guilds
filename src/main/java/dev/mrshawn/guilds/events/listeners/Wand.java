package dev.mrshawn.guilds.events.listeners;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.utils.Chat;
import dev.mrshawn.guilds.utils.Items;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Wand implements Listener {

	private Guilds main;

	public Wand(Guilds main) {
		this.main = main;
	}

	@EventHandler
	public void onLeftClick(PlayerInteractEvent e) {
		if (!(e.getAction() == null) || !(e.getClickedBlock() == null)) {
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
				Player player = e.getPlayer();
				Location location = e.getClickedBlock().getLocation();
				if (player.hasPermission("oreregen.admin")) {
					ItemStack item = player.getItemInHand();
					if (Items.isWand(item)) {
						e.setCancelled(true);
						if (main.getSelectionManager().hasSelections(player)) {
							main.getSelectionManager().getPlayerSelection(player).setLocOne(location);
						} else {
							main.getSelectionManager().addPlayer(player);
							main.getSelectionManager().getPlayerSelection(player).setLocOne(location);
						}
						Chat.tell(player, "&aFirst position set to: &dX " +
								location.getBlockX() +
								" Y " + location.getBlockY() +
								" Z " + location.getBlockZ());
					}
				}
			}
		}
	}

	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		if (!(e.getAction() == null) || !(e.getClickedBlock() == null)) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Player player = e.getPlayer();
				Location location = e.getClickedBlock().getLocation();
				if (player.hasPermission("oreregen.admin")) {
					ItemStack item = player.getItemInHand();
					if (Items.isWand(item)) {
						e.setCancelled(true);
						if (main.getSelectionManager().hasSelections(player)) {
							main.getSelectionManager().getPlayerSelection(player).setLocTwo(location);
						} else {
							main.getSelectionManager().addPlayer(player);
							main.getSelectionManager().getPlayerSelection(player).setLocTwo(location);
						}
						Chat.tell(player, "&aSecond position set to: &dX " +
								location.getBlockX() +
								" Y " + location.getBlockY() +
								" Z " + location.getBlockZ());
					}
				}
			}
		}
	}

}
