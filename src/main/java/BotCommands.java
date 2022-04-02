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

        for (int i = 0; i < commands.length; i++)
            commands[i] = DiscordBot.prefix + commands[i];

        String[] args = event.getMessage().getContentRaw().split("\\s+"); // changing each word in a message to arguments separated by spaces

        int prefixLength = DiscordBot.prefix.length();

        if (args.length > prefixLength)
            return;

        switch (args[0].substring(prefixLength)) {
            case "help":
                bot.send("commands: " + Arrays.toString(commands));
                break;
            case "test":
                bot.send("I AM HERE");
                break;
            case "embed":
                bot.setTitle("RANDOM INFO");
                bot.setDescription("useless info\nwords");
                bot.addField("Alex", "is cool");
                bot.setFooter(event.getMessage().getContentRaw(), event.getMember().getUser().getAvatarUrl());

                bot.send();
                bot.clearEmbed();
                break;
            case "getpublic":
                bot.setTitle("Public Posts:");
                bot.setDescription("Description");
                bot.addField("Field title", "Field value");

                bot.send();
                bot.clearEmbed();
                break;
            case "getprivate":
                break;
        }

//        if (args[0].equalsIgnoreCase(DiscordBot.prefix + "help")){
//            bot.send("commands: " + Arrays.toString(commands));
//        }
//
//        if (args[0].equalsIgnoreCase(DiscordBot.prefix + "test")){
//            bot.send("I AM HERE");
//        }
//
//        if (args[0].equalsIgnoreCase(DiscordBot.prefix + "embed")){
//            bot.setTitle("RANDOM INFO");
//            bot.setDescription("useless info\nwords");
//            bot.addField("Alex", "is cool");
//            bot.setFooter(event.getMessage().getContentRaw(), event.getMember().getUser().getAvatarUrl());
//
//            bot.send();
//            bot.clearEmbed();
//        }

    }
}
