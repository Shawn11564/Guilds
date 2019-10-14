package dev.mrshawn.guilds.guilds;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.utils.Chat;
import org.bukkit.entity.Player;

import java.util.List;

public class TaxRunnable implements Runnable {

	private Guilds main;
	private List<Guild> guilds;

	public TaxRunnable(Guilds main) {
		this.main = main;
		guilds = main.getGuildManager().getGuilds();
	}

	@Override
	public void run() {
		for (Guild guild : guilds) {
			if (guild.getOwedTaxes() <= 0) {
				guild.setOwedTaxes(guild.getOwedTaxes() + guild.getNewTaxCost());
				for (Player player : guild.getOnlineMembers()) {
					Chat.tell(player, "&cYour guild has been taxed: &6$" + guild.getTaxOwedFormatted() + "&a!");
				}
				guild.setMissedCycles(guild.getMissedCycles() + 1);
			} else {
				if (guild.getMissedCycles() > main.getConfig().getInt("max-cycles")) {
					for (Player player : guild.getOnlineMembers()) {
						Chat.tell(player, "&cYour guild has been disbanded for being in debt too long!");
					}
					guild.delete();
				} else {
					guild.setOwedTaxes(guild.getOwedTaxes() + guild.getNewTaxCost());
					for (Player player : guild.getOnlineMembers()) {
						Chat.tell(player, "&cYour guild has been taxed: &6" + guild.getTaxOwedFormatted() + "&a!");
					}
					guild.setMissedCycles(guild.getMissedCycles() + 1);
				}
			}
		}
	}
}
