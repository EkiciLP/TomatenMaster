package net.Tomatentum.TomatenMaster.autovoicechannels;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;

public class RequestMessage extends ListenerAdapter {

	private final Button approveButton;
	private final Button rejectButton;

	private long memberId;
	private PrivateVoiceChannel pvc;
	private Message message;
	private EmbedBuilder builder;
	private boolean accepted = false;


	public RequestMessage(long memberId, PrivateVoiceChannel pvc){

		rejectButton = Button.danger("reject", "❌");
		approveButton = Button.success("approve", "✔");

		this.memberId = memberId;
		this.pvc = pvc;

		TomatenMaster.getINSTANCE().getBot().addEventListener(this);

		this.builder = new EmbedBuilder();
		builder.setColor(Color.ORANGE);
		builder.setAuthor(getMember().getEffectiveName(), null, getMember().getUser().getAvatarUrl());
		builder.setTitle("Join Request!");
		builder.setTimestamp(OffsetDateTime.now());
		builder.setDescription("Hey " + pvc.getOwner().getAsMention() +
				"!\n" + getMember().getAsMention() + " wants to join your private channel!");
		builder.setThumbnail(getMember().getUser().getAvatarUrl());
		builder.setFooter(getMember().getGuild().getName(), getMember().getGuild().getIconUrl());

		message = new MessageBuilder().setEmbeds(builder.build()).setActionRows(ActionRow.of(approveButton, rejectButton)).build();


	}

	/**
	 * Not available until {@link RequestMessage#send()} is run
	 */

	public void accept() {

		this.accepted = true;
		pvc.addMember(getMember());
		builder.setColor(Color.GREEN);
		builder.setDescription("Hey " + pvc.getOwner().getAsMention() +
				"!\n Click ❌ to kick " + getMember().getAsMention());
		this.message = message.editMessage(builder.build()).setActionRow(approveButton.asDisabled(), rejectButton).complete();
	}

	public void reject() {
		getMember().getGuild().kickVoiceMember(getMember()).queue();
		message.delete().queue();
	}

	public void send() {
		this.message = pvc.getRequestChannel().sendMessage(message).complete();
	}

	public void remove() {
		TomatenMaster.getINSTANCE().getBot().removeEventListener(this);

		pvc.removeMember(getMember());
		if (getMember().getVoiceState().inVoiceChannel()) {
			getMember().getGuild().kickVoiceMember(getMember()).queue();
			this.message.delete().queue();
		}
	}


	@Override
	public void onButtonClick(@NotNull ButtonClickEvent event) {
		if (event.getMessage().getIdLong() == message.getIdLong()) {
			if (event.getButton().equals(approveButton)) {
				if (!event.getButton().isDisabled())
					accept();
			}else {
				if (accepted) {
					remove();
				} else
					reject();
			}
		}
	}

	@Override
	public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
		if (event.getChannelLeft().getIdLong() == pvc.getWaitChannel().getIdLong()) {
			if (event.getMember().getIdLong() == memberId) {

				if (event.getChannelLeft().getIdLong() == pvc.getIdLong())
					return;

				remove();
			}
		}
	}

	@Override
	public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
		if (event.getChannelLeft().getIdLong() == pvc.getWaitChannel().getIdLong()) {
			if (event.getMember().getIdLong() == memberId) {

				if (event.getChannelJoined().getIdLong() == pvc.getIdLong())
					return;

				remove();

			}
		}
	}

	public Member getMember() {
		return pvc.getGuild().getMemberById(memberId);
	}
}
