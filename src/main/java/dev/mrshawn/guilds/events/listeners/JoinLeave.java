package dev.mrshawn.guilds.events.listeners;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.guilds.Guild;
import dev.mrshawn.guilds.guilds.managers.GuildManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeave implements Listener {

	private Guilds main;
	private GuildManager guildManager;

	public JoinLeave(Guilds main) {
		this.main = main;
		guildManager = main.getGuildManager();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (guildManager.isInGuild(e.getPlayer())) {
			guildManager.getPlayerGuild(e.getPlayer()).getOnlineMembers().add(e.getPlayer());
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		if (guildManager.isInGuild(e.getPlayer())) {
			guildManager.getPlayerGuild(e.getPlayer()).getOnlineMembers().remove(e.getPlayer());
		}
		for (Guild guild : guildManager.getGuilds()) {
			guild.getInvitedMembers().remove(e.getPlayer());
		}
	}

}
