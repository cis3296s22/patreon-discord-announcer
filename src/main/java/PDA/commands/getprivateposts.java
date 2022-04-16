package PDA.commands;

import PDA.DiscordBot;
import PDA.PDA;
import PDA.PostCard;

/**
 * getprivateposts discord bot command.
 *
 * Responsibilities:
 *
 * 1) Will print out all private posts associated with the discord server
 *
 */

public class getprivateposts extends GenericBotCommand {

	@Override
	public void execute(DiscordBot bot) {
		bot.setTitle("Private Posts:", null, guild);
		bot.send(guild);

		for (PostCard currentPostCard : PDA.postCards.get(guild)) {
			if (!currentPostCard.isPrivate())
				continue;

			bot.setTitle(currentPostCard.getTitle(), null, guild);
			bot.setDescription(currentPostCard.getContent(), guild);
			bot.setFooter(currentPostCard.getPublishDate(), currentPostCard.getUrl(), guild);
			bot.send(guild);
		}
	}
}
