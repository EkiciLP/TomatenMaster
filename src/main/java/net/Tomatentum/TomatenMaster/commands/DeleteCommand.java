package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class DeleteCommand extends SlashCommand {


	public DeleteCommand() {
		super("delete", "Deletes the current ticket", Permission.MANAGE_CHANNEL);
	}

	@Override
	public void execute(SlashCommandEvent command) {
		if (command.getTextChannel().getName().contains("ticket-")) {
			command.reply("⚠ Deleting...").setEphemeral(true).queue();
			command.getTextChannel().delete().queue();
		}else {
			command.reply("❌ Not available here").setEphemeral(true).queue();
		}
	}
}
