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

public class ReactionRoleCommand implements GuildCommand {
	private DiscordBot bot;
	public ReactionRoleCommand(DiscordBot bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		if (member.hasPermission(Permission.MANAGE_ROLES)) {
			/*
			 * args[1] = add/remove
			 * args[2] = #channel
			 * args[3] = messageID
			 * args[4] = Emoji
			 * args[5] = @role
			 */
			if (args.length >= 6) {
				TextChannel rchannel;
				Message message;
				String emoji;
				Role role;
				try {
					rchannel = msg.getMentionedChannels().get(0);
					message = rchannel.retrieveMessageById(args[3]).complete();
					emoji = args[4];
					role = msg.getMentionedRoles().get(0);
				}catch (IndexOutOfBoundsException e) {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!rr <add/remove> #channel <messageID> <Emoji> @role");
					channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
					builder.clear();
					return;
				}
				if (args[1].equals("add")) {
					bot.getReactionRole().addReactionRole(role, emoji, message);
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.GREEN);
					builder.setDescription("Added Emoji " + emoji + " with the Role " + role.getAsMention() + " on message: " + message.getIdLong());
					channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
					builder.clear();
				}else if (args[1].equals("remove")) {
					bot.getReactionRole().removeReactionRole(message, emoji);
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.RED);
					builder.setDescription("Removed Emoji " + emoji + " with the Role " + role.getAsMention() + " on message: " + message.getIdLong());
					channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
					builder.clear();
				}else {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!rr <add/remove> #channel <messageID> <Emoji> @role");
					channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
					builder.clear();
				}
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!rr <add/remove> #channel <messageID> <Emoji> @role");
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				builder.clear();
			}
		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !rr");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}
	}
}
