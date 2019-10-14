package dev.mrshawn.guilds.events.listeners;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.guilds.Guild;
import dev.mrshawn.guilds.guilds.managers.GuildManager;
import dev.mrshawn.guilds.protections.BowArrow;
import dev.mrshawn.guilds.protections.DispenserArrow;
import dev.mrshawn.guilds.protections.ProtectionManager;
import dev.mrshawn.guilds.utils.Chat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public class LandProtection implements Listener {

	private Guilds main;
	private GuildManager guildManager;
	private ProtectionManager pm;

	public LandProtection(Guilds main) {
		this.main = main;
		guildManager = main.getGuildManager();
		pm = main.getProtectionManager();
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Location location = e.getBlock().getLocation();
		if (guildManager.inGuildLand(location)) {
			if (!main.getRaidingManager().isRaidTime()) {
				Guild guild = guildManager.getGuildByLocation(location);
				if (guildManager.getPlayerGuild(player) != guild || player.hasPermission("guilds.bypass")) {
					e.setCancelled(true);
					Chat.tell(player, "&cYou can't break blocks in other guild's land!");
				}
			}
		} else {
			if (!player.hasPermission("guilds.bypass")) {
				e.setCancelled(true);
				Chat.tell(player, "&cYou can't break blocks here!");
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		Location location = e.getBlockPlaced().getLocation();
		if (guildManager.inGuildLand(location)) {
			if (!main.getRaidingManager().isRaidTime()) {
				Guild guild = guildManager.getGuildByLocation(location);
				if (guildManager.getPlayerGuild(player) != guild || player.hasPermission("guilds.bypass")) {
					e.setCancelled(true);
					Chat.tell(player, "&cYou can't place blocks in other guild's land!");
				}
			} else {
				if (main.getPlaceableMaterials().contains(e.getBlockPlaced().getType())) {
					main.getRaidingManager().getPlaceBlocks().put(e.getBlockPlaced(), Material.AIR);
				} else {
					Chat.tell(player, "&cYou can't place this block during raid time!");
				}
			}
		} else {
			if (!player.hasPermission("guilds.bypass")) {
				e.setCancelled(true);
				Chat.tell(player, "&cYou can't place blocks here!");
			}
		}

	}

	@EventHandler
	public void onContainerOpen(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (!main.getRaidingManager().isRaidTime()) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK && !player.hasPermission("guilds.bypass")) {
				Location location = Objects.requireNonNull(e.getClickedBlock()).getLocation();
				if (guildManager.inGuildLand(location)) {
					Guild guild = guildManager.getGuildByLocation(location);
					if (guildManager.getPlayerGuild(player) != guild && !player.hasPermission("guilds.bypass")) {
						e.setCancelled(true);
						Chat.tell(player, "&cYou can't interact in other guild's land!");
					}
				} else {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			if (e.getDamager() instanceof Player) {
				Player attacker = (Player) e.getDamager();
				if (guildManager.inSameGuild(player, attacker)) {
					e.setCancelled(true);
					Chat.tell(attacker, "&cYou can't attack fellow guild members!");
				}
			}
		}
	}

	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if (e.getHitEntity() != null && e.getHitEntity().getType() == EntityType.PLAYER) {
			Player player = (Player) e.getHitEntity();
			if (e.getEntity() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getEntity();
				if (pm.bowArrowInList(arrow.getUniqueId())) {
					BowArrow ba = pm.getBowArrow(arrow.getUniqueId());
					if (guildManager.inSameGuild(ba.getShooter(), player)) {
						e.getEntity().remove();
						Chat.tell(ba.getShooter(), "&cYou can't shoot your guild members!");
					}
				}

				if (pm.dispenserArrowInList(arrow.getUniqueId())) {
					DispenserArrow da = pm.getDispenserArrow(arrow.getUniqueId());
					if (guildManager.inGuildLand(da.getDispenserLocation())) {
						if (guildManager.getPlayerGuild(player) == guildManager.getGuildByLocation(da.getDispenserLocation())) {
							da.getArrow().remove();
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		Arrow arrow = (Arrow) e.getEntity();
		if (arrow.getShooter() instanceof Player) {
			Player player = (Player) arrow.getShooter();
			BowArrow ba = new BowArrow(arrow.getUniqueId(), arrow, player);
			pm.addBowArrow(ba);
		}
	}

	@EventHandler
	public void onLaunch(ProjectileLaunchEvent e) {
		if (e.getEntity().getShooter() == null) {
			if (e.getEntityType() == EntityType.ARROW) {
				Arrow arrow = (Arrow) e.getEntity();
				Location location = e.getLocation();
				if (guildManager.inGuildLand(location)) {
					DispenserArrow da = new DispenserArrow(arrow.getUniqueId(), arrow, location);
					pm.addDispenserArrow(da);
				}
			}
		}
	}

}
