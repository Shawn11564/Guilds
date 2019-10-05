package dev.mrshawn.guilds.protections;

import lombok.Getter;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class BowArrow {

	private UUID uuid;
	private Arrow arrow;
	private Player shooter;

	public BowArrow(UUID uuid, Arrow arrow, Player shooter) {
		this.uuid = uuid;
		this.arrow = arrow;
		this.shooter = shooter;
	}
}
