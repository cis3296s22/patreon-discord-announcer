package PDA;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.entities.Guild;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		log = (Logger) LoggerFactory.getLogger("PDA");

		log.info("Reading config.json...");
		discordToken = parseConfig();

		if (discordToken.isEmpty()) {
			log.error("config.json does not contain a 'TOKEN' key.");
			System.exit(1);
		}

		DiscordBot bot = new DiscordBot(discordToken);

		log.info("Reading posts.json...");
		JSONHelper.parseSavedData(bot, "posts.json");
		log.info("Loaded {} URLs and their guild posts.", postCards.size());

		log.info("Starting Selenium thread...");
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
	private static String parseConfig() {
		JSONObject configJson = JSONHelper.parseJSONFile("config.json");

		if (configJson == null) {
			log.error("An error occurred while reading config.json");
			System.exit(1);
		}

		try {
			return (String) configJson.get("TOKEN");
		} catch (JSONException e) {
			return "";
		}
	}

	/**
	 * Will save the announced post to the posts.json file
	 *
	 * @param patreonUrl is the patreonUrl that the post was created from
	 * @param guild is the reference to the guild that we want to save the posts for
	 * @param postCard is the object holding all the information from the post on patreon
	 */
	public static void saveAnnouncedPostCard(String patreonUrl, Guild guild, PostCard postCard) {
		JSONObject savedJson = JSONHelper.parseJSONFile("posts.json");

		if (savedJson == null) {
			log.error("An error occurred while reading posts.json");
			System.exit(1);
		}

		if (savedJson.has(patreonUrl)) { // * This Patreon URL already exists
			JSONObject guildIds = savedJson.getJSONObject(patreonUrl);

			saveInExistingPatreonURL(patreonUrl, guildIds, guild, postCard);
		} else { // * New Patreon URL
			JSONObject newGuildId = new JSONObject();
			JSONObject newPost = new JSONObject();

			// Put the post card details into the new post JSON Object
			newPost.put(postCard.getUrl(), JSONHelper.createPostJSONObject(postCard));

			// Put the new post JSON Object into the new guild ID JSON Object
			newGuildId.put(guild.getId(), newPost);

			// Put the new guild ID JSON Object into the master JSON file
			savedJson.put(patreonUrl, newGuildId);
		}

		// * Finally, save the new JSON file into posts.json
		try {
			FileWriter file = new FileWriter("posts.json");
			file.write(savedJson.toString());
			file.flush();
			file.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 *
	 * @param patreonUrl
	 * @param guildIds
	 * @param guild
	 * @param postCard
	 */
	private static void saveInExistingPatreonURL(String patreonUrl, JSONObject guildIds, Guild guild, PostCard postCard) {
		if (guildIds.has(guild.getId())) { // * This guild already contains posts from this Patreon URL
			JSONObject postUrls = guildIds.getJSONObject(guild.getId());

			if (postUrls.has(postCard.getUrl())) { // ! This guild already stored this post URL
				log.warn("The post tried storing a post that already exists.  Guild: '{}' -- URL: '{}'", guild.getId(), patreonUrl);
			} else { // * This is a new post URL
				postUrls.put(postCard.getUrl(), JSONHelper.createPostJSONObject(postCard));
			}
		} else { // * New guild ID for this Patreon URL
			JSONObject patreonUrlObject = new JSONObject();

			patreonUrlObject.put(postCard.getUrl(), JSONHelper.createPostJSONObject(postCard));
			guildIds.put(guild.getId(), patreonUrlObject);
		}
	}

	/**
	 * Allows the ability to add a {@link PostCard} object to the postCards HashMap that holds all sent posts to dis
	 *
	 * @param postCard holds a reference to a {@link PostCard} object to be added to the postCards HashMap
	 * @param guild    holds a reference to a {@link Guild} object that will be used to know which guild to attach the {@link PostCard} object to
	 */
	public static void addPostCard(PostCard postCard, Guild guild) {
		LinkedList<PostCard> cards = postCards.get(guild);

		if (!cards.contains(postCard))
			cards.add(postCard);

		postCards.put(guild, cards);
		log.info("Saved post URL '{}' to guild '{}'", postCard.getUrl(), guild.getId());
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