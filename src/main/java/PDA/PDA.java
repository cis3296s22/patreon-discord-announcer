package PDA;

import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PDA {

	// Global variables
	// public static HashMap<Guild, ArrayList<String>> patreonUrls = new HashMap<>(); // https://www.patreon.com/pda_example or https://www.patreon.com/alexzwicky
	public static HashMap<String, ArrayList<Guild>> patreonUrls = new HashMap<>();
	public static String prefix = "/";
	public static Set<Guild> guildSet = new HashSet<>();
	public static HashMap<Guild, LinkedList<PostCard>> postCards = new HashMap<>();

	static String discordToken = "";

	public static void main(String[] arg) throws InterruptedException, LoginException {

		parseConfig();

		DiscordBot bot = new DiscordBot(discordToken);

		PatreonThread testThread = new PatreonThread(bot);
		testThread.start();
		testThread.join();

		System.out.println("Finished!");
	}

	private static void parseConfig(){
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
	}
}
