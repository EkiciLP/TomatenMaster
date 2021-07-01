package net.Tomatentum.TomatenMaster.music.commands;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.music.GuildMusicManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class VolumeCommand implements GuildCommand {
	private GuildMusicManager musicManager;
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		musicManager = DiscordBot.getINSTANCE().getAudioManager().getGuildMusicManager(channel.getGuild());
		if (channel.getGuild().getAudioManager().isConnected() && !member.getVoiceState().getChannel().equals(channel.getGuild().getAudioManager().getConnectedChannel())) {
			channel.sendMessage(new EmbedBuilder().setColor(Color.RED).setDescription("Already conneced to channel: " + channel.getGuild().getAudioManager().getConnectedChannel().getName()).build()).queue();
			return;
		}
		int newVolume = Math.max(10, Math.min(100, Integer.parseInt(args[1])));
		int oldVolume = musicManager.getPlayer().getVolume();
		musicManager.getPlayer().setVolume(newVolume);
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.CYAN);
		builder.setTitle("🔊 Volume Changed");
		builder.setDescription("Volume Changed from "+ oldVolume + " to " + newVolume);
		builder.setFooter("0 - 100");
		channel.sendMessage(builder.build()).queue();


	}
}
