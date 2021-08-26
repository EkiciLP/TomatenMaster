package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.Tomatentum.TomatenMaster.util.GuildCommand;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class ShutDownCommand implements GuildCommand {
	private TomatenMaster bot;

	public ShutDownCommand(TomatenMaster bot) {
		this.bot = bot;
	}


	@Override
	public void onCommand(Member member, TextChannel channel, Message msg, String[] args) {
		if (member.hasPermission(Permission.MANAGE_SERVER)) {
			channel.sendMessage("```diff\nThe Bot has been shut down!\n```").complete();
			bot.getBot().getPresence().setStatus(OnlineStatus.OFFLINE);

			bot.getBot().shutdownNow();
			System.exit(0);
		}
	}
}
