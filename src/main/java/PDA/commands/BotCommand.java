package PDA.commands;

import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

/**
 * Interface for the bot commands
 */

public interface BotCommand {

	/**
	 * execute the bot command
	 *
	 * @param bot holds the reference to the singular {@link DiscordBot} object
	 */
	void execute(DiscordBot bot);

	/**
	 * set the arguments for the bot command
	 *
	 * @param args holds the arguments given to the command
	 */
	void setArgs(String[] args);

	/**
	 * set the discord server id for the bot command
	 *
	 * @param guild holds the reference to the discord server id for the bot command
	 */
	void setGuildID(Guild guild);
}
