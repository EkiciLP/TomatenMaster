package net.Tomatentum.TomatenMaster.commands;


import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

public class WarnCommand implements GuildCommand {
	private DiscordBot bot;
	public WarnCommand(DiscordBot bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		if (member.hasPermission(Permission.VOICE_MUTE_OTHERS)) {
			if (args.length >= 3) {
				Member tomute;
				try {
					tomute = msg.getMentionedMembers().get(0);
				}catch (IndexOutOfBoundsException e) {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!warn @member/clear <reason>");
					channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
					builder.clear();
					return;
				}
				if (!args[2].equals("clear")) {
					StringBuilder reason = new StringBuilder();
					for (int i = 2; i < args.length; i++) {
						reason.append(args[i]).append(" ");
					}
					bot.getWarningManager().addWarning(tomute, channel, reason.toString());
					msg.addReaction("✔").queue();
				}else {
					bot.getWarningManager().clearWarnings(tomute);
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.GREEN);
					builder.setTimestamp(OffsetDateTime.now());
					builder.setDescription(tomute.getAsMention() + "'s warnings clear!");
					builder.setThumbnail(channel.getGuild().getIconUrl());
					channel.sendMessage(builder.build()).queue();
					builder.clear();
					msg.addReaction("✔").queue();
				}
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!warn @member/clear <reason>");
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				builder.clear();
			}
		}else  {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !warn");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}

	}
}
