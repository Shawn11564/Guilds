package dev.mrshawn.guilds.events.listeners;

import dev.mrshawn.guilds.events.PlayerEnterChunkEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class MoveEvent implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (event.getFrom().getChunk() != Objects.requireNonNull(event.getTo()).getChunk()) {
			PlayerEnterChunkEvent playerEnterChunkEvent = new PlayerEnterChunkEvent(event.getFrom().getChunk(), event.getTo().getChunk(), event.getPlayer());
			Bukkit.getPluginManager().callEvent(playerEnterChunkEvent);
		}
	}

}
