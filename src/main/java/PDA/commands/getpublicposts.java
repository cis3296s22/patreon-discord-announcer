package PDA.commands;

import PDA.PDA;
import PDA.apis.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public class getpublicposts implements BotCommand {

	private Guild guild;

	@Override
	public void execute(DiscordBot bot) {
		bot.clearEmbed(guild);
		bot.setTitle("Public Posts:", guild);
		bot.send(guild);

		bot.setTitle(null, guild);
		for (String currentPost : PDA.privatePosts) {
			bot.setDescription(currentPost, guild);
			bot.send(guild);
		}
		bot.clearEmbed(guild);
	}

	@Override
	public void setArgs(String[] args) {

	}

	@Override
	public void setGuildID(Guild guild) {
		this.guild = guild;
	}
}
