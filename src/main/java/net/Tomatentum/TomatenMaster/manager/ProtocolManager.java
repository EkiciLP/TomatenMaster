package net.Tomatentum.TomatenMaster.manager;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;

public class ProtocolManager extends ListenerAdapter {
	private DiscordBot bot;
	private TextChannel protocolChannel;
	private EmbedBuilder builder;

	public ProtocolManager(DiscordBot bot) {
		this.bot = bot;
		builder = new EmbedBuilder();
	}


	@Override
	public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
		builder.clear();

		this.protocolChannel = event.getGuild().getTextChannelById(835092656836050964L);

		if (event.getUser().isBot()) { return; }
		if (event.getRoles().equals(Collections.singletonList(event.getGuild().getRoleById(835091081837543435L)))) {return;}

		StringBuilder stringBuilder = new StringBuilder();
		for (Role role : event.getRoles()) {
			stringBuilder.append("✅ ").append(role.getAsMention()).append("\n");
		}



		builder.setTimestamp(OffsetDateTime.now());
		builder.setColor(Color.GREEN);
		builder.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());
		builder.setDescription(event.getMember().getAsMention() + "✍ has been updated!");
		builder.addField("***Roles:***", stringBuilder.toString(), false);
		builder.setThumbnail(event.getUser().getAvatarUrl());

		protocolChannel.sendMessage(builder.build()).queue();
		builder.clear();

	}

	@Override
	public void onGuildMemberRoleRemove(@NotNull GuildMemberRoleRemoveEvent event) {
		builder.clear();

		this.protocolChannel = event.getGuild().getTextChannelById(835092656836050964L);

		if (event.getUser().isBot()) { return; }

		StringBuilder stringBuilder = new StringBuilder();
		for (Role role : event.getRoles()) {
			stringBuilder.append("❌ ").append(role.getAsMention()).append("\n");
		}



		builder.setTimestamp(OffsetDateTime.now());
		builder.setColor(Color.RED);
		builder.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());
		builder.setDescription(event.getMember().getAsMention() + "✍ has been updated!");
		builder.addField("***Roles:***", stringBuilder.toString(), false);
		builder.setThumbnail(event.getUser().getAvatarUrl());
		protocolChannel.sendMessage(builder.build()).queue();
		builder.clear();


	}

	@Override
	public void onGuildBan(@NotNull GuildBanEvent event) {
		builder.clear();

		this.protocolChannel = event.getGuild().getTextChannelById(835092656836050964L);


		builder.setTimestamp(OffsetDateTime.now());
		builder.setColor(Color.RED);
		builder.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());
		builder.setDescription(event.getUser().getAsMention() + "🛑 has been BANNED!");
		builder.setThumbnail(event.getUser().getAvatarUrl());
		protocolChannel.sendMessage(builder.build()).queue();
		builder.clear();

	}

	@Override
	public void onGuildUnban(@NotNull GuildUnbanEvent event) {
		builder.clear();

		this.protocolChannel = event.getGuild().getTextChannelById(835092656836050964L);

		builder.setTimestamp(OffsetDateTime.now());
		builder.setColor(Color.GREEN);
		builder.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());
		builder.setDescription(event.getUser().getAsMention() + "⬆ has been unbanned!");
		builder.setThumbnail(event.getUser().getAvatarUrl());
		protocolChannel.sendMessage(builder.build()).queue();
		builder.clear();

	}

}
