package PDA.commands;

import PDA.PDA;
import PDA.apis.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;

public class removelink implements BotCommand {

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
            ArrayList<String> links = PDA.patreonUrls.get(guild);

            if (links.remove(args[1])){
                bot.send(args[1] + " has been removed from the patreon link list", guild);
            }
            else{
                bot.send(args[1] + " was never in the list of patreon links", guild);
            }

            PDA.patreonUrls.put(guild, links);
            System.out.println("all links: " + links); // temporary testing
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
