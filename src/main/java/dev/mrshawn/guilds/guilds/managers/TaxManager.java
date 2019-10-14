package dev.mrshawn.guilds.guilds.managers;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.guilds.TaxRunnable;
import dev.mrshawn.guilds.utils.Time;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaxManager {

	private Guilds main;

	public TaxManager(Guilds main) {
		this.main = main;
		start();
	}

	public void start() {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of(main.getConfig().getString("time-zone")));
		ZonedDateTime nextRun = now.withHour(Time.getTaxHour()).withMinute(Time.getTaxMinute());

		if (now.compareTo(nextRun) > 0) {
			nextRun = nextRun.plusDays(1);
		}

		Duration duration = Duration.between(now, nextRun);
		long initialDelay = duration.getSeconds();

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new TaxRunnable(main), initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);

	}

}
