package net.Tomatentum.TomatenMaster.commands;


import net.Tomatentum.TomatenMaster.util.Embed;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;


public class WarnCommand extends SlashCommand {
	private TomatenMaster bot;
	public WarnCommand(TomatenMaster bot) {
		super("warn", "Warns the specified Member", Permission.VOICE_MUTE_OTHERS);
		this.bot = bot;


		getCommand().editCommand()
				.addOption(OptionType.USER, "member", "The Member to warn", true)
				.addOption(OptionType.STRING, "reason", "Why you want to mute the member", true)
				.queue();

	}
	@Override
	public void execute(SlashCommandEvent command) {
		command.deferReply(true).queue();
		Member target = command.getOption("member").getAsMember();
		int caseid = bot.getPunishManager().addWarning(target, command.getOption("reason").getAsString(), command.getMember());

		command.getHook().sendMessageEmbeds(Embed.simple(
						Color.GREEN,
						"✅ **Case** " + caseid + " closed!\n**" + target.getAsMention() + "** warned with reason " + command.getOption("reason").getAsString()))
				.setEphemeral(true).queue();


	}
}
