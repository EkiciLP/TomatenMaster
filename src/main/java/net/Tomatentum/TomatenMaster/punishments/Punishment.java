package net.Tomatentum.TomatenMaster.punishments;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

class Punishment {
	private final int caseId;
	private final long guildId;
	private final long userId;
	private final String reason;
	private final boolean active;
	private final CaseType caseType;


	Punishment(int caseId, long guildid, long userid, String reason, boolean active, String caseType) {
		this.caseId = caseId;
		this.guildId = guildid;
		this.userId = userid;
		this.reason = reason;
		this.active = active;
		this.caseType = CaseType.valueOf(caseType);

	}

	public int getCaseId() {
		return caseId;
	}

	public Guild getGuild() {
		return TomatenMaster.getINSTANCE().getBot().getGuildById(guildId);
	}

	public User getUser() {
		return TomatenMaster.getINSTANCE().getBot().getUserById(userId);
	}

	public String getReason() {
		return reason;
	}

	public CaseType getCaseType() {
		return caseType;
	}

	public boolean isActive() {
		return active;
	}
}
