package net.Tomatentum.TomatenMaster.managers;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.utils.AttachmentOption;

import java.awt.*;
import java.io.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class Ticket {
	private TextChannel channel;
	private Message mainMessage;
	private List<Member> members;
	private final Guild guild;
	private Category category;
   	private TicketManager ticketManager;
   	private int ID;
   	
   	private File transcriptFolder = new File("Transcripts");

	public Ticket(Member owner, Category category, DiscordBot bot) {
		this.members = new ArrayList<>();
		members.add(owner);
		this.guild = category.getGuild();
		this.category = category;
		this.ticketManager = bot.getTicketManager();
		this.ID = bot.getConfig().getYML().getInt("TicketIds") + 1;
		bot.getConfig().getYML().set("TicketIds", ID);
		try {
			bot.getConfig().getYML().save(bot.getConfig().getFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		initTicket();
	}

	private void initTicket() {
		Member owner = members.get(0);
		TextChannel channel = category.createTextChannel("Ticket-" + ID).complete();
		channel.putPermissionOverride(owner).grant(Permission.VIEW_CHANNEL).grant(Permission.MESSAGE_ADD_REACTION).grant(Permission.MESSAGE_HISTORY).grant(Permission.MESSAGE_WRITE).grant(Permission.MESSAGE_READ).queue();


		channel.sendMessage(owner.getAsMention() + "\n" + guild.getRoleById(840543668763099165L).getAsMention()).complete();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("🟢 Ticket-" + ID);
		builder.setDescription("Welcome to The TomatenTum Support!\n\nPlease describe you Problem as detailed as possible so we can help you quickly!\n\nNote that the support Team is working in their free time so it can take some time for us to answer your question");
		builder.setTimestamp(OffsetDateTime.now());
		builder.setColor(0x34ebd8);
		builder.setThumbnail("https://i.imgur.com/nqtmoYX.png");
		builder.setAuthor(members.get(0).getEffectiveName(), null, members.get(0).getUser().getAvatarUrl());
		builder.setFooter("- TomatenTum Staff Team", guild.getIconUrl());
		builder.addField("Closing", "To close the Ticket you can: \n- Type !close\n- or react with 🔒", false);

		StringBuilder stringBuilder = new StringBuilder();
		members.forEach(member -> {stringBuilder.append(member.getEffectiveName() + "\n");});
		String memberlist = stringBuilder.toString();

		builder.addField("Members", memberlist, false);
		builder.addField("Commands", "- !add <@member> (adds a member to the Ticket)\n- !remove <@member> (removes a member from the Ticket)", false);
		mainMessage = channel.sendMessage(builder.build()).complete();
		builder.clear();
		mainMessage.addReaction("🔒").queue();
		ticketManager.getOpenTickets().put(channel, this);
		ticketManager.getOwnerlist().add(owner);
		this.channel = channel;
		System.out.println("[Ticket-" + ID + "] initialized!");
	}

	public void close() {


		if (!transcriptFolder.exists()) {
			transcriptFolder.mkdir();

		}


		File transcript = new File(transcriptFolder, "Ticket-" + getID() + ".txt");
		if (!transcript.exists()) {
			try {
				transcript.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}

		BufferedWriter writer;

		try {
			writer = new BufferedWriter(new FileWriter(transcript));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		List<Message> messages = (getChannel().getIterableHistory().complete());
		Collections.reverse(messages);
		
		for (Message message : messages) {
			if (message.getAuthor().isBot())
				continue;

			OffsetDateTime time = message.getTimeCreated();

			try {
				writer.write("[" + time.getDayOfMonth() + "." + time.getMonthValue() + "." + time.getYear() + " | " + time.getHour() + ":" + time.getMinute()
						+ "] " + message.getAuthor().getName() + " >> " + message.getContentRaw() + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		EmbedBuilder transcriptBuilder = new EmbedBuilder();
		transcriptBuilder.setColor(Color.CYAN);
		transcriptBuilder.setTimestamp(OffsetDateTime.now());
		transcriptBuilder.setAuthor(guild.getName(), null, guild.getIconUrl());
		transcriptBuilder.setTitle("Transcript for Ticket-" + ID);
		transcriptBuilder.setDescription("See Attachement");
		
		for (Member member : getMembers()) {
			member.getUser().openPrivateChannel().complete().sendMessage(transcriptBuilder.build()).addFile(new File(transcript.getAbsolutePath())).complete();
		}

		channel.getManager().setParent(channel.getGuild().getCategoryById(846836212030373940L)).complete();
		for (Member member : members) {
			channel.putPermissionOverride(member).deny(Permission.MESSAGE_WRITE).queue();
		}
		ticketManager.getOpenTickets().remove(channel);
		ticketManager.getClosedTickets().put(channel, this);
		ticketManager.getOwnerlist().remove(members.get(0));
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("🔴 Ticket-" + ID);
		builder.setDescription("Welcome to The TomatenTum Support!\n\nPlease describe you Problem as detailed as possible so we can help you quickly!\n\nNote that the support Team is working in their free time so it can take some time for us to answer your question");
		builder.setTimestamp(OffsetDateTime.now());
		builder.setColor(0xfc0307);
		builder.setThumbnail("https://i.imgur.com/nqtmoYX.png");
		builder.setAuthor(members.get(0).getEffectiveName(), null, members.get(0).getUser().getAvatarUrl());
		builder.setFooter("- TomatenTum Staff Team", guild.getIconUrl());
		builder.addField("Closed", "You can reopen this ticket by typing !reopen or by reacting with 🔓.\nYou can delete this ticket by typing !delete or by reacting with 🛑.", false);
		StringBuilder stringBuilder = new StringBuilder();
		members.forEach(member -> {stringBuilder.append(member.getEffectiveName() + "\n");});
		String memberlist = stringBuilder.toString();

		builder.addField("Members", memberlist, false);
		mainMessage = mainMessage.editMessage(builder.build()).complete();
		builder.clear();
		mainMessage.clearReactions().queue();
		mainMessage.addReaction("🔓").queue();
		mainMessage.addReaction("🛑").queue();
		


		System.out.println("[Ticket-" + ID + "] closed!");
	}
	public void addMember(Member member) {
		members.add(member);
		channel.putPermissionOverride(member).grant(Permission.VIEW_CHANNEL).grant(Permission.MESSAGE_ADD_REACTION).grant(Permission.MESSAGE_HISTORY).grant(Permission.MESSAGE_WRITE).grant(Permission.MESSAGE_READ).queue();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("🟢 Ticket-" + ID);
		builder.setDescription("Welcome to The TomatenTum Support!\n\nPlease describe you Problem as detailed as possible so we can help you quickly!\n\nNote that the support Team is working in their free time so it can take some time for us to answer your question");
		builder.setTimestamp(OffsetDateTime.now());
		builder.setColor(0x34ebd8);
		builder.setThumbnail("https://i.imgur.com/nqtmoYX.png");
		builder.setAuthor(members.get(0).getEffectiveName(), null, members.get(0).getUser().getAvatarUrl());
		builder.setFooter("- TomatenTum Staff Team", guild.getIconUrl());
		builder.addField("Closing", "To close the Ticket you can: \n- Type !close\n- or react with 🔒", false);

		StringBuilder stringBuilder = new StringBuilder();
		members.forEach(i -> {stringBuilder.append(i.getEffectiveName() + "\n");});
		String memberlist = stringBuilder.toString();

		builder.addField("Members", memberlist, false);
		builder.addField("Commands", "- !add <@member> (adds a member to the Ticket)\n- !remove <@member> (removes a member from the Ticket)", false);
		mainMessage = mainMessage.editMessage(builder.build()).complete();
		builder.clear();
		System.out.println("[Ticket-" + ID + "] added member " + member.getEffectiveName());
	}
	public void removeMember(Member member) {
		members.remove(member);
		channel.putPermissionOverride(member).deny(Permission.VIEW_CHANNEL).deny(Permission.MESSAGE_HISTORY).deny(Permission.MESSAGE_WRITE).deny(Permission.MESSAGE_READ).deny(Permission.MESSAGE_ADD_REACTION).queue();
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("🟢 Ticket-" + ID);
		builder.setDescription("Welcome to The TomatenTum Support!\n\nPlease describe you Problem as detailed as possible so we can help you quickly!\n\nNote that the support Team is working in their free time so it can take some time for us to answer your question");
		builder.setTimestamp(OffsetDateTime.now());
		builder.setColor(0x34ebd8);
		builder.setThumbnail("https://i.imgur.com/nqtmoYX.png");
		builder.setAuthor(members.get(0).getEffectiveName(), null, members.get(0).getUser().getAvatarUrl());
		builder.setFooter("- TomatenTum Staff Team", guild.getIconUrl());
		builder.addField("Closing", "To close the Ticket you can: \n- Type !close\n- or react with 🔒", false);


		StringBuilder stringBuilder = new StringBuilder();
		members.forEach(i -> {stringBuilder.append(i.getEffectiveName() + "\n");});
		String memberlist = stringBuilder.toString();

		builder.addField("Members", memberlist, false);
		mainMessage = mainMessage.editMessage(builder.build()).complete();
		builder.clear();
		System.out.println("[Ticket-" + ID + "] removed member " + member.getEffectiveName());
	}
	public void reOpen() {
		ticketManager.getOpenTickets().put(channel, this);
		ticketManager.getClosedTickets().remove(channel);
		ticketManager.getOwnerlist().add(members.get(0));
		channel.getManager().setParent(channel.getGuild().getCategoryById(835131959574921277L)).complete();
		channel.getManager().sync().queue();
		for (Member member : members) {
			channel.putPermissionOverride(member).grant(Permission.VIEW_CHANNEL).grant(Permission.MESSAGE_ADD_REACTION).grant(Permission.MESSAGE_HISTORY).grant(Permission.MESSAGE_WRITE).grant(Permission.MESSAGE_READ).queue();
		}
		EmbedBuilder builder = new EmbedBuilder();
		builder.setTitle("🟢 Ticket-" + ID);
		builder.setDescription("Welcome to The TomatenTum Support!\n\nPlease describe you Problem as detailed as possible so we can help you quickly!\n\nNote that the support Team is working in their free time so it can take some time for us to answer your question");
		builder.setTimestamp(OffsetDateTime.now());
		builder.setColor(0x34ebd8);
		builder.setThumbnail("https://i.imgur.com/nqtmoYX.png");
		builder.setAuthor(members.get(0).getEffectiveName(), null, members.get(0).getUser().getAvatarUrl());
		builder.setFooter("- TomatenTum Staff Team", guild.getIconUrl());
		builder.addField("Closing", "To close the Ticket you have to: \n- Type !close\n- React to the main message", false);
		StringBuilder stringBuilder = new StringBuilder();
		members.forEach(i -> {stringBuilder.append(i.getEffectiveName() + "\n");});
		String memberlist = stringBuilder.toString();

		builder.addField("Members", memberlist, false);
		mainMessage = mainMessage.editMessage(builder.build()).complete();
		builder.clear();
		mainMessage.clearReactions().queue();
		mainMessage.addReaction("🔒").queue();
		System.out.println("[Ticket-" + ID + "] reOpened");
	}


	//Getters/Setters

	public TextChannel getChannel() {
		return channel;
	}

	public Message getMainMessage() {
		return mainMessage;
	}

	public List<Member> getMembers() {
		return members;
	}

	public Guild getGuild() {
		return guild;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public TicketManager getTicketManager() {
		return ticketManager;
	}

	public void setTicketManager(TicketManager ticketManager) {
		this.ticketManager = ticketManager;
	}


	public int getID() {
		return ID;
	}
}
