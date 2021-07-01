package net.Tomatentum.TomatenMaster.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.music.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class SkipCommand implements GuildCommand {
	private GuildMusicManager musicManager;
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		musicManager = DiscordBot.getINSTANCE().getAudioManager().getGuildMusicManager(channel.getGuild());
		if (channel.getGuild().getAudioManager().isConnected() && !member.getVoiceState().getChannel().equals(channel.getGuild().getAudioManager().getConnectedChannel())) {
			channel.sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription("Bound to Channel: " + channel.getGuild().getAudioManager().getConnectedChannel().getName()).build()).queue();
			return;
		}
		EmbedBuilder builder = new EmbedBuilder();
		try {
			AudioTrack track = musicManager.getTrackScheduler().skip();
			builder.setColor(Color.GREEN);
			builder.setDescription("Skipped to track: " + track.getInfo().title + " [" + DiscordBot.getINSTANCE().getTimestamp(track.getDuration()) + "]");
		}catch (IllegalArgumentException e) {
			builder.setColor(Color.RED);
			builder.setDescription("Queue is Empty");
		}
		channel.sendMessage(builder.build()).queue();

	}
}
