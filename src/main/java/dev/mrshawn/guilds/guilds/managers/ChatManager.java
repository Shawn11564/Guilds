package dev.mrshawn.guilds.guilds.managers;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.utils.Chat;
import dev.mrshawn.guilds.utils.Config;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatManager {

	private Guilds main;
	private List<Player> guildChat;

	public ChatManager(Guilds main) {
		this.main = main;
		guildChat = new ArrayList<>();
	}

	public void toggle(Player player) {
		if (isGuildChat(player)) {
			removePlayer(player);
			Chat.tell(player, "&aYou are now chatting globally!");
		} else {
			addPlayer(player);
			Chat.tell(player, "&aYou are now chatting to your guild!");
		}
	}

	public void addPlayer(Player player) {
		guildChat.add(player);
	}

	public void removePlayer(Player player) {
		guildChat.remove(player);
	}

	public boolean isGuildChat(Player player) {
		return guildChat.contains(player);
	}

	public void sendToGuild(Player player, String message) {
		String formatted = Config.getGuildChatFormatted(player, message);
		for (Player p : main.getGuildManager().getPlayerGuild(player).getOnlineMembers()) {
			Chat.tell(p, formatted);
		}
	}
}
