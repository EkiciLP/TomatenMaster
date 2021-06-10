package net.Tomatentum.TomatenMaster.manager;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;


public class ReactionRoleManager extends ListenerAdapter {
	private DiscordBot bot;
	private HashMap<Long, HashMap<String , Long>> messageEmojiMap;
	private List<Long> messagelist;
	public ReactionRoleManager(DiscordBot bot) {
		this.bot = bot;
		messageEmojiMap = new HashMap<>();
		messagelist = bot.getConfig().getYML().getLongList("ReactionRole.messagelist");
		for (Long id : messagelist) {
			HashMap<String, Long> tempmap = new HashMap<>();
				for (String str : bot.getConfig().getRrconfig().getKeys(true)) {
					long p = bot.getConfig().getRrconfig().getLong(str);
					tempmap.put(str, p);
				}

			messageEmojiMap.put(id, tempmap);
			System.out.println("added rr");
		}
	}



	public void addReactionRole(Role role, String emoji, Message message) {
		bot.getConfig().getRrconfig().set(emoji, role.getIdLong());
		if (!messagelist.contains(message.getIdLong())) {
			messagelist.add(message.getIdLong());
		}
		bot.getConfig().getYML().set("ReactionRole.messagelist", messagelist);
		bot.getConfig().save();
		HashMap<String, Long> tempmap = new HashMap<>();
		for (String str : bot.getConfig().getRrconfig().getKeys(true)) {
			long p = bot.getConfig().getRrconfig().getLong(str);
			tempmap.put(str, p);
		}
		messageEmojiMap.put(message.getIdLong(), tempmap);
		message.addReaction(emoji).queue();
	}



	public void removeReactionRole(Message message, String emoji) {
		bot.getConfig().getRrconfig().set(emoji, null);
		bot.getConfig().save();
		HashMap<String, Long> tempmap = new HashMap<>();
		for (String str : bot.getConfig().getRrconfig().getKeys(true)) {
			long p = bot.getConfig().getRrconfig().getLong(str);
			tempmap.put(str, p);
		}
		messageEmojiMap.put(message.getIdLong(), tempmap);
		message.clearReactions(emoji).queue();
	}


	@Override
	public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
		if (!event.getUser().isBot()) {
			if (bot.getConfig().getYML().getLongList("ReactionRole.messagelist").contains(event.getMessageIdLong())) {
				System.out.println("contains message id");
				if (messageEmojiMap.get(event.getMessageIdLong()).containsKey(event.getReaction().getReactionEmote().getEmoji())) {
					System.out.println("contains emoji");
					System.out.println(messageEmojiMap.get(event.getMessageIdLong()).get(event.getReaction().getReactionEmote().getEmoji()));
					event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(messageEmojiMap.get(event.getMessageIdLong()).get(event.getReaction().getReactionEmote().getEmoji()))).queue();
				} else
					event.getReaction().removeReaction(event.getUser()).queue();
			}
		}
	}

	@Override
	public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
			if (messageEmojiMap.containsKey(event.getMessageIdLong())) {
				if (messageEmojiMap.get(event.getMessageIdLong()).containsKey(event.getReaction().getReactionEmote().getEmoji())) {
					event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(messageEmojiMap.get(event.getMessageIdLong()).get(event.getReaction().getReactionEmote().getEmoji()))).queue();
				}
			}
	}
}
