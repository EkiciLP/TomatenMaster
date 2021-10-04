package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.managers.Suggestion;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class SuggestCommand extends SlashCommand {


	public SuggestCommand() {
		super("suggest", "Suggest a Feature etc.", Permission.MESSAGE_WRITE);

		getCommand().editCommand()
				.addOption(OptionType.STRING, "suggestion", "The suggestion you want to suggest", true)

				.queue();
	}

	@Override
	public void execute(SlashCommandEvent command) {

		new Suggestion(command.getTextChannel(), command.getMember(), command.getOption("suggestion").getAsString());
		command.reply("✍ Suggestion created!").setEphemeral(true).queue();
	}
}
