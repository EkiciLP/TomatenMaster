package net.Tomatentum.TomatenMaster.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

public class Embed {

	public static MessageEmbed simple(Color color, String text) {

		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(color);
		builder.setDescription(text);
		builder.setTimestamp(OffsetDateTime.now());
		return builder.build();
	}

	public static MessageEmbed timer(Color color, String text, String title, long endTime) {

		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(color);
		builder.setDescription(text);
		builder.setTimestamp(Instant.ofEpochMilli(endTime));
		return builder.build();
	}

	public static MessageEmbed user(Color color, String text, User user) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(color);
		builder.setDescription(text);
		builder.setAuthor(user.getName(), null, user.getAvatarUrl());
		builder.setTimestamp(OffsetDateTime.now());
		return builder.build();
	}

	public static MessageEmbed member(Color color, String text, Member member) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor(member.getUser().getName(), null, member.getUser().getAvatarUrl());
		builder.setDescription(text);
		builder.setFooter(member.getGuild().getName(), member.getGuild().getIconUrl());
		builder.setTimestamp(OffsetDateTime.now());
		builder.setColor(color);
		return builder.build();
	}

	public static MessageEmbed log(Color color, String text, User user) {
		EmbedBuilder builder = new EmbedBuilder();

		builder.setColor(color);
		builder.setDescription(text);
		builder.setThumbnail(user.getAvatarUrl());
		builder.setTimestamp(OffsetDateTime.now());

		return builder.build();
	}

	public static MessageEmbed fieldEmbed(String title, String description, User user, List<MessageEmbed.Field> fields) {
		EmbedBuilder builder = new EmbedBuilder();

		builder.setTitle(title);
		builder.setDescription(description);
		builder.setTimestamp(OffsetDateTime.now());
		builder.setAuthor(user.getName(), null, user.getAvatarUrl());
		for (MessageEmbed.Field field : fields) {
			builder.addField(field);
		}

		return builder.build();
	}
}
