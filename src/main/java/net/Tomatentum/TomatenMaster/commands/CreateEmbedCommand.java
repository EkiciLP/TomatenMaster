package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class CreateEmbedCommand implements GuildCommand {
	private TomatenMaster bot;
	public CreateEmbedCommand(TomatenMaster bot) {
		this.bot = bot;
	}

	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		channel.sendTyping().complete();
		msg.delete().queue();
		if (member.hasPermission(Permission.MESSAGE_MANAGE)) {
			if (args.length == 3) {
			/*
			arg1 = channel
			arg2 = Title
			 */
				EmbedBuilder builder = new EmbedBuilder();
				builder.setTitle(args[2]);
				TextChannel EmbedChannel = msg.getMentionedChannels().get(0);
				bot.getEmbedManager().createEmbed(builder, EmbedChannel);
				msg.addReaction("✔").queue();
			} else
				channel.sendMessage(new EmbedBuilder().setDescription("Usage >> !createembed <channel> <Title>").setColor(0xfc0307).build()).queue();
		}else {

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !createembed");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}
	}
}
