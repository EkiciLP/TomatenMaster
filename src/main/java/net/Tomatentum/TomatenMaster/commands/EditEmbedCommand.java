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
							builder.setColor(Integer.parseInt(args[3].substring("#".length()), 16));
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
							builder.addField(args[3], "-", false);
							msg.addReaction("✔").queue();
							break;
						case "removeimage":
							builder.setImage(null);
							break;
						case "removethumbnail":
							builder.setThumbnail(null);
							break;
						case "editfield":
							if (args.length >= 5) {
								if (args[4].equalsIgnoreCase("removefield") || args[4].equalsIgnoreCase("settitle") || args[4].equalsIgnoreCase("setdescription") || args[4].equalsIgnoreCase("setinline")) {
									List<MessageEmbed.Field> fieldlist = new ArrayList<>(builder.getFields());


									if (fieldlist.get(Integer.parseInt(args[3]) - 1) != null) {



										MessageEmbed.Field oldfield = fieldlist.get(Integer.parseInt(args[3]) - 1);
										MessageEmbed.Field field = null;
										StringBuilder stringBuilder = new StringBuilder();


										for (int i = 5; i < args.length; i++) {
											stringBuilder.append(args[i]).append(" ");
										}

										switch (args[4].toLowerCase()) {
											case "removefield": ;
												fieldlist.remove(oldfield);
												msg.addReaction("✔").queue();
												break;
											case "settitle":
												field = new MessageEmbed.Field(stringBuilder.toString(), oldfield.getValue(), oldfield.isInline());
												fieldlist.set(Integer.parseInt(args[3]) - 1, field);

												msg.addReaction("✔").queue();
												break;
											case "setdescription":
												field = new MessageEmbed.Field(oldfield.getName(), stringBuilder.toString() + "\n", oldfield.isInline());
												fieldlist.set(Integer.parseInt(args[3]) - 1, field);
												msg.addReaction("✔").queue();
												break;
											case "setinline":
												field = new MessageEmbed.Field(oldfield.getName(), oldfield.getValue(), Boolean.parseBoolean(args[5]));
												fieldlist.set(Integer.parseInt(args[3]) - 1, field);
												msg.addReaction("✔").queue();
												break;
											default:
												msg.addReaction("❌").queue();
												sendHelp(channel);
												break;
										}
										builder.clearFields();
										for (MessageEmbed.Field addfield : fieldlist) {
											builder.addField(addfield);
										}
									}else
										sendHelp(channel);
								} else
									sendHelp(channel);
							} else {
								msg.addReaction("❌").queue();
								sendHelp(channel);
							}
								break;
							default:
								msg.addReaction("❌").queue();
								sendHelp(channel);
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
				sendHelp(channel);
			}
		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("No Permission for Command: !editembed");
			builder.setColor(Color.RED);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);
		}

	}
	public void sendHelp(TextChannel channel) {
		EmbedBuilder helpbuilder = new EmbedBuilder();
		helpbuilder.setAuthor("!editembed Command Help");
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
		channel.sendMessage(helpbuilder.build()).complete().delete().queueAfter(5, TimeUnit.MINUTES);
	}
}
