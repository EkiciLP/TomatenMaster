package net.Tomatentum.TomatenMaster.managers;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Suggestion {
	public static List<Suggestion> suggestionList;

	private Message message;
	private final TextChannel channel;
	private final TomatenMaster bot = TomatenMaster.getINSTANCE();
	private EmbedBuilder builder;
	private final int ID;

	public static Suggestion getSuggestionById(int ID) {
		if (ID < TomatenMaster.getINSTANCE().getConfig().getYML().getInt("Suggestions.nextID")) {
			return new Suggestion(ID);
		}

	return null;
	}

	private Suggestion(int ID) {
		this.ID = ID;
		this.channel = bot.getBot().getTextChannelById(bot.getConfig().getYML().getLong("Suggestions." + ID + ".channelid"));
		this.message = channel.retrieveMessageById(bot.getConfig().getYML().getLong("Suggestions." + ID + ".msgid")).complete();
		this.builder = new EmbedBuilder(this.message.getEmbeds().get(0));

	}



	public Suggestion(TextChannel channel, Member suggestor, String suggestion) {
		this.channel = channel;
		ID = bot.getConfig().getYML().getInt("Suggestions.nextID");

		builder = new EmbedBuilder();
		builder.setFooter("ID - " + ID, channel.getGuild().getIconUrl());
		builder.setTitle("Voting");
		builder.setAuthor(suggestor.getEffectiveName(), null, suggestor.getUser().getAvatarUrl());
		builder.setColor(Color.ORANGE);
		builder.setDescription(suggestion);
		builder.setTimestamp(OffsetDateTime.now());
		message = channel.sendMessage(builder.build()).complete();
		message.addReaction("✅").queue();
		message.addReaction("❌").queue();

		bot.getConfig().getYML().set("Suggestions." + ID + ".msgid", message.getIdLong());
		bot.getConfig().getYML().set("Suggestions." + ID + ".channelid", channel.getIdLong());
		bot.getConfig().getYML().set("Suggestions.nextID", ID+1);
		bot.getConfig().save();

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				message = channel.retrieveMessageById(bot.getConfig().getYML().getLong("Suggestions." + ID + ".msgid")).complete();

				int yescount = 0;
				int nocount = 0;

				for (MessageReaction reaction : message.getReactions()) {
					if (reaction.getReactionEmote().getEmoji().equals("✅")) {
						yescount = reaction.getCount()-1;
					}else if (reaction.getReactionEmote().getEmoji().equals("❌")) {
						nocount = reaction.getCount()-1;
					}
				}
				if (yescount <= nocount) {
					reject();
				}else
					approve();
			}
		}, 86400000);
	}

	public void approve() {
		int yescount = 0;
		int nocount = 0;

		for (MessageReaction reaction : message.getReactions()) {
			if (reaction.getReactionEmote().getEmoji().equals("✅")) {
				yescount = reaction.getCount()-1;
			}else if (reaction.getReactionEmote().getEmoji().equals("❌")) {
				nocount = reaction.getCount()-1;
			}
		}


		builder.setColor(Color.GREEN);
		builder.setTitle("Approved");
		builder.addField("Results", "✅ " + yescount + "\n❌ " + nocount, false);
		builder.setTimestamp(OffsetDateTime.now());
		message.editMessage(builder.build()).queue();
		message.clearReactions().queue();
	}

	public void reject() {
		int yescount = 0;
		int nocount = 0;

		for (MessageReaction reaction : message.getReactions()) {
			if (reaction.getReactionEmote().getEmoji().equals("✅")) {
				yescount = reaction.getCount()-1;
			}else if (reaction.getReactionEmote().getEmoji().equals("❌")) {
				nocount = reaction.getCount();
			}
		}


		builder.setColor(Color.RED);
		builder.setTitle("Rejected");
		builder.addField("Results", "✅ " + yescount + "\n❌ " + nocount, false);
		builder.setTimestamp(OffsetDateTime.now());
		message.editMessage(builder.build()).queue();
		message.clearReactions().queue();
	}








	public Message getMessage() {
		return message;
	}

	public TextChannel getChannel() {
		return channel;
	}


	public int getID() {
		return ID;
	}
}
