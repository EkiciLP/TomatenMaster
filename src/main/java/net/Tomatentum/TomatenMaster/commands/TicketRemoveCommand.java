package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.concurrent.TimeUnit;


public class TicketRemoveCommand implements GuildCommand {
	private TomatenMaster bot;
	public TicketRemoveCommand(TomatenMaster bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		channel.sendTyping().complete();
		if (bot.getTicketManager().getOpenTickets().containsKey(channel)) {
			if (args.length > 1) {
				Member target = msg.getMentionedMembers().get(0);
				if (bot.getTicketManager().getOpenTickets().get(channel).getMembers().contains(target)) {
					bot.getTicketManager().getOpenTickets().get(channel).removeMember(target);

					EmbedBuilder builder = new EmbedBuilder();
					builder.setDescription("Member " + target.getAsMention() + " removed!");
					builder.setColor(0xff0000);
					channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
					builder.clear();
				}else {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setColor(0xff0000);
					builder.setDescription(target.getAsMention() + " is not in this Ticket!");
					channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
					builder.clear();
				}
			}
		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("Not available");
			builder.setColor(0xfc0307);
			builder.setDescription("The Command is not available in this channel.\nIt is only available in open tickets");
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
			builder.clear();
		}
	}
}
