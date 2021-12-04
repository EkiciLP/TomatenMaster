package net.Tomatentum.TomatenMaster.punishments;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.database.Database;
import net.Tomatentum.TomatenMaster.util.Embed;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;


public class PunishManager {
	private final TomatenMaster bot;
	private final String mutedRoleName = "muted";

	public PunishManager(TomatenMaster bot) {
		this.bot = bot;
		Database.executeUpdate("CREATE TABLE IF NOT EXISTS punishments" +
				"(caseid int PRIMARY KEY AUTO_INCREMENT, caseType varchar(7), guildid BIGINT, userid BIGINT, expiryDate BIGINT, reason varchar(400), active BOOLEAN, moderatorid BIGINT, punishTime BIGINT)");

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					ResultSet resultSet = Database.executeQuery("SELECT * FROM punishments WHERE active=true");
					while (resultSet.next()) {
						if (resultSet.getString("caseType").equals(CaseType.WARNING)) {
							continue;
						}
						long endTime = resultSet.getLong("expiryDate");
						if (resultSet.wasNull()) {
							return;
						}

						Guild guild = bot.getBot().getGuildById(resultSet.getLong("guildid"));
						User user = bot.getBot().getUserById(resultSet.getLong("userid"));
						if (endTime <= System.currentTimeMillis()) {
							switch (CaseType.valueOf(resultSet.getString("CaseType"))) {
								case MUTE:

									Member member = guild.getMemberById(user.getIdLong());
									unmuteMember(member, guild.getSelfMember());
									break;
								case BAN:
									unBanUser(user, guild, guild.getSelfMember());
									break;
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


	public int muteMember(Member member, int minuteTime, String reason, Member moderator) {
		member.getGuild().addRoleToMember(member, getMutedRole(member.getGuild())).queue();
		int time = minuteTime * 60000;


		member.getUser().openPrivateChannel().complete().sendMessage(member.getUser().getAsMention()).setEmbeds(Embed.user(Color.RED,
				"You have been muted on " + member.getGuild().getName() + " for " + minuteTime + " minutes!\nReason: " + reason + "\nResponsible Moderator: " + moderator.getAsMention(),
				member.getUser())).queue();


		Database.executeUpdate("INSERT INTO punishments(casetype, guildid, userid, expiryDate, reason, active, moderatorid, punishTime) " +
				"VALUES('" + CaseType.MUTE + "', " + member.getGuild().getIdLong() + ", " + member.getIdLong() + ", " + (System.currentTimeMillis()+time) + ", '" + reason + "', true, " + moderator.getIdLong() + ", " + System.currentTimeMillis() + ")");




		ResultSet resultSet = Database.executeQuery("SELECT * FROM punishments WHERE active=true && userid=" + member.getIdLong() + " && casetype='" + CaseType.MUTE + "' && guildid=" + member.getGuild().getIdLong());
		try {
			if (resultSet.next()) {
				ModLogHandler.getInstance().log(resultSet.getInt("caseid"), moderator);
				return resultSet.getInt("caseid");
			}
			return 0;
		}catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	public int unmuteMember(Member member, Member moderator) {
		ResultSet resultSet = Database.executeQuery(
				"SELECT * FROM punishments WHERE active=true && userid=" + member.getIdLong() + " && casetype='" + CaseType.MUTE + "' && guildid=" + member.getGuild().getIdLong());
		Database.executeUpdate(
				"UPDATE punishments SET active=false WHERE active=true && userid=" + member.getIdLong() + " && casetype='" + CaseType.MUTE + "' && guildid=" + member.getGuild().getIdLong());

		member.getGuild().removeRoleFromMember(member, getMutedRole(member.getGuild())).queue();

		try {
			resultSet.first();
			ModLogHandler.getInstance().log(resultSet.getInt("caseid"), moderator);
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
			if (resultSet != null) {
				resultSet.next();
			}

			return resultSet.getInt(1) != 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


	public int banUser(Guild guild, User user, int minuteTime, String reason, Member moderator) {
		int time = minuteTime*60000;

		user.openPrivateChannel().complete().sendMessage(user.getAsMention()).setEmbeds(Embed.user(
				Color.RED,
				"You have been banned on " + guild.getName() + " " + (time != 0 ? "for " + minuteTime/60 + "hours!" : "permanently!") + " \nReason: "+ reason + "\nResponsible Moderator: " + moderator.getAsMention(), user
				)).queue();

		List<User> bannedUsers = new ArrayList<>();
		guild.retrieveBanList().complete().forEach(ban -> bannedUsers.add(ban.getUser()));

		if (!bannedUsers.contains(user))
			guild.ban(user, 1, reason).queue();

		Database.executeUpdate("INSERT INTO punishments(casetype, guildid, userid, expiryDate, reason, active, moderatorid, punishTime) " +
				"VALUES('" + CaseType.BAN + "', " + guild.getIdLong() + ", " + user.getIdLong() + ", " + (time != 0 ? (System.currentTimeMillis()+time) : "NULL") + ", '" + reason + "', true, " + moderator.getIdLong() + ", " + System.currentTimeMillis() + ")");

		ResultSet resultSet = Database.executeQuery("SELECT * FROM punishments WHERE userid=" + user.getIdLong() + " && casetype='" + CaseType.BAN +
				"' && guildid=" + guild.getIdLong() + " && reason='" + reason + "' && active=true" );
		try {
			resultSet.first();
			ModLogHandler.getInstance().log(resultSet.getInt("caseid"), moderator);
			return resultSet.getInt("caseid");
		}catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int unBanUser(User user, Guild guild, Member moderator) {

		System.out.println(user);
		System.out.println(guild);
		System.out.println(moderator);

		ResultSet resultSet = Database.executeQuery("SELECT * FROM punishments WHERE userid=" + user.getIdLong() + " && casetype='" + CaseType.BAN +
				"' && guildid=" + guild.getIdLong() + " && active=true");

		Database.executeUpdate("UPDATE punishments SET active=false WHERE active=true && userid=" + user.getIdLong() + " AND casetype='" + CaseType.MUTE + "' && guildid=" + guild.getIdLong());


		List<User> bannedUsers = new ArrayList<>();
		guild.retrieveBanList().complete().forEach(ban -> bannedUsers.add(ban.getUser()));

		if (bannedUsers.contains(user))
			guild.unban(user).queue();

		try {
			resultSet.first();
			ModLogHandler.getInstance().log(resultSet.getInt("caseid"), moderator);
			return resultSet.getInt("caseid");
		}catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	public int addWarning(Member member, String reason, Member moderator) {
		try {
			ResultSet resultSet = Database.executeQuery("SELECT COUNT(caseid) FROM punishments WHERE userid=" + member.getIdLong() + " && caseType='" + CaseType.WARNING + "'");

			resultSet.next();
			int warningCount = resultSet.getInt(1)+1;

			Database.executeUpdate("INSERT INTO punishments(casetype, guildid, userid, expiryDate, reason, active, moderatorid, punishTime)" +
					" VALUES('" + CaseType.WARNING + "', " + member.getGuild().getIdLong() + ", " + member.getIdLong() + ", NULL, '" + reason + "', NULL, " + moderator.getIdLong() + ", " + System.currentTimeMillis() + ")");


			PrivateChannel privateChannel = member.getUser().openPrivateChannel().complete();

			privateChannel.sendMessage(member.getUser().getAsMention()).setEmbeds(Embed.user(Color.RED,
					"You have been warned on " + member.getGuild().getName() +
							"\nReason: " + reason +
							"\nResponsible Moderator: " + moderator.getAsMention() +
							"\nYour Warning Count: " + warningCount,
					member.getUser())).queue();


			switch (warningCount) {
				case 3:
					bot.getPunishManager().muteMember(member, 120, "Automatic 3 Warnings", member.getGuild().getSelfMember());
					break;
				case 6:
					bot.getPunishManager().muteMember(member, 240, "Automatic 6 Warnings", member.getGuild().getSelfMember());
				case 9:
					bot.getPunishManager().banUser(member.getGuild(), member.getUser(), 240, "Automatic TempBan for 9 Warnings", member.getGuild().getSelfMember());
					break;
				case 12:
					bot.getPunishManager().banUser(member.getGuild(), member.getUser(), 0, "Automatic Ban for 12 Warnings", member.getGuild().getSelfMember());
					break;

			}



			ResultSet caseresultSet = Database.executeQuery(
					"SELECT * FROM punishments WHERE userid=" + member.getIdLong() + " && casetype='" + CaseType.WARNING +
							"' && guildid=" + member.getGuild().getIdLong() + " && reason='" + reason + "'");

			caseresultSet.first();
			ModLogHandler.getInstance().log(caseresultSet.getInt("caseid"), moderator);
			return caseresultSet.getInt("caseid");
		}catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
	}
	public List<Punishment> getPunishments(Member member) {
		List<Punishment> warnings = new ArrayList<>();
		try {
			ResultSet resultSet = Database.executeQuery("SELECT * FROM punishments WHERE userid=" + member.getIdLong() + " && guildid=" + member.getGuild().getIdLong());

			while (resultSet.next()) {
				warnings.add(Punishment.makePunishment(resultSet));
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

	public Role getMutedRole(Guild guild) {
		return guild.getRolesByName(mutedRoleName, true).get(0);
	}


}

