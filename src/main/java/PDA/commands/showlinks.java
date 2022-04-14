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

        for (String link : PDA.patreonUrls.get(guild)){
            linkContainer.append(link).append("\n");
        }
        if (PDA.patreonUrls.get(guild).size() == 0) linkContainer.append("no links added");

        bot.setTitle("Links", "", guild);
        bot.setDescription(linkContainer.toString(), guild);
        bot.send(guild);
    }

    @Override
    public void setArgs(String[] args) {}

    @Override
    public void setGuildID(Guild guild){
        this.guild = guild;
    }
}
