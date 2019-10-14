package dev.mrshawn.guilds.guilds.managers;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.guilds.RaidRunnable;
import dev.mrshawn.guilds.utils.Time;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class RaidingManager {

	private Guilds main;
	private boolean raidTime;
	// BlockOne = new Material = old
	private Map<Block, Material> placeBlocks;

	public RaidingManager(Guilds main) {
		this.main = main;
		placeBlocks = new HashMap<>();
		raidTime = false;
		start();
	}

	public void start() {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of(main.getConfig().getString("time-zone")));
		ZonedDateTime nextRun = now.withHour(Time.getRaidHour()).withMinute(Time.getRaidMinute());

		if (now.compareTo(nextRun) > 0) {
			nextRun = nextRun.plusDays(1);
		}

		Duration duration = Duration.between(now, nextRun);
		long initialDelay = duration.getSeconds();

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new RaidRunnable(main), initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);

	}

}
