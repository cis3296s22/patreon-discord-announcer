package PDA.commands;

import PDA.PDA;
import PDA.apis.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public class setlink implements BotCommand {

	private String[] args = null;
	Guild guild;

	@Override
	public void execute(DiscordBot bot) {
		if (args == null) {
			System.out.println("null args rip");
			return;
		}

		if (args.length <= 1) {
			bot.send("No link provided", guild);
		} else {
			PDA.patreonUrls.put(guild, args[1]);
			bot.send(args[1] + " has been set as the patreon link", guild);
		}
	}

	@Override
	public void setArgs(String[] args) {
		this.args = args;
	}

	@Override
	public void setGuildID(Guild guild){
		this.guild = guild;
	}
}
