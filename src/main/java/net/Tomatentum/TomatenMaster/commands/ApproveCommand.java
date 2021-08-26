package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.managers.Suggestion;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class ApproveCommand extends SlashCommand {

	public ApproveCommand() {
		super("approve", "Approves the specified suggestion.", Permission.MESSAGE_MANAGE);
		getCommand().editCommand()
				.addOption(OptionType.INTEGER, "id", "The id of the suggestion", true)
				.queue();
	}
	@Override
	public void execute(SlashCommandEvent command) {
		command.deferReply(true).queue();
		Suggestion suggestion = Suggestion.getSuggestionById((int) command.getOption("id").getAsLong());
		if (suggestion != null) {
			suggestion.approve();
			command.getHook().sendMessage("✅ Suggestion **" + command.getOption("id").getAsString() + "** approved!").setEphemeral(true).queue();
		}else {
			command.getHook().sendMessage("❎ Suggestion **" + command.getOption("id").getAsString() + "** not existing!").setEphemeral(true).queue();

		}
	}
}
