package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class LockCommand implements GuildCommand {
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		if (member.hasPermission(Permission.MANAGE_CHANNEL)) {
			if (args.length == 1) {
				Role role = channel.getGuild().getRoleById(835089895092387872L);
				EmbedBuilder builder = new EmbedBuilder();

				if (!channel.getPermissionOverride(role).getDenied().contains(Permission.MESSAGE_WRITE)) {
					channel.putPermissionOverride(role).deny(Permission.MESSAGE_WRITE).queue();
					builder.setTitle("🔒 Channel Locked");
					builder.setColor(Color.RED);
					channel.sendMessage(builder.build()).queue();
				}else {
					channel.putPermissionOverride(role).grant(Permission.MESSAGE_WRITE).queue();
					builder.setTitle("🔓 Channel Unlocked");
					builder.setColor(Color.GREEN);
					channel.sendMessage(builder.build()).queue();
				}
			}
		}else {

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !lock");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}

	}
}
