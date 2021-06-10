package net.Tomatentum.TomatenMaster.main.util;

import net.dv8tion.jda.api.entities.*;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

	public ConcurrentHashMap<String, GuildCommand> commands;
	public CommandManager() {
		this.commands = new ConcurrentHashMap<>();

	}

	public void registerCommand(String Command, GuildCommand provider){
		this.commands.put(Command, provider);
	}
	public boolean runCommand(String Command, Member member, TextChannel channel, Message msg, String [] args) {
		if (this.commands.containsKey(Command.toLowerCase())) {
			GuildCommand cmd = this.commands.get(Command.toLowerCase());
			System.out.println("[CMDManager] Ran >> " + Command);
			cmd.onCommand(member, channel, msg, args);



			return true;
		}
		return false;
	}
}
