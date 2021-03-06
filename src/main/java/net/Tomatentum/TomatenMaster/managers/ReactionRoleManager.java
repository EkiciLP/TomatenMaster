package net.Tomatentum.TomatenMaster.managers;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bspfsystems.yamlconfiguration.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class ReactionRoleManager extends ListenerAdapter {
	private TomatenMaster bot;
	public ReactionRoleManager(TomatenMaster bot) {
		this.bot = bot;

	}



	public void addReactionRole(Role role, String emoji, Message message, MessageChannel channel) {
		bot.getConfig().getRrconfig().set(channel.getIdLong() + "." + message.getIdLong() + "." + emoji, role.getIdLong());
		bot.getConfig().save();
		message.addReaction(emoji).queue();
	}



	public void removeReactionRole(MessageChannel channel, Message message, String emoji) {
		bot.getConfig().getRrconfig().getConfigurationSection(channel.getIdLong() + "." + message.getIdLong()).getKeys(false).remove(emoji);
		bot.getConfig().save();
		message.clearReactions(emoji).queue();
	}


	@Override
	public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
		if (!event.getUser().isBot()) {

			ConfigurationSection section = bot.getConfig().getRrconfig().getConfigurationSection(event.getTextChannel().getIdLong() + "." + event.getMessageIdLong());

			HashMap<String, Role> roles = new HashMap<>();

			try {
				for (String key : section.getKeys(false)) {
					roles.put(key, event.getGuild().getRoleById(section.getLong(key)));
				}
			} catch (NullPointerException ignored) {
			}
			for (Map.Entry<String, Role> role : roles.entrySet()) {
				if (event.getReaction().getReactionEmote().getEmoji().equals(role.getKey())) {
					event.getGuild().addRoleToMember(event.getMember(), role.getValue()).queue();
				}
			}
		}
	}

	@Override
	public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
		if (!event.getUser().isBot()) {

			ConfigurationSection section = bot.getConfig().getRrconfig().getConfigurationSection(event.getTextChannel().getIdLong() + "." + event.getMessageIdLong());

			HashMap<String, Role> roles = new HashMap<>();

			try {
				for (String key : section.getKeys(false)) {
					roles.put(key, event.getGuild().getRoleById(section.getLong(key)));
				}
			} catch (NullPointerException ignored) {
			}
			for (Map.Entry<String, Role> role : roles.entrySet()) {
				if (event.getReaction().getReactionEmote().getEmoji().equals(role.getKey())) {
					event.getGuild().removeRoleFromMember(event.getMember(), role.getValue()).queue();
				}
			}
		}
	}
}
