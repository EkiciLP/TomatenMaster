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
import java.util.concurrent.TimeUnit;

public class AutoRoleCommand implements GuildCommand {
	private DiscordBot bot;
	public AutoRoleCommand(DiscordBot bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		if (member.hasPermission(Permission.MANAGE_SERVER)) {
			if (args.length == 2) {
				if (!args[1].equals("remove")) {
					Role role;
					try {
						role = msg.getMentionedRoles().get(0);
					}catch (IndexOutOfBoundsException e) {
						EmbedBuilder builder = new EmbedBuilder();
						builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!Autorole @role/remove");
						channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
						builder.clear();
						return;
					}
					bot.getConfig().getYML().set("AutoRole", role.getIdLong());
					channel.sendMessage("✔").complete().delete().queueAfter(10, TimeUnit.SECONDS);
				}else {
					bot.getConfig().getYML().set("AutoRole", null);
					channel.sendMessage("✔").complete().delete().queueAfter(10, TimeUnit.SECONDS);
				}
				bot.getConfig().save();
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!Autorole @role/remove");
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				builder.clear();
			}
		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !AutoRole");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}
	}
}
