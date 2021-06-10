package net.Tomatentum.TomatenMaster.listeners;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceListener extends ListenerAdapter {
	private DiscordBot bot;

	public VoiceListener(DiscordBot bot) {
		this.bot = bot;
	}

	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
		VoiceChannel channel = e.getChannelJoined();
		if (channel.getIdLong() == 846726050938355732L) {
			if (!bot.getAutoVoiceChannelManager().addTempChannel(e.getChannelJoined().getParent(),e.getMember(), e.getGuild())) {
				e.getGuild().kickVoiceMember(e.getMember()).queue();
				e.getMember().getUser().openPrivateChannel().complete().sendMessage("***Max Sessions reached***").queue();
			}
		}
	}
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
		VoiceChannel channel = e.getChannelJoined();
		if (channel.getIdLong() == 846726050938355732L) {
			if (!bot.getAutoVoiceChannelManager().addTempChannel(e.getChannelJoined().getParent(),e.getMember(), e.getGuild())) {
				e.getGuild().kickVoiceMember(e.getMember()).queue();
				e.getMember().getUser().openPrivateChannel().complete().sendMessage("***Max Sessions reached***").queue();
			}
		}else if (bot.getAutoVoiceChannelManager().getTempChannels().contains(e.getChannelLeft().getIdLong()) || e.getChannelLeft().getName().contains("'s Room")) {
			if (e.getChannelLeft().getMembers().size() <= 0) {
				bot.getAutoVoiceChannelManager().removeTempChannel(e.getChannelLeft());
			}else {
				if (bot.getAutoVoiceChannelManager().getTempChannels().contains(e.getChannelLeft().getIdLong())) {
					bot.getAutoVoiceChannelManager().renameChannel(e.getChannelLeft(), e.getMember());
				}
			}
		}
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
		if (bot.getAutoVoiceChannelManager().getTempChannels().contains(e.getChannelLeft().getIdLong()) || e.getChannelLeft().getName().contains("'s Room")) {
			if (e.getChannelLeft().getMembers().size() <= 0) {
				bot.getAutoVoiceChannelManager().removeTempChannel(e.getChannelLeft());
			}else {
				if (bot.getAutoVoiceChannelManager().getTempChannels().contains(e.getChannelLeft().getIdLong())) {
					bot.getAutoVoiceChannelManager().renameChannel(e.getChannelLeft(), e.getMember());
				}
			}
		}
	}
}
