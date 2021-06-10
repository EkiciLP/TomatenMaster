package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class BanCommand implements GuildCommand {
	private DiscordBot bot;
	public BanCommand(DiscordBot bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		if (member.hasPermission(Permission.BAN_MEMBERS)) {
			User target;
			StringBuilder stringBuilder = new StringBuilder();
			int time = Integer.parseInt(args[2])*60000;
			for (int i = 3; i<args.length; i++) {
				stringBuilder.append(args[i]).append(" ");
			}
			try {
				target = msg.getMentionedMembers().get(0).getUser();
			}catch (IndexOutOfBoundsException e) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!ban @member <time(minutes)> <reason>");
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				builder.clear();
				return;
			}
			if (args.length == 4) {
				if (!channel.getGuild().retrieveBanList().complete().contains(channel.getGuild().retrieveBan(target).complete())) {
					bot.getPunishManager().banMember(member, Integer.parseInt(args[2]), stringBuilder.toString());
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.GREEN);
					builder.setDescription("❌ Banned " + target.getAsMention() + "\nBanned for " + args[2] + " minutes!");
					builder.setFooter(channel.getGuild().getName(), channel.getGuild().getIconUrl());
					channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);
					builder.clear();
				}else{
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.GREEN);
					builder.setDescription("✅ Unbanned " + target.getAsMention());
					builder.setFooter(channel.getGuild().getName(), channel.getGuild().getIconUrl());
					channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);
					builder.clear();
				}
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!ban @member <time(minutes)> <reason>");
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				builder.clear();
			}
		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !ban");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}
	}
}
