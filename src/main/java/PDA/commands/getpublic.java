package PDA.commands;

import PDA.apis.DiscordBot;

public class getpublic implements BotCommand {
	@Override
	public void execute(DiscordBot bot) {
		bot.setTitle("Public Posts:");
		bot.setDescription("Description");
		bot.addField("Field title", "Field value");

		bot.send();
		bot.clearEmbed();
	}

	@Override
	public void setArgs(String[] args) {

	}
}
