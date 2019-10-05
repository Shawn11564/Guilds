package dev.mrshawn.guilds.guilds.managers;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.guilds.Guild;
import lombok.Getter;
import org.bukkit.Chunk;

import java.util.*;

@Getter
public class LandManager {

	private Map<Chunk, Guild> ownedChunks;

	public LandManager() {
		ownedChunks = new HashMap<>();
		init();
	}

	public void init() {
		for (Guild guild : Guilds.getInstance().getGuildManager().getGuilds()) {
			for (Chunk chunk : guild.getChunkList()) {
				ownedChunks.put(chunk, guild);
			}
		}
	}

	public void update(Guild guild, Chunk chunk) {
		ownedChunks.put(chunk, guild);
	}

	public void removeGuildChunks(Guild guild) {
		List<Chunk> temp = new ArrayList<>();
		for (Chunk chunk : ownedChunks.keySet()) {
			if (ownedChunks.get(chunk) == guild) {
				temp.add(chunk);
			}
		}
		for (Chunk chunk : temp) {
			ownedChunks.remove(chunk);
		}
	}

	public boolean isOwned(Chunk chunk) {
		return ownedChunks.containsKey(chunk);
	}

	public void removeChunk(Chunk chunk) {
		ownedChunks.remove(chunk);
	}

	public Guild getOwningGuild(Chunk chunk) {
		return ownedChunks.get(chunk);
	}

}
