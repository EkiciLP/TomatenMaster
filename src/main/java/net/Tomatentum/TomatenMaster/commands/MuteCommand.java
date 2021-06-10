package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MuteCommand implements GuildCommand {
	private DiscordBot bot;
	public MuteCommand(DiscordBot bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		if (member.hasPermission(Permission.KICK_MEMBERS)) {
			Member target;
			Role role;
			if (channel.getGuild().getRolesByName("muted", true).size() == 1) {
				role = channel.getGuild().getRolesByName("muted", true).get(0);
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED);
				builder.setDescription("No Muted Role defined!\nThere has to be 1 Role named 'muted'");
				builder.setFooter(channel.getGuild().getName(), channel.getGuild().getIconUrl());
				channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);
				builder.clear();
				return;
			}

			try {
				target = msg.getMentionedMembers().get(0);
			}catch (IndexOutOfBoundsException e) {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!ban @member [time(minutes)]");
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				builder.clear();
				return;
			}
			if (args.length == 2) {
				if (!target.getRoles().contains(role)) {
					channel.getGuild().addRoleToMember(target, role).queue();
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.GREEN);
					builder.setDescription("🔇 Muted " + target.getAsMention());
					builder.setFooter(channel.getGuild().getName(), channel.getGuild().getIconUrl());
					channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);
					builder.clear();
				} else {
					channel.getGuild().removeRoleFromMember(target, role).queue();
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.GREEN);
					builder.setDescription("🔊 Unmuted " + target.getAsMention());
					builder.setFooter(channel.getGuild().getName(), channel.getGuild().getIconUrl());
					channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);
					builder.clear();
				}
			}else if (args.length == 3) {
				int time = Integer.parseInt(args[2])*60000;
				if (!target.getRoles().contains(role)) {
					channel.getGuild().addRoleToMember(target, role).queue();
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.GREEN);
					builder.setDescription("🔇 Muted " + target.getAsMention() + "\nMuted for " + args[2] + " minutes!");
					builder.setFooter(channel.getGuild().getName(), channel.getGuild().getIconUrl());
					channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);
					builder.clear();
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							channel.getGuild().removeRoleFromMember(target, role).queue();
						}
					}, time);
				} else {
					channel.getGuild().removeRoleFromMember(target, role).queue();
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.GREEN);
					builder.setDescription("🔊 Unmuted " + target.getAsMention());
					builder.setFooter(channel.getGuild().getName(), channel.getGuild().getIconUrl());
					channel.sendMessage(builder.build()).complete().delete().queueAfter(20, TimeUnit.SECONDS);
					builder.clear();
				}
			}else{
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!ban @member [time(minutes)]");
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				builder.clear();
			}
		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !warn");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}
	}
}
