package PDA.commands;

import PDA.PDA;
import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;

public class addlink implements BotCommand {

	private String[] args = null;
	private Guild guild;

	@Override
	public void execute(DiscordBot bot) {
		if (args == null) {
			System.out.println("no arguments provided");
			return;
		}

		if (args.length <= 1) {
			bot.send("No link provided", guild);
		} else {
			ArrayList<Guild> guilds;

			if (PDA.patreonUrls.containsKey(args[1])) {
				guilds = PDA.patreonUrls.get(args[1]);

				if (guilds.contains(guild)) {
					bot.send(args[1] + " is already in the list of links", guild);
				} else {
					guilds.add(guild);
					PDA.patreonUrls.put(args[1], guilds);
				}
			} else {
				guilds = new ArrayList<>();
				guilds.add(guild);
				PDA.patreonUrls.put(args[1], guilds);
				bot.send(args[1] + " has been added to the list of links", guild);
			}
		}
	}

	@Override
	public void setArgs(String[] args) {
		this.args = args;
	}

	@Override
	public void setGuildID(Guild guild) {
		this.guild = guild;
	}
}
