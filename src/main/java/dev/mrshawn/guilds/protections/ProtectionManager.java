package dev.mrshawn.guilds.protections;

import dev.mrshawn.guilds.Guilds;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtectionManager {

	private Guilds main;
	private List<BowArrow> bowArrows;
	private List<DispenserArrow> dispenserArrows;

	public ProtectionManager(Guilds main) {
		this.main = main;
		bowArrows = new ArrayList<>();
		dispenserArrows = new ArrayList<>();
	}

	public void addBowArrow(BowArrow arrow) {
		bowArrows.add(arrow);
	}

	public boolean bowArrowInList(UUID uuid) {
		for (BowArrow ba : bowArrows) {
			if (ba.getUuid() == uuid) {
				return true;
			}
		}
		return false;
	}

	public BowArrow getBowArrow(UUID uuid) {
		for (BowArrow ba : bowArrows) {
			if (ba.getUuid() == uuid) {
				return ba;
			}
		}
		return null;
	}

	public void addDispenserArrow(DispenserArrow arrow) {
		dispenserArrows.add(arrow);
	}

	public boolean dispenserArrowInList(UUID uuid) {
		for (DispenserArrow da : dispenserArrows) {
			if (da.getUuid() == uuid) {
				return true;
			}
		}
		return false;
	}

	public DispenserArrow getDispenserArrow(UUID uuid) {
		for (DispenserArrow da : dispenserArrows) {
			if (da.getUuid() == uuid) {
				return da;
			}
		}
		return null;
	}

}
