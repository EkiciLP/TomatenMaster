package net.Tomatentum.TomatenMaster.punishments;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.database.Database;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Punishment {
	private final int caseId;
	private final long guildId;
	private final long userId;
	private final String reason;
	private final boolean active;
	private final CaseType caseType;
	private final long endTime;
	private final long punishTime;
	private final long moderator;

	protected Punishment(int caseId, long guildId, long userId, String reason, boolean active, String caseType, long endTime, long punishTime, long moderatorId) {
		this.caseId = caseId;
		this.guildId = guildId;
		this.userId = userId;
		this.reason = reason;
		this.active = active;
		this.caseType = CaseType.valueOf(caseType);
		this.endTime = endTime;
		this.punishTime = punishTime;
		moderator = moderatorId;
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

	public long getEndTime() {
		return endTime;
	}

	public long getPunishTime() {
		return punishTime;
	}

	public Member getModerator() {
		return getGuild().getMemberById(moderator);
	}

	public boolean isActive() {
		return active;
	}

	public static Punishment makePunishment(ResultSet resultSet) {
		try {
			Punishment punishment = new Punishment(
					resultSet.getInt("caseid"),
					resultSet.getLong("guildid"),
					resultSet.getLong("userid"),
					resultSet.getString("reason"),
					resultSet.getBoolean("active"),
					resultSet.getString("casetype"),
					resultSet.getLong("expiryDate"),
					resultSet.getLong("punishTime"),
					resultSet.getLong("moderatorid")
					);
			return punishment;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Punishment getPunishment(int caseid) {
		ResultSet resultSet = Database.executeQuery("SELECT * FROM punishments WHERE caseid=" + caseid);
		Punishment punishment;
		try {
			resultSet.first();

			punishment = new Punishment(
					resultSet.getInt("caseid"),
					resultSet.getLong("guildid"),
					resultSet.getLong("userid"),
					resultSet.getString("reason"),
					resultSet.getBoolean("active"),
					resultSet.getString("casetype"),
					resultSet.getLong("expiryDate"),
					resultSet.getLong("punishTime"),
					resultSet.getLong("moderatorid")
					);

		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return null;
		}


		return punishment;
	}
}
