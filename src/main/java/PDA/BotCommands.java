package PDA;

import PDA.apis.DiscordBot;
import PDA.commands.*;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

// TODO: have an admin set what channel they want the patreon messages to go
// TODO: only allow admins (or admin selected users) to use PDA.commands

public class BotCommands extends ListenerAdapter {

	private final DiscordBot bot;

	public BotCommands(DiscordBot bot) {
		this.bot = bot;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
//		bot.setChannel(String.valueOf(event.getChannel()));

//		for (int i = 0; i < commands.length; i++)
//			commands[i] = Settings.prefix + commands[i];

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
//        if (command != null) {
		command.setGuildID(event.getGuild());
		command.setArgs(args);
		command.execute(bot);
//        }
	}

	@Override
	public void onGuildJoin(@NotNull GuildJoinEvent event){
		System.out.println("Server added to list for guild: " + event.getGuild().getName());
		bot.addGuild(event.getGuild());
		PDA.patreonUrls.put(event.getGuild(), "https://www.patreon.com/pda_example");
		PDA.guildSet.add(event.getGuild());
		bot.addChannel(event.getGuild().getTextChannels().get(0).getId(), event.getGuild());
	}
}
