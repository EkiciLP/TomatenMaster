package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.managers.Ticket;
import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class NewTicketCommand implements GuildCommand {
	private TomatenMaster bot;
	public NewTicketCommand(TomatenMaster bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.addReaction("✔").queue();
		msg.delete().queueAfter(10, TimeUnit.SECONDS);
		new Ticket(member, Objects.requireNonNull(channel.getGuild().getCategoryById(835131959574921277L)), bot);
	}
}
