package dev.mrshawn.guilds.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.guilds.Guild;
import dev.mrshawn.guilds.guilds.RankType;
import dev.mrshawn.guilds.guilds.creator.GuildCreator;
import dev.mrshawn.guilds.guilds.managers.GuildManager;
import dev.mrshawn.guilds.utils.Chat;
import dev.mrshawn.guilds.utils.Config;
import dev.mrshawn.guilds.utils.Items;
import dev.mrshawn.guilds.utils.Selection;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("guild|g|guilds")
public class GuildCMD extends BaseCommand {

	private GuildManager guildManager = Guilds.getInstance().getGuildManager();

	@Default
	@CatchUnknown
	@Subcommand("help|?")
	public void onHelp(CommandSender sender, String[] args) {
		if (sender.hasPermission("guilds.help")) {
			Chat.tell(sender, Config.getHelpMessage());
		} else {
			Chat.tell(sender, Config.getNoPermissionMessage());
		}
	}

	@Subcommand("create")
	public void onCreate(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("guilds.create")) {
				if (!guildManager.isInGuild(player)) {
					if (args.length > 0) {
						GuildCreator gc = new GuildCreator(args[0], player);
						Chat.tell(sender, Config.getGuildCreated(args[0]));
					} else {
						Chat.tell(sender, "&c/guild create <name>");
					}
				} else {
					Chat.tell(sender, Config.getAlreadyInGuildMessage());
				}
			} else {
				Chat.tell(sender, Config.getNoPermissionMessage());
			}
		} else {
			Chat.tell(sender, Config.getMustBePlayerMessage());
		}
	}

	@Subcommand("claim")
	public void onClaim(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("guilds.claim")) {
				if (guildManager.isInGuild(player)) {
					Guild guild = guildManager.getPlayerGuild(player);
					if (guild.getMembers().get(player.getUniqueId()) == RankType.LEADER) {
						if (guild.getChunkList().size() < guild.getMaxLand()) {
							if (guild.canAfford()) {
								if (Guilds.getInstance().getRegionManager().inRegion(player.getLocation().getChunk())) {
									if (guild.getChunkList().isEmpty()) {
										guild.claimChunk(player.getLocation().getChunk());
										Guilds.getInstance().getLandManager().update(guild, player.getLocation().getChunk());
										Guilds.getInstance().getGuildFileManager().save(guild);
										Chat.tell(sender, Config.getClaimedLand());
									} else {
										if (guild.isChunkConnected(player.getLocation().getChunk())) {
											if (!guild.getChunkList().contains(player.getLocation().getChunk())) {
												guild.claimChunk(player.getLocation().getChunk());
												Guilds.getInstance().getLandManager().update(guild, player.getLocation().getChunk());
												Guilds.getInstance().getGuildFileManager().save(guild);
												Chat.tell(sender, Config.getClaimedLand());
											} else {
												Chat.tell(sender, Config.getGuildAlreadyOwnsChunk());
											}
										} else {
											Chat.tell(sender, Config.getChunkNotConnected());
										}
									}
								} else {
									Chat.tell(sender, Config.getRegionNotClaimable());
								}
							} else {
								Chat.tell(sender, Config.getGuildCantAffordMoreLand());
							}
						} else {
							Chat.tell(sender, Config.getGuildCantClaimMoreLand());
						}
					} else {
						Chat.tell(sender, Config.getNeedHigherGuildRank());
					}
				} else {
					Chat.tell(sender, Config.getNotInGuild());
				}
			} else {
				Chat.tell(sender, Config.getNoPermissionMessage());
			}
		} else {
			Chat.tell(sender, Config.getMustBePlayerMessage());
		}
	}

	@Subcommand("unclaim")
	public void onUnclaim(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("guilds.unclaim")) {
				if (guildManager.isInGuild(player)) {
					Guild guild = guildManager.getPlayerGuild(player);
					if (guild.getLeaders().contains(player.getUniqueId().toString())) {
						if (guild.getChunkList().contains(player.getLocation().getChunk())) {
							if (guild.isUnclaimableChunk(player.getLocation().getChunk())) {
								guild.unclaimChunk(player.getLocation().getChunk());
								Guilds.getInstance().getLandManager().removeChunk(player.getLocation().getChunk());
								Chat.tell(sender, Config.getUnclaimedChunk());
							} else {
								Chat.tell(sender, Config.getChunkWouldDisconnectLand());
							}
						} else {
							Chat.tell(sender, Config.getGuildDoesntOwnChunk());
						}
					} else {
						Chat.tell(sender, Config.getNeedHigherGuildRank());
					}
				} else {
					Chat.tell(sender, Config.getNotInGuild());
				}
			} else {
				Chat.tell(sender, Config.getNoPermissionMessage());
			}
		} else {
			Chat.tell(sender, Config.getMustBePlayerMessage());
		}
	}

	@Subcommand("disband")
	public void onDisband(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("guilds.disband")) {
				if (guildManager.isInGuild(player)) {
					Guild guild = guildManager.getPlayerGuild(player);
					if (guild.getMembers().get(player.getUniqueId()) == RankType.LEADER) {
						guildManager.deleteGuild(player, guild);
						Chat.tell(sender, Config.getDisbandedGuild());
						guild.delete();
					} else {
						Chat.tell(sender, Config.getNeedHigherGuildRank());
					}
				} else {
					Chat.tell(sender, Config.getNotInGuild());
				}
			} else {
				Chat.tell(sender, Config.getNoPermissionMessage());
			}
		} else {
			Chat.tell(sender, Config.getMustBePlayerMessage());
		}
	}

	@Subcommand("bank")
	public class BankCMD extends BaseCommand {

		@Subcommand("view")
		public void onView(CommandSender sender, String[] args) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("guilds.bank")) {
					if (guildManager.isInGuild(player)) {
						Guild guild = guildManager.getPlayerGuild(player);
						Chat.tell(sender, Config.getGuildBankBalanceMessage(guild));
					} else {
						Chat.tell(sender, Config.getNotInGuild());
					}
				} else {
					Chat.tell(sender, Config.getNoPermissionMessage());
				}
			} else {
				Chat.tell(sender, Config.getMustBePlayerMessage());
			}
		}

		@Subcommand("deposit")
		public void onDeposit(CommandSender sender, String[] args) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("guilds.bank")) {
					if (guildManager.isInGuild(player)) {
						if (args.length > 0) {
							if (NumberUtils.isNumber(args[0])) {
								double amount = Double.parseDouble(args[0]);
								if (Guilds.getEconomy().getBalance(player) >= amount) {
									EconomyResponse response = Guilds.getEconomy().withdrawPlayer(player, amount);
									if (response.transactionSuccess()) {
										guildManager.getPlayerGuild(player).addMoney(amount);
										Chat.tell(sender, Config.getBankDepositSuccess(amount));
									} else {
										Chat.tell(sender, "&cSomething went wrong here, please try again.");
									}
								} else {
									Chat.tell(sender, Config.getCantAffordBankDeposit());
								}
							} else {
								Chat.tell(sender, "&c/guild bank deposit <amount>");
							}
						} else {
							Chat.tell(sender, "&c/guild bank deposit <amount>");
						}
					} else {
						Chat.tell(sender, Config.getNotInGuild());
					}
				} else {
					Chat.tell(sender, Config.getNoPermissionMessage());
				}
			} else {
				Chat.tell(sender, Config.getMustBePlayerMessage());
			}
		}

		@Subcommand("withdraw")
		public void onWithdraw(CommandSender sender, String[] args) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("guilds.bank")) {
					if (guildManager.isInGuild(player)) {
						if (guildManager.getPlayerGuildRank(player) == RankType.LEADER) {
							if (args.length > 0) {
								if (NumberUtils.isNumber(args[0])) {
									double amount = Double.parseDouble(args[0]);
									if (guildManager.getPlayerGuild(player).canAfford(amount)) {
										EconomyResponse response = Guilds.getEconomy().depositPlayer(player, amount);
										if (response.transactionSuccess()) {
											guildManager.getPlayerGuild(player).removeMoney(amount);
											Chat.tell(sender, Config.getBankWithdrawSuccess(amount));
										} else {
											Chat.tell(sender, "&cSomething went wrong here, please try again.");
										}
									} else {
										Chat.tell(sender, Config.getGuildBankCantAffordWithdraw());
									}
								} else {
									Chat.tell(sender, "&c/guild bank withdraw <amount>");
								}
							} else {
								Chat.tell(sender, "&c/guild bank withdraw <amount>");
							}
						} else {
							Chat.tell(sender, Config.getNeedHigherGuildRank());
						}
					} else {
						Chat.tell(sender, Config.getNotInGuild());
					}
				} else {
					Chat.tell(sender, Config.getNoPermissionMessage());
				}
			} else {
				Chat.tell(sender, Config.getMustBePlayerMessage());
			}
		}

	}

	@Subcommand("admin")
	public class AdminCMD extends BaseCommand {

		@Subcommand("regions|region")
		public class RegionCMD extends BaseCommand {

			@Subcommand("wand")
			public void onWand(CommandSender sender, String[] args) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (player.hasPermission("guilds.admin")) {
						Items.regionWand(player);
						Chat.tell(sender, "&aYou've been given a region wand!");
					} else {
						Chat.tell(sender, Config.getNoPermissionMessage());
					}
				} else {
					Chat.tell(sender, Config.getMustBePlayerMessage());
				}
			}

			@Subcommand("save|claim")
			public void onClaim(CommandSender sender, String[] args) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (player.hasPermission("guilds.admin")) {
						if (Guilds.getInstance().getSelectionManager().hasSelections(player)) {
							Selection selection = Guilds.getInstance().getSelectionManager().getPlayerSelection(player);
							if (selection.bothSet()) {
								if (args.length > 0) {
									if (!Guilds.getInstance().getRegionManager().regionSameName(args[0])) {
										Guilds.getInstance().getRegionManager().addRegion(args[0], selection.getLocOne(), selection.getLocTwo());
										Chat.tell(sender, "&aSuccessfully created new region!");
									} else {
										Chat.tell(sender, Config.selectionWithNameAlreadyExists());
									}
								} else {
									Chat.tell(sender, Config.needSelectionName());
								}
							} else {
								Chat.tell(sender, Config.bothSelectionsNotSet());
							}
						}
					} else {
						Chat.tell(sender, Config.getNoPermissionMessage());
					}
				} else {
					Chat.tell(sender, Config.getMustBePlayerMessage());
				}
			}

			@Subcommand("remove|delete")
			public void onRemove(CommandSender sender, String[] args) {
				if (sender.hasPermission("guilds.admin")) {
					if (args.length > 0) {
						if (Guilds.getInstance().getRegionManager().regionSameName(args[0])) {
							Guilds.getInstance().getRegionManager().removeRegion(args[0]);
							Guilds.getInstance().getRegionFileManager().getConfig().set(args[0], null);
							Guilds.getInstance().getRegionFileManager().save();
							Chat.tell(sender, "&cSuccessfully removed region!");
						} else {
							Chat.tell(sender, "&cThere is no region with that name!");
							Chat.tell(sender, "&cValid regions are:");
							Chat.regionList(sender);
						}
					} else {
						Chat.tell(sender, "&cPlease give the name of the region to remove!");
					}
				} else {
					Chat.tell(sender, Config.getNoPermissionMessage());
				}
			}

		}

	}

}
