package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.Tomatentum.TomatenMaster.managers.Suggestion;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SuggestCommand implements GuildCommand {
	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		msg.delete().queue();
		if (member.hasPermission(Permission.MANAGE_CHANNEL)) {
			if (args[1].equals("setchannel")) {
				TomatenMaster.getINSTANCE().getConfig().getYML().set("Suggestions.suggestionchannel", channel.getIdLong());
				TomatenMaster.getINSTANCE().getConfig().save();
				return;
			}
		}
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 1; i < args.length; i++) {
			stringBuilder.append(args[i]).append(" ");
		}
		TextChannel sugchannel = channel.getGuild().getTextChannelById(TomatenMaster.getINSTANCE().getConfig().getYML().getLong("Suggestions.suggestionchannel"));
		new Suggestion(sugchannel, member, stringBuilder.toString());
	}
}
