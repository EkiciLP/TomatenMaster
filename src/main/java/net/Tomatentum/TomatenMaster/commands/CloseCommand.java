package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class CloseCommand extends SlashCommand {
	private TomatenMaster bot;
	public CloseCommand(TomatenMaster bot) {
		super("close", "Closes the current Ticket.", Permission.MANAGE_CHANNEL);
		this.bot = bot;
	}

	@Override
	public void execute(SlashCommandEvent command) {

		TextChannel channel = command.getTextChannel();

		if (bot.getTicketManager().getOpenTickets().containsKey(channel)) {


			command.reply("Do you really want to Close this ticket?")
					.addActionRow(
					Button.success("closeyes", Emoji.fromUnicode("✔")),
							Button.danger("closeno", Emoji.fromUnicode("❌"))
							).queue();
		}else {
			command.reply("The Command is not available in this channel.\nIt is only available in tickets").setEphemeral(true).queue();
		}
	}

	@Override
	public void onButtonClick(@NotNull ButtonClickEvent event) {
		if (event.getComponentId().equals("closeyes")) {
			event.editMessage("✔ The Ticket has been closed").setActionRow(
					Button.success("closeyes", Emoji.fromUnicode("✔")).asDisabled(),
					Button.danger("closeno", Emoji.fromUnicode("❌")).asDisabled()
			).queue();

			bot.getTicketManager().getOpenTickets().get(event.getTextChannel()).close();
		}else if (event.getComponentId().equals("closeno")) {
			event.getMessage().delete().queue();
		}
	}
}
