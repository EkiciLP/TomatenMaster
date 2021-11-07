package net.Tomatentum.TomatenMaster.commands.ticket;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.concurrent.TimeUnit;


public class TicketRemoveCommand extends SlashCommand {
	private TomatenMaster bot;
	public TicketRemoveCommand(TomatenMaster bot) {
		super("remove", "Removes the Member from the Ticket", Permission.MANAGE_ROLES);
		this.bot = bot;

		getCommand().editCommand()
				.addOption(OptionType.USER, "member", "The Member to be removed", true)
				.queue();
	}
	@Override
	public void execute(SlashCommandEvent command) {
		if (bot.getTicketManager().getOpenTickets().containsKey(command.getTextChannel())) {
				Member target = command.getOption("member").getAsMember();

				if (bot.getTicketManager().getOpenTickets().get(command.getTextChannel()).getMembers().contains(target)) {
					bot.getTicketManager().getOpenTickets().get(command.getTextChannel()).removeMember(target);
					command.reply("✔ " + target.getAsMention() + " removed!").setEphemeral(true).queue();

				}else {
					command.reply("❌ " + target.getAsMention() + " already added!").setEphemeral(true).queue();
				}
		}else {
			command.reply("❌ Not available here!").setEphemeral(true).queue();
		}
	}
}
