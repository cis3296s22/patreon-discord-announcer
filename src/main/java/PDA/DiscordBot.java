package PDA;

// import PDA.DiscordBotJoin;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the JDA discord bot API.
 * <p>
 * Responsibilities:
 * <p>
 * 1) Initialize the discord bot with a given token
 * 2) Initialize containers for embeds, text channels, and posts per discord server
 * 3) Provide a layer of abstraction between the rest of the program and the JDA discord bot API to allow for sending messages and embeds to a server
 */

public class DiscordBot {
	/**
	 * jda holds the reference to the Java Discord API
	 */
	private JDA jda;
	/**
	 * embedMap holds the mapping from each discord server to its own {@link EmbedBuilder} object
	 */
	private final HashMap<Guild, EmbedBuilder> embedMap;
	/**
	 * channels holds the mapping from each discord server to its own {@link TextChannel} object, so it knows where to output messages
	 */
	private final HashMap<Guild, TextChannel> channels;
	/**
	 * log holds the reference to a {@link Logger} object to output clean messages to the console
	 */
	public final Logger log;

	/**
	 * Constructor initializes instance variables, sets up the JDA bot, initializes each {@link EmbedBuilder} in embedMap, and initializes each {@link TextChannel} in channels
	 *
	 * @param token is the discord bot token that allows us to initialize a discord bot
	 *
	 * @throws InterruptedException in case a thread is interrupted
	 * @throws LoginException 		in case the login for the discord bot token doesn't work
	 */
	public DiscordBot(String token) throws LoginException, InterruptedException {
		this.log = (Logger) LoggerFactory.getLogger(this.getClass().getName());
		this.log.info("Initializing Discord Bot...");

		this.embedMap = new HashMap<>();
		this.channels = new HashMap<>();

		//! setup JDA bot
		setupJDA(token);

		//! setup all the embeds for each guild the bot is in
		setupEmbeds();

		//! setup all text channels per discord server
		setupTextChannels();

		//! setup the container for private and public posts so it's not null
		setupPosts();

		this.log.info("Servers containing PDA: " + channels);
	}

	/**
	 *
	 *
	 * @param title
	 * @param url
	 * @param id
	 */
	public void setTitle(String title, String url, Guild id) {
		this.embedMap.put(id, this.embedMap.get(id).setTitle(title, url));
	}

	public void addChannel(String channelId, Guild id) {

		// JDA has another overloaded getTextChannelById() method that uses a long instead of a String
		// for some reason it will print a NumberFormatException error when using the String version even though it works
		try {
			this.channels.put(id, jda.getTextChannelById(channelId));
		} catch (NumberFormatException ignored) {
		}
	}

	public void setDescription(String description, Guild id) {
		this.embedMap.put(id, this.embedMap.get(id).setDescription(description));
	}

	public void addField(String title, String value, Guild id) {
		this.embedMap.put(id, this.embedMap.get(id).addField(title, value, true));
	}

	public void setColor(Color color, Guild id) {
		this.embedMap.put(id, this.embedMap.get(id).setColor(color));
	}

	public void setFooter(String text, String userUrl, Guild id) {
		this.embedMap.put(id, this.embedMap.get(id).setFooter(text, userUrl));
	}

	public void send(Guild id) { // sending embed
		this.channels.get(id).sendMessageEmbeds(this.embedMap.get(id).build()).queue();
		this.embedMap.get(id).clear();
	}

	public synchronized void send(String text, Guild id) { // sending text
		this.channels.get(id).sendMessage(text).queue();
	}

	public void addGuild(Guild id) {
		this.embedMap.put(id, this.embedMap.getOrDefault(id, new EmbedBuilder()));
	}

	// function to help with testing
	public Set<Guild> getAllGuilds() {
		return this.embedMap.keySet();
	}

	// function to help with testing
	public JDA getJDA() {
		return this.jda;
	}

	private void setupJDA(String token) throws InterruptedException {
		try {
			this.jda = JDABuilder.createDefault(token).build();
			this.jda.getPresence().setActivity(Activity.playing("Type /help"));
		} catch (LoginException e) {
			this.log.error("The given Discord Bot token '{}' is invalid!", token);
			System.exit(1);
		}
		this.jda.awaitReady();
		this.jda.addEventListener(new EventListener(this));
	}

	private void setupEmbeds() {
		for (Guild guild : this.jda.getGuilds()) {
			if (!this.embedMap.containsKey(guild)) {
				addGuild(guild);
				PDA.guildSet.add(guild);
			}
		}
	}

	private void setupTextChannels() {
		for (Guild guild : PDA.guildSet) {
			List<TextChannel> chanList = new LinkedList<>(); //guild.getTextChannelsByName("testing", true);

			// if we can't find a "testing" then output to the first channel we find
			if (chanList.isEmpty()) {
				chanList = guild.getTextChannels();
			}

			if (!chanList.isEmpty())
				addChannel(chanList.get(0).getId(), guild);
		}
	}

	private void setupPosts() {
		for (Guild guild : PDA.guildSet) {
			LinkedList<PostCard> temp = new LinkedList<>();

			// TODO: when we save the posts we will take it from there instead
			PDA.postCards.put(guild, temp);
		}
	}
}