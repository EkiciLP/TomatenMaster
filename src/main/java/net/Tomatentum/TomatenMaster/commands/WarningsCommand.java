package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

public class WarningsCommand implements GuildCommand {
	DiscordBot bot;
	public WarningsCommand(DiscordBot bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		if (args.length >= 2) {
			Member target;
			try {
				target = msg.getMentionedMembers().get(0);
			}catch (IndexOutOfBoundsException e) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!warnings @member");
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				builder.clear();
				return;
			}
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Color.CYAN);
			builder.setAuthor(target.getEffectiveName(), null, target.getUser().getAvatarUrl());
			builder.setTimestamp(OffsetDateTime.now());
			builder.setFooter(channel.getGuild().getName(), channel.getGuild().getIconUrl());
			StringBuilder sbuilder = new StringBuilder();
			for (int i = 1; i <= bot.getWarningManager().getWarnings(target).size(); i++) {
				if (bot.getWarningManager().getWarnings(target).get(i) != null) {
					sbuilder.append(i + ": " + bot.getWarningManager().getWarnings(target).get(i)).append("\n");
				}
			}
			builder.setDescription(sbuilder.toString());
			channel.sendMessage(builder.build()).complete().delete().queueAfter(60, TimeUnit.SECONDS);
			builder.clear();

		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!warnings @member");
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
			builder.clear();
		}
	}
}
