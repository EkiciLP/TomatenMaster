package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.managers.Ticket;
import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.TimeUnit;

public class ReOpenCommand implements GuildCommand {
	private DiscordBot bot;
	public ReOpenCommand(DiscordBot bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		channel.sendTyping().complete();
		if (bot.getTicketManager().getClosedTickets().containsKey(channel)) {
			Ticket ticket = bot.getTicketManager().getClosedTickets().get(channel);
			ticket.reOpen();
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Ticket-" + ticket.getID() + " Reopened");
			builder.setColor(0x03fc3d);
			builder.setDescription("This Ticket is now reopened and\nmembers can write again");
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
			builder.clear();
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
