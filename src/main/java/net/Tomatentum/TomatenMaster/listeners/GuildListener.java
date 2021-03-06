package net.Tomatentum.TomatenMaster.listeners;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.Embed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;

public class GuildListener extends ListenerAdapter {
	private TomatenMaster bot;
	public GuildListener(TomatenMaster bot) {
		this.bot = bot;
	}

	@Override
	public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
		if (event.getRoles().contains(event.getGuild().getRolesByName("muted", true).get(0))) {
			for (Role role : event.getRoles()) {
				event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
			}
		}
	}


	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		Role role = event.getGuild().getRoleById(bot.getConfig().getYML().getLong("AutoRole"));
		if (role != null) {
			event.getGuild().addRoleToMember(event.getMember(), role).queue();
		}else {
			bot.getConfig().getYML().set("AutoRole", null);
			bot.getConfig().save();
			System.out.println("No AutoRole has been set!");
		};

		//
		TextChannel channel = event.getGuild().getTextChannelById(bot.getConfig().getYML().getLong("WelcomeChannel"));
		if (channel != null) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setColor(Color.GREEN);
			builder.setAuthor(event.getUser().getName(), null, event.getMember().getUser().getAvatarUrl());
			builder.setTimestamp(OffsetDateTime.now());
			builder.setTitle("Welcome " + event.getMember().getEffectiveName());
			builder.setDescription("Hello " + event.getUser().getAsMention() + " and welcome to the TomatenTum Network!\n" +
					"We are currently working on our Minecraft Network so chill out on the discord and wait for us :p" );
			channel.sendMessage(" ").setEmbeds(builder.build()).queue();
			builder.clear();
		}else {
			bot.getConfig().getYML().set("WelcomeChannel", null);
			bot.getConfig().save();
			System.out.println("No Welcome Channel has been set!");
		}


		if (bot.getPunishManager().isMuted(event.getMember()))
			event.getGuild().addRoleToMember(event.getMember(), bot.getPunishManager().getMutedRole(event.getGuild())).queue();
	}

	@Override
	public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
		TextChannel channel = event.getGuild().getTextChannelById(bot.getConfig().getYML().getLong("WelcomeChannel"));
		if (channel != null) {
			channel.sendMessage(" ").setEmbeds(Embed.user(Color.ORANGE, "Bye " + event.getUser().getName(), event.getMember().getUser())).queue();
		}else {
			bot.getConfig().getYML().set("WelcomeChannel", null);
			bot.getConfig().save();
			System.out.println("No Welcome Channel has been set!");
		}
	}


}
