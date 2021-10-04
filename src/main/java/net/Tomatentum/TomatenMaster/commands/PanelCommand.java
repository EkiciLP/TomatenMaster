package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class PanelCommand extends SlashCommand {
	private TomatenMaster bot;
	public PanelCommand(TomatenMaster bot) {
		super("panel", "Spawn the support panel in the current channel", Permission.MESSAGE_MANAGE);
		this.bot = bot;
	}
	@Override
	public void execute(SlashCommandEvent command) {
			if (bot.getTicketManager().getPanel() != null) {
				bot.getTicketManager().getPanel().delete().queue();
			}
			command.reply("Done").setEphemeral(true).queue();

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("TomatenTum Support");
			builder.setColor(0x0328fc);
			builder.setDescription("Welcome to the TomatenTum Support Panel!\nReact with " +
					"\n" +
					"\uD83C\uDFAB \nto start a conversation with us!\nWe will help you as soon as we can!");
			builder.setFooter("- TomatenTum Staff Team", command.getGuild().getIconUrl());
			Message message = command.getChannel().sendMessageEmbeds(builder.build()).complete();
			message.addReaction("🎫").queue();
			bot.getTicketManager().setPanel(message);
			builder.clear();
	}
}
