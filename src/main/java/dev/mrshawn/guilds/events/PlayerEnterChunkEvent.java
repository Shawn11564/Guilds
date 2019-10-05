package dev.mrshawn.guilds.events;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerEnterChunkEvent extends Event implements Cancellable {

	private Chunk from;
	private Chunk to;
	private Player player;
	private static final HandlerList HANDLERS_LIST = new HandlerList();
	private boolean isCancelled;

	public PlayerEnterChunkEvent(Chunk from, Chunk to, Player player) {
		this.from = from;
		this.to = to;
		this.player = player;
		this.isCancelled = false;
	}

	@Override
	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.isCancelled = cancelled;
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return HANDLERS_LIST;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS_LIST;
	}

	public Chunk getFrom() {
		return from;
	}

	public Chunk getTo() {
		return to;
	}

	public Player getPlayer() {
		return player;
	}
}
