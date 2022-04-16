package PDA.commands;

import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

/**
 * setchannel discord bot command.
 *
 * Responsibilities:
 *
 * 1) Check if a text channel ID was provided
 * 2) Check if the link is already in the list of links
 * 3) Set the text channel where the discord bot outputs depending on what text channel ID was provided
 *
 */

public class setchannel implements BotCommand {

	private String[] args = null;
	private Guild guild;

	@Override
	public void execute(DiscordBot bot) {
		if (args == null) {
			System.out.println("no arguments provided");
			return;
		}

		if (args.length <= 1) {
			bot.send("No link provided", guild);
		} else {
			bot.addChannel(args[1], guild);
			bot.send(args[1] + " has been set as the bot output channel", guild);
		}
	}

	@Override
	public void setArgs(String[] args) {
		this.args = args;
	}

	@Override
	public void setGuildID(Guild guild) {
		this.guild = guild;
	}

}
