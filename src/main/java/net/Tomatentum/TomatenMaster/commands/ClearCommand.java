package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClearCommand extends SlashCommand {
	public ClearCommand() {
		super("clear", "Clears the specified amount of Messages", Permission.MESSAGE_MANAGE);

		getCommand().editCommand()
				.addOption(OptionType.INTEGER, "amount", "The amount of Messages to be deleted", true)
				.queue();

	}

	@Override
	public void execute(SlashCommandEvent command) {

		command.deferReply(true).queue();
		TextChannel channel = command.getTextChannel();
				List<Message> messages = getChannelHistory(channel, (int) command.getOption("amount").getAsLong()+1);
				int msgcount = messages.size()-1;
				channel.purgeMessages(messages);
				command.getHook().setEphemeral(true).sendMessage("```\nDeleted " + msgcount + " Messages\n```").queue();

	}
	public List<Message> getChannelHistory(TextChannel channel, int number) {
		List<Message> messages = new ArrayList<>();
		int counter = 0;
		for (Message msg : channel.getIterableHistory()) {
			if (!msg.isPinned()) {
				if (counter < number) {
					messages.add(msg);
					counter++;
				}
			}
		}
		return messages;

	}
}
