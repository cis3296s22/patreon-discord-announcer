package PDA.commands;

import PDA.PDA;
import PDA.PostCard;
import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public class getpublicposts implements BotCommand {

	private Guild guild;

	@Override
	public void execute(DiscordBot bot) {
		bot.setTitle("Public Posts:", null, guild);
		bot.send(guild);

		for (PostCard currentPostCard : PDA.privatePosts.get(guild)) {
			bot.send("post: " + currentPostCard.getTitle(), guild);
			bot.setTitle(currentPostCard.getTitle(), null, guild);
			bot.setDescription(currentPostCard.getContent(), guild);
			bot.setFooter(currentPostCard.getPublishDate(), currentPostCard.getUrl(), guild);
			bot.send(guild);
		}
	}

	@Override
	public void setArgs(String[] args) {

	}

	@Override
	public void setGuildID(Guild guild) {
		this.guild = guild;
	}
}
