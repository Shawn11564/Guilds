package dev.mrshawn.guilds.events.listeners;

import dev.mrshawn.guilds.Guilds;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GuildChat implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (Guilds.getInstance().getChatManager().isGuildChat(e.getPlayer())) {
			Guilds.getInstance().getChatManager().sendToGuild(e.getPlayer(), e.getMessage());
			e.setCancelled(true);
		}
	}

}
