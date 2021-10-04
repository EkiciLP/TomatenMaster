package net.Tomatentum.TomatenMaster.managers;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.Embed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class ModLogHandler {


	private static final ModLogHandler instance = new ModLogHandler();
	private final TomatenMaster bot = TomatenMaster.getINSTANCE();
	private final TextChannel logChannel = bot.getProtocolChannel();

	public void log(int caseid, Member moderator) {
		Punishment punishment = bot.getPunishManager().getPunishment(caseid);
		LogType logType;

		if (punishment.getCaseType().equals(CaseType.BAN)) {
			if (punishment.isActive()) {
				logType = LogType.BAN;
			}else
				logType = LogType.UNBAN;
		}else if (punishment.getCaseType().equals(CaseType.MUTE)){
			if (punishment.isActive()) {
				logType = LogType.MUTE;
			}else
				logType = LogType.UNMUTE;
		}else {
			logType = LogType.WARN;
		}

		String finalText = logType.getText().replace("{@target}", punishment.getUser().getAsMention()).replace("{@mod}", moderator.getAsMention()).replace("{reason}", punishment.getReason()).replace("{caseid}", String.valueOf(punishment.getCaseId()));
		MessageEmbed embed = Embed.log(logType.getColor(), finalText, punishment.getUser());

		logChannel.sendMessageEmbeds(embed).queue();

	}

	public static ModLogHandler getInstance() {
		return instance;
	}
}

enum LogType {
	BAN(Color.RED, "{@target} has been banned by {@mod}\nwith reason {reason} in Case {caseid}"),
	UNBAN(Color.GREEN, "{@target} has been unbanned by {@mod}\nwith reason {reason} in Case {caseid}"),
	MUTE(Color.BLACK, "{@target} has been muted by {@mod}\nwith reason {reason} in Case {caseid}"),
	UNMUTE(Color.GRAY, "{@target} has been unmuted by {@mod}\nwith reason {reason} in Case {caseid}"),
	WARN(Color.ORANGE, "{@target} has been warned by {@mod}\nwith reason {reason} in Case {caseid}");

	private Color color;
	private String text;

	LogType(Color color, String text) {
		this.color = color;
		this.text = text;
	}

	String getText() {
		return text;
	}

	Color getColor() {
		return color;
	}


}


