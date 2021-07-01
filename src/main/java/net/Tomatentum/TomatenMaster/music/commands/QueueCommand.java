package net.Tomatentum.TomatenMaster.music.commands;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class QueueCommand implements GuildCommand {
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		channel.sendMessage(new EmbedBuilder().setColor(Color.GREEN).setTitle("Current Track").setDescription(DiscordBot.getINSTANCE().getAudioManager().getGuildMusicManager(channel.getGuild()).getTrackScheduler().getCurrentTrack()).addField("Queue", DiscordBot.getINSTANCE().getAudioManager().getGuildMusicManager(channel.getGuild()).getTrackScheduler().getQueueString(), false).build()).queue();

	}
}
