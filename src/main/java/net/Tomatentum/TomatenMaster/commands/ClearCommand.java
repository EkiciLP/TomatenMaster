package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClearCommand implements GuildCommand {
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		channel.sendTyping().complete();
		msg.delete().queue();
		if (member.hasPermission(channel, Permission.MESSAGE_MANAGE)) {
			if (args.length == 2) {
				List<Message> messages = getChannelHistory(channel, Integer.parseInt(args[1]));
				int msgcount = messages.size();
				channel.purgeMessages(messages);
				channel.sendMessage("```apache\nDeleted " + msgcount + " Messages\n```").complete().delete().queueAfter(10, TimeUnit.SECONDS);
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setColor(0xfc0303);
				builder.setTitle("Wrong Usage");
				builder.setDescription("Usage >> !clear <count>");
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				builder.clear();
			}
		}else {

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !clear");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}
	}
	public List<Message> getChannelHistory(TextChannel channel, int number) {
		List<Message> messages = new ArrayList<>();
		int counter = 0;
		for (Message msg : channel.getIterableHistory().cache(false)) {
			if (!msg.isPinned()) {
				if (counter < number) {
					messages.add(msg);
					counter++;
				}
			}
		}
		return messages;

	}
}
