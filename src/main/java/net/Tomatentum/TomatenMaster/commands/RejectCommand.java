package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.managers.Suggestion;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;


public class RejectCommand extends SlashCommand {

	public RejectCommand() {
		super("reject", "Rejects the specified", Permission.MESSAGE_MANAGE);

		getCommand().editCommand()
				.addOption(OptionType.INTEGER, "id", "The ID of the suggestion.", true)

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
						suggestion.reject();
						command.reply("✔ Suggestion ***" + suggestion.getID() + "*** was rejected!").setEphemeral(true).queue();
					}else {
						command.reply("❌ Invalid suggestion ID").setEphemeral(true).queue();
					}


	}
}
