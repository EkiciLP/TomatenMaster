package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.Embed;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;

public class BanCommand extends SlashCommand
{
	private TomatenMaster bot;
	public BanCommand(TomatenMaster bot)
	{
		super("ban", "Bans the specified member for the specified time.", Permission.BAN_MEMBERS);
		this.bot = bot;

		getCommand().editCommand()
				.addOption(OptionType.USER,"user", "The user you want to ban", true)
				.addOption(OptionType.STRING, "reason", "Why you want to ban this user.", true)
				.addOption(OptionType.INTEGER, "time", "The duration of the Ban in minutes.", true)
				.queue();
	}


	@Override
	public void execute(SlashCommandEvent command)
	{
		command.deferReply(true).queue();
		Member target = command.getOption("user").getAsMember();
		int time;

		if (target == null)
		{
			command.getHook().setEphemeral(true)
					.sendMessageEmbeds(Embed.simple(Color.RED, "The user is not a member of this guild")).queue();
			return;
		}


		int caseid = bot.getPunishManager().banUser(command.getGuild(), target.getUser(), (int) command.getOption("time").getAsLong(), command.getOption("reason").getAsString(), command.getMember());
		command.getHook().sendMessageEmbeds(
				Embed.user(Color.GREEN,"✅ **Case** " + caseid + " closed!\n" + target.getUser().getName() + " has been banned!", target.getUser()))
				.queue();

	}
}
