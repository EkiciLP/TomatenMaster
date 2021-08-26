package net.Tomatentum.TomatenMaster.util;

import net.Tomatentum.TomatenMaster.TomatenMaster;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class SlashCommand extends ListenerAdapter {

	public static final List<SlashCommand> SLASH_COMMANDS = new ArrayList<>();
	private Command command;
	private Permission[] permissions;

	protected SlashCommand(String label, String description, Permission... permissions) {
		for (Guild guild : TomatenMaster.getINSTANCE().getBot().getGuilds()) {
			this.command = guild.upsertCommand(label, description).setDefaultEnabled(true).complete();
		}
		SLASH_COMMANDS.add(this);
		TomatenMaster.getINSTANCE().getBot().addEventListener(this);

		this.permissions = permissions;


	}

	protected abstract void execute(SlashCommandEvent command);


	@Override
	public void onSlashCommand(@NotNull SlashCommandEvent event) {
		if (command.getName().equals(event.getName())) {
			for (Permission permission : permissions) {
				if (event.getMember().hasPermission(permission)) {
					execute(event);
					return;
				}
			}

			event.reply("You are not permitted to use this command.").setEphemeral(true).queue();
		}
	}

	protected Command getCommand() {
		return command;
	}
}
