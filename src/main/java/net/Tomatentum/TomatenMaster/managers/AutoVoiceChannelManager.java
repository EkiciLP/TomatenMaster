package net.Tomatentum.TomatenMaster.managers;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.entities.*;

import java.util.*;

public class AutoVoiceChannelManager {
	private DiscordBot bot;
	private List<VoiceChannel> tempChannels;
	private HashMap<VoiceChannel, Member> ownerlist;

	public AutoVoiceChannelManager(DiscordBot bot) {
		this.bot = bot;
		this.tempChannels =  new ArrayList<>();
		this.ownerlist = new HashMap<>();
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

	//


	public List<VoiceChannel> getTempChannels() {
		return tempChannels;
	}
}
