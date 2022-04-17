package PDA;

import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDA {
	// TODO: make patreonUrl a list so we can have multiple patreon pages to check (maybe hashmap<Integer(guild id), List> so it depends on the discord server its in)
	// public static String patreonUrl = "https://www.patreon.com/pda_example";
	public static HashMap<Guild, ArrayList<String>> patreonUrls = new HashMap<>();
	static String webhookUrl = ""; // https://discord.com/api/webhooks/958181437402644520/Nw6LLM7JGm176hDd6KgtUK3h3FXif-m7fRcnSAvyjrWP7p1lHuIhRJFTZ76RD1sHL0C4
	static String discordToken = "";
	static String discordChannel = "";

	// Global variables
	public static String prefix = "/";
	public static Set<Guild> guildSet = new HashSet<>();
	public static HashMap<Guild, LinkedList<PostCard>> publicPosts = new HashMap<>(), privatePosts = new HashMap<>();

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

			ArrayList<String> links = new ArrayList<>();
			links.add("https://www.patreon.com/pda_example");
			patreonUrls.put(id, links);
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

	public static boolean urlValid(String url){

		//Regex for a valid URL
		//The URL must start with either http or https and
		//    then followed by :// and
		//    then it must contain www. and
		//    then followed by subdomain of length (2, 256) and
		//    last part contains top level domain like .com, .org etc
		String reg = "((http|https)://)(www.)?"
				+ "[a-zA-Z0-9@:%._\\+~#?&//=]"
				+ "{2,256}\\.[a-z]"
				+ "{2,6}\\b([-a-zA-Z0-9@:%"
				+ "._\\+~#?&//=]*)";

		//compiles the regex
		Pattern p = Pattern.compile(reg);

		//If string is empty return false
		if(url == null){
			return false;
		}

		//find a match on the string
		Matcher m = p.matcher(url);

		//return the string if matched the regex
		return m.matches();

	}
}
