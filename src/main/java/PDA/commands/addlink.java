package PDA.commands;

import PDA.PDA;
import PDA.apis.DiscordBot;
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
            ArrayList<String> links = PDA.patreonUrls.get(guild);

            if (!links.contains(args[1]) && true){ // TODO: check if valid patreon link
                links.add(args[1]);
                PDA.patreonUrls.put(guild, links);
                bot.send(args[1] + " has been added as a patreon link", guild);
            }
            else{
                bot.send("given patreon link has already been added", guild);
            }
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
