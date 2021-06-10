package net.Tomatentum.TomatenMaster.manager;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;

public class EmbedManager {
	private DiscordBot bot;


	public EmbedManager(DiscordBot bot) {
		this.bot = bot;
		System.out.println("[EmbedManager] initialized");
	}

	/**
	 *
	 * @param builder setting the footer will not work
	 * @param channel channel where the Embed should go
	 */
	public void createEmbed(EmbedBuilder builder, TextChannel channel) {
		int nextID = bot.getConfig().getYML().getInt("Embeds.nextID");
		builder.setFooter("- TomatenTum Staff Team | ID: " + nextID, channel.getGuild().getIconUrl());
		Message msg = channel.sendMessage(builder.build()).complete();
		builder.clear();
		bot.getConfig().getYML().set("Embeds." + nextID + ".ChannelID", channel.getIdLong());
		bot.getConfig().getYML().set("Embeds." + nextID + ".MessageID", msg.getIdLong());
		bot.getConfig().getYML().set("Embeds.nextID", nextID+1);
		try {
			bot.getConfig().getYML().save(bot.getConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[EmbedManager] created embed ID: " + nextID);

	}
	public boolean editEmbed(int ID, EmbedBuilder builder) {

		TextChannel EmbedChannel = bot.getBot().getTextChannelById(bot.getConfig().getYML().getLong("Embeds."+ID+".ChannelID"));
		if (EmbedChannel == null) {return false;}
		Message EmbedMessage = EmbedChannel.retrieveMessageById(bot.getConfig().getYML().getLong("Embeds."+ID+".MessageID")).complete();
		builder.setFooter("- TomatenTum Staff Team | ID: " + ID, EmbedChannel.getGuild().getIconUrl());
		EmbedMessage.editMessage(builder.build()).queue();
		builder.clear();
		return true;
	}
	public MessageEmbed getEmbed(int ID) {
		TextChannel channel = bot.getBot().getTextChannelById(bot.getConfig().getYML().getLong("Embeds." + ID + ".ChannelID"));
		Message message = channel.retrieveMessageById(bot.getConfig().getYML().getLong("Embeds." + ID + ".MessageID")).complete();
		return message.getEmbeds().get(0);
	}
}
