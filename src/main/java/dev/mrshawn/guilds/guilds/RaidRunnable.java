package dev.mrshawn.guilds.guilds;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.utils.Chat;
import dev.mrshawn.guilds.utils.Time;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RaidRunnable implements Runnable {

	private Guilds main;

	public RaidRunnable(Guilds main) {
		this.main = main;
	}

	@Override
	public void run() {
		main.getRaidingManager().setRaidTime(true);
		for (Player player : Bukkit.getOnlinePlayers()) {
			Chat.tell(player, "&6Guild raiding has been enabled! It will end in: &e" + Time.getRaidDuration() + " minutes&6!");
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(main, new BukkitRunnable() {
			@Override
			public void run() {
				main.getRaidingManager().setRaidTime(false);
				for (Block b : main.getRaidingManager().getPlaceBlocks().keySet()) {
					b.setType(main.getRaidingManager().getPlaceBlocks().get(b));
				}
				for (Player player : Bukkit.getOnlinePlayers()) {
					Chat.tell(player, "&aRaiding has ended!");
				}
			}
		}, (long) 20 * 60 * Time.getRaidDuration());
	}
}
