package net.Tomatentum.TomatenMaster.manager;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.entities.*;

import java.io.IOException;
import java.util.*;

public class TicketManager {

	private Message panel;
	private DiscordBot bot;
	private HashMap<TextChannel, Ticket> openTickets;
	public List<Member> ownerlist;
	private HashMap<TextChannel, Ticket> closedTickets;
	public TicketManager(DiscordBot bot) {
		this.bot = bot;
		this.openTickets = new HashMap<>();
		this.closedTickets = new HashMap<>();
		this.ownerlist = new ArrayList<>();
		try {
			long channelid = bot.getConfig().getYML().getLong("PanelChannelId");
			long messageid = bot.getConfig().getYML().getLong("PanelMessageId");
			long guildid = bot.getConfig().getYML().getLong("PanelGuildId");
			Guild guild = bot.getBot().getGuildById(guildid);
			TextChannel channel = guild.getTextChannelById(channelid);
			Message msg = channel.retrieveMessageById(messageid).complete();

			if (guild == null || channel == null || msg == null) {
				return;
			}

			if (bot.getBot().getTextChannelById(channelid) != null) {
				panel = bot.getBot().getTextChannelById(channelid).retrieveMessageById(messageid).complete();

			}else
				System.out.println("No panel defined");

		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("[TicketSystem] initialized!");
	}
	//getters/setters


	public HashMap<TextChannel, Ticket> getOpenTickets() {
		return openTickets;
	}

	public HashMap<TextChannel, Ticket> getClosedTickets() {
		return closedTickets;
	}

	public Message getPanel() {
		return panel;
	}

	public List<Member> getOwnerlist() {
		return ownerlist;
	}

	public void setPanel(Message panel) {
		this.panel = panel;
		bot.getConfig().getYML().set("PanelMessageId",panel.getIdLong());
		bot.getConfig().getYML().set("PanelChannelId", panel.getChannel().getIdLong());
		bot.getConfig().getYML().set("PanelGuildId", panel.getGuild().getIdLong());
		try {
			bot.getConfig().getYML().save(bot.getConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


