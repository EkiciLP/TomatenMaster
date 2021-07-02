package net.Tomatentum.TomatenMaster.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.Tomatentum.TomatenMaster.music.TrackScheduler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class GuildMusicManager {

	private AudioPlayer player;
	private Guild guild;
	private TrackScheduler trackScheduler;
	private AudioPlayerSendHandler sendHandler;
	private AudioPlayerManager playerManager;
	private int DEFAULT_VOLUME = 35;
	private TextChannel currentTextChannel;


	public GuildMusicManager(AudioPlayerManager playerManager, Guild guild) {
		this.guild = guild;
		this.player = playerManager.createPlayer();
		this.trackScheduler = new TrackScheduler(player, this);
		this.playerManager = playerManager;
		this.sendHandler = new AudioPlayerSendHandler(player);
		player.setVolume(DEFAULT_VOLUME);

		player.addListener(trackScheduler);

		startCleanupLoop(20000);
	}

	public void connect(VoiceChannel channel, TextChannel commandChannel) {
		if (!guild.getAudioManager().isConnected())
			commandChannel.sendMessage("🔗 Connected to ```" + channel.getName() + "``` and bound to " + commandChannel.getAsMention()).queue();

		this.guild.getAudioManager().openAudioConnection(channel);
		this.guild.getAudioManager().setSendingHandler(sendHandler);
		this.currentTextChannel = commandChannel;
	}
	public void quit() {
		guild.getAudioManager().closeAudioConnection();
		guild.getAudioManager().setSendingHandler(null);
		trackScheduler.clear();
		currentTextChannel = null;
	}



	public boolean isPermitted(VoiceChannel vc, TextChannel tc) {
		if (guild.getAudioManager().isConnected()) {
			if (tc.equals(currentTextChannel) && vc.equals(guild.getAudioManager().getConnectedChannel())) {
				return true;
			}else {
				tc.sendMessage("Bot bound to ```" + guild.getAudioManager().getConnectedChannel().getName() + "``` and to " + currentTextChannel.getAsMention()).queue();
				return false;
			}
		}else
			return true;
	}

	public void loadAndQueue(TextChannel commandchannel, String URL) {
		String trackURL;
		if (URL.startsWith("<") && URL.endsWith(">")) {
			trackURL = URL.substring(1, URL.length()-1);
		}else
			trackURL = URL;
		commandchannel.sendMessage("🔎 Searching: " + trackURL).queue();
		this.playerManager.loadItem(trackURL, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack audioTrack) {
				if (trackScheduler.isEmpty()) {
					commandchannel.sendMessage("▶ Now Playing ``" + audioTrack.getInfo().title + "``").queue();
				}else
					commandchannel.sendMessage("▶ Added to Queue: ``" + audioTrack.getInfo().title + "``").queue();


				trackScheduler.queue(audioTrack);
			}

			@Override
			public void playlistLoaded(AudioPlaylist audioPlaylist) {
				if (trackScheduler.isEmpty()) {
					commandchannel.sendMessage("▶ Now Playing Playlist: ``" + audioPlaylist.getName() + "``").queue();
				}else
					commandchannel.sendMessage("▶ Playlist added to Queue: ``" + audioPlaylist.getName() + "``").queue();

				audioPlaylist.getTracks().forEach(trackScheduler::queue);

			}

			@Override
			public void noMatches() {
				playerManager.loadItem("ytsearch:" + trackURL, new AudioLoadResultHandler() {
					@Override
					public void trackLoaded(AudioTrack audioTrack) {
						if (trackScheduler.isEmpty()) {
							commandchannel.sendMessage("▶ Now Playing ``" + audioTrack.getInfo().title + "``").queue();
						}else
							commandchannel.sendMessage("▶ Added to Queue: ``" + audioTrack.getInfo().title + "``").queue();


						trackScheduler.queue(audioTrack);

					}

					@Override
					public void playlistLoaded(AudioPlaylist audioPlaylist) {
						AudioTrack track = audioPlaylist.getTracks().get(0);
						if (trackScheduler.isEmpty()) {
							commandchannel.sendMessage("▶ Now Playing: ``" + audioPlaylist.getTracks().get(0).getInfo().title + "``").queue();
						}else
							commandchannel.sendMessage("▶ Added to Queue: ``" + audioPlaylist.getTracks().get(0).getInfo().title + "``").queue();




						trackScheduler.queue(track);
					}

					@Override
					public void noMatches() {
						commandchannel.sendMessage("🛑 No Matches for: ```" + trackURL + "```").queue();
					}

					@Override
					public void loadFailed(FriendlyException e) {
						commandchannel.sendMessage("```" + e.getMessage() + "```").queue();
					}
				});
			}

			@Override
			public void loadFailed(FriendlyException e) {
				commandchannel.sendMessage("```" + e.getMessage() + "```").queue();
			}
		});

	}

	public void startCleanupLoop(long delaymillis) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (guild.getAudioManager().isConnected()) {
					if (guild.getAudioManager().getConnectedChannel().getMembers().size() < 2) {
						quit();
					}
				}
			}
		}, delaymillis, delaymillis);
	}



	public TrackScheduler getTrackScheduler() {
		return trackScheduler;
	}

	public AudioPlayer getPlayer() {
		return player;
	}

	public Guild getGuild() {
		return guild;
	}
}
