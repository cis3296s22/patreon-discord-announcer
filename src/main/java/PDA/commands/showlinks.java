package PDA.commands;

import PDA.DiscordBot;
import PDA.PDA;

/**
 * showlinks discord bot command.
 * <p>
 * Responsibilities:
 * <p>
 * 1) Check if the user has any patreon links currently being tracked for their discord server
 * 2) Send an embed holding the list of patreon links that the discord server is currently tracking
 */

public class showlinks extends GenericBotCommand {

	/**
	 * Prints out the list of all links added to the discord server that issued the command
	 *
	 * @param bot holds the reference to the singular {@link DiscordBot} object
	 */
	@Override
	public void execute(DiscordBot bot) {
		StringBuilder linkContainer = new StringBuilder("");

		for (String patreonUrl : PDA.patreonUrls.keySet()) {
			if (PDA.patreonUrls.get(patreonUrl).contains(guild)) {
				linkContainer.append(patreonUrl).append("\n");
			}
		}
		if (linkContainer.length() == 0)
			linkContainer.append("no links added");

		synchronized (bot){
			bot.setTitle("Links", "", guild);
			bot.setDescription(linkContainer.toString(), guild);
			bot.send(guild);
		}
	}
}
