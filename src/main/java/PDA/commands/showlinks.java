package PDA.commands;

import PDA.PDA;
import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;

public class showlinks implements BotCommand {

	private Guild guild;

	@Override
	public void execute(DiscordBot bot) {

		StringBuilder linkContainer = new StringBuilder("");

		for (String patreonUrl : PDA.patreonUrls.keySet()) {
			if (PDA.patreonUrls.get(patreonUrl).contains(guild)) {
				linkContainer.append(patreonUrl).append("\n");
			}
		}
		if (linkContainer.length() == 0)
			linkContainer.append("no links added");

		bot.setTitle("Links", "", guild);
		bot.setDescription(linkContainer.toString(), guild);
		bot.send(guild);
	}

	@Override
	public void setArgs(String[] args) {
	}

	@Override
	public void setGuildID(Guild guild) {
		this.guild = guild;
	}
}
