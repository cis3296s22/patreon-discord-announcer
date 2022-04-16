package PDA;

import PDA.commands.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Asynchronous listener that waits for events to happen in a discord server.
 *
 * Responsibilities:
 *
 * 1) Wait for MessageReceivedEvents that occur for every message sent in a discord server
 * 2) Parse the message sent and see if a command recognized by the program was sent and act accordingly
 * 3) Wait for GuildJoinEvents that occur everytime a bot is added to the server during runtime
 * 4) Set up the objects needed for the PDA when a bot is added during runtime
 *
 */

public class EventListener extends ListenerAdapter {

	private final DiscordBot bot;
	public boolean commandRan = false;

	public EventListener(DiscordBot bot) {
		this.bot = bot;
	}

	//! When a message is sent to the discord
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {

		String[] args = event.getMessage().getContentRaw().split("\\s+"); // changing each word in a message to arguments separated by spaces

		int prefixLength = PDA.prefix.length();

		// Return if args array is invalid or if the string given is too short (prefix + 3 characters)
		if (args.length == 0 || args[0].length() < prefixLength + 3)
			return;

		// Command container
		BotCommand command;

		// use Class.forName to generate a new class with the given arguments, cutting out the prefix and case sensitivity.
		try {
			Class<?> clazz = Class.forName("PDA.commands." + args[0].substring(prefixLength).toLowerCase());
			command = (BotCommand) clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			// Ignore any exceptions as we don't care if someone puts an invalid command name
			return;
//			e.printStackTrace();
		}

		// command variable should never be null as we will never reach here if it is null
		command.setGuildID(event.getGuild());
		command.setArgs(args);
		command.execute(bot);
	}

	//! When a discord bot is added to a server while the program is running
	@Override
	public void onGuildJoin(@NotNull GuildJoinEvent event) {
		// adding to guild list/set
		System.out.println("Server added to list for guild: " + event.getGuild().getName());
		bot.addGuild(event.getGuild());
		PDA.guildSet.add(event.getGuild());

		// adding patreonUrl
//		ArrayList<String> links = new ArrayList<>();
//		// links.add("https://www.patreon.com/pda_example");
//		PDA.patreonUrls.put("", event.getGuild());

		// adding channel
		bot.addChannel(event.getGuild().getTextChannels().get(0).getId(), event.getGuild());

		// adding to private/public posts container
		LinkedList<PostCard> temp = new LinkedList<>();
		PDA.postCards.put(event.getGuild(), temp);
//		PDA.publicPosts.put(event.getGuild(), temp);
//		PDA.privatePosts.put(event.getGuild(), temp);
		commandRan = true;
	}

}
