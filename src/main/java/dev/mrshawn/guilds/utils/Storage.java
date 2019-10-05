package dev.mrshawn.guilds.utils;

import dev.mrshawn.guilds.guilds.regions.Region;

public class Storage {

	public static String format(Region region) {
		return region.getCuboid().worldName + ":" +
				region.getLocOne().getBlockX() + ":" +
				region.getLocOne().getBlockY() + ":" +
				region.getLocOne().getBlockZ() + ":" +
				region.getLocTwo().getBlockX() + ":" +
				region.getLocTwo().getBlockY() + ":" +
				region.getLocTwo().getBlockZ();
	}

}
