package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.Tomatentum.TomatenMaster.main.util.GuildCommand;
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
				DiscordBot.getINSTANCE().getConfig().getYML().set("Suggestions.suggestionchannel", channel.getIdLong());
				DiscordBot.getINSTANCE().getConfig().save();
				return;
			}
		}
		msg.delete().queue();
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 1; i < args.length; i++) {
			stringBuilder.append(args[i]).append(" ");
		}
		TextChannel sugchannel = channel.getGuild().getTextChannelById(DiscordBot.getINSTANCE().getConfig().getYML().getLong("Suggestions.suggestionchannel"));
		new Suggestion(sugchannel, member, stringBuilder.toString());
	}
}
