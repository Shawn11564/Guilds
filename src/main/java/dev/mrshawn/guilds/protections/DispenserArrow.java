package dev.mrshawn.guilds.protections;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;

import java.util.UUID;

@Getter
public class DispenserArrow {

	private UUID uuid;
	private Arrow arrow;
	private Location dispenserLocation;

	public DispenserArrow(UUID uuid, Arrow arrow, Location dispenserLocation) {
		this.uuid = uuid;
		this.arrow = arrow;
		this.dispenserLocation = dispenserLocation;
	}
}
