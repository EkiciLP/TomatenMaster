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
		Suggestion suggestion;
		try {
			suggestion = Suggestion.getSuggestionById((int) command.getOption("id").getAsLong());
		}catch (NullPointerException ex) {
			command.reply("❌ Invalid suggestion ID").setEphemeral(true).queue();
			return;
		}

		if (suggestion != null) {
			suggestion.approve();
			command.reply("✔ Suggestion ***" + command.getOption("id").getAsString() + "*** was approved!").setEphemeral(true).queue();
		}else {
			command.reply("❌ Invalid suggestion ID").setEphemeral(true).queue();

		}
	}
}
