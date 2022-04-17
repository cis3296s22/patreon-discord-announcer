package PDA;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Patreon Discord Announcer startup implementation.
 * <p>
 * Responsibilities:
 * <p>
 * 1) Parse config file for discord bot token
 * 2) Declare and instantiate DiscordBot object
 * 3) Declare and instantiate PatreonThread object
 */

public class PDA {

	// Global variables
	/**
	 * patreonUrls holds each of the patreon links mapped to each server that is using that patreon link
	 */
	public static HashMap<String, ArrayList<Guild>> patreonUrls = new HashMap<>();
	/**
	 * prefix is used to denote a command in a discord message
	 */
	public static String prefix = "/";
	/**
	 * guildSet holds a HashSet of every discord server that is connected to the program
	 */
	public static Set<Guild> guildSet = new HashSet<>();
	/**
	 * postCards holds each of the discord servers mapped to all the posts that each unique discord server has announced
	 */
	public static HashMap<Guild, LinkedList<PostCard>> postCards = new HashMap<>();
	/**
	 * discordToken holds the value of the discordToken provided to the program
	 */
	static String discordToken = "";
	/**
	 * log holds the reference to a {@link Logger} object to output clean messages to the console
	 */
	static Logger log;

	/**
	 * Main method that will declare and initialize the {@link DiscordBot} and {@link PatreonThread} objects
	 *
	 * @param args holds command line arguments
	 * @throws InterruptedException in case a thread is interrupted
	 * @throws LoginException       in case the login for the discord bot token doesn't work
	 */
	public static void main(String[] args) throws InterruptedException, LoginException {
		parseConfig();

		log = (Logger) LoggerFactory.getLogger("PDA");
		DiscordBot bot = new DiscordBot(discordToken);

		PatreonThread testThread = new PatreonThread(bot);

		testThread.start();
		testThread.join();

		log.error("An error has occurred.  Stopping...");
		bot.getJDA().shutdown();
		System.exit(1);
	}

	/**
	 * parseConfig method that will read through the config.json file and get the discord token needed for initialization
	 */
	private static void parseConfig() {
		JSONParser parser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("config.json"));
			Object token = jsonObject.get("TOKEN");
			discordToken = token.toString().replaceAll("[\\[\\](){}]", "");
		} catch (FileNotFoundException e) {
			log.error("The configuration file 'config.json' was not found!");
			System.exit(1);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Allows the ability to add a {@link PostCard} object to the postCards HashMap that holds all sent posts to dis
	 *
	 * @param postCard holds a reference to a {@link PostCard} object to be added to the postCards HashMap
	 * @param id holds a reference to a {@link Guild} object that will be used to know which guild to attach the {@link PostCard} object to
	 */
	public static void addPostCard(PostCard postCard, Guild id){
		LinkedList<PostCard> cards = postCards.get(id);

		if (!cards.contains(postCard)){
			cards.add(postCard);
		}

		postCards.put(id, cards);
	}
}