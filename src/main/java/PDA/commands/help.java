package PDA.commands;

import PDA.DiscordBot;

/**
 * help discord bot command.
 *
 * Responsibilities:
 *
 * 1) Will print out a list of each discord bot command available through the PDA
 *
 */

public class help extends GenericBotCommand {

	/**
	 * Prints out the list of commands available to be used by a user
	 *
	 * @param bot holds the reference to the singular {@link DiscordBot} object
	 */
	@Override
	public void execute(DiscordBot bot) {
		synchronized (bot){
			bot.setTitle("PDA Commands", null, guild);
			bot.setDescription("/help\n/setchannel\n/addlink\n/removelink\n/showlinks\n/getpublicposts\n/getprivateposts", guild);
			bot.send(guild);
		}
	}
}
