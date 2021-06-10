package net.Tomatentum.TomatenMaster.listeners;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class CommandListener extends ListenerAdapter {
	private DiscordBot bot;
	private Thread cmdthread;
	public CommandListener(DiscordBot bot)  {
		this.bot = bot;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		String msg = e.getMessage().getContentDisplay();
		if (e.isFromType(ChannelType.TEXT)) {
			TextChannel channel = e.getTextChannel();
			if (msg.startsWith("!")) {
				String[] args = (msg.substring(1).split(" "));
				if (!bot.getCmdmanager().runCommand(args[0], e.getMember(), channel, e.getMessage(), args)) {
					channel.sendMessage("```Unknown Command```").complete().delete().queueAfter(5, TimeUnit.SECONDS);
					e.getMessage().delete().queue();
				}
			}
		}
	}
}
