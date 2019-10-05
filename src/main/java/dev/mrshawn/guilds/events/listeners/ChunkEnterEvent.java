package dev.mrshawn.guilds.events.listeners;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.events.PlayerEnterChunkEvent;
import dev.mrshawn.guilds.guilds.managers.LandManager;
import dev.mrshawn.guilds.utils.Chat;
import dev.mrshawn.guilds.utils.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChunkEnterEvent implements Listener {

	private LandManager lm = Guilds.getInstance().getLandManager();

	@EventHandler
	public void onChunkEnter(PlayerEnterChunkEvent event) {
		if (lm.isOwned(event.getFrom()) && lm.isOwned(event.getTo())) {
			if (lm.getOwningGuild(event.getFrom()) != lm.getOwningGuild(event.getTo())) {
				Chat.tell(event.getPlayer(), Config.getLeftGuildLand(lm.getOwningGuild(event.getFrom())));
				Chat.tell(event.getPlayer(), Config.getEnteredGuildLand(lm.getOwningGuild(event.getTo())));
				return;
			}
		}
		if (lm.isOwned(event.getFrom()) && !lm.isOwned(event.getTo())) {
			Chat.tell(event.getPlayer(), Config.getLeftGuildLand(lm.getOwningGuild(event.getFrom())));
		}
		if (!lm.isOwned(event.getFrom()) && lm.isOwned(event.getTo())) {
			Chat.tell(event.getPlayer(), Config.getEnteredGuildLand(lm.getOwningGuild(event.getTo())));
		}
	}

}
