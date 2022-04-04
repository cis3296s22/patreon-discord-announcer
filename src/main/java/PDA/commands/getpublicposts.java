package PDA.commands;

import PDA.PDA;
import PDA.apis.DiscordBot;
import org.openqa.selenium.WebElement;

public class getpublicposts implements BotCommand {
	@Override
	public void execute(DiscordBot bot) {
		bot.setTitle("Public Posts:");

		for (WebElement currentPost : PDA.publicPosts) {
			bot.setDescription(currentPost.getText());
			bot.send();
		}
		bot.clearEmbed();
	}

	@Override
	public void setArgs(String[] args) {

	}
}
