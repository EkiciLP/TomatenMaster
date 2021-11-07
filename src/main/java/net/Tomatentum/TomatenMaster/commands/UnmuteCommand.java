package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.punishments.PunishManager;
import net.Tomatentum.TomatenMaster.util.Embed;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;

public class UnmuteCommand extends SlashCommand {

	private PunishManager punishManager;

	public UnmuteCommand() {
		super("unmute", "Unmutes the specified member", Permission.VOICE_MUTE_OTHERS);

		this.punishManager = TomatenMaster.getINSTANCE().getPunishManager();


		getCommand().editCommand().addOption(OptionType.USER, "member", "The member to unmute", true).queue();

	}

	@Override
	protected void execute(SlashCommandEvent command) {
		command.deferReply(true).queue();
		Member target = command.getOption("member").getAsMember();

		if (!punishManager.isMuted(target)) {
			command.getHook().sendMessageEmbeds(Embed.simple(Color.RED, target.getAsMention() + " is not muted!")).setEphemeral(true).queue();
			return;
		}

			int caseid = punishManager.unmuteMember(target, command.getMember());
			command.getHook().sendMessageEmbeds(Embed.simple(
							Color.GREEN,
							"✅ **Case** " + caseid + " updated!\n**" + target.getAsMention() + "** unmuted"))
					.setEphemeral(true).queue();



	}
}
