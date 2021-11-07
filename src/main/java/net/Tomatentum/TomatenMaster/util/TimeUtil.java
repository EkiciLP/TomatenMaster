package net.Tomatentum.TomatenMaster.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TimeUtil {

	public static String getTimestamp(long milliseconds)
	{
		int seconds = (int) (milliseconds / 1000) % 60 ;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
		int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

		if (hours > 0)
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		else if (minutes > 0)
			return String.format("%02d:%02d", minutes, seconds);
		else
			return String.format("%02d seconds", seconds);
	}

	public static String getDate(long milliseconds) {
		return DateTimeFormatter
				.ofPattern("dd.MM.uuuu   HH:mm")
				.withLocale(Locale.UK)
				.withZone(ZoneId.of("UTC"))
				.format(Instant.ofEpochMilli(milliseconds));
	}
}
