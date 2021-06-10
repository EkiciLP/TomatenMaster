package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EditEmbedCommand implements GuildCommand {
	private DiscordBot bot;

	public EditEmbedCommand(DiscordBot bot) {
		this.bot = bot;
	}

	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		/*
		args1 = ID
		args2 = action
			setDescription = String
			setTitle = String
			setColor = int
			setAuthor = String, URL (arg3 = name, arg4 = URL)
			setThumbnail = URL
			setImage = URL
			addField = String(title)
			editField = int FieldNumber, action, message
				settitle = String
				setdescription = string
				setinline = boolean
		args>3 = message


		 */
		EmbedBuilder helpbuilder = new EmbedBuilder();
		helpbuilder.setAuthor("!editembed Command Help", null, member.getUser().getAvatarUrl());
		helpbuilder.setColor(Color.RED);
		helpbuilder.setTitle("Unknown Usage");
		helpbuilder.setDescription("USAGE >> !editembed <EmbedID> <EditAction> <Text/FieldID> [FieldAction] [Text]\nFor URLs we recommend https://imgur.com");
		helpbuilder.addField("EditAction: setDescription", "!editembed <EmbedID> setDescription <Text>", false);
		helpbuilder.addField("EditAction: setTitle", "!editembed <EmbedID> setTitle <Text>", false);
		helpbuilder.addField("EditAction: setColor", "!editembed <EmbedID> setColor <Hex>", false);
		helpbuilder.addField("EditAction: setAuthor", "!editembed <EmbedID> setAuthor <Text> <IconURL>", false);
		helpbuilder.addField("EditAction: setThumbnail", "!editembed <EmbedID> setThumbnail <URL>", false);
		helpbuilder.addField("EditAction: removeThumbnail", "!editembed <EmbedID> removeThumbnail ", false);
		helpbuilder.addField("EditAction: setImage", "!editembed <EmbedID> setImage <URL>", false);
		helpbuilder.addField("EditAction: removeImage", "!editembed <EmbedID> removeImage ", false);
		helpbuilder.addField("EditAction: addField", "!editembed <EmbedID> addField <Title> ", false);
		helpbuilder.addField("EditAction: editField", "!editembed <EmbedID> editField <FieldNumber> <FieldAction> <Text>", false);
		helpbuilder.addField("FieldAction: setTitle", "!editembed <EmbedID> editField <FieldNumber> setTitle <Text>", true);
		helpbuilder.addField("FieldAction: setDescription", "!editembed <EmbedID> editField <FieldNumber> setDescription <Text>", true);
		helpbuilder.addField("FieldAction: setTitle", "!editembed <EmbedID> editField <FieldNumber> setInline <true/false>", true);
		helpbuilder.addField("FieldAction: removeField", "!editembed <EmbedID> editField <FieldNumber> RemoveField", true);
		helpbuilder.setFooter("- TomatenMaster Help", channel.getGuild().getIconUrl());
		if (member.hasPermission(Permission.MESSAGE_MANAGE)) {
			if (args.length >= 3) {
				if (Integer.parseInt(args[1]) < bot.getConfig().getYML().getInt("Embeds.nextID")) {
					EmbedBuilder builder = new EmbedBuilder(bot.getEmbedManager().getEmbed(Integer.parseInt(args[1])));
					switch (args[2].toLowerCase()) {
						case "setdescription":
							StringBuilder descBuilder = new StringBuilder();
							for (int i = 3; i < args.length; i++) {
								descBuilder.append(args[i]).append(" ");
							}
							builder.setDescription(descBuilder.toString());
							msg.addReaction("✔").queue();
							break;
						case "settitle":
							StringBuilder titleBuilder = new StringBuilder();
							for (int i = 3; i < args.length; i++) {
								titleBuilder.append(args[i]).append(" ");
							}
							builder.setTitle(titleBuilder.toString());
							msg.addReaction("✔").queue();
							break;
						case "setcolor":
							builder.setColor(Integer.parseInt(args[3].replace("#", "0x"), 16));
							msg.addReaction("✔").queue();
							break;
						case "setauthor":
							builder.setAuthor(args[3], null, args[4]);
							msg.addReaction("✔").queue();
							break;
						case "setthumbnail":
							builder.setThumbnail(args[3]);
							msg.addReaction("✔").queue();
							break;
						case "setimage":
							builder.setImage(args[3]);
							msg.addReaction("✔").queue();
							break;
						case "addfield":
							builder.addField(args[3], " ", false);
							msg.addReaction("✔").queue();
							break;
						case "removeimage":
							builder.setImage(null);
							break;
						case "removeThumbnail":
							builder.setThumbnail(null);
							break;
						case "editfield":
							if (args.length >= 5) {
								List<MessageEmbed.Field> fieldlist = new ArrayList<>();
								fieldlist.addAll(builder.getFields());
								MessageEmbed.Field oldfield = fieldlist.get(Integer.parseInt(args[3]) -1);
								MessageEmbed.Field field = null;
								StringBuilder stringBuilder = new StringBuilder();
								for (int i = 5; i < args.length; i++) {
									stringBuilder.append(args[i]).append(" ");
								}
								switch (args[4].toLowerCase()) {
									case "removefield":
										fieldlist.remove(Integer.parseInt(args[3])-1);
										msg.addReaction("✔").queue();
										break;
									case "settitle":
										field = new MessageEmbed.Field(stringBuilder.toString(), oldfield.getValue(), oldfield.isInline());
										msg.addReaction("✔").queue();
										break;
									case "setdescription":
										field = new MessageEmbed.Field(oldfield.getName(), stringBuilder.toString(), oldfield.isInline());
										msg.addReaction("✔").queue();
										break;
									case "setinline":
										field = new MessageEmbed.Field(oldfield.getName(), oldfield.getValue(), Boolean.parseBoolean(args[5]));
										msg.addReaction("✔").queue();
										break;
									default:
										msg.addReaction("❌").queue();
										channel.sendMessage(helpbuilder.build()).complete().delete().queueAfter(10, TimeUnit.MINUTES);
										break;
								}
								builder.clearFields();
								fieldlist.set(Integer.parseInt(args[3])-1, field);
								for (MessageEmbed.Field addfield : fieldlist) {
									builder.addField(addfield);
								}
							}else {
								msg.addReaction("❌").queue();
								channel.sendMessage(helpbuilder.build()).complete().delete().queueAfter(10, TimeUnit.MINUTES);
							}
							break;
						default:
							msg.addReaction("❌").queue();
							channel.sendMessage(helpbuilder.build()).complete().delete().queueAfter(10, TimeUnit.MINUTES);
							break;
					}
					bot.getEmbedManager().editEmbed(Integer.parseInt(args[1]), builder);
					msg.delete().queueAfter(10, TimeUnit.SECONDS);
				}else {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setTitle("Unknown ID");
					builder.setColor(Color.RED);
					channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
				}
			}else {
				msg.addReaction("❌").queue();
				channel.sendMessage(helpbuilder.build()).complete().delete().queueAfter(10, TimeUnit.MINUTES);
			}
		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !editembed");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}

	}
}
