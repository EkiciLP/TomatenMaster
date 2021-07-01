package net.Tomatentum.TomatenMaster.managers;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.HashMap;


public class WarningManager {
	private DiscordBot bot;
	public WarningManager(DiscordBot bot) {
		this.bot = bot;
	}
	public void addWarning(Member member, TextChannel channel, String reason) {
		bot.getConfig().getYML().set("Warnings." + member.getIdLong() + ".count", bot.getConfig().getYML().getInt("Warnings." + member.getIdLong() + ".count")+1);
		bot.getConfig().save();
		bot.getConfig().getYML().set("Warnings." + member.getIdLong() + "." + (bot.getConfig().getYML().getInt("Warnings." + member.getIdLong() + ".count")),reason);
		bot.getConfig().save();
		switch (bot.getConfig().getYML().getInt("Warnings." + member.getIdLong() + ".count")) {
			case 3:
				bot.getPunishManager().muteMember(member.getGuild(), member, 120, "Automatic 3 Warnings");
			break;
			case 6:
				bot.getPunishManager().muteMember(member.getGuild(), member, 240, "Automatic 6 Warnings");
			case 9:
				bot.getPunishManager().banMember(member, 240, "Automatic TempBan for 9 Warnings");
				break;
			case 12:
				bot.getPunishManager().banMember(member, 241920, "Automatic Ban for 12 Warnings");
				break;
			default:
				EmbedBuilder ebuilder = new EmbedBuilder();
				ebuilder.setColor(Color.GREEN);
				ebuilder.setTimestamp(OffsetDateTime.now());
				ebuilder.setDescription("⚠" + member.getAsMention() + " warned! Warning: " + bot.getConfig().getYML().getInt("Warnings." + member.getIdLong() + ".count") + "\nReason: "+ reason);
				ebuilder.setThumbnail(channel.getGuild().getIconUrl());
				channel.sendMessage(ebuilder.build()).queue();
				ebuilder.clear();
				break;
		}
	}
	public HashMap<Integer, String> getWarnings(Member member) {
		HashMap<Integer, String> warnings = new HashMap<>();
		for (int i = 0; i <= bot.getConfig().getYML().getInt("Warnings." + member.getIdLong() + ".count"); i++) {
			if (i > 0) {
				warnings.put(i, bot.getConfig().getYML().getString("Warnings." + member.getIdLong() + "." + i));
			}
		}
		return warnings;
	}
	public void clearWarnings(Member member) {
		bot.getConfig().getYML().set("Warnings." + member.getIdLong() + ".count", 0);
		for (int i = 1; i<=bot.getConfig().getYML().getInt("Warnings." + member.getIdLong() + ".count");i++) {
			bot.getConfig().getYML().set("Warnings." + member.getIdLong() + "." + i, null);
			bot.getConfig().save();
		}
		bot.getConfig().save();
	}
}
