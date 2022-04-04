package PDA.apis;

import PDA.BotCommands;
import PDA.PDA;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

// TODO: implement HashMap to allow for storing of embed messages
public class DiscordBot {
	private JDA jda;
	private HashMap<Guild, EmbedBuilder> embedMap;
	private HashMap<Guild, TextChannel> channels;

	public DiscordBot(String token, String channel) throws LoginException, InterruptedException {
		embedMap = new HashMap<>();
		channels = new HashMap<>();
		try {
			jda = JDABuilder.createDefault(token).build();
		} catch (LoginException e) {
			System.out.println("The given Discord Bot token '" + token + "' is invalid!");
			System.exit(1);
		}
		jda.awaitReady();
		jda.addEventListener(new BotCommands(this));

		// if someone added the bot to their server when the bot wasn't running then add it to the embedMap
		for (Guild guild : jda.getGuilds()){
			if (!embedMap.containsKey(guild)){
				addGuild(guild);
				PDA.guildSet.add(guild);
			}
		}

		// setting up the text channel id per different discord server
		for (Guild guild : PDA.guildSet){
			List<TextChannel> chanList = guild.getTextChannelsByName("testing", true);

			// if we can't find a "bot-commands" then output to the first channel we find
			if (chanList.isEmpty()){
				chanList = guild.getTextChannels();
				// TextChannel initChannel = guild.getTextChannels().get(0);

			}
			addChannel(chanList.get(0).getId(), guild);
		}

		System.out.println("channels: " + channels);

		// this is setting the wrong channel for certain guilds but will be
	}

	public void setTitle(String title, Guild id) {
		embedMap.put(id, embedMap.get(id).setTitle(title, null));
	}

	public void addChannel(String channelId, Guild id) {

		// JDA has another overloaded getTextChannelById() method that uses a long instead of a String
		// for some reason it will print a NumberFormatException error when using the String version even though it works
		try {
			channels.put(id, jda.getTextChannelById(channelId));
		} catch (NumberFormatException ignored) {
		}
	}

	public void setDescription(String description, Guild id) {
		embedMap.put(id, embedMap.get(id).setDescription(description));
	}

	public void addField(String title, String value, Guild id) {
		embedMap.put(id, embedMap.get(id).addField(title, value, true));
	}

	public void setColor(Color color, Guild id) {
		embedMap.put(id, embedMap.get(id).setColor(color));
	}

	public void setFooter(String text, String userUrl, Guild id) {
		embedMap.put(id, embedMap.get(id).setFooter(text, userUrl));
	}

	public void clearEmbed(Guild id) {
		embedMap.put(id, embedMap.get(id).clear());
	}

	public void send(Guild id) { // sending embed
		channels.get(id).sendMessageEmbeds(embedMap.get(id).build()).queue();
	}

	public void send(String text, Guild id) { // sending text
		channels.get(id).sendMessage(text).queue();
	}

	public void addGuild(Guild guildID){
		embedMap.put(guildID, embedMap.getOrDefault(guildID, new EmbedBuilder()));
	}

}
