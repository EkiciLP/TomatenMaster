package net.Tomatentum.TomatenMaster.music.commands;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.music.GuildMusicManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class VolumeCommand implements GuildCommand {
	private GuildMusicManager musicManager;
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		musicManager = TomatenMaster.getINSTANCE().getAudioManager().getGuildMusicManager(channel.getGuild());
		if (member.getVoiceState().inVoiceChannel()) {
			if (!musicManager.isPermitted(member.getVoiceState().getChannel(), channel)) {
				return;
			}
		}

		int newVolume = Math.max(10, Math.min(100, Integer.parseInt(args[1])));
		int oldVolume = musicManager.getPlayer().getVolume();
		musicManager.getPlayer().setVolume(newVolume);

		channel.sendMessage("🔊 Volume Changed from "+ oldVolume + " to " + newVolume).queue();

	}
}
