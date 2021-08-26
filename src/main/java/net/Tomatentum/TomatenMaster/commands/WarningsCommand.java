package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.Embed;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.Map;

public class WarningsCommand extends SlashCommand {
	TomatenMaster bot;
	public WarningsCommand(TomatenMaster bot) {
		super("warnings", "Shows the warnings of the specified member", Permission.VOICE_MUTE_OTHERS);
		this.bot = bot;


		getCommand().editCommand()
				.addOption(OptionType.USER, "member", "The member you want to see the warnings of.", true)
				.queue();

	}

	@Override
	public void execute(SlashCommandEvent command) {
		command.deferReply(true).queue();
		Member target = command.getOption("member").getAsMember();


		StringBuilder sbuilder = new StringBuilder();
		for (Map.Entry<Integer, String> entry : bot.getPunishManager().getWarnings(target).entrySet()) {
			sbuilder.append("**Case " + entry.getKey() + " **: " + entry.getValue()).append("\n");
		}

		command.getHook().sendMessageEmbeds(Embed.user(Color.ORANGE, sbuilder.toString(), target.getUser())).queue();

	}

}
