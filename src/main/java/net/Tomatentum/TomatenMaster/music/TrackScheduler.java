package net.Tomatentum.TomatenMaster.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.Tomatentum.TomatenMaster.main.DiscordBot;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

	private Queue<AudioTrack> queue;
	private AudioPlayer player;
	private String currentTrack;
	private GuildMusicManager musicManager;
	private boolean repeating = false;

	public TrackScheduler(AudioPlayer player, GuildMusicManager musicManager) {
		this.musicManager = musicManager;
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
		this.currentTrack = "- None -";
	}

	public void queue(AudioTrack track) {
		if (currentTrack.equalsIgnoreCase("- None -")) {
			player.playTrack(track);
			currentTrack = track.getInfo().title;
		}else
			queue.offer(track);
	}
	public void clear() {
		queue.clear();
		currentTrack = "- None -";
		player.stopTrack();
	}

	public AudioTrack skip() throws IllegalArgumentException{
		if (queue.isEmpty()) {
			throw new IllegalArgumentException("Queue is empty");
		}else {
			AudioTrack audioTrack = queue.poll();
			player.playTrack(audioTrack);
			currentTrack = audioTrack.getInfo().title;
			return audioTrack;
		}
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			if (repeating) {
				player.playTrack(track.makeClone());
				return;
			}

			if (!queue.isEmpty()) {
				AudioTrack audioTrack = queue.poll();
				player.playTrack(audioTrack);
				currentTrack = audioTrack.getInfo().title;
			}else {
				musicManager.getGuild().getAudioManager().closeAudioConnection();
				musicManager.getGuild().getAudioManager().setSendingHandler(null);
				currentTrack = "- None -";
			}
		}
	}

	public String getQueueString() {
		StringBuilder stringBuilder = new StringBuilder();
		int count = 1;
		for (AudioTrack track : queue) {
			stringBuilder.append(count).append(": ").append(track.getInfo().title).append(" [").append(DiscordBot.getINSTANCE().getTimestamp(track.getDuration())).append("]\n");
			count++;
		}
		return stringBuilder.toString();
	}


	public void shuffle() {
		Collections.shuffle((List<?>) queue);
	}

	public String getCurrentTrack() {
		return currentTrack;
	}

	public boolean isRepeating() {
		return repeating;
	}

	public void setRepeating(boolean repeating) {
		this.repeating = repeating;
	}
}
