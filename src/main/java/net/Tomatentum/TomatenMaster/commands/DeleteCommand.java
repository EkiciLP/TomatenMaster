package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.TimeUnit;

public class DeleteCommand implements GuildCommand {
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().complete();
		channel.sendTyping().complete();
		if (channel.getName().contains("ticket-")) {
			channel.delete().queue();
		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Not available");
			builder.setColor(0xfc0307);
			builder.setDescription("The Command is not available in this channel.\nIt is only available in tickets");
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
			builder.clear();
		}
	}
}
