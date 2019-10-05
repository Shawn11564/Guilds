package dev.mrshawn.guilds.guilds.regions;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.utils.Selection;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class RegionManager {

	private Guilds main;
	private List<Region> regions;

	public RegionManager(Guilds main) {
		this.main = main;
		regions = new ArrayList<>();
	}

	public void addRegion(String name, Selection selection) {
		regions.add(new Region(name, selection.getLocOne(), selection.getLocTwo()));
	}

	public void addRegion(String name, Location one, Location two) {
		regions.add(new Region(name, one, two));
	}

	public boolean regionSameName(String name) {
		for (Region r : regions) {
			if (r.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean inRegion(Chunk chunk) {
		for (Region r : regions) {
			if (r.getCuboid().getChunks().contains(chunk)) {
				return true;
			}
		}
		return false;
	}

	public void removeRegion(String name) {
		for (Region r : regions) {
			if (r.getName().equals(name)) {
				regions.remove(r);
				break;
			}
		}
	}

	public List<Region> getRegions() {
		return regions;
	}
}
