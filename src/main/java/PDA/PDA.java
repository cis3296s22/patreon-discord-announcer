package PDA;

import PDA.apis.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PDA {
	// TODO: make patreonUrl a list so we can have multiple patreon pages to check (maybe hashmap<Integer(guild id), List> so it depends on the discord server its in)
	// public static String patreonUrl = "https://www.patreon.com/pda_example";
	public static HashMap<Guild, String> patreonUrls = new HashMap<>();
	static String webhookUrl = ""; // https://discord.com/api/webhooks/958181437402644520/Nw6LLM7JGm176hDd6KgtUK3h3FXif-m7fRcnSAvyjrWP7p1lHuIhRJFTZ76RD1sHL0C4
	static String discordToken = "";
	static String discordChannel = "";

	// Global variables
	public static String prefix = "/";
	public static Set<Guild> guildSet = new HashSet<>();
	public static List<String> publicPosts = new LinkedList<>();
	public static List<String> privatePosts = new LinkedList<>();

	public static void main(String[] arg) throws InterruptedException, LoginException {
		disableLoggingOutput();

		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("config.json"));
			System.out.println(jsonObject);
			Object token = jsonObject.get("TOKEN");
			discordToken = token.toString().replaceAll("[\\[\\](){}]", "");
//			discordToken = discordToken.replaceAll("[\\[\\](){}]", "");
			System.out.println(discordToken);
			Object channel = jsonObject.get("Channel");
			discordChannel = channel.toString().replaceAll("[\\[\\](){}]", "");
//			discordChannel = discordChannel.replaceAll("[\\[\\](){}]", "");
			System.out.println(discordChannel);
		} catch (FileNotFoundException e) {
			System.out.println("The configuration file 'config.json' was not found!");
			System.exit(1);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		DiscordBot bot = new DiscordBot(discordToken, discordChannel);

		// TODO: add guild(discord server id) to the config file
		// bot.addChannel(guild, discordChannel);

		for (Guild id : guildSet){ // initialization of all patreon links to example patreon
			patreonUrls.put(id, "https://www.patreon.com/pda_example");
		}

		System.out.println("PatreonURLs: " + patreonUrls);

		PatreonThread testThread = new PatreonThread(webhookUrl, bot, discordChannel);
		testThread.start();
		testThread.join();

		System.out.println("Finished!");
	}

	private static void disableLoggingOutput() {
		Logger.getLogger("org.openqa.selenium").setLevel(Level.OFF);
		System.setProperty("webdriver.chrome.silentOutput", "true");
	}
}
