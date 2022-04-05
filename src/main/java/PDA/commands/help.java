package PDA.commands;

import PDA.apis.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public class help implements BotCommand {

	private Guild guild;

	@Override
	public void execute(DiscordBot bot) {
		bot.send("PDA commands:  /help  /test  /embed  /setlink  /setchannel", guild);
	}

	@Override
	public void setArgs(String[] args) {

	}

	@Override
	public void setGuildID(Guild guild){
		this.guild = guild;
	}
}
