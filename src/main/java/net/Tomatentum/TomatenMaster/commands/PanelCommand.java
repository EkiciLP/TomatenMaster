package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class PanelCommand implements GuildCommand {
	private TomatenMaster bot;
	public PanelCommand(TomatenMaster bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		channel.sendTyping().complete();
		if (member.hasPermission(Permission.MANAGE_SERVER)) {
			if (bot.getTicketManager().getPanel() != null) {
				bot.getTicketManager().getPanel().delete().queue();
			}

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("TomatenTum Support");
			builder.setColor(0x0328fc);
			builder.setDescription("Welcome to the TomatenTum Support Panel!\nReact with " +
					"\n" +
					"\uD83C\uDFAB \nto start a conversation with us!\nWe will help you as soon as we can!");
			builder.setFooter("- TomatenTum Staff Team", channel.getGuild().getIconUrl());
			Message message = channel.sendMessage(builder.build()).complete();
			message.addReaction("🎫").queue();
			bot.getTicketManager().setPanel(message);
			builder.clear();
		}
	}
}
