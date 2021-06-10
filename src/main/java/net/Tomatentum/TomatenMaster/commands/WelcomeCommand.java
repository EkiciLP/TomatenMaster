package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class WelcomeCommand implements GuildCommand {
	private DiscordBot bot;
	public WelcomeCommand(DiscordBot bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		if (member.hasPermission(Permission.MANAGE_SERVER)) {
			if (args.length == 2) {
				if (!args[1].equals("remove")) {
					TextChannel tc;
					try {
						tc = msg.getMentionedChannels().get(0);
					}catch (IndexOutOfBoundsException e) {
						EmbedBuilder builder = new EmbedBuilder();
						builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!welcome #channel/remove");
						channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
						builder.clear();
						return;
					}
					bot.getConfig().getYML().set("WelcomeChannel", tc.getIdLong());
					channel.sendMessage("✔").complete().delete().queueAfter(10, TimeUnit.SECONDS);
				}else {
					bot.getConfig().getYML().set("WelcomeChannel", null);
					channel.sendMessage("✔").complete().delete().queueAfter(10, TimeUnit.SECONDS);
				}
				bot.getConfig().save();
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!welcome #channel/remove");
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				builder.clear();
			}
		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !welcome");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}
	}
}
