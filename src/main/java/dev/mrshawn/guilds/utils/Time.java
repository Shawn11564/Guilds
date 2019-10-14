package dev.mrshawn.guilds.utils;

import dev.mrshawn.guilds.Guilds;

public class Time {

	private static Guilds instance = Guilds.getInstance();
	private static String[] taxTime = instance.getConfig().getString("run-time").split(":");
	private static String[] raidTime = instance.getConfig().getString("raiding-run-time").split(":");

	public static int getTaxHour() {
		return Integer.parseInt(taxTime[0]);
	}

	public static int getTaxMinute() {
		return Integer.parseInt(taxTime[1]);
	}

	public static int getRaidHour() {
		return Integer.parseInt(raidTime[0]);
	}

	public static int getRaidMinute() {
		return Integer.parseInt(raidTime[1]);
	}

	public static int getRaidDuration() {
		return instance.getConfig().getInt("raiding-duration");
	}

	public static int getMaxCycles() {
		return instance.getConfig().getInt("max-cycles");
	}

}
