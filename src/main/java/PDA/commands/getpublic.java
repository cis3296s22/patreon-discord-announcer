package PDA.commands;

import PDA.apis.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public class getpublic implements BotCommand {

	Guild guild;

	@Override
	public void execute(DiscordBot bot) {
		bot.setTitle("Public Posts:", guild);
		bot.setDescription("Description", guild);
		bot.addField("Field title", "Field value", guild);

		bot.send(guild);
		bot.clearEmbed(guild);
	}

	@Override
	public void setArgs(String[] args) {

	}

	@Override
	public void setGuildID(Guild guild){
		this.guild = guild;
	}
}
