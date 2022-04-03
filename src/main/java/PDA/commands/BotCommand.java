package PDA.commands;

import PDA.apis.DiscordBot;

public interface BotCommand {
	void execute(DiscordBot bot);

	void setArgs(String[] args);
}
