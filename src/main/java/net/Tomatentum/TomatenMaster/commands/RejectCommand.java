package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.manager.Suggestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class RejectCommand implements GuildCommand {
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		if (member.hasPermission(Permission.MESSAGE_MANAGE)) {
			if (args.length == 2) {
				try {
					Suggestion.getSuggestionById(Integer.parseInt(args[1])).reject();
				} catch (NumberFormatException e) {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!reject SuggestionID");
					channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
					builder.clear();
				}
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(Color.RED).setTitle("Invalid args").setDescription("!reject SuggestionID");
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				builder.clear();
			}
		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !reject");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}
	}
}
