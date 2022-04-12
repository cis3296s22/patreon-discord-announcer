package PDA.commands;

import PDA.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;

public class setchannel implements BotCommand{

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
            bot.addChannel(args[1], guild);
            bot.send(args[1] + " has been set as the bot output channel", guild);
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
