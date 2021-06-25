package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class HelpCommand implements GuildCommand {
	private DiscordBot bot;
	public HelpCommand(DiscordBot bot) {
		this.bot = bot;
	}
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.addReaction("✔").queue();
		msg.delete().queueAfter(20, TimeUnit.SECONDS);
		PrivateChannel privateChannel = member.getUser().openPrivateChannel().complete();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(0x00ff44);
		builder.setTitle("Help");
		builder.setAuthor(channel.getGuild().getName(), null, channel.getGuild().getIconUrl());
		builder.setDescription("This is a List of available Commands!\nYou can open a Ticket by reacting to the message in " + bot.getTicketManager().getPanel().getTextChannel().getAsMention() + "!\n Or you can create a Session in '+ New Session'");
		builder.addField("!help", "Sends this message!", false);
		builder.addField("!clear <amount>", "Clears the specified amount of messages!\nOnly with permission: MANAGE_MESSAGES", false);
		builder.addField("!close", "Closes the current ticket!\nOnly available in Tickets", true);
		builder.addField("!delete", "Deletes your current Ticket!\nOnly available in Tickets", true);
		builder.addField("!panel", "Sets the Ticket Panel to your current channel!\nOnly with Permission: MANAGE_SERVER", true);
		builder.addField("!reopen", "ReOpens the current Ticket!\nOnly available in Tickets\nCan only be used by staff because you cant send messages in Tickets after its closed.", true);
		builder.addField("!add <@member>", "Adds the specified member to your current Ticket!\nOnly available in Tickets", false);
		builder.addField("!remove <@member>", "Removes the specified member from your current Ticket!\nOnly available in Tickets", false);
		builder.addField("!createembed <Channel> <Title>", "Creates a new Embed in the specified channel which you can edit later!\nOnly with Permission: MANAGER_MESSAGES", false);
		builder.addField("!editembed", "Help in second Message!\nOnly with Permission: MANAGE_MESSAGES", false);
		builder.addField("!AutoRole", "Sets the Autorole Role.\n!AutoRole (@role/remove)", false);
		builder.addField("!welcome", "Sets the welcome channel.\n!welcome (#channel/remove)", false);
		builder.addField("!warn", "Warns the specified member!\n!warn @member <reason/clear>", false);
		builder.addField("!warnings", "Shows the warnings of specified member!\n!warn @member <reason/clear>", false);
		builder.addField("!mute", "Muted the specified member!(unmutes if muted)\n!mute @member [time(minutes)]", false);
		builder.addField("!ban", "Bans the specified member!(unbans if banned)\n!ban @member <time(minutes)> <reason>", false);
		builder.addField("!rr", "ReactionRole Command. Usage: \"!rr <add/remove> #channel <messageID> <Emoji> @role\"", false);
		builder.addField("!shutdown", "Shuts down the bot!", false);



		privateChannel.sendTyping().queue();
		privateChannel.sendMessage(builder.build()).queue();
		builder.clear();
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
		helpbuilder.addField("EditAction: setImage", "!editembed <EmbedID> setImage <URL>", false);
		helpbuilder.addField("EditAction: addField", "!editembed <EmbedID> addField <Title> ", false);
		helpbuilder.addField("EditAction: editField", "!editembed <EmbedID> editField <FieldNumber> <FieldAction> <Text>", false);
		helpbuilder.addField("FieldAction: setTitle", "!editembed <EmbedID> editField <FieldNumber> setTitle <Text>", true);
		helpbuilder.addField("FieldAction: setDescription", "!editembed <EmbedID> editField <FieldNumber> setDescription <Text>", true);
		helpbuilder.addField("FieldAction: setTitle", "!editembed <EmbedID> editField <FieldNumber> setInline <true/false>", true);
		helpbuilder.setFooter("- TomatenMaster Help", channel.getGuild().getIconUrl());
		privateChannel.sendTyping().queue();
		privateChannel.sendMessage(helpbuilder.build()).queue();
	}
}
