package net.Tomatentum.TomatenMaster.util;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface GuildCommand {
	public void onCommand(Member member, TextChannel channel, Message msg, String [] args);
}
