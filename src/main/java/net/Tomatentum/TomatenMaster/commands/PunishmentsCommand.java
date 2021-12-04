package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.punishments.CaseType;
import net.Tomatentum.TomatenMaster.punishments.Punishment;
import net.Tomatentum.TomatenMaster.util.Embed;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PunishmentsCommand extends SlashCommand {
	TomatenMaster bot;
	public PunishmentsCommand(TomatenMaster bot) {
		super("punishments", "Shows the punishments of the specified member", Permission.VOICE_MUTE_OTHERS);
		this.bot = bot;


		getCommand().editCommand()
				.addOption(OptionType.USER, "member", "The member you want to see the warnings of.", true)
				.queue();

	}

	@Override
	public void execute(SlashCommandEvent command) {
		command.deferReply(true).queue();
		Member target = command.getOption("member").getAsMember();


		List<MessageEmbed.Field> fields = new ArrayList<>();
		for (Punishment punishment : bot.getPunishManager().getPunishments(target)) {
			fields.add(new MessageEmbed.Field(
					"**Case " + punishment.getCaseId() + " **",
							"\n  Type: " + punishment.getCaseType() +
							"\n  Active: " + (punishment.isActive() ? "✅" : "❌") +
							"\n  Reason: " + punishment.getReason() +
							"\n  Moderator: " + punishment.getModerator().getAsMention() +
							"\n  punishTime: <t:" + (punishment.getPunishTime()/1000) + ":F>" +
							(punishment.getCaseType() != CaseType.WARNING ? "\n  endTime: <t:" + (punishment.getEndTime()/1000) + ":F>" : "") +
							"\n"
					, false

			));
		}

		command.getHook().sendMessageEmbeds(Embed.fieldEmbed("Warnings", null, target.getUser(), fields)).queue();

	}

}
