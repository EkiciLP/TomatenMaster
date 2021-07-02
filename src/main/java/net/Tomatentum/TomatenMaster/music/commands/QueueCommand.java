package net.Tomatentum.TomatenMaster.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.music.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class QueueCommand implements GuildCommand {
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		GuildMusicManager musicManager = DiscordBot.getINSTANCE().getAudioManager().getGuildMusicManager(channel.getGuild());
		AudioTrack currentTrack = musicManager.getTrackScheduler().getCurrentTrack();
		try {
			String currentTimestamp = " [" + DiscordBot.getTimestamp(musicManager.getPlayer().getPlayingTrack().getPosition()) + "/" + DiscordBot.getTimestamp(currentTrack.getDuration()) + "] ";

			channel.sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Current Track").setDescription(currentTrack.getInfo().title + currentTimestamp)
					.addField("Queue", musicManager.getTrackScheduler().getQueueString(), false).build()).queue();
		}catch (NullPointerException e) {
			channel.sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setTitle("Current Track").setDescription("No Track currently Playing")
					.addField("Queue", musicManager.getTrackScheduler().getQueueString(), false).build()).queue();
		}

	}
}
