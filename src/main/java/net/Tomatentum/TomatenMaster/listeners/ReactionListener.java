package net.Tomatentum.TomatenMaster.listeners;

import net.Tomatentum.TomatenMaster.managers.Ticket;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ReactionListener extends ListenerAdapter {
	private DiscordBot bot;

	public ReactionListener(DiscordBot bot) {
		this.bot = bot;
	}

	@Override
	public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
		if (event.getMember().getUser().isBot()) {return;}
		if (!event.getMember().getUser().isBot()) {
			if (event.getMessageIdLong() == bot.getTicketManager().getPanel().getIdLong()) {
				if (event.getReactionEmote().getEmoji().equals("🎫")) {
					if (!bot.getTicketManager().getOwnerlist().contains(event.getMember())) {
						new Ticket(event.getMember(), Objects.requireNonNull(event.getGuild().getCategoryById(835131959574921277L)), bot);
						event.getReaction().removeReaction(event.getUser()).queue();
					}else {
						event.getReaction().removeReaction(event.getUser()).queue();
						event.getMember().getUser().openPrivateChannel().complete().sendMessage("***You already have a ticket opened***").queue();
					}
				}
			}
			for (Ticket ticket : bot.getTicketManager().getOpenTickets().values()) {
				if (event.getMessageIdLong() == ticket.getMainMessage().getIdLong()) {
						event.getReaction().removeReaction(event.getUser()).queue();
						switch (event.getReactionEmote().getEmoji()) {
							case "🔒":
								if (event.getMember().getUser().isBot()) {return;}
								ticket.getMainMessage().clearReactions().complete();
								ticket.getMainMessage().addReaction("❌").queue();
								ticket.getMainMessage().addReaction("✔").queue();
								break;
							case "✔":
								if (event.getMember().getUser().isBot()) {return;}
								event.getReaction().removeReaction(event.getUser()).queue();
								ticket.close();
								break;
							case "❌":
								if (event.getMember().getUser().isBot()) {return;}
								event.getReaction().removeReaction(event.getUser()).queue();
								ticket.getMainMessage().clearReactions().queue();
								ticket.getMainMessage().addReaction("🔒").queue();

								break;
						}
					}
				}
			for (Ticket ticket : bot.getTicketManager().getClosedTickets().values()) {
				if (event.getMessageIdLong() == ticket.getMainMessage().getIdLong()) {
					if (event.getReactionEmote().getEmoji().equals("🔓")) {
						ticket.reOpen();
					}
				}
			}
		}
		if (!event.getMember().getUser().isBot()) {
			if (event.getTextChannel().getName().contains("ticket-")) {
				if (event.getReactionEmote().getEmoji().equals("🛑")) {
					event.getReaction().removeReaction().queue();
					if (event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
						event.getTextChannel().delete().queue();
					}
				}
			}
		}
	}
}

