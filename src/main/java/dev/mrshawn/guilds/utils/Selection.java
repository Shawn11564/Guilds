package dev.mrshawn.guilds.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@Getter
@Setter
public class Selection {

	private Player owner;
	private Location locOne, locTwo;

	public Selection(Player owner) {
		this.owner = owner;
	}

	public boolean bothSet() {
		return locOne != null && locTwo != null;
	}
}
