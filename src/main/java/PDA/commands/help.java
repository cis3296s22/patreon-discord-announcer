package PDA.commands;

import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public class help implements BotCommand {

	private Guild guild;

	@Override
	public void execute(DiscordBot bot) {
		bot.send("PDA commands:  /help  /setchannel  /addlink  /removelink", guild);
	}

	@Override
	public void setArgs(String[] args) {

	}

	@Override
	public void setGuildID(Guild guild){
		this.guild = guild;
	}
}
