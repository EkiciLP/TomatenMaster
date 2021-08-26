package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.TimeUnit;

public class CloseCommand implements GuildCommand {
	private TomatenMaster bot;
	private boolean confirmation = false;
	public CloseCommand(TomatenMaster bot) {
		this.bot = bot;
	}

	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		channel.sendTyping().complete();
		if (bot.getTicketManager().getOpenTickets().containsKey(channel)) {
			if (confirmation) {
				bot.getTicketManager().getOpenTickets().get(channel).close();
				EmbedBuilder builder = new EmbedBuilder();
				builder.setDescription("🔴 Ticket Closed");
				builder.setColor(0xfc0000);
				channel.sendMessage(builder.build()).complete().delete().queueAfter(5, TimeUnit.SECONDS);
				builder.clear();
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setTitle("Confirmation");
				builder.setColor(0xfc0307);
				builder.setDescription("Type !close again to confirm")	;
				channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				confirmation = true;
				builder.clear();
			}
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
