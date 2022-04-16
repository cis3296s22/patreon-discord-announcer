package PDA.commands;

import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public abstract class GenericBotCommand implements BotCommand {
	protected String[] args = null;
	protected Guild guild = null;

	// This remains abstract to force child classes to implement their own
	@Override
	public abstract void execute(DiscordBot bot);

	@Override
	public void setArgs(String[] args) {
		this.args = args;
	}

	@Override
	public void setGuildID(Guild guild) {
		this.guild = guild;
	}
}
