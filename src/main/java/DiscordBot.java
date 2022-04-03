import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.security.auth.login.LoginException;
import java.awt.*;

// TODO: implement HashMap to allow for storing of embed messages
public class DiscordBot {

    private JDA jda;
    private EmbedBuilder embed;
    private TextChannel channel;
    // private String patreonUrl;
    public static String prefix = "/"; // we want other files to be able to use this prefix

    DiscordBot(String token /*, String patreonUrl*/) throws LoginException {
        jda = JDABuilder.createDefault(token).build();
        jda.addEventListener(new BotCommands(this));
        embed = new EmbedBuilder();
        // this.patreonUrl = patreonUrl;
    }

    public void setTitle(String title){
        embed.setTitle(title, null);
    }

    public void setChannel(String channelId){

        // JDA has another overloaded getTextChannelById() method that uses a long instead of a String
        // for some reason it will print a NumberFormatException error when using the String version even though it works
        try{
            this.channel = jda.getTextChannelById(channelId);
        }
        catch(NumberFormatException ignored){ }
    }

    public void setDescription(String description){
        embed.setDescription(description);
    }

    public void addField(String title, String value){
        embed.addField(title, value, true);
    }

    public void setColor(Color color){
        embed.setColor(color);
    }

    public void setFooter(String text, String userUrl){
        embed.setFooter(text, userUrl);
    }

    public void setPatreonUrl(String patreonUrl){
        PDA.patreonUrl = patreonUrl;
    }

    public void clearEmbed(){
        embed.clear();
    }

    public void send(){ // sending embed
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    public void send(String text){ // sending text
        channel.sendMessage(text).queue();
    }

}
