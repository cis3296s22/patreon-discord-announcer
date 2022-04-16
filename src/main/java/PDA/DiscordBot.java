package PDA;

// import PDA.DiscordBotJoin;

import PDA.PostCard;
import PDA.EventListener;
import PDA.PDA;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Implementation of the JDA discord bot API.
 *
 * Responsibilities:
 *
 * 1) Initialize the discord bot with a given token
 * 2) Initialize containers for embeds, text channels, and posts per discord server
 * 3) Provide a layer of abstraction between the rest of the program and the JDA discord bot API to allow for sending messages and embeds to a server
 *
 */

public class DiscordBot {
	private JDA jda;
	private HashMap<Guild, EmbedBuilder> embedMap;
	private HashMap<Guild, TextChannel> channels;

	public DiscordBot(String token) throws LoginException, InterruptedException {
		embedMap = new HashMap<>();
		channels = new HashMap<>();

		//! setup JDA bot
		setupJDA(token);

		//! setup all the embeds for each guild the bot is in
		setupEmbeds();

		//! setup all text channels per discord server
		setupTextChannels();

		//! setup the container for private and public posts so it's not null
		setupPosts();

		System.out.println("channels: " + channels);
	}

	public void setTitle(String title, String url, Guild id) {
		embedMap.put(id, embedMap.get(id).setTitle(title, url));
	}

	public void addChannel(String channelId, Guild id) {

		// JDA has another overloaded getTextChannelById() method that uses a long instead of a String
		// for some reason it will print a NumberFormatException error when using the String version even though it works
		try {
			channels.put(id, jda.getTextChannelById(channelId));
		} catch (NumberFormatException ignored) {
		}
	}

	public void setDescription(String description, Guild id) {
		embedMap.put(id, embedMap.get(id).setDescription(description));
	}

	public void addField(String title, String value, Guild id) {
		embedMap.put(id, embedMap.get(id).addField(title, value, true));
	}

	public void setColor(Color color, Guild id) {
		embedMap.put(id, embedMap.get(id).setColor(color));
	}

	public void setFooter(String text, String userUrl, Guild id) {
		embedMap.put(id, embedMap.get(id).setFooter(text, userUrl));
	}

	public void send(Guild id) { // sending embed
		channels.get(id).sendMessageEmbeds(embedMap.get(id).build()).queue();
		embedMap.get(id).clear();
	}

	public void send(String text, Guild id) { // sending text
		channels.get(id).sendMessage(text).queue();
	}

	public void addGuild(Guild id) {
		embedMap.put(id, embedMap.getOrDefault(id, new EmbedBuilder()));
	}

	// function to help with testing
	public Set<Guild> getAllGuilds() {
		return embedMap.keySet();
	}

	// function to help with testing
	public JDA getJDA() {
		return this.jda;
	}

	private void setupJDA(String token) throws InterruptedException {
		try {
			jda = JDABuilder.createDefault(token).build();
		} catch (LoginException e) {
			System.out.println("The given Discord Bot token '" + token + "' is invalid!");
			System.exit(1);
		}
		jda.awaitReady();
		jda.addEventListener(new EventListener(this));
	}

	private void setupEmbeds() {
		for (Guild guild : jda.getGuilds()) {
			if (!embedMap.containsKey(guild)) {
				addGuild(guild);
				PDA.guildSet.add(guild);
			}
		}
	}

	private void setupTextChannels() {
		for (Guild guild : PDA.guildSet) {
			List<TextChannel> chanList = guild.getTextChannelsByName("testing", true);

			// if we can't find a "testing" then output to the first channel we find
			if (chanList.isEmpty()) {
				chanList = guild.getTextChannels();
			}
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
