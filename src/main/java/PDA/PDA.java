package PDA;

import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

public class PDA {
	// TODO: make patreonUrl a list so we can have multiple patreon pages to check (maybe hashmap<Integer(guild id), List> so it depends on the discord server its in)
	// public static String patreonUrl = "https://www.patreon.com/pda_example";
	public static HashMap<Guild, ArrayList<String>> patreonUrls = new HashMap<>();
	static String webhookUrl = ""; // https://discord.com/api/webhooks/958181437402644520/Nw6LLM7JGm176hDd6KgtUK3h3FXif-m7fRcnSAvyjrWP7p1lHuIhRJFTZ76RD1sHL0C4
	static String discordToken = "";

	// Global variables
	public static String prefix = "/";
	public static Set<Guild> guildSet = new HashSet<>();
	public static HashMap<Guild, LinkedList<PostCard>> publicPosts = new HashMap<>(), privatePosts = new HashMap<>();

	public static void main(String[] arg) throws InterruptedException, LoginException {
		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("config.json"));
			Object token = jsonObject.get("TOKEN");
			discordToken = token.toString().replaceAll("[\\[\\](){}]", "");
		} catch (FileNotFoundException e) {
			System.out.println("The configuration file 'config.json' was not found!");
			System.exit(1);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		DiscordBot bot = new DiscordBot(discordToken);


		// bot.addChannel(guild, discordChannel);

		for (Guild id : guildSet){ // initialization of all patreon links to example patreon

			ArrayList<String> links = new ArrayList<>();
			links.add("https://www.patreon.com/pda_example");
			patreonUrls.put(id, links);
		}

		System.out.println("PatreonURLs: " + patreonUrls);

		PatreonThread testThread = new PatreonThread(webhookUrl, bot);
		testThread.start();
		testThread.join();

		System.out.println("Finished!");
	}
}
