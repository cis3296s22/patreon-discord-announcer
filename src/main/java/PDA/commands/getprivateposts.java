package PDA.commands;

import PDA.PDA;
import PDA.PostCard;
import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public class getprivateposts implements BotCommand {

    private Guild guild;

    @Override
    public void execute(DiscordBot bot) {
        bot.clearEmbed(guild);
        bot.setTitle("Private Posts:", null, guild);
        bot.send(guild);

        bot.setTitle(null, null, guild);
        for (PostCard currentPostCard : PDA.privatePosts.get(guild)) {
            bot.setTitle(currentPostCard.getTitle(), null, guild);
            bot.setDescription(currentPostCard.getContent(), guild);
            bot.setFooter(currentPostCard.getPublishDate(), currentPostCard.getUrl(), guild);
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
