package PDA.commands;

import PDA.DiscordBot;
import PDA.PDA;
import PDA.PostCard;

/**
 * getpublicposts discord bot command.
 *
 * Responsibilities:
 *
 * 1) Will print out all public posts associated with the discord server
 *
 */

public class getpublicposts extends GenericBotCommand {

	/**
	 * Prints out all public posts unique to the discord that issued the command
	 *
	 * @param bot holds the reference to the singular {@link DiscordBot} object
	 */
	@Override
	public void execute(DiscordBot bot) {
		bot.setTitle("Public Posts:", null, guild);
		bot.send(guild);

		for (PostCard currentPostCard : PDA.postCards.get(guild)) {
			if (currentPostCard.isPrivate())
				continue;

			bot.setTitle(currentPostCard.getTitle(), null, guild);
			bot.setDescription(currentPostCard.getContent(), guild);
			bot.setFooter(currentPostCard.getPublishDate(), currentPostCard.getUrl(), guild);
			bot.send(guild);
		}
	}
}
