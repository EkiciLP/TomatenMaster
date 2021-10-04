package net.Tomatentum.TomatenMaster.commands;

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

public class TicketAddCommand extends SlashCommand {
	private TomatenMaster bot;
	public TicketAddCommand(TomatenMaster bot) {
		super("add", "Adds a Member to the current Ticket.", Permission.MANAGE_ROLES);
		this.bot = bot;

		getCommand().editCommand()
				.addOption(OptionType.USER, "member", "The Member to be added", true)
				.queue();
	}
	@Override
	public void execute(SlashCommandEvent command) {
		if (bot.getTicketManager().getOpenTickets().containsKey(command.getTextChannel())) {
				Member target = command.getOption("member").getAsMember();
				TextChannel channel = command.getTextChannel();

				if (!bot.getTicketManager().getOpenTickets().get(channel).getMembers().contains(target)) {
					bot.getTicketManager().getOpenTickets().get(channel).addMember(target);
					command.reply("✔ " + target.getAsMention() + " added!").setEphemeral(true).queue();
				}else {
					command.reply("❌ " + target.getAsMention() + " already added!").setEphemeral(true).queue();
				}
		}else {
			command.reply("❌ Not available here!").setEphemeral(true).queue();;
		}
	}
}
