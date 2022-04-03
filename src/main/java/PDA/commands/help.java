package PDA.commands;

import PDA.apis.DiscordBot;

public class help implements BotCommand {

	@Override
	public void execute(DiscordBot bot) {
		bot.send("PDA commands:  /help  /test  /embed  /getpublicposts");
	}

	@Override
	public void setArgs(String[] args) {

	}
}
