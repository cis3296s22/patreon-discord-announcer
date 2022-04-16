package PDA.commands;

import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

/**
 * help discord bot command.
 *
 * Responsibilities:
 *
 * 1) Will print out a list of each discord bot command available through the PDA
 *
 */

public class help implements BotCommand {

	private Guild guild;

	@Override
	public void execute(DiscordBot bot) {
		bot.send("PDA commands:  /help  /setchannel  /addlink  /removelink  /showlinks", guild);
	}

	@Override
	public void setArgs(String[] args) {

	}

	@Override
	public void setGuildID(Guild guild) {
		this.guild = guild;
	}
}
