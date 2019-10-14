package dev.mrshawn.guilds.utils;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.guilds.Guild;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;

public class Config {

	private static FileConfiguration config = Guilds.getInstance().getConfig();

	public static List<String> getHelpMessage() {
		return config.getStringList("help-message");
	}

	public static String getNoPermissionMessage() {
		return config.getString("no-permission");
	}

	public static String getMustBePlayerMessage() {
		return config.getString("must-be-player");
	}

	public static String getAlreadyInGuildMessage() {
		return config.getString("already-in-guild");
	}

	public static String getNotInGuild() {
		return config.getString("not-in-guild");
	}

	public static String getNeedHigherGuildRank() {
		return config.getString("need-higher-guild-rank");
	}

	public static String getChunkNotConnected() {
		return config.getString("chunk-not-connected");
	}

	public static String getGuildCreated(String name) {
		return config.getString("created-guild").replace("%guild-name%", name);
	}

	public static String getEnteredGuildLand(Guild guild) {
		return config.getString("entered-guild-land").replace("%guild%", guild.getName());
	}

	public static String getLeftGuildLand(Guild guild) {
		return config.getString("left-guild-land").replace("%guild%", guild.getName());
	}

	public static String getClaimedLand() {
		return config.getString("claimed-land");
	}

	public static String getGuildAlreadyOwnsChunk() {
		return config.getString("chunk-already-owned");
	}

	public static String getGlobalGuildDisbanded(Player player, String guildName) {
		return config.getString("global-guild-disbanded").replace("%player%", player.getDisplayName()).replace("%guild%", guildName);
	}

	public static String getDisbandedGuild() {
		return config.getString("disbanded-guild");
	}

	public static int getMaxLandClaimable() {
		return config.getInt("max-land-per-guild");
	}

	public static String getGuildCantClaimMoreLand() {
		return config.getString("cannot-claim-more-chunks");
	}

	public static String getGuildCantAffordMoreLand() {
		return config.getString("guild-cannot-afford-claim");
	}

	public static String getGuildBankBalanceMessage(Guild guild) {
		return config.getString("bank-balance").replace("%guild-balance%", guild.getBankBalanceFormatted());
	}

	public static String getCantAffordBankDeposit() {
		return config.getString("cannot-afford-bank-deposit");
	}

	public static String getBankDepositSuccess(double amount) {
		DecimalFormat df = new DecimalFormat("##.00");
		return config.getString("money-deposited").replace("%amount%", df.format(amount));
	}

	public static String getBankWithdrawSuccess(double amount) {
		DecimalFormat df = new DecimalFormat("##.00");
		return config.getString("money-withdrawn").replace("%amount%", df.format(amount));
	}

	public static String getGuildBankCantAffordWithdraw() {
		return config.getString("bank-cannot-afford-payout");
	}

	public static String getChunkWouldDisconnectLand() {
		return config.getString("chunk-would-disconnect-land");
	}

	public static String getUnclaimedChunk() {
		return config.getString("unclaimed-land");
	}

	public static String getGuildDoesntOwnChunk() {
		return config.getString("chunk-not-owned");
	}

	public static String bothSelectionsNotSet() {
		return config.getString("both-not-selected");
	}

	public static String needSelectionName() {
		return config.getString("need-selection-name");
	}

	public static String selectionWithNameAlreadyExists() {
		return config.getString("selection-with-name-already-exists");
	}

	public static String getRegionNotClaimable() {
		return config.getString("not-claimable-region");
	}

	public static String getGuildChatFormatted(Player player, String message) {
		return config.getString("guild-chat").replace("%player%", player.getDisplayName()).replace("%message%", message);
	}

}
