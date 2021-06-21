package net.Tomatentum.TomatenMaster.listeners;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

public class GuildListener extends ListenerAdapter {
	private DiscordBot bot;
	public GuildListener(DiscordBot bot) {
		this.bot = bot;
	}



	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Role role = event.getGuild().getRoleById(bot.getConfig().getYML().getLong("AutoRole"));
		if (role != null) {
			event.getGuild().addRoleToMember(event.getMember(), role).queue();
		}else {
			bot.getConfig().getYML().set("AutoRole", null);
			bot.getConfig().save();
			System.out.println("not role set");
		}


		//
		TextChannel channel = event.getGuild().getTextChannelById(bot.getConfig().getYML().getLong("WelcomeChannel"));
		if (channel != null) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Color.GREEN);
			builder.setAuthor(event.getMember().getEffectiveName(), null, event.getMember().getUser().getAvatarUrl());
			builder.setThumbnail(event.getGuild().getIconUrl());
			builder.setTimestamp(OffsetDateTime.now());
			builder.setTitle("Welcome " + event.getMember().getEffectiveName());
			builder.setDescription("Hello " + event.getMember().getAsMention() + " and welcome to the TomatenTum Network!\nCheck " + event.getGuild().getTextChannelById("849304093382017054").getAsMention() + " for Info about the Space Engineers Server!");
			channel.sendMessage(builder.build()).queue();
			builder.clear();
		}else {
			bot.getConfig().getYML().set("WelcomeChannel", null);
			bot.getConfig().save();
			System.out.println("no channel set");
		}
	}

	@Override
	public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
		TextChannel channel = event.getGuild().getTextChannelById(bot.getConfig().getYML().getLong("WelcomeChannel"));
		if (channel != null) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Color.RED);
			builder.setAuthor(event.getUser().getName(), null, event.getUser().getAvatarUrl());
			builder.setThumbnail(event.getGuild().getIconUrl());
			builder.setTimestamp(OffsetDateTime.now());
			builder.setDescription("Bye " + event.getUser().getAsMention());
			channel.sendMessage(builder.build()).queue();
			builder.clear();
		}else {
			bot.getConfig().getYML().set("WelcomeChannel", null);
			bot.getConfig().save();
			System.out.println("no channel set");
		}
	}


}
