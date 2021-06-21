package net.Tomatentum.TomatenMaster.main;

import net.Tomatentum.TomatenMaster.manager.TicketManager;
import net.Tomatentum.TomatenMaster.commands.*;
import net.Tomatentum.TomatenMaster.main.util.CommandManager;
import net.Tomatentum.TomatenMaster.commands.EditEmbedCommand;
import net.Tomatentum.TomatenMaster.listeners.*;
import net.Tomatentum.TomatenMaster.manager.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;


import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class DiscordBot {
	private JDABuilder buildbot;
	private JDA bot;
	private CommandManager cmdmanager;
	private AutoVoiceChannelManager autoVoiceChannelManager;
	private TicketManager ticketManager;
	private Config config;
	private EmbedManager embedManager;
	private PresenceManager presenceManager;
	private WarningManager warningManager;
	private PunishManager punishManager;
	private ReactionRoleManager reactionRole;

	public DiscordBot() throws LoginException, InterruptedException {
		config = new Config();
		buildbot = JDABuilder.createDefault(config.getYML().getString("TOKEN"));
		buildbot.setStatus(OnlineStatus.IDLE);
		buildbot.addEventListeners(new CommandListener(this));
		buildbot.addEventListeners(new VoiceListener(this));
		buildbot.addEventListeners(new ReactionListener(this));
		buildbot.addEventListeners(new GuildListener(this));
		buildbot.addEventListeners(new AutoModListener(this));
		buildbot.addEventListeners(new ReactionRoleManager(this));
		buildbot.addEventListeners(new ProtocolManager(this));

		buildbot.enableIntents(GatewayIntent.GUILD_MEMBERS);
		bot = buildbot.build();
		bot.awaitReady();
		System.out.println("[TomatenMaster] initialized");
		cmdmanager = new CommandManager();
		autoVoiceChannelManager = new AutoVoiceChannelManager(this);
		ticketManager = new TicketManager(this);
		embedManager = new EmbedManager(this);
		presenceManager = new PresenceManager(this);
		warningManager = new WarningManager(this);
		punishManager = new PunishManager(this);
		reactionRole = new ReactionRoleManager(this);
		cmdmanager.registerCommand("clear", new ClearCommand());
		cmdmanager.registerCommand("panel",new PanelCommand(this));
		cmdmanager.registerCommand("close", new CloseCommand(this));
		cmdmanager.registerCommand("reopen", new ReOpenCommand(this));
		cmdmanager.registerCommand("add", new TicketAddCommand(this));
		cmdmanager.registerCommand("delete", new DeleteCommand());
		cmdmanager.registerCommand("remove", new TicketRemoveCommand(this));
		cmdmanager.registerCommand("createembed", new CreateEmbedCommand(this));
		cmdmanager.registerCommand("editembed", new EditEmbedCommand(this));
		cmdmanager.registerCommand("help", new HelpCommand(this));
		cmdmanager.registerCommand("new", new NewTicketCommand(this));
		cmdmanager.registerCommand("lock", new LockCommand());
		cmdmanager.registerCommand("autorole", new AutoRoleCommand(this));
		cmdmanager.registerCommand("welcome", new WelcomeCommand(this));
		cmdmanager.registerCommand("warn", new WarnCommand(this));
		cmdmanager.registerCommand("warnings", new WarningsCommand(this));
		cmdmanager.registerCommand("mute", new MuteCommand(this));
		cmdmanager.registerCommand("ban", new BanCommand(this));
		cmdmanager.registerCommand("rr", new ReactionRoleCommand(this));
		exitlistener();


	}
	public static void main(String[] args) {
		try {
			new DiscordBot();
		}catch (LoginException | InterruptedException e) {
			e.printStackTrace();
			System.out.println("Bot could not be loaded!");
		}
	}


	public void exitlistener() {
		Scanner scanner = new Scanner(System.in);
		if (scanner.nextLine().equals("exit")) {
			bot.getPresence().setStatus(OnlineStatus.OFFLINE);
			System.out.println("Bot offline");
			bot.shutdown();
			System.exit(0);

		}
	}

	//getters/setters


	public CommandManager getCmdmanager() {
		return cmdmanager;
	}

	public JDA getBot() {
		return bot;
	}

	public AutoVoiceChannelManager getAutoVoiceChannelManager() {
		return autoVoiceChannelManager;
	}

	public TicketManager getTicketManager() {
		return ticketManager;
	}

	public Config getConfig() {
		return config;
	}

	public EmbedManager getEmbedManager() {
		return embedManager;
	}

	public WarningManager getWarningManager() {
		return warningManager;
	}

	public PunishManager getPunishManager() {
		return punishManager;
	}

	public ReactionRoleManager getReactionRole() {
		return reactionRole;
	}
}
