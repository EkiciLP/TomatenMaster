package net.Tomatentum.TomatenMaster.main;

import
		net.Tomatentum.TomatenMaster.managers.TicketManager;
import net.Tomatentum.TomatenMaster.commands.*;
import net.Tomatentum.TomatenMaster.main.util.CommandManager;
import net.Tomatentum.TomatenMaster.commands.EditEmbedCommand;
import net.Tomatentum.TomatenMaster.listeners.*;
import net.Tomatentum.TomatenMaster.managers.*;
import net.Tomatentum.TomatenMaster.music.AudioManager;
import net.Tomatentum.TomatenMaster.music.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


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
	private static DiscordBot INSTANCE;
	private AudioManager audioManager;

	public DiscordBot() throws LoginException, InterruptedException {
		INSTANCE = this;
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


		buildbot.setMemberCachePolicy(MemberCachePolicy.ALL);
		buildbot.enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS);
		buildbot.enableIntents(GatewayIntent.GUILD_MESSAGES);
		buildbot.enableIntents(GatewayIntent.GUILD_MEMBERS);
		buildbot.enableIntents(GatewayIntent.GUILD_VOICE_STATES);
		buildbot.enableCache(CacheFlag.VOICE_STATE);


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
		audioManager = new AudioManager(this);

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
		cmdmanager.registerCommand("shutdown", new ShutDownCommand(this));
		cmdmanager.registerCommand("suggest", new SuggestCommand());
		cmdmanager.registerCommand("approve", new ApproveCommand());
		cmdmanager.registerCommand("reject", new RejectCommand());
		cmdmanager.registerCommand("play", new PlayCommand());
		cmdmanager.registerCommand("skip", new SkipCommand());
		cmdmanager.registerCommand("clearqueue", new ClearQueueCommand());
		cmdmanager.registerCommand("stop", new StopCommand());
		cmdmanager.registerCommand("loop", new LoopCommand());
		cmdmanager.registerCommand("shuffle", new ShuffleCommand());
		cmdmanager.registerCommand("queue", new QueueCommand());
		cmdmanager.registerCommand("volume", new VolumeCommand());
		cmdmanager.registerCommand("pause", new PauseCommand());
		cmdmanager.registerCommand("resume", new PauseCommand());

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

			getAudioManager().getMusicManagers().forEach((guild, musicManager) ->
					musicManager.getGuild().getAudioManager().closeAudioConnection()
			);

			bot.getPresence().setStatus(OnlineStatus.OFFLINE);
			System.out.println("Bot offline");
			bot.shutdown();
			System.exit(0);

		}
	}

	public static String getTimestamp(long milliseconds)
	{
		int seconds = (int) (milliseconds / 1000) % 60 ;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
		int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

		if (hours > 0)
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		else
			return String.format("%02d:%02d", minutes, seconds);
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

	public static DiscordBot getINSTANCE() {
		return INSTANCE;
	}

	public AudioManager getAudioManager() {
		return audioManager;
	}
}
