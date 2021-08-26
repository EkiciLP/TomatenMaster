package net.Tomatentum.TomatenMaster.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.entities.Activity;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

	private BlockingQueue<AudioTrack> queue;
	private AudioPlayer player;
	private AudioTrack currentTrack;
	private GuildMusicManager musicManager;
	private boolean repeating = false;

	public TrackScheduler(AudioPlayer player, GuildMusicManager musicManager) {
		this.musicManager = musicManager;
		this.player = player;
		this.queue = new LinkedBlockingQueue<>();
		this.currentTrack = null;

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (currentTrack != null) {
					String track = player.getPlayingTrack().getInfo().title + " [" + TomatenMaster.getTimestamp(player.getPlayingTrack().getPosition()) + "/" + TomatenMaster.getTimestamp(player.getPlayingTrack().getDuration()) + "]";
					TomatenMaster.getINSTANCE().getBot().getPresence().setActivity(Activity.playing(track));
				}else
					TomatenMaster.getINSTANCE().getBot().getPresence().setActivity(null);
			}
		}, 6000, 6000);
	}

	public void queue(AudioTrack track) {
		if (currentTrack == null) {
			player.playTrack(track);
			currentTrack = track;
		}else
			queue.offer(track);
	}
	public void clear() {
		player.stopTrack();
		queue.clear();
		currentTrack = null;

	}

	public AudioTrack skip() throws IllegalArgumentException{
		if (queue.isEmpty()) {
			throw new IllegalArgumentException("Queue is empty");
		}else {
			AudioTrack audioTrack = queue.poll();
			player.playTrack(audioTrack);
			currentTrack = audioTrack;
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
				currentTrack = audioTrack;
			}else {
				musicManager.getGuild().getAudioManager().closeAudioConnection();
				musicManager.getGuild().getAudioManager().setSendingHandler(null);
				currentTrack = null;
			}
		}
	}

	public String getQueueString() {
		StringBuilder stringBuilder = new StringBuilder();
		int count = 1;
		for (AudioTrack track : queue) {
			stringBuilder.append(count).append(": ").append(track.getInfo().title).append(" [").append(TomatenMaster.getTimestamp(track.getDuration())).append("]\n");
			count++;
		}
		return stringBuilder.toString();
	}


	public void shuffle() {
		List<AudioTrack> audioTrackList = new ArrayList<>(queue);
		Collections.shuffle(audioTrackList);
		this.queue = new LinkedBlockingQueue<>(audioTrackList);
	}


	public AudioTrack getCurrentTrack() {
		return currentTrack;
	}

	public boolean isRepeating() {
		return repeating;
	}

	public void setRepeating(boolean repeating) {
		this.repeating = repeating;
	}
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
