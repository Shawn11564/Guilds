package dev.mrshawn.guilds;

import co.aikar.commands.PaperCommandManager;
import dev.mrshawn.guilds.commands.GuildCMD;
import dev.mrshawn.guilds.events.listeners.*;
import dev.mrshawn.guilds.files.guilds.GuildFileManager;
import dev.mrshawn.guilds.files.regions.RegionFileManager;
import dev.mrshawn.guilds.files.regions.SelectionManager;
import dev.mrshawn.guilds.guilds.Guild;
import dev.mrshawn.guilds.guilds.managers.*;
import dev.mrshawn.guilds.guilds.regions.RegionManager;
import dev.mrshawn.guilds.protections.ProtectionManager;
import dev.mrshawn.guilds.utils.Chat;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public final class Guilds extends JavaPlugin {

	private static Guilds instance;
	private static Economy economy;
	private GuildManager guildManager;
	private GuildFileManager guildFileManager;
	private LandManager landManager;
	private RegionFileManager regionFileManager;
	private RegionManager regionManager;
	private SelectionManager selectionManager;
	private ProtectionManager protectionManager;
	private TaxManager taxManager;
	private RaidingManager raidingManager;
	private ChatManager chatManager;
	private List<Double> landCosts;
	private List<Double> taxCosts;
	private List<Material> placeableMaterials;

	@Override
	public void onEnable() {
		setup();
	}

	@Override
	public void onDisable() {
		for (Guild guild : guildManager.getGuilds()) {
			guildFileManager.save(guild);
		}
		regionFileManager.save();
		raidingManager.setRaidTime(false);
	}

	public void setup() {
		instance = this;
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			Chat.log("&4Please install Vault and restart the server!");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		economy = rsp != null ? rsp.getProvider() : null;
		getConfig().options().copyDefaults();
		saveDefaultConfig();
		guildManager = new GuildManager();
		guildFileManager = new GuildFileManager();
		landManager = new LandManager();
		regionManager = new RegionManager(this);
		regionFileManager = new RegionFileManager(this);
		selectionManager = new SelectionManager();
		protectionManager = new ProtectionManager(this);
		taxManager = new TaxManager(this);
		raidingManager = new RaidingManager(this);
		chatManager = new ChatManager(this);
		landCosts = new ArrayList<>();
		landCosts.addAll(getConfig().getDoubleList("land-buy-cost"));
		taxCosts = new ArrayList<>();
		taxCosts.addAll(getConfig().getDoubleList("tax-land-cost"));
		placeableMaterials = new ArrayList<>();
		placeableMaterials.addAll((Collection<? extends Material>) getConfig().getList("placeable-blocks"));
		registerCommands();
		registerEvents();
	}

	public void registerCommands() {
		PaperCommandManager pcm = new PaperCommandManager(this);
		pcm.registerCommand(new GuildCMD());
	}

	public void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new MoveEvent(), this);
		pm.registerEvents(new ChunkEnterEvent(), this);
		pm.registerEvents(new Wand(this), this);
		pm.registerEvents(new LandProtection(this), this);
		pm.registerEvents(new JoinLeave(this), this);
		pm.registerEvents(new GuildChat(), this);
	}

	public static Guilds getInstance() {
		return instance;
	}

	public static Economy getEconomy() {
		return economy;
	}
}
