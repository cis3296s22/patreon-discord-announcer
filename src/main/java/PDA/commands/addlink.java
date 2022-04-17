package PDA.commands;

import PDA.PDA;
import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;

/**
 * addlink discord bot command.
 * <p>
 * Responsibilities:
 * <p>
 * 1) Check if a link was provided
 * 2) Check if the link is already in the list of links
 * 3) Add the guild to the list of guilds associated with the particular link
 */

public class addlink extends GenericBotCommand {

	/**
	 * Adds a patreonUrl link to the HashMap patreonUrls mapped to the guild that issued the command
	 *
	 * @param bot holds the reference to the singular {@link DiscordBot} object
	 */
	@Override
	public void execute(DiscordBot bot) {
		if (args.length <= 1) {
			bot.send("No link provided", guild);
		} else {
			ArrayList<Guild> guilds;

			if (PDA.patreonUrls.containsKey(args[1])) {
				guilds = PDA.patreonUrls.get(args[1]);

				if (guilds.contains(guild)) {
					bot.send(args[1] + " is already in the list of links", guild);
				} else {
					guilds.add(guild);
					bot.send(args[1] + " has been added to the list of links", guild);
					PDA.patreonUrls.put(args[1], guilds);
				}
			} else {
				guilds = new ArrayList<>();
				guilds.add(guild);
				PDA.patreonUrls.put(args[1], guilds);
				bot.send(args[1] + " has been added to the list of links", guild);
			}
		}
	}
}
