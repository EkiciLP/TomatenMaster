package net.Tomatentum.TomatenMaster.managers;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Timer;
import java.util.TimerTask;


public class PunishManager {
	private DiscordBot bot;
	public PunishManager(DiscordBot bot) {
		this.bot = bot;
	}
	public void muteMember(Guild guild, Member member, int MinuteTime, String reason) {
		guild.addRoleToMember(member, guild.getRoleById(843856295814889472L)).queue();
		int time = MinuteTime * 60000;
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.RED);
		builder.setTimestamp(OffsetDateTime.now());
		builder.setFooter(member.getGuild().getName(), member.getGuild().getIconUrl());
		builder.setDescription("You have been muted on " + member.getGuild().getName() + " for " + time/60 + "hours!\nReason: " + reason);
		member.getUser().openPrivateChannel().complete().sendMessage(builder.build()).queue();
		builder.clear();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				guild.removeRoleFromMember(member, guild.getRoleById(843856295814889472L)).queue();
			}
		}, time, time);
	}
	public void unmuteMember(Member member, Guild guild) {
		guild.removeRoleFromMember(member, guild.getRoleById(843856295814889472L)).queue();
	}
	public void banMember(Member member, int minuteTime, String reason) {
		int time = minuteTime*60000;
		EmbedBuilder bbuilder = new EmbedBuilder();
		bbuilder.setColor(Color.RED);
		bbuilder.setTimestamp(OffsetDateTime.now());
		bbuilder.setFooter(member.getGuild().getName(), member.getGuild().getIconUrl());
		bbuilder.setDescription("You have been banned on " + member.getGuild().getName() + " for " + time/60 + "hours!\nReason: "+ reason);
		member.getUser().openPrivateChannel().complete().sendMessage(bbuilder.build()).queue();
		bbuilder.clear();
		member.getGuild().ban(member, 1, reason).queue();
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				member.getGuild().unban(member.getUser()).queue();
			}
		}, time);
	}
	public void unBanMember(User user, Guild guild) {
		guild.unban(user).queue();
	}
}