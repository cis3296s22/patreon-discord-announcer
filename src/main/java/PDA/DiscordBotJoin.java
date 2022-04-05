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
        System.out.println("Server added to list for guild: " + event.getGuild().getName());
        bot.addGuild(event.getGuild());
        PDA.patreonUrls.put(event.getGuild(), "https://www.patreon.com/pda_example");
        PDA.guildSet.add(event.getGuild());
        bot.addChannel(event.getGuild().getTextChannels().get(0).getId(), event.getGuild());
    }
}
