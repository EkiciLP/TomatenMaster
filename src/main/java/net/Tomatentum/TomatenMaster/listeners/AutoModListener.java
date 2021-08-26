package net.Tomatentum.TomatenMaster.listeners;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutoModListener extends ListenerAdapter {
	private List<String> badwordlist;
	private HashMap<Member, Long> spamlist;
	private HashMap<Member, Integer> spammsgcount;
	private TomatenMaster bot;
	public AutoModListener(TomatenMaster bot) {
		this.bot = bot;
		spamlist = new HashMap<>();
		badwordlist = new ArrayList<>();
		spammsgcount = new HashMap<>();
	}
	@Override
	public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
		if (event.getMessage().getContentStripped().contains("discord.gg")) {
			event.getMessage().delete().queue();
		}
		for (String word : badwordlist) {
			if (event.getMessage().getContentDisplay().toLowerCase().contains(word)) {
				event.getMessage().delete().queue();
				bot.getPunishManager().addWarning(event.getMember(),"Bad word: " + word);
			}
		}
		if (spamlist.containsKey(event.getMember())) {
			if (!event.getAuthor().isBot()) {
				if (spamlist.get(event.getMember()) >= System.currentTimeMillis() - 600) {
					if (spammsgcount.get(event.getMember()) != null) {
						spammsgcount.put(event.getMember(), spammsgcount.get(event.getMember()) + 1);
					}else {
						spammsgcount.put(event.getMember(), 1);
					}
					event.getMessage().delete().queue();
					spamlist.remove(event.getMember());
					if (spammsgcount.get(event.getMember()) == 3) {
						bot.getPunishManager().addWarning(event.getMember(), "Spam (Automatic)");
						spammsgcount.remove(event.getMember());
					}
				}else {
					spamlist.put(event.getMember(), System.currentTimeMillis());
				}
			}
		}else {
			spamlist.put(event.getMember(), System.currentTimeMillis());
		}
	}
}
