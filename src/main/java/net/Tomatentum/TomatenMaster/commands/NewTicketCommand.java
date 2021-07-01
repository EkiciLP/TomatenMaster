package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.managers.Ticket;
import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class NewTicketCommand implements GuildCommand {
	private DiscordBot bot;
	public NewTicketCommand(DiscordBot bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.addReaction("✔").queue();
		msg.delete().queueAfter(10, TimeUnit.SECONDS);
		new Ticket(member, Objects.requireNonNull(channel.getGuild().getCategoryById(835131959574921277L)), bot);
	}
}
