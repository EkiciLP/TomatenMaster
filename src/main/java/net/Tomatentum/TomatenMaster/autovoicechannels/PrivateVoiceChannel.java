package net.Tomatentum.TomatenMaster.autovoicechannels;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.ChannelManager;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PrivateVoiceChannel extends ListenerAdapter implements VoiceChannel {

	private Member owner;
	private final List<Member> members;
	private final List<RequestMessage> requestMessages;
	private final Category category;
	private VoiceChannel channel;
	private TextChannel requestChannel;
	private VoiceChannel waitChannel;

	public PrivateVoiceChannel(Member owner, Category category) {
		this.owner = owner;
		this.category = category;
		this.members = new ArrayList<>();
		this.requestMessages = new ArrayList<>();

		TomatenMaster.getINSTANCE().getBot().addEventListener(this);
	}

	public void create() {

		TomatenMaster.getINSTANCE().getAutoVoiceChannelManager().getPrivateVoiceChannels().put(owner.getIdLong(), this);
		this.channel = this.category.createVoiceChannel("⛔ " + owner.getEffectiveName() + "'s Private Room").complete();
		this.requestChannel = category.createTextChannel("⭕ (" + owner.getEffectiveName() + ") Requests").complete();
		this.waitChannel = category.createVoiceChannel("⏫ Waiting for move ⏫ (" + owner.getEffectiveName() + ")").complete();

		channel.putPermissionOverride(category.getGuild().getPublicRole()).deny(Permission.VOICE_CONNECT).queue();
		requestChannel.putPermissionOverride(category.getGuild().getPublicRole()).deny(Permission.VIEW_CHANNEL).queue();
		requestChannel.putPermissionOverride(owner).grant(Permission.VIEW_CHANNEL).queue();
		waitChannel.putPermissionOverride(category.getGuild().getPublicRole()).deny(Permission.VOICE_SPEAK).queue();

		category.getGuild().moveVoiceMember(owner, channel).queue();

	}

	@NotNull
	public AuditableRestAction<Void> delete() {
		TomatenMaster.getINSTANCE().getBot().removeEventListener(this);
		waitChannel.delete().queue();
		requestChannel.delete().queue();
		TomatenMaster.getINSTANCE().getAutoVoiceChannelManager().getPrivateVoiceChannels().remove(owner.getIdLong());

		return channel.delete();
	}

	@NotNull
	@Override
	public PermissionOverrideAction createPermissionOverride(@NotNull IPermissionHolder iPermissionHolder) {
		return channel.createPermissionOverride(iPermissionHolder);
	}

	@NotNull
	@Override
	public PermissionOverrideAction putPermissionOverride(@NotNull IPermissionHolder iPermissionHolder) {
		return channel.putPermissionOverride(iPermissionHolder);
	}

	@NotNull
	@Override
	public InviteAction createInvite() {
		return channel.createInvite();
	}

	@NotNull
	@Override
	public RestAction<List<Invite>> retrieveInvites() {
		return channel.retrieveInvites();
	}

	public void setOwner(Member owner) {
		channel.putPermissionOverride(this.owner).deny(Permission.VOICE_CONNECT).queue();
		requestChannel.putPermissionOverride(this.owner).deny(Permission.VIEW_CHANNEL).queue();

		this.owner = owner;


		channel.putPermissionOverride(owner).grant(Permission.VOICE_CONNECT).queue();
		requestChannel.putPermissionOverride(this.owner).grant(Permission.VIEW_CHANNEL).queue();

	}

	public void addMember(Member member) {
		members.add(member);
		channel.putPermissionOverride(member).grant(Permission.VIEW_CHANNEL).queue();
		member.getGuild().moveVoiceMember(member,channel).queue();
	}

	public void removeMember(Member member) {
		if (members.contains(member)) {
			members.remove(member);

			if (channel != null)
				channel.putPermissionOverride(member).deny(Permission.VIEW_CHANNEL).queue();

			if (member.getVoiceState().inVoiceChannel())
				member.getGuild().kickVoiceMember(member).queue();

		}
	}


	@Override
	public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
		if (event.getChannelJoined().getIdLong() == (waitChannel.getIdLong())) {
			RequestMessage requestMessage = new RequestMessage(event.getMember().getIdLong(), this);
			this.requestMessages.add(requestMessage);
			requestMessage.send();

		}
	}

	@Override
	public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
		if (event.getChannelLeft().getIdLong() == channel.getIdLong()) {
			removeMember(event.getMember());
			if (event.getChannelLeft().getMembers().size() == 0) {
				delete().queue();
				return;
			}

			if (event.getMember().getIdLong() == owner.getIdLong()) {
				setOwner(event.getChannelLeft().getMembers().get(0));
			}
		}
	}

	@Override
	public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
		if (event.getChannelLeft().getIdLong() == channel.getIdLong()) {
			removeMember(event.getMember());
			if (event.getChannelLeft().getMembers().size() == 0) {
				delete().queue();
				return;
			}

			if (event.getMember().getIdLong() == owner.getIdLong()) {
				setOwner(event.getChannelLeft().getMembers().get(0));
			}
		}

		if (event.getChannelJoined().getIdLong() == (waitChannel.getIdLong())) {
			RequestMessage requestMessage = new RequestMessage(event.getMember().getIdLong(), this);
			this.requestMessages.add(requestMessage);
			requestMessage.send();

		}
	}


	public Member getOwner() {
		return owner;
	}

	public TextChannel getRequestChannel() {
		return requestChannel;
	}

	public VoiceChannel getWaitChannel() {
		return waitChannel;
	}

	@Override
	public int getUserLimit() {
		return channel.getUserLimit();
	}

	@Override
	public int getBitrate() {
		return channel.getBitrate();
	}

	@NotNull
	@Override
	public Region getRegion() {
		return channel.getRegion();
	}

	@Nullable
	@Override
	public String getRegionRaw() {
		return channel.getRegionRaw();
	}

	@NotNull
	@Override
	public Guild getGuild() {
		return channel.getGuild();
	}

	@Nullable
	@Override
	public Category getParent() {
		return channel.getParent();
	}

	@NotNull
	@Override
	public List<Member> getMembers() {
		return channel.getMembers();
	}

	@Override
	public int getPosition() {
		return channel.getPosition();
	}

	@Override
	public int getPositionRaw() {
		return channel.getPositionRaw();
	}

	@Nullable
	@Override
	public PermissionOverride getPermissionOverride(@NotNull IPermissionHolder iPermissionHolder) {
		return channel.getPermissionOverride(iPermissionHolder);
	}

	@NotNull
	@Override
	public List<PermissionOverride> getPermissionOverrides() {
		return channel.getPermissionOverrides();
	}

	@NotNull
	@Override
	public List<PermissionOverride> getMemberPermissionOverrides() {
		return channel.getPermissionOverrides();
	}

	@NotNull
	@Override
	public List<PermissionOverride> getRolePermissionOverrides() {
		return channel.getRolePermissionOverrides();
	}

	@Override
	public boolean isSynced() {
		return channel.isSynced();
	}

	@NotNull
	@Override
	public ChannelAction<VoiceChannel> createCopy(@NotNull Guild guild) {
		return channel.createCopy(guild);
	}

	@NotNull
	@Override
	public ChannelAction<VoiceChannel> createCopy() {
		return channel.createCopy();
	}

	@NotNull
	@Override
	public ChannelManager getManager() {
		return channel.getManager();
	}

	@Override
	public int compareTo(@NotNull GuildChannel o) {
		return channel.compareTo(o);
	}

	@NotNull
	@Override
	public String getName() {
		return channel.getName();
	}

	@NotNull
	@Override
	public ChannelType getType() {
		return channel.getType();
	}

	@NotNull
	@Override
	public JDA getJDA() {
		return channel.getJDA();
	}

	@NotNull
	@Override
	public String getAsMention() {
		return channel.getAsMention();
	}

	@Override
	public long getIdLong() {
		return channel.getIdLong();
	}
}
