package PDA.commands;

import PDA.apis.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public class embed implements BotCommand {

	Guild guild;

	@Override
	public void execute(DiscordBot bot) {
		bot.setTitle("RANDOM INFO", guild);
		bot.setDescription("useless info\nwords", guild);
		bot.addField("Alex", "is cool", guild);
//		bot.setFooter(event.getMessage().getContentRaw(), event.getMember().getUser().getAvatarUrl());

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
