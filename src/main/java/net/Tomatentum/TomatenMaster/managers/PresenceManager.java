package net.Tomatentum.TomatenMaster.managers;

import net.Tomatentum.TomatenMaster.main.DiscordBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PresenceManager {

	private String[] status = new String[]{"!help", "%membercount% members"};
	private Random random;
	private int counter = 10;
	private DiscordBot bot;

	public PresenceManager(DiscordBot bot) {
		this.bot = bot;
		random = new Random();
		System.out.println("[ActivityManager] initialized");
		new Thread(this::initActivities).start();

	}

	public void initActivities() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				int i = random.nextInt(status.length);
				JDA jda = bot.getBot();
				String currentStatus = status[i].replace("%membercount%", "" + jda.getGuildById("835089895092387872").getMemberCount());
				switch (i) {
					case 0:
						jda.getPresence().setActivity(Activity.listening(currentStatus));
						break;
					case 1:
						jda.getPresence().setActivity(Activity.watching(currentStatus));
						break;
				}
			}
		}, 10000, 10000);
	}

}
