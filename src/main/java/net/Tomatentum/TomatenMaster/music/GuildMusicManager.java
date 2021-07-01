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
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.awt.*;

public class GuildMusicManager {

	private AudioPlayer player;
	private Guild guild;
	private TrackScheduler trackScheduler;
	private AudioPlayerSendHandler sendHandler;
	private AudioPlayerManager playerManager;


	public GuildMusicManager(AudioPlayerManager playerManager, Guild guild) {
		this.guild = guild;
		this.player = playerManager.createPlayer();
		this.trackScheduler = new TrackScheduler(player, this);
		this.playerManager = playerManager;
		this.sendHandler = new AudioPlayerSendHandler(player);

		player.addListener(trackScheduler);
	}

	public void connect(VoiceChannel channel) {
		this.guild.getAudioManager().openAudioConnection(channel);
		this.guild.getAudioManager().setSendingHandler(sendHandler);
	}

	public void loadAndQueue(TextChannel commandchannel, String URL) {
		String trackURL;
		if (URL.startsWith("<") && URL.endsWith(">")) {
			trackURL = URL.substring(1, URL.length()-1);
		}else
			trackURL = URL;
		EmbedBuilder builder = new EmbedBuilder();
		builder.setFooter(guild.getName(), guild.getIconUrl());


		this.playerManager.loadItem(trackURL, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack audioTrack) {
				trackScheduler.queue(audioTrack);
				builder.setColor(Color.GREEN);
				builder.setTitle("▶ Adding to Queue");
				builder.setDescription(audioTrack.getInfo().title + " [" + DiscordBot.getINSTANCE().getTimestamp(audioTrack.getDuration()) + "]");
				builder.setAuthor(audioTrack.getInfo().author);
				builder.addField("Queue", trackScheduler.getQueue(), false);
				commandchannel.sendMessage(builder.build()).queue();
			}

			@Override
			public void playlistLoaded(AudioPlaylist audioPlaylist) {
				audioPlaylist.getTracks().forEach(trackScheduler::queue);
				builder.setColor(Color.GREEN);
				builder.setTitle("▶ Added Playlist");
				builder.setDescription(audioPlaylist.getName() + " [" + DiscordBot.getINSTANCE().getTimestamp(audioPlaylist.getTracks().size()) + "]");
				builder.addField("Queue", trackScheduler.getQueue(), false);

				commandchannel.sendMessage(builder.build()).queue();
			}

			@Override
			public void noMatches() {
				playerManager.loadItem("ytsearch:" + trackURL, new AudioLoadResultHandler() {
					@Override
					public void trackLoaded(AudioTrack audioTrack) {
						trackScheduler.queue(audioTrack);
						builder.setColor(Color.GREEN);
						builder.setTitle("▶ Adding to Queue");
						builder.setDescription(audioTrack.getInfo().title + " [" + DiscordBot.getINSTANCE().getTimestamp(audioTrack.getDuration()) + "]");
						builder.setAuthor(audioTrack.getInfo().author);
						builder.addField("Queue", trackScheduler.getQueue(), false);
						commandchannel.sendMessage(builder.build()).queue();
					}

					@Override
					public void playlistLoaded(AudioPlaylist audioPlaylist) {
						trackScheduler.queue(audioPlaylist.getTracks().get(0));
						builder.setColor(Color.GREEN);
						builder.setTitle("▶ Added to Queue");
						builder.setDescription(audioPlaylist.getTracks().get(0) + " [" + DiscordBot.getINSTANCE().getTimestamp(audioPlaylist.getTracks().get(0).getDuration()) + "]");
						builder.addField("Queue", trackScheduler.getQueue(), false);

						commandchannel.sendMessage(builder.build()).queue();
					}

					@Override
					public void noMatches() {
						builder.setColor(Color.RED);
						builder.setTitle("🛑 No Match!");
						builder.setDescription("Search: " + URL);
						builder.addField("Queue", trackScheduler.getQueue(), false);
						commandchannel.sendMessage(builder.build()).queue();
					}

					@Override
					public void loadFailed(FriendlyException e) {
						builder.setColor(Color.RED);
						builder.setTitle("🛑 Load Failed");
						builder.setDescription("Search: " + URL + "\n" + e.getMessage());
						builder.addField("Queue", trackScheduler.getQueue(), false);
						commandchannel.sendMessage(builder.build()).queue();
					}
				});
			}

			@Override
			public void loadFailed(FriendlyException e) {
				builder.setColor(Color.RED);
				builder.setTitle("🛑 Load Failed");
				builder.setDescription("Search: " + URL + "\n" + e.getMessage());
				builder.addField("Queue", trackScheduler.getQueue(), false);
				commandchannel.sendMessage(builder.build()).queue();
			}
		});

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
