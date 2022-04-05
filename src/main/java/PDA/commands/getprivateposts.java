package PDA.commands;

import PDA.PDA;
import PDA.apis.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import org.openqa.selenium.WebElement;

public class getprivateposts implements BotCommand {

    private Guild guild;

    @Override
    public void execute(DiscordBot bot) {
        bot.clearEmbed(guild);
        bot.setTitle("Private Posts:", guild);
        bot.send(guild);

        bot.setTitle(null, guild);
        for (String currentPost : PDA.privatePosts) {
            bot.setDescription(currentPost, guild);
            bot.send(guild);
        }
        bot.clearEmbed(guild);
    }

    @Override
    public void setArgs(String[] args) {

    }

    @Override
    public void setGuildID(Guild guild) {
        this.guild = guild;
    }
}
