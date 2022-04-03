package PDA.commands;

import PDA.apis.DiscordBot;

public class embed implements BotCommand {
	@Override
	public void execute(DiscordBot bot) {
		bot.setTitle("RANDOM INFO");
		bot.setDescription("useless info\nwords");
		bot.addField("Alex", "is cool");
//		bot.setFooter(event.getMessage().getContentRaw(), event.getMember().getUser().getAvatarUrl());

		bot.send();
		bot.clearEmbed();
	}

	@Override
	public void setArgs(String[] args) {

	}
}
