package PDA;

import PDA.apis.DiscordBot;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class DiscordBotJoin extends ListenerAdapter {

    private final DiscordBot bot;

    public DiscordBotJoin(DiscordBot bot){
        this.bot = bot;
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event){
        bot.addGuild(event.getGuild());
        PDA.guildSet.add(event.getGuild());
        bot.addChannel(event.getJDA().getTextChannels().get(0).getId(), event.getGuild());
    }
}
