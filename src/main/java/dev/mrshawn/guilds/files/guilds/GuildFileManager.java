package dev.mrshawn.guilds.files.guilds;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.files.FileCreator;
import dev.mrshawn.guilds.guilds.Guild;
import dev.mrshawn.guilds.guilds.RankType;
import dev.mrshawn.guilds.utils.Chat;
import dev.mrshawn.guilds.utils.Chunks;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Getter
public class GuildFileManager {

	private Map<Guild, FileConfiguration> guildFiles;
	private FileCreator fileCreator;
	private File folder;

	public GuildFileManager() {
		guildFiles = new HashMap<>();
		fileCreator = new FileCreator();
		folder = new File(Guilds.getInstance().getDataFolder() + File.separator + "Guilds");
		init();
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void init() {
		if (!folder.exists()) {
			folder.mkdir();
		}
		for (File file : Objects.requireNonNull(folder.listFiles())) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			String name = config.getString("name");
			UUID uuid = UUID.fromString(Objects.requireNonNull(config.getString("uuid")));
			int maxLand = config.getInt("max-land");
			double bankBalance = config.getDouble("bank-balance");
			Map<UUID, RankType> members = new HashMap<>();
			for (String s : config.getStringList("members.leaders")) {
				members.put(UUID.fromString(s), RankType.LEADER);
			}
			for (String s : config.getStringList("members.coleaders")) {
				members.put(UUID.fromString(s), RankType.COLEADER);
			}
			for (String s : config.getStringList("members.members")) {
				members.put(UUID.fromString(s), RankType.MEMBER);
			}
			List<Chunk> chunks = new ArrayList<>();
			for (String s : config.getStringList("chunks")) {
				String[] temp = s.split(":");
				World world = Bukkit.getWorld(temp[0]);
				int x = Integer.parseInt(temp[1]);
				int y = Integer.parseInt(temp[2]);
				if (world != null) {
					chunks.add(world.getChunkAt(x, y));
				}
			}
			Guild guild = new Guild(name, members, chunks, uuid, bankBalance, maxLand);
			Guilds.getInstance().getGuildManager().addGuild(guild);
			guildFiles.put(guild, config);
		}
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void store(Guild guild) {
		File file = guild.getFile();
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Chat.log("&4Unable to create data file for guild: &6" + guild.getName());
				e.printStackTrace();
			}
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			config.set("name", guild.getName());
			config.set("uuid", guild.getUuid().toString());
			config.set("max-land", guild.getMaxLand());
			config.set("bank-balance", guild.getBankBalance());
			config.set("members.leaders", guild.getLeaders());
			config.set("members.coleaders", guild.getCoLeaders());
			config.set("members.members", guild.getRegularMembers());
			List<String> chunkStrings = new ArrayList<>();
			for (Chunk chunk : guild.getChunkList()) {
				chunkStrings.add(Chunks.formatChunk(chunk));
			}
			config.set("chunks", chunkStrings);
			guildFiles.put(guild, config);
			save(guild);
		}
	}

	public void save(Guild guild) {
		try {
			guildFiles.get(guild).save(guild.getFile());
		} catch (IOException e) {
			Chat.log("&4Unable to save guild file for: &6" + guild.getFile());
			e.printStackTrace();
		}
	}

	public void modify(Guild guild, String path, double value) {
		guildFiles.get(guild).set(path, value);
		save(guild);
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void delete(Guild guild) {
		guild.getFile().delete();
	}

}
