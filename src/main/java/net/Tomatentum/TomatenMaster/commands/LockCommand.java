package net.Tomatentum.TomatenMaster.commands;

import net.Tomatentum.TomatenMaster.util.SlashCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class LockCommand extends SlashCommand {


	public LockCommand() {
		super("lock", "Locks the current channel which prevents users from sending Messages", Permission.MANAGE_CHANNEL);
	}

	@Override
	public void execute(SlashCommandEvent command) {
		TextChannel channel = command.getTextChannel();
		Role role = channel.getGuild().getRoleById(835089895092387872L);


		try {
			if (!channel.getPermissionOverride(role).getDenied().contains(Permission.MESSAGE_WRITE)) {
				channel.putPermissionOverride(role).deny(Permission.MESSAGE_WRITE).queue();
				command.reply("🔒 Channel locked by " + command.getMember().getAsMention()).queue();
			} else {
				channel.putPermissionOverride(role).grant(Permission.MESSAGE_WRITE).queue();
				command.reply("🔓 Channel unlocked by " + command.getMember().getAsMention()).queue();

			}
		}catch (NullPointerException ex) {
			channel.putPermissionOverride(role).deny(Permission.MESSAGE_WRITE).queue();
			command.reply("🔒 Channel locked by " + command.getMember().getAsMention()).queue();
		}


	}
}
