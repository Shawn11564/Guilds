package dev.mrshawn.guilds.files.regions;

import dev.mrshawn.guilds.utils.Selection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SelectionManager {

	private Map<Player, Selection> selections;

	public SelectionManager() {
		selections = new HashMap<>();
	}

	public void addPlayer(Player player) {
		selections.put(player, new Selection(player));
	}

	public boolean hasSelections(Player player) {
		return selections.containsKey(player);
	}

	public boolean bothSelectionsSet(Player player) {
		return selections.get(player).bothSet();
	}

	public Selection getPlayerSelection(Player player) {
		return selections.get(player);
	}
}
