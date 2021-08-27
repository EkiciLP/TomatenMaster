package net.Tomatentum.TomatenMaster.managers;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.database.Database;
import net.Tomatentum.TomatenMaster.util.Embed;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


public class PunishManager {
	private final TomatenMaster bot;

	public PunishManager(TomatenMaster bot) {
		this.bot = bot;

		Database.executeUpdate("CREATE TABLE IF NOT EXISTS punishments" +
				"(caseid int PRIMARY KEY AUTO_INCREMENT, caseType varchar(7), guildid BIGINT, userid BIGINT, expiryDate BIGINT, reason varchar(400), active BOOLEAN)");

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					ResultSet resultSet = Database.executeQuery("SELECT * FROM punishments WHERE active=true");
					while (resultSet.next()) {
						if (resultSet.getString("caseType").equals(CaseType.WARNING)) {
							continue;
						}

						if (resultSet.getLong("expiryDate") <= System.currentTimeMillis()) {
							switch (CaseType.valueOf(resultSet.getString("CaseType"))) {
								case MUTE:
									unmuteMember(bot.getBot().getGuildById(resultSet.getLong("guildid")).getMemberById(resultSet.getLong("userid")));
									continue;
								case BAN:
									unBanUser(bot.getBot().getUserById(resultSet.getLong("userid")), bot.getBot().getGuildById(resultSet.getLong("guildid")));
									continue;
							}
						}
					}
				}catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}, 10000, 10000);
	}

	/**
	 *
	 * @param member
	 * @param minuteTime
	 * @param reason
	 * @return the caseid
	 */


	public int muteMember(Member member, int minuteTime, String reason) {
		member.getGuild().addRoleToMember(member, member.getGuild().getRolesByName("muted", true).get(0)).queue();
		int time = minuteTime * 60000;


		member.getUser().openPrivateChannel().complete().sendMessage(member.getUser().getAsMention()).setEmbeds(Embed.user(Color.RED,
				"You have been muted on " + member.getGuild().getName() + " for " + minuteTime + " minutes!\nReason: " + reason,
				member.getUser())).queue();


		Database.executeUpdate("INSERT INTO punishments(casetype, guildid, userid, expiryDate, reason, active) " +
				"VALUES('" + CaseType.MUTE + "', " + member.getGuild().getIdLong() + ", " + member.getIdLong() + ", " + (System.currentTimeMillis()+time) + ", '" + reason + "', true)");


		ResultSet resultSet = Database.executeQuery("SELECT * FROM punishments WHERE active=true && userid=" + member.getIdLong() + " && casetype='" + CaseType.MUTE + "' && guildid=" + member.getGuild().getIdLong());
		try {
			if (resultSet.next()) {
				return resultSet.getInt("caseid");
			}
			return 0;
		}catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	public int unmuteMember(Member member) {
		ResultSet resultSet = Database.executeQuery(
				"SELECT * FROM punishments WHERE active=true && userid=" + member.getIdLong() + " && casetype='" + CaseType.MUTE + "' && guildid=" + member.getGuild().getIdLong());
		Database.executeUpdate(
				"UPDATE punishments SET active=false WHERE userid=" + member.getIdLong() + " && casetype='" + CaseType.MUTE + "' && guildid=" + member.getGuild().getIdLong());

		member.getGuild().removeRoleFromMember(member, member.getGuild().getRolesByName("muted", true).get(0)).queue();

		try {
			resultSet.first();
			return resultSet.getInt("caseid");
		}catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}

	}

	public boolean isMuted(Member member) {
		ResultSet resultSet = Database.executeQuery("SELECT COUNT(caseid) FROM punishments " +
				"WHERE userid=" + member.getIdLong() + " && guildid=" + member.getGuild().getIdLong() + " && caseType='" + CaseType.MUTE + "' && active=true");

		try {
			resultSet.next();
			return resultSet.getInt(1) != 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	public int banUser(Guild guild, User user, int minuteTime, String reason) {
		int time = minuteTime*60000;

		user.openPrivateChannel().complete().sendMessage(user.getAsMention()).setEmbeds(Embed.user(
				Color.RED,
				"You have been banned on " + guild.getName() + " for " + minuteTime/60 + "hours!\nReason: "+ reason, user
				)).queue();

		List<User> bannedUsers = new ArrayList<>();
		guild.retrieveBanList().complete().forEach(ban -> bannedUsers.add(ban.getUser()));

		if (!bannedUsers.contains(user))
			guild.ban(user, 1, reason).queue();

		Database.executeUpdate("INSERT INTO punishments(casetype, guildid, userid, expiryDate, reason, active) " +
				"VALUES('" + CaseType.BAN + "', " + guild.getIdLong() + ", " + user.getIdLong() + ", " + (System.currentTimeMillis()+time) + ", '" + reason + "', true)");

		ResultSet resultSet = Database.executeQuery("SELECT * FROM punishments WHERE userid=" + user.getIdLong() + " && casetype='" + CaseType.WARNING +
				"' && guildid=" + guild.getIdLong() + " && reason='" + reason + "'");
		try {
			resultSet.first();
			return resultSet.getInt("caseid");
		}catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int unBanUser(User user, Guild guild) {

		ResultSet resultSet = Database.executeQuery("SELECT * FROM punishments WHERE userid=" + user.getIdLong() + " && casetype='" + CaseType.WARNING +
				"' && guildid=" + guild.getIdLong() + " && active=true");

		Database.executeUpdate("UPDATE punishments SET active=false WHERE userid=" + user.getIdLong() + " AND casetype='" + CaseType.MUTE + "' && guildid=" + guild.getIdLong());


		List<User> bannedUsers = new ArrayList<>();
		guild.retrieveBanList().complete().forEach(ban -> bannedUsers.add(ban.getUser()));

		if (bannedUsers.contains(user))
			guild.unban(user).queue();

		try {
			resultSet.first();
			return resultSet.getInt("caseid");
		}catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int addWarning(Member member, String reason) {
		try {
			ResultSet resultSet = Database.executeQuery("SELECT COUNT(caseid) FROM punishments WHERE userid=" + member.getIdLong() + " && caseType='" + CaseType.WARNING + "'");

			resultSet.next();
			int warningCount = resultSet.getInt(1)+1;

			Database.executeUpdate("INSERT INTO punishments(casetype, guildid, userid, expiryDate, reason, active)" +
					" VALUES('" + CaseType.WARNING + "', " + member.getGuild().getIdLong() + ", " + member.getIdLong() + ", NULL, '" + reason + "', NULL)");


			switch (warningCount) {
				case 3:
					bot.getPunishManager().muteMember(member, 120, "Automatic 3 Warnings");
					break;
				case 6:
					bot.getPunishManager().muteMember(member, 240, "Automatic 6 Warnings");
				case 9:
					bot.getPunishManager().banUser(member.getGuild(), member.getUser(), 240, "Automatic TempBan for 9 Warnings");
					break;
				case 12:
					bot.getPunishManager().banUser(member.getGuild(), member.getUser(), 241920, "Automatic Ban for 12 Warnings");
					break;

			}

			ResultSet caseresultSet = Database.executeQuery(
					"SELECT * FROM punishments WHERE userid=" + member.getIdLong() + " && casetype='" + CaseType.WARNING +
							"' && guildid=" + member.getGuild().getIdLong() + " && reason='" + reason + "'");
			caseresultSet.first();
			return caseresultSet.getInt("caseid");
		}catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	public Map<Integer, String> getWarnings(Member member) {
		Map<Integer, String> warnings = new HashMap<>();
		try {
			ResultSet resultSet = Database.executeQuery("SELECT * FROM punishments WHERE userid=" + member.getIdLong() + " AND caseType='" + CaseType.WARNING + "' && guildid=" + member.getGuild().getIdLong());

			while (resultSet.next()) {
				warnings.put(resultSet.getInt("caseid"), resultSet.getString("reason"));
			}

			return warnings;
		}catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public void clearWarnings(Member member) {
		Database.executeUpdate("DELETE FROM punishments WHERE userid=" + member.getIdLong() + " AND casetype='" + CaseType.WARNING + "' && guildid=" + member.getGuild().getIdLong());
	}


	public int getNextCaseId() {
		ResultSet resultSet = Database.executeQuery("SELECT COUNT(caseid) FROM punishments");
		try {
			resultSet.next();
			return resultSet.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	enum CaseType {
		BAN,
		MUTE,
		WARNING;

	}
}