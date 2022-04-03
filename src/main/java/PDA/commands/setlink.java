package PDA.commands;

import PDA.PDA;
import PDA.apis.DiscordBot;

public class setlink implements BotCommand {

	String[] args = null;

	@Override
	public void execute(DiscordBot bot) {
		if (args == null)
			return;

		if (args.length <= 1) {
			bot.send("No link provided");
		} else {
			PDA.patreonUrl = args[1];
			bot.send(args[1] + "has been set as the patreon link");
		}
	}

	@Override
	public void setArgs(String[] args) {
		this.args = args;
	}
}
