package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.Embed;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;


public class ClearWarningsCommand extends SlashCommand {


	public ClearWarningsCommand() {
		super("clearwarnings", "Clears the Warnings of the specified Member", Permission.MANAGE_SERVER);

		getCommand().editCommand().addOption(OptionType.USER, "member", "The member to clear the warnings of", true)
				.queue();
	}

	@Override
	protected void execute(SlashCommandEvent command) {
		command.deferReply(true).queue();

		Member target = command.getOption("member").getAsMember();

		TomatenMaster.getINSTANCE().getPunishManager().clearWarnings(target);

		command.getHook().setEphemeral(true)
				.sendMessageEmbeds(Embed.simple(Color.GREEN, target.getAsMention() + "'s warnings have been cleared!"))
				.queue();
	}
}
