package net.Tomatentum.TomatenMaster.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.bandcamp.BandcampAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.local.LocalAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;

public class AudioManager {
	private AudioPlayerManager playerManager;
	private DiscordBot bot;
	private HashMap<Long, GuildMusicManager> musicManagers;

	public AudioManager(DiscordBot bot) {
		this.musicManagers = new HashMap<>();
		this.bot = bot;
		this.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		playerManager.registerSourceManager(new YoutubeAudioSourceManager());
		playerManager.registerSourceManager(new BandcampAudioSourceManager());
		playerManager.registerSourceManager(new VimeoAudioSourceManager());
		playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
		playerManager.registerSourceManager(new HttpAudioSourceManager());
		playerManager.registerSourceManager(new LocalAudioSourceManager());
	}

	public GuildMusicManager getGuildMusicManager(Guild guild) {
		if (musicManagers.containsKey(guild.getIdLong())) {
			return musicManagers.get(guild.getIdLong());
		}else {
			GuildMusicManager musicManager = new GuildMusicManager(playerManager, guild);
			this.musicManagers.put(guild.getIdLong(), musicManager);
			return musicManager;
		}

	}

	public HashMap<Long, GuildMusicManager> getMusicManagers() {
		return musicManagers;
	}
}
