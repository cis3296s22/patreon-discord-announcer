import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Arrays;

// TODO: have an admin set what channel they want the patreon messages to go
// TODO: only allow admins (or admin selected users) to use commands

public class BotCommands extends ListenerAdapter {

    private DiscordBot bot;

    BotCommands(DiscordBot bot){
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        bot.setChannel(String.valueOf(event.getChannel()));

        final String[] commands = {"help", "test", "embed"};

        String[] args = event.getMessage().getContentRaw().split("\\s+"); // changing each word in a message to arguments separated by spaces

        if (args[0].equalsIgnoreCase(DiscordBot.prefix + "help")){
            bot.send("commands: " + Arrays.toString(commands));
        }

        if (args[0].equalsIgnoreCase(DiscordBot.prefix + "test")){
            bot.send("I AM HERE");
        }

        if (args[0].equalsIgnoreCase(DiscordBot.prefix + "embed")){
            bot.setTitle("RANDOM INFO");
            bot.setDescription("useless info\nwords");
            bot.addField("Alex", "is cool");
            bot.setFooter(event.getMessage().getContentRaw(), event.getMember().getUser().getAvatarUrl());

            bot.send();
            bot.clearEmbed();
        }

    }
}
