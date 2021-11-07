package net.Tomatentum.TomatenMaster.commands.ticket;

import net.Tomatentum.TomatenMaster.managers.Ticket;
import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.concurrent.TimeUnit;

public class ReOpenCommand extends SlashCommand {
	private TomatenMaster bot;
	public ReOpenCommand(TomatenMaster bot) {
		super("reopen", "Reopens the current ticket.", Permission.MANAGE_CHANNEL);
		this.bot = bot;
	}
	@Override
	public void execute(SlashCommandEvent command) {
		if (bot.getTicketManager().getClosedTickets().containsKey(command.getTextChannel())) {
			Ticket ticket = bot.getTicketManager().getClosedTickets().get(command.getTextChannel());
			ticket.reOpen();

			command.reply("✔ Ticket-" + ticket.getID() + " has been reopened!").queue();
		}else {
			command.reply("❌ Not available here!").setEphemeral(true).queue();
		}
	}
}
