package dev.mrshawn.guilds.utils;

import org.bukkit.Chunk;

public class Chunks {

	public static String formatChunk(Chunk chunk) {
		return chunk.getWorld().getName() + ":" +
				chunk.getX() + ":" +
				chunk.getZ();
	}

}
