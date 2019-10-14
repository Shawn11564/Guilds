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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

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
						if (!guild.getChunkList().contains(player.getLocation().getChunk())) {
							if (guild.getChunkList().size() < guild.getMaxLand()) {
								if (Guilds.getEconomy().getBalance(player) > guild.getNewLandCost()) {
									if (Guilds.getInstance().getRegionManager().inRegion(player.getLocation().getChunk())) {
										if (guild.getChunkList().isEmpty()) {
											guild.claimChunk(player.getLocation().getChunk());
											Guilds.getEconomy().withdrawPlayer(player, guild.getNewLandCost());
											Guilds.getInstance().getLandManager().update(guild, player.getLocation().getChunk());
											Guilds.getInstance().getGuildFileManager().save(guild);
											Chat.tell(sender, Config.getClaimedLand());
										} else {
											if (guild.isChunkConnected(player.getLocation().getChunk())) {
												if (!guild.getChunkList().contains(player.getLocation().getChunk())) {
													guild.claimChunk(player.getLocation().getChunk());
													Guilds.getEconomy().withdrawPlayer(player, guild.getNewLandCost());
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
							Chat.tell(sender, "&cYour guild already owns this land!");
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
								if (guild.getChunkList().size() < 2) {
									guild.unclaimChunk(player.getLocation().getChunk());
									Guilds.getInstance().getLandManager().removeChunk(player.getLocation().getChunk());
									Chat.tell(sender, Config.getUnclaimedChunk());
								} else {
									Chat.tell(sender, Config.getChunkWouldDisconnectLand());
								}
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

	@Subcommand("chat")
	public void onChatToggle(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (Guilds.getInstance().getGuildManager().isInGuild(player)) {
				if (player.hasPermission("guilds.chat")) {
					Guilds.getInstance().getChatManager().toggle(player);
				} else {
					Chat.tell(sender, Config.getNoPermissionMessage());
				}
			} else {
				Chat.tell(sender, Config.getNotInGuild());
			}
		} else {
			Chat.tell(sender, Config.getMustBePlayerMessage());
		}
	}

	@Subcommand("rename")
	public void onRename(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("guilds.rename")) {
				if (Guilds.getInstance().getGuildManager().isInGuild(player)) {
					Guild guild = Guilds.getInstance().getGuildManager().getPlayerGuild(player);
					if (guild.getMembers().get(player.getUniqueId()) == RankType.LEADER) {
						if (args.length > 0) {
							guild.setName(args[0]);
						} else {
							Chat.tell(sender, "&c/guild rename <name>");
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

	@Subcommand("invite")
	public void onInvite(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("guilds.invite")) {
				if (Guilds.getInstance().getGuildManager().isInGuild(player)) {
					Guild guild = Guilds.getInstance().getGuildManager().getPlayerGuild(player);
					RankType rank = guild.getMembers().get(player.getUniqueId());
					if (rank == RankType.LEADER || rank == RankType.COLEADER) {
						if (args.length > 0) {
							for (Player p : Bukkit.getOnlinePlayers()) {
								if (p.getName().equalsIgnoreCase(args[0])) {
									guild.invite(p);
									Chat.tell(sender, "&aSuccessfully invited &6" + args[0]);
									return;
								}
							}
							Chat.tell(sender, "&cUnable to find that player, are they online?");
						} else {
							Chat.tell(sender, "&c/guild invite <player>");
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

	@Subcommand("kick")
	public void onKick(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("guilds.kick")) {
				if (Guilds.getInstance().getGuildManager().isInGuild(player)) {
					Guild guild = Guilds.getInstance().getGuildManager().getPlayerGuild(player);
					if (guild.getMembers().get(player.getUniqueId()) == RankType.LEADER || guild.getMembers().get(player.getUniqueId()) == RankType.COLEADER) {
						if (args.length > 0) {
							for (UUID u : guild.getMembers().keySet()) {
								if (Bukkit.getPlayer(u).getName().equalsIgnoreCase(args[0])) {
									if (guild.getLeaders().contains(u.toString())) {
										Chat.tell(sender, "&cYou can't kick the guild leader!");
									} else {
										guild.getMembers().remove(u);
										Chat.tell(sender, "&aSuccessfully kicked &6" + args[0]);
										return;
									}
								}
							}
							Chat.tell(sender, "&cUnable to find that player, are they online?");
						} else {
							Chat.tell(sender, "&c/guild kick <player>");
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

	@Subcommand("promote")
	public void onPromote(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("guilds.promote")) {
				if (Guilds.getInstance().getGuildManager().isInGuild(player)) {
					Guild guild = Guilds.getInstance().getGuildManager().getPlayerGuild(player);
					if (guild.getMembers().get(player.getUniqueId()) == RankType.LEADER) {
						if (args.length > 0) {
							for (UUID u : guild.getMembers().keySet()) {
								if (Bukkit.getPlayer(u).getName().equalsIgnoreCase(args[0])) {
									if (guild.getLeaders().contains(u.toString())) {
										Chat.tell(sender, "&cGuild leader is the highest rank!");
									} else {
										RankType rank = guild.getMembers().get(u);
										if (rank == RankType.MEMBER) {
											guild.getMembers().remove(u);
											guild.getMembers().put(u, RankType.COLEADER);
											Chat.tell(sender, "&aSuccessfully promoted &6" + Bukkit.getPlayer(u).getName());
										} else if (rank == RankType.COLEADER) {
											guild.getMembers().remove(player.getUniqueId());
											guild.getMembers().put(player.getUniqueId(), RankType.COLEADER);
											guild.getMembers().remove(u);
											guild.getMembers().put(u, RankType.LEADER);
											Chat.tell(sender, "&aSuccessfully promoted &6" + Bukkit.getPlayer(u).getName());
										}
									}
								}
							}
							Chat.tell(sender, "&cUnable to find that player, are they online?");
						} else {
							Chat.tell(sender, "&c/guild promote <player>");
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

	@Subcommand("demote")
	public void onDemote(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("guilds.demote")) {
				if (Guilds.getInstance().getGuildManager().isInGuild(player)) {
					Guild guild = Guilds.getInstance().getGuildManager().getPlayerGuild(player);
					if (guild.getMembers().get(player.getUniqueId()) == RankType.LEADER) {
						if (args.length > 0) {
							for (UUID u : guild.getMembers().keySet()) {
								if (Bukkit.getPlayer(u).getName().equalsIgnoreCase(args[0])) {
									if (guild.getLeaders().contains(u.toString())) {
										Chat.tell(sender, "&cYou can't demote the leader.");
									} else {
										RankType rank = guild.getMembers().get(u);
										if (rank == RankType.MEMBER) {
											Chat.tell(sender, "&cMember is the lowest rank!");
										} else if (rank == RankType.COLEADER) {
											guild.getMembers().remove(u);
											guild.getMembers().put(u, RankType.MEMBER);
											Chat.tell(sender, "&aSuccessfully demoted &6" + Bukkit.getPlayer(u).getName());
										}
									}
								}
							}
							Chat.tell(sender, "&cUnable to find that player, are they online?");
						} else {
							Chat.tell(sender, "&c/guild demote <player>");
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

	@Subcommand("join")
	public void onJoin(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (player.hasPermission("guilds.join")) {
				if (!Guilds.getInstance().getGuildManager().isInGuild(player)) {
					if (args.length > 0) {
						Guild guild = Guilds.getInstance().getGuildManager().isInvitedToGuild(player, args[0]);
						if (guild != null) {
							for (Player p : guild.getOnlineMembers()) {
								Chat.tell(p, "&6" + player.getName() + " &ahas joined your guild!");
							}
							guild.getMembers().put(player.getUniqueId(), RankType.MEMBER);
							Chat.tell(sender, "&aYou have joined the guild!");
						} else {
							Chat.tell(sender, "&cThat guild does not exist!");
						}
					} else {
						Chat.tell(sender, "&c/guild join <guild name>");
					}
				} else {
					Chat.tell(sender, "&cYou are already in a guild!");
				}
			} else {
				Chat.tell(sender, Config.getNoPermissionMessage());
			}
		} else {
			Chat.tell(sender, Config.getMustBePlayerMessage());
		}
	}

	// Bank
	/*@Subcommand("bank")
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

	}*/

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

	@Subcommand("tax|taxes")
	public class TaxCMD extends BaseCommand {

		@Subcommand("view")
		public void onView(CommandSender sender) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("guilds.taxes")) {
					if (Guilds.getInstance().getGuildManager().isInGuild(player)) {
						Guild guild = Guilds.getInstance().getGuildManager().getPlayerGuild(player);
						Chat.tell(sender, "&aYour guild owes: &6" + guild.getTaxOwedFormatted() + " &ain taxes! Pay them soon!");
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

		@Subcommand("pay")
		public void onPay(CommandSender sender, String[] args) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("guilds.taxes")) {
					if (Guilds.getInstance().getGuildManager().isInGuild(player)) {
						Guild guild = Guilds.getInstance().getGuildManager().getPlayerGuild(player);
						if (guild.getMembers().get(player.getUniqueId()) == RankType.LEADER || guild.getMembers().get(player.getUniqueId()) == RankType.COLEADER) {
							if (!(guild.getOwedTaxes() <= 0)) {
								if (args.length > 0 && NumberUtils.isNumber(args[0])) {
									double amount = Double.parseDouble(args[0]);
									if (Guilds.getEconomy().getBalance(player) >= amount) {
										EconomyResponse response = Guilds.getEconomy().withdrawPlayer(player, amount);
										if (response.transactionSuccess()) {
											if (guild.getOwedTaxes() <= amount) {
												double refund = amount - guild.getOwedTaxes();
												Guilds.getEconomy().depositPlayer(player, refund);
												Chat.tell(sender, "&aYou have paid your guild taxes in full!");
											} else {
												guild.setOwedTaxes(guild.getOwedTaxes() - amount);
												Chat.tell(sender, "&aYou have paid your guild taxes, your now owe &6$" + guild.getTaxOwedFormatted());
											}
										} else {
											Chat.tell(sender, "&cSomething went wrong when paying your taxes.");
										}
									} else {
										Chat.tell(sender, "&cYou can't afford to pay this much!");
									}
								} else {
									Chat.tell(sender, "&c/guild tax pay <amount>");
								}
							} else {
								Chat.tell(sender, "&cYour guild doesn't owe any taxes!");
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

}
