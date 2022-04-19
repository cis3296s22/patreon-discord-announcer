package PDA.commands;

import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

/**
 * Implementation of the abstract class for all bot commands
 * <p>
 * Responsibilities:
 * <p>
 * 1) Initialize the arguments per bot command
 * 2) Initialize the discord server id per bot command
 */

public abstract class GenericBotCommand implements BotCommand {
	/**
	 * args holds the arguments given to the bot command
	 */
	protected String[] args = null;
	/**
	 * guild holds the reference to the discord server that we will output the command's output to
	 */
	protected Guild guild = null;

	/**
	 * Abstract so each bot command will implement it their own way, execute() will be where each bot command differs in implementation
	 *
	 * @param bot holds the reference to the singular {@link DiscordBot} object
	 */
	@Override
	public abstract void execute(DiscordBot bot);

	/**
	 * set the arguments for the bot command
	 *
	 * @param args args holds the arguments given to the command
	 */
	@Override
	public void setArgs(String[] args) {
		this.args = args;
	}

	/**
	 * set the discord server id for the bot command
	 *
	 * @param guild holds the reference to the discord server id for the bot command
	 */
	@Override
	public void setGuildID(Guild guild) {
		this.guild = guild;
	}
}
