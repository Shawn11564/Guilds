package dev.mrshawn.guilds.guilds.creator;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.guilds.Guild;
import dev.mrshawn.guilds.guilds.RankType;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class GuildCreator {

	public GuildCreator(String name, Player owner) {
		Map<UUID, RankType> temp = new HashMap<>();
		temp.put(owner.getUniqueId(), RankType.LEADER);
		Guilds.getInstance().getGuildManager().addGuild(new Guild(name, temp, UUID.randomUUID()));
		Guilds.getInstance().getGuildFileManager().store(Guilds.getInstance().getGuildManager().getGuildByName(name));
	}

	public GuildCreator(String name, Map<UUID, RankType> members, List<Chunk> chunkList, UUID uuid, double bankBalance, int maxLand, double taxesOwed, int missedCycles) {
		Guilds.getInstance().getGuildManager().addGuild(new Guild(name, members, chunkList, uuid, bankBalance, maxLand, taxesOwed, missedCycles));
	}

}
