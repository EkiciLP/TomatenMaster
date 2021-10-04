package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class CreateEmbedCommand extends SlashCommand {
	private TomatenMaster bot;
	public CreateEmbedCommand(TomatenMaster bot) {
		super("createembed", "Create a new embed.", Permission.MESSAGE_MANAGE);
		this.bot = bot;

		getCommand().editCommand()
				.addOption(OptionType.CHANNEL, "channel", "The MessageChannel in which the embed should be sent to.", true)
				.addOption(OptionType.STRING, "title", "The title of the embed.", true)

				.queue();
	}

	@Override
	public void execute(SlashCommandEvent command) {
			/*
			arg1 = channel
			arg2 = Title
			 */

		MessageChannel channel;

		try {
			channel = command.getOption("channel").getAsMessageChannel();
		}catch (NullPointerException ex) {
			command.reply("❌ Please specify a message channel.").queue();
			return;
		}

		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle(command.getOption("title").getAsString());
		bot.getEmbedManager().createEmbed(builder, (TextChannel) channel);
		command.reply("✍ Embed created!").setEphemeral(true).queue();
	}
}
