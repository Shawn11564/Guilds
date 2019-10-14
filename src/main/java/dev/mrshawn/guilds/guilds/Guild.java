package dev.mrshawn.guilds.guilds;

import dev.mrshawn.guilds.Guilds;
import dev.mrshawn.guilds.utils.Chunks;
import dev.mrshawn.guilds.utils.Config;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class Guild {

	private String name;
	private Map<UUID, RankType> members;
	private List<Player> onlineMembers;
	private List<Chunk> chunkList;
	private List<Player> invitedMembers;
	private UUID uuid;
	private double bankBalance;
	private double owedTaxes;
	private int missedCycles;
	private int maxLand;
	private File file;

	public Guild(String name, Map<UUID, RankType> members, UUID uuid) {
		this.name = name;
		this.members = members;
		this.uuid = uuid;
		chunkList = new ArrayList<>();
		onlineMembers = new ArrayList<>();
		invitedMembers = new ArrayList<>();
		bankBalance = 0;
		maxLand = Config.getMaxLandClaimable();
		file = new File(Guilds.getInstance().getDataFolder() + File.separator + "Guilds" + File.separator + getUuid() + ".yml");
	}

	public Guild(String name, Map<UUID, RankType> members, List<Chunk> chunkList, UUID uuid, double bankBalance, int maxLand, double owedTaxes, int missedCycles) {
		this.name = name;
		this.members = members;
		this.chunkList = chunkList;
		this.uuid = uuid;
		this.bankBalance = bankBalance;
		this.maxLand = maxLand;
		this.owedTaxes = owedTaxes;
		this.missedCycles = missedCycles;
		file = new File(Guilds.getInstance().getDataFolder() + File.separator + "Guilds" + File.separator + getUuid() + ".yml");
	}

	public boolean claimChunk(Chunk chunk) {
		if (canAfford()) {
			chunkList.add(chunk);
			List<String> chunkStrings = new ArrayList<>();
			for (Chunk c : getChunkList()) {
				chunkStrings.add(Chunks.formatChunk(c));
			}
			Guilds.getInstance().getGuildFileManager().getGuildFiles().get(this).set("chunks", chunkStrings);
			return true;
		}
		return false;
	}

	public boolean canAfford() {
		return bankBalance >= getNewLandCost();
	}

	public boolean canAfford(double amount) {
		return bankBalance >= amount;
	}

	public double getNewLandCost() {
		if (chunkList.size() == 0) {
			return Guilds.getInstance().getLandCosts().get(0);
		}
		return Guilds.getInstance().getLandCosts().get(chunkList.size());
	}

	public double getNewTaxCost() {
		if (chunkList.size() == 0) {
			return Guilds.getInstance().getTaxCosts().get(0);
		}
		return Guilds.getInstance().getTaxCosts().get(chunkList.size());
	}

	public void addMoney(double amount) {
		bankBalance += amount;
		Guilds.getInstance().getGuildFileManager().modify(this, "bank-balance", Guilds.getInstance().getGuildFileManager().getGuildFiles().get(this).getDouble("bank-balance") + amount);
	}

	public void removeMoney(double amount) {
		bankBalance -= amount;
		Guilds.getInstance().getGuildFileManager().modify(this, "bank-balance", Guilds.getInstance().getGuildFileManager().getGuildFiles().get(this).getDouble("bank-balance") - amount);
	}

	public String getBankBalanceFormatted() {
		DecimalFormat df = new DecimalFormat("##.00");
		return df.format(bankBalance);
	}

	public String getTaxOwedFormatted() {
		DecimalFormat df = new DecimalFormat("##.00");
		return df.format(owedTaxes);
	}

	public boolean isChunkConnected(Chunk chunk) {
		for (Chunk c : chunkList) {
			Chunk n = c.getWorld().getChunkAt(c.getX() + 1, c.getZ());
			Chunk s = c.getWorld().getChunkAt(c.getX() - 1, c.getZ());
			Chunk e = c.getWorld().getChunkAt(c.getX(), c.getZ() + 1);
			Chunk w = c.getWorld().getChunkAt(c.getX(), c.getZ() - 1);
			if (chunk == n || chunk == s || chunk == e || chunk == w) {
				return true;
			}
		}
		return false;
	}

	public boolean isUnclaimableChunk(Chunk chunk) {
		Chunk n = chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ());
		Chunk s = chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ());
		Chunk e = chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() + 1);
		Chunk w = chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() - 1);
		Chunk[] chunks = new Chunk[]{n, s, e, w};
		int counter = 0;
		for (Chunk c : chunks) {
			if (chunkList.contains(c)) {
				counter++;
			}
		}
		return counter == 1;
	}

	public void unclaimChunk(Chunk chunk) {
		chunkList.remove(chunk);
		List<String> chunkStrings = new ArrayList<>();
		for (Chunk c : getChunkList()) {
			chunkStrings.add(Chunks.formatChunk(c));
		}
		Guilds.getInstance().getGuildFileManager().getGuildFiles().get(this).set("chunks", chunkStrings);
	}

	public List<String> getLeaders() {
		List<String> temp = new ArrayList<>();
		for (UUID uuid : members.keySet()) {
			if (members.get(uuid) == RankType.LEADER) {
				temp.add(uuid.toString());
			}
		}
		return temp;
	}

	public List<String> getCoLeaders() {
		List<String> temp = new ArrayList<>();
		for (UUID uuid : members.keySet()) {
			if (members.get(uuid) == RankType.COLEADER) {
				temp.add(uuid.toString());
			}
		}
		return temp;
	}

	public List<String> getRegularMembers() {
		List<String> temp = new ArrayList<>();
		for (UUID uuid : members.keySet()) {
			if (members.get(uuid) == RankType.MEMBER) {
				temp.add(uuid.toString());
			}
		}
		return temp;
	}

	public void invite(Player player) {
		invitedMembers.add(player);
	}

	public void delete() {
		chunkList.clear();
		members.clear();
		Guilds.getInstance().getGuildFileManager().delete(this);
	}
}
