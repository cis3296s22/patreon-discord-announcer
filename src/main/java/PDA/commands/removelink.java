package PDA.commands;

import PDA.PDA;
import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;

/**
 * removelink discord bot command.
 * <p>
 * Responsibilities:
 * <p>
 * 1) Check if a link was provided
 * 2) Check if the link is not in the list of links
 * 3) Remove the guild from the list of guilds associated with the particular link
 */

public class removelink extends GenericBotCommand {

	@Override
	public void execute(DiscordBot bot) {
		if (args.length <= 1) {
			bot.send("No link provided", guild);
		} else {
			ArrayList<Guild> guilds;

			if ((guilds = PDA.patreonUrls.get(args[1])) == null) {
				bot.send(args[1] + " is not in the list of links", guild);
				return;
			}

			if (PDA.patreonUrls.containsKey(args[1]) && guilds.remove(guild)) {
				PDA.patreonUrls.put(args[1], guilds);
				bot.send(args[1] + " has been removed from the patreon link list", guild);
			}
		}
	}
}
