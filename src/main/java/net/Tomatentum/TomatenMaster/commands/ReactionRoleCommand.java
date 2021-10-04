package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class ReactionRoleCommand extends SlashCommand {
	private TomatenMaster bot;
	public ReactionRoleCommand(TomatenMaster bot) {
		super("rr", "Reaction role command for adding and removing reaction roles!", Permission.MANAGE_ROLES);
		this.bot = bot;

		getCommand().editCommand()
				.addSubcommands(
						new SubcommandData("add", "Adds a reaction role")
						.addOption(OptionType.CHANNEL, "channel", "The channel of the message",true)
						.addOption(OptionType.NUMBER, "messageid", "The MessageID of the message where the reaction role should be added.", true)
						.addOption(OptionType.STRING, "emoji", "The emoji of the reaction role", true)
						.addOption(OptionType.ROLE, "role", "The role of the reaction role", true),
						new SubcommandData("remove", "Removes a reaction role")
								.addOption(OptionType.CHANNEL, "channel", "The channel of the message",true)
								.addOption(OptionType.NUMBER, "messageid", "The MessageID of the message where the reaction role should be removed.", true)
								.addOption(OptionType.STRING, "emoji", "The emoji of the reaction role", true)

				).queue();
	}

	@Override
	public void execute(SlashCommandEvent command) {
			/*
			 * args[1] = add/remove
			 * args[2] = #channel
			 * args[3] = messageID
			 * args[4] = Emoji
			 * args[5] = @role
			 */

				MessageChannel channel = command.getOption("channel").getAsMessageChannel();
				Message message;
				String emoji = command.getOption("emoji").getAsString();

				try {
					message = channel.retrieveMessageById(command.getOption("messageid").getAsLong()).complete();
					Emoji.fromUnicode(emoji);
				}catch (NullPointerException | IllegalArgumentException ex) {
					command.reply("❌ Please specify correct parameters!").setEphemeral(true).queue();
					return;
				}

				if (command.getSubcommandName().equals("add")) {
					bot.getReactionRole().addReactionRole(command.getOption("role").getAsRole(), emoji, message, channel);
					command.reply("Added Emoji " + emoji + " with the Role " + command.getOption("role").getAsRole().getAsMention() + " on message: " + message.getIdLong()).setEphemeral(true).queue();

				}else if (command.getSubcommandName().equals("remove")) {
					bot.getReactionRole().removeReactionRole(channel, message, emoji);

					command.reply("Removed Emoji " + emoji + " on message: " + message.getIdLong()).setEphemeral(true).queue();

				}


	}
}
