package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.Embed;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;


public class MuteCommand extends SlashCommand {
	private TomatenMaster bot;
	public MuteCommand(TomatenMaster bot) {
		super("mute", "Mutes the specified member for the specified time in minutes.", Permission.VOICE_MUTE_OTHERS);
		this.bot = bot;


		getCommand().editCommand()
				.addOption( OptionType.USER, "member", "The member you want to mute.", true)
				.addOption(OptionType.STRING, "reason", "The reason why you want to mute the member", true)
				.addOption(OptionType.INTEGER, "time", "The time in minutes you want to mute the member.", false)
				.queue();

	}


	@Override
	public void execute(SlashCommandEvent command) {
		command.deferReply(true).queue();

		Member target = command.getOption("member").getAsMember();

		if (bot.getPunishManager().isMuted(target)) {
			command.getHook().sendMessageEmbeds(Embed.simple(Color.RED, target.getAsMention() + " is already muted!")).setEphemeral(true).queue();
			return;
		}


		if (command.getOption("time") == null) {
				int caseid = bot.getPunishManager().muteMember(target, 30, /*TODO*/command.getOption("reason").getAsString());

				command.getHook().sendMessageEmbeds(Embed.simple(
								Color.GREEN,
								"✅ **Case** " + caseid + " closed!\n**" + target.getAsMention() + "** muted for **30** minutes"))
						.setEphemeral(true).queue();


		}else {
					int time = Integer.parseInt(command.getOption("time").getAsString());
						int caseid = bot.getPunishManager().muteMember(target, time, command.getOption("reason").getAsString());

						command.getHook().sendMessageEmbeds(Embed.simple(
										Color.GREEN,
										"✅ **Case** " + caseid + " closed!\n**" + target.getAsMention() + "** muted for **" + time + "** minutes"))
								.setEphemeral(true).queue();


				}

	}
}
