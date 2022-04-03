package PDA.commands;

import PDA.apis.DiscordBot;

public class test implements BotCommand {
	@Override
	public void execute(DiscordBot bot) {
		bot.send("I AM HERE");
	}

	@Override
	public void setArgs(String[] args) {

	}
}
