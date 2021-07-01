package net.Tomatentum.TomatenMaster.music.commands;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.music.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class PlayCommand implements GuildCommand {
	private GuildMusicManager musicManager;

	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		musicManager = DiscordBot.getINSTANCE().getAudioManager().getGuildMusicManager(channel.getGuild());
		if (channel.getGuild().getAudioManager().isConnected() && !member.getVoiceState().getChannel().equals(channel.getGuild().getAudioManager().getConnectedChannel())) {
			channel.sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription("Already conneced to channel: " + channel.getGuild().getAudioManager().getConnectedChannel().getName()).build()).queue();
			return;
		}
		if (args.length == 2) {
			if (member.getVoiceState().inVoiceChannel()) {
				musicManager.connect(member.getVoiceState().getChannel());
				musicManager.loadAndQueue(channel, args[1]);
			}

		}

	}
}
