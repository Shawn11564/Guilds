package dev.mrshawn.guilds.guilds.managers;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.guilds.Guild;
import dev.mrshawn.guilds.guilds.RankType;
import dev.mrshawn.guilds.utils.Chat;
import dev.mrshawn.guilds.utils.Config;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GuildManager {

	private List<Guild> guilds;

	public GuildManager() {
		guilds = new ArrayList<>();
	}

	public void addGuild(Guild guild) {
		guilds.add(guild);
	}

	public void deleteGuild(Player player, Guild guild) {
		guilds.remove(guild);
		Guilds.getInstance().getLandManager().removeGuildChunks(guild);
		Bukkit.getServer().broadcastMessage(Chat.colorize(Config.getGlobalGuildDisbanded(player, guild.getName())));
	}

	public Guild getGuildByName(String name) {
		for (Guild guild : guilds) {
			if (guild.getName().equals(name)) {
				return guild;
			}
		}
		return null;
	}

	public boolean isInGuild(Player player) {
		for (Guild guild : guilds) {
			if (guild.getMembers().containsKey(player.getUniqueId())) {
				return true;
			}
		}
		return false;
	}

	public boolean inSameGuild(Player one, Player two) {
		if (isInGuild(one) && isInGuild(two)) {
			return getPlayerGuild(one) == getPlayerGuild(two);
		}
		return false;
	}

	public Guild getPlayerGuild(Player player) {
		for (Guild guild : guilds) {
			if (guild.getMembers().containsKey(player.getUniqueId())) {
				return guild;
			}
		}
		return null;
	}

	public RankType getPlayerGuildRank(Player player) {
		if (isInGuild(player)) {
			return getPlayerGuild(player).getMembers().get(player.getUniqueId());
		}
		return RankType.GUEST;
	}

	public boolean inGuildLand(Location location) {
		for (Guild guild : guilds) {
			if (guild.getChunkList().contains(location.getChunk())) {
				return true;
			}
		}
		return false;
	}

	public Guild getGuildByLocation(Location location) {
		for (Guild guild : guilds) {
			if (guild.getChunkList().contains(location.getChunk())) {
				return guild;
			}
		}
		return null;
	}
}
