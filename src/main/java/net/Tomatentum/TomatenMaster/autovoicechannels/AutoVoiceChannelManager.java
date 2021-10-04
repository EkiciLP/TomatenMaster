package net.Tomatentum.TomatenMaster.autovoicechannels;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class AutoVoiceChannelManager extends ListenerAdapter {
	private TomatenMaster bot;
	private List<VoiceChannel> tempChannels;
	private HashMap<VoiceChannel, Member> ownerlist;

	private HashMap<Long, PrivateVoiceChannel> privateVoiceChannels;

	public AutoVoiceChannelManager(TomatenMaster bot) {
		this.bot = bot;
		this.tempChannels =  new ArrayList<>();
		this.ownerlist = new HashMap<>();
		this.privateVoiceChannels = new HashMap<>();

		TomatenMaster.getINSTANCE().getBot().addEventListener(this);

		startDeleteTimer();
		System.out.println("[AVCManager] initialized");
	}
	public boolean addTempChannel(Category category, Member member, Guild guild) {
		if (tempChannels.size() != 10) {
			VoiceChannel newchannel = category.createVoiceChannel("\uD83D\uDEAB" + member.getEffectiveName() + "'s Room").complete();
			guild.moveVoiceMember(member, newchannel).queue();
			tempChannels.add(newchannel);
			ownerlist.put(newchannel, member);
			System.out.println("[AVCManager] created TempChannel " + newchannel.getName());
			return true;
		}else
			return false;

	}
	public void removeTempChannel(VoiceChannel channel) {
		tempChannels.remove(channel);
		ownerlist.remove(channel);
		System.out.println("[AVCManager] removed TempChannel " + channel.getName());
		channel.delete().queue();

	}
	public void renameChannel(VoiceChannel channel, Member  memberleft) {
		if (ownerlist.containsValue(memberleft)) {
			Member newowner = channel.getMembers().get(0);
			ownerlist.put(channel, memberleft);
			channel.getManager().setName("\uD83D\uDEAB" + newowner.getEffectiveName() + "'s Room").queue();
			System.out.println("[AVCManager] renamed channel " + channel.getName() + " to " + "\uD83D\uDEAB" + newowner.getEffectiveName() + "'s Room");
		}
	}
	public void startDeleteTimer() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				List<VoiceChannel> vcs = new ArrayList<>();
				for (VoiceChannel channel : tempChannels) {
					if (channel.getMembers().size() == 0) {
						channel.delete().queue();
						vcs.add(channel);
						System.out.println("[AVCManager] removed TempChannel " + channel.getName());
					}
				}
				tempChannels.removeAll(vcs);
			}
		}, 50000, 30000);
	}

	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent e) {
		VoiceChannel channel = e.getChannelJoined();
		if (channel.getIdLong() == 846726050938355732L) {
			if (!bot.getAutoVoiceChannelManager().addTempChannel(e.getChannelJoined().getParent(),e.getMember(), e.getGuild())) {
				e.getGuild().kickVoiceMember(e.getMember()).queue();
				e.getMember().getUser().openPrivateChannel().complete().sendMessage("***Max Sessions reached***").queue();
			}
		}else if (channel.getIdLong() == 873558807979106326L) {
			new PrivateVoiceChannel(e.getMember(), e.getChannelJoined().getParent()).create();
		}
	}
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent e) {
		VoiceChannel channel = e.getChannelJoined();
		if (channel.getIdLong() == 846726050938355732L) {
			if (!bot.getAutoVoiceChannelManager().addTempChannel(e.getChannelJoined().getParent(),e.getMember(), e.getGuild())) {
				e.getGuild().kickVoiceMember(e.getMember()).queue();
				e.getMember().getUser().openPrivateChannel().complete().sendMessage("***Max Sessions reached***").queue();
			}
		}else if (channel.getIdLong() == 873558807979106326L) {
			new PrivateVoiceChannel(e.getMember(), e.getChannelJoined().getParent()).create();

		}else if (bot.getAutoVoiceChannelManager().getTempChannels().contains(e.getChannelLeft().getIdLong()) || e.getChannelLeft().getName().contains("'s Room")) {
			if (e.getChannelLeft().getMembers().size() <= 0) {
				bot.getAutoVoiceChannelManager().removeTempChannel(e.getChannelLeft());
			} else {
				if (bot.getAutoVoiceChannelManager().getTempChannels().contains(e.getChannelLeft().getIdLong())) {
					bot.getAutoVoiceChannelManager().renameChannel(e.getChannelLeft(), e.getMember());
				}
			}
		}
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent e) {
		if (bot.getAutoVoiceChannelManager().getTempChannels().contains(e.getChannelLeft().getIdLong()) || e.getChannelLeft().getName().contains("'s Room")) {
			if (e.getChannelLeft().getMembers().size() <= 0) {
				bot.getAutoVoiceChannelManager().removeTempChannel(e.getChannelLeft());
			}else {
				if (bot.getAutoVoiceChannelManager().getTempChannels().contains(e.getChannelLeft().getIdLong())) {
					bot.getAutoVoiceChannelManager().renameChannel(e.getChannelLeft(), e.getMember());
				}
			}
		}
	}

	public List<VoiceChannel> getTempChannels() {
		return tempChannels;
	}

	public HashMap<Long, PrivateVoiceChannel> getPrivateVoiceChannels() {
		return privateVoiceChannels;
	}
}
