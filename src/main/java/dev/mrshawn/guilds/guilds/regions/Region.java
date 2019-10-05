package dev.mrshawn.guilds.guilds.regions;

import dev.mrshawn.guilds.utils.Cuboid;
import lombok.Getter;
import org.bukkit.Chunk;
import org.bukkit.Location;

@Getter
public class Region {

	private String name;
	private Location locOne, locTwo;
	private Cuboid cuboid;

	public Region(String name, Location one, Location two) {
		this.name = name;
		locOne = one;
		locTwo = two;
		cuboid = new Cuboid(one, two);
	}

	public boolean inRegion(Location location) {
		return cuboid.getChunks().contains(location.getChunk());
	}

	public boolean inRegion(Chunk chunk) {
		return cuboid.getChunks().contains(chunk);
	}

}
