package net.Tomatentum.TomatenMaster.music.commands;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.music.GuildMusicManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayCommand implements GuildCommand {
	private GuildMusicManager musicManager;

	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		musicManager = TomatenMaster.getINSTANCE().getAudioManager().getGuildMusicManager(channel.getGuild());
		if (member.getVoiceState().inVoiceChannel()) {
			if (!musicManager.isPermitted(member.getVoiceState().getChannel(), channel))
				return;
		}


		if (args.length > 1) {
			if (member.getVoiceState().inVoiceChannel()) {
				StringBuilder stringBuilder = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					stringBuilder.append(args[i]).append(" ");
				}

				musicManager.connect(member.getVoiceState().getChannel(), channel);
				musicManager.loadAndQueue(channel, stringBuilder.toString());
			}
		}
	}
}
