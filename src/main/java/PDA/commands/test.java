package PDA.commands;

import PDA.apis.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public class test implements BotCommand {

	Guild guild;
	@Override
	public void execute(DiscordBot bot) {
		bot.send("I AM HERE", guild);
	}

	@Override
	public void setArgs(String[] args) {

	}

	@Override
	public void setGuildID(Guild guild){
		this.guild = guild;
	}
}
