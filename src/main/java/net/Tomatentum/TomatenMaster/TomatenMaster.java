package net.Tomatentum.TomatenMaster;

import net.Tomatentum.TomatenMaster.autovoicechannels.AutoVoiceChannelManager;
import net.Tomatentum.TomatenMaster.commands.ticket.CloseCommand;
import net.Tomatentum.TomatenMaster.commands.ticket.ReOpenCommand;
import net.Tomatentum.TomatenMaster.commands.ticket.TicketAddCommand;
import net.Tomatentum.TomatenMaster.commands.ticket.TicketRemoveCommand;
import net.Tomatentum.TomatenMaster.database.Database;
import net.Tomatentum.TomatenMaster.managers.TicketManager;
import net.Tomatentum.TomatenMaster.commands.*;
import net.Tomatentum.TomatenMaster.punishments.PunishManager;
import net.Tomatentum.TomatenMaster.util.CommandManager;
import net.Tomatentum.TomatenMaster.commands.EditEmbedCommand;
import net.Tomatentum.TomatenMaster.listeners.*;
import net.Tomatentum.TomatenMaster.managers.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;


import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class TomatenMaster {
	private final TextChannel protocolChannel;
	private final JDA bot;
	private final CommandManager cmdmanager;
	private final AutoVoiceChannelManager autoVoiceChannelManager;
	private final TicketManager ticketManager;
	private final Config config;
	private final EmbedManager embedManager;
	private final PunishManager punishManager;
	private final ReactionRoleManager reactionRole;
	private static TomatenMaster INSTANCE;

	public TomatenMaster() throws LoginException, InterruptedException {

		INSTANCE = this;
		config = new Config();
		JDABuilder buildbot = JDABuilder.createDefault(config.getYML().getString("TOKEN"));
		buildbot.addEventListeners(new CommandListener(this));
		buildbot.addEventListeners(new ReactionListener(this));
		buildbot.addEventListeners(new GuildListener(this));
		buildbot.addEventListeners(new AutoModListener(this));
		buildbot.addEventListeners(new ReactionRoleManager(this));


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
		punishManager = new PunishManager(this);
		reactionRole = new ReactionRoleManager(this);
		protocolChannel = bot.getTextChannelById(835092656836050964L);


		registerCommands();

		exitlistener();


	}

	private void registerCommands() {
		cmdmanager.registerCommand(new ClearCommand());
		cmdmanager.registerCommand(new PanelCommand(this));
		cmdmanager.registerCommand(new CloseCommand(this));
		cmdmanager.registerCommand(new ReOpenCommand(this));
		cmdmanager.registerCommand(new TicketAddCommand(this));
		cmdmanager.registerCommand(new DeleteCommand());
		cmdmanager.registerCommand(new TicketRemoveCommand(this));
		cmdmanager.registerCommand(new CreateEmbedCommand(this));
		cmdmanager.registerCommand(new EditEmbedCommand(this));
		cmdmanager.registerCommand(new LockCommand());
		cmdmanager.registerCommand(new WarnCommand(this));
		cmdmanager.registerCommand(new WarningsCommand(this));
		cmdmanager.registerCommand(new MuteCommand(this));
		cmdmanager.registerCommand(new UnmuteCommand());
		cmdmanager.registerCommand(new BanCommand(this));
		cmdmanager.registerCommand(new ClearWarningsCommand());
		cmdmanager.registerCommand(new ReactionRoleCommand(this));
		cmdmanager.registerCommand(new SuggestCommand());
		cmdmanager.registerCommand(new ApproveCommand());
		cmdmanager.registerCommand(new RejectCommand());


	}


	public static void main(String[] args) {
		try {
			new TomatenMaster();
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
			Database.close();
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

	public PunishManager getPunishManager() {
		return punishManager;
	}

	public ReactionRoleManager getReactionRole() {
		return reactionRole;
	}

	public static TomatenMaster getINSTANCE() {
		return INSTANCE;
	}


	public TextChannel getProtocolChannel() {
		return protocolChannel;
	}
}
