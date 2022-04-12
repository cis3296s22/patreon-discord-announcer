package PDA.commands;

import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public interface BotCommand {
	void execute(DiscordBot bot);

	void setArgs(String[] args);

	void setGuildID(Guild guild);
}
