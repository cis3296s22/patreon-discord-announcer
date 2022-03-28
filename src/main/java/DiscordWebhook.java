import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;


public class DiscordWebhook {

    public WebhookClient client;
    WebhookEmbedBuilder embed;

    DiscordWebhook(String webhookUrl){
        client = WebhookClient.withUrl(webhookUrl);
        embed = new WebhookEmbedBuilder();
        embed.setTimestamp(OffsetDateTime.now()); // this line might not be useful, its just to test what we can currently do
        embed.setAuthor(new WebhookEmbed.EmbedAuthor("PDA", "https://i.imgur.com/KlveixN.png", "https://github.com/cis3296s22/patreon-discord-announcer"));
        // TODO: the "https://i.imgur.com/KlveixN.png" needs to be changed/fixed

    }

    // layer of abstraction between WebhookEmbedBuilder and the code to only use what we need
    public void send(){
        client.send(embed.build());
    }

    public void setColor(int color){
        embed.setColor(color);
    }

    public void setTitle(String title){
        embed.setTitle(new WebhookEmbed.EmbedTitle(title, null));
    }

    public void setDescription(String description){
        embed.setDescription(description);
    }

    public void addField(String title, String value){
        embed.addField(new WebhookEmbed.EmbedField(true, title, value));
    }

    public void setImage(String url){
        embed.setImageUrl(url);
    }

    public void close(){
        client.close();
    }

}
