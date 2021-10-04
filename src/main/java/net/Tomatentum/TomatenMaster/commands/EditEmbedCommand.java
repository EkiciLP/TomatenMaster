package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EditEmbedCommand extends SlashCommand {
	private TomatenMaster bot;

	public EditEmbedCommand(TomatenMaster bot) {
		super("editembed", "Edits Embeds!", Permission.MESSAGE_MANAGE);
		this.bot = bot;
		getCommand().editCommand()


				.addSubcommandGroups(
						new SubcommandGroupData("description", "Sets the Description of the Embed")
								.addSubcommands(new SubcommandData("set", "Sets the Description of the Embed")
										.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
										.addOption(OptionType.STRING, "input", "The input that should be used as the description",true)

								),
						new SubcommandGroupData("color", "Sets the color of the Embed")
								.addSubcommands(new SubcommandData("set", "Sets the color of the Embed")
										.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
										.addOption(OptionType.STRING, "input", "The Hex Code that should be used as the color",true)
								),
						new SubcommandGroupData("title", "Sets the Title of the Embed (URL: The link users will be redirected to on Title click)")
								.addSubcommands(new SubcommandData("set", "Sets the Title of the Embed (URL: The link users will be redirected to on Title click)")
										.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
										.addOption(OptionType.STRING, "input", "The input that should be used as the title",true)
										.addOption(OptionType.STRING, "url", "The URL to which users should be redirected on title click")
								),
						new SubcommandGroupData("author", "Sets the Author displayed at the bottom of the Embed (URL: Icon image)")
								.addSubcommands(new SubcommandData("set", "Sets the Author displayed at the bottom of the Embed (URL: Icon image)")
										.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
										.addOption(OptionType.STRING, "input", "The input that should be used as the Authors name",true)
										.addOption(OptionType.STRING, "url", "The URL of the image that should be used as the icon of the Author",true)
								),
						new SubcommandGroupData("thumbnail", "Sets the Thumbnail displayed at the top right of the Embed")
								.addSubcommands(new SubcommandData("set", "Sets the Thumbnail displayed at the top right of the Embed")
										.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
										.addOption(OptionType.STRING, "input", "The URL of the Image that should be used as the thumbnail",true)

								)
								.addSubcommands(new SubcommandData("remove", "Removes the Thumbnail")
										.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
								),
						new SubcommandGroupData("image", "Sets the Image displayed at the bottom of the Embed above the Author")
								.addSubcommands(new SubcommandData("set", "Sets the Image displayed at the bottom of the Embed above the Author")
										.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
										.addOption(OptionType.STRING, "input", "The URL of the Image that should be used as the Image",true)
								)
								.addSubcommands(new SubcommandData("remove", "Removes the Image")
										.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
								),
						new SubcommandGroupData("addfield","Adds a Field to the Embed")
								.addSubcommands(new SubcommandData("set", "Adds a Field to the Embed")
										.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
										.addOption(OptionType.STRING, "input", "The input that should be used as the Title of the Field",true)
								),
						new SubcommandGroupData("editfield", "Edits the specified Field (FieldID parameter has to be set)")
							.addSubcommands(new SubcommandData("settitle", "Sets the title of the Field")
									.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
									.addOption(OptionType.STRING, "input", "The input that should be used as the Title of the Field",true)
									.addOption(OptionType.INTEGER, "fieldid", "The targetted Field ID (if you want to edit a Field)",true)

							)
							.addSubcommands(new SubcommandData("setdescription", "Sets the description of the Field")
									.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
									.addOption(OptionType.STRING, "input", "The input that should be used as the Description of the Field",true)
									.addOption(OptionType.INTEGER, "fieldid", "The targetted Field ID (if you want to edit a Field)",true)

							)
							.addSubcommands(new SubcommandData("setinline", "Sets if the Field is Inline.")
									.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
									.addOption(OptionType.BOOLEAN, "input", "If the Field should be Inline",true)
									.addOption(OptionType.INTEGER, "fieldid", "The targetted Field ID (if you want to edit a Field)",true)

							)
							.addSubcommands(new SubcommandData("removefield", "Removes the Field")
									.addOption(OptionType.INTEGER, "embedid", "The Embed's Id",true)
									.addOption(OptionType.INTEGER, "fieldid", "The targetted Field ID",true)

							)
				)

				.complete();


	}

	@Override
	public void execute(SlashCommandEvent command) {
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

		command.getHook().setEphemeral(true);
		command.deferReply(true).setEphemeral(true).queue();

		Member member = command.getMember();

		String subCommandGroup = command.getSubcommandGroup();
		String subCommand = command.getSubcommandName();

		int embedId = (int) command.getOption("embedid").getAsLong();
		Integer FieldID = null;
		String input = null;
		String URL = null;

		try {
			FieldID = (int) command.getOption("fieldid").getAsLong();
		}catch (NullPointerException ignored) {}

		try {
			URL = command.getOption("url").getAsString();
		}catch (NullPointerException ignored) {}

		try {
			input = command.getOption("input").getAsString();
		}catch (NullPointerException ignored) {}

		if (embedId>= bot.getConfig().getYML().getInt("Embeds.nextID")) {
			command.getHook().sendMessage("The EmbedID is invalid").queue();
			return;
		}
		try {
			bot.getEmbedManager().getEmbed(embedId);
		}catch (NullPointerException ex) {
			command.getHook().sendMessage("The EmbedID is invalid").queue();
			return;
		}




		EmbedBuilder builder = new EmbedBuilder(bot.getEmbedManager().getEmbed(embedId));
		if (!subCommand.equals("remove")) {
			switch (command.getSubcommandGroup()) {
				case "description":
					builder.setDescription(input);
					break;
				case "title":
					builder.setTitle(input, URL);
					break;
				case "color":
					builder.setColor(Integer.parseInt(input.replace("#", ""), 16));
					break;
				case "author":
					builder.setAuthor(input, null, URL);
					break;
				case "thumbnail":
					builder.setThumbnail(input);
					break;
				case "image":
					builder.setImage(input);
					break;
				case "addfield":
					builder.addField(input, "-", false);
					break;
				case "editfield":
					List<MessageEmbed.Field> fieldlist = new ArrayList<>(builder.getFields());
					if (fieldlist.get(FieldID - 1) == null) {
						command.getHook().sendMessage("❌ The Specified Field does not exist").queue();
						return;
					}
					MessageEmbed.Field oldfield = fieldlist.get(FieldID - 1);
					MessageEmbed.Field field = null;

					switch (subCommand) {
						case "settitle":
							field = new MessageEmbed.Field(input, oldfield.getValue(), oldfield.isInline());
							fieldlist.set(FieldID - 1, field);
							break;
							case "setdescription":
								field = new MessageEmbed.Field(oldfield.getName(), input, oldfield.isInline());
								fieldlist.set(FieldID - 1, field);
								break;
								case "setinline":
									field = new MessageEmbed.Field(oldfield.getName(), oldfield.getValue(), command.getOption("input").getAsBoolean());
									fieldlist.set(FieldID - 1, field);
									break;
									case "removefield":
										fieldlist.remove(FieldID -1);
										break;
					}
					builder.clearFields();
					for (MessageEmbed.Field addfield : fieldlist) {
						builder.addField(addfield);
					}
							break;
					}
				}else {
					switch (subCommandGroup) {
						case "image":
							builder.setImage(null);
							break;
						case "thumbnail":
							builder.setThumbnail(null);
							break;
					}
				}

		command.getHook().editOriginal("The New Embed:").setEmbeds(builder.build()).queue();
		bot.getEmbedManager().editEmbed(embedId, builder);

	}
}
