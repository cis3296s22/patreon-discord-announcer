package PDA;

import PDA.apis.DiscordBot;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebElement;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PDA {
	// TODO: make patreonUrl a list so we can have multiple patreon pages to check (maybe hashmap<Integer(guild id), List> so it depends on the discord server its in)
	public static String patreonUrl = "https://www.patreon.com/pda_example";
	static String webhookUrl = ""; // https://discord.com/api/webhooks/958181437402644520/Nw6LLM7JGm176hDd6KgtUK3h3FXif-m7fRcnSAvyjrWP7p1lHuIhRJFTZ76RD1sHL0C4
	static String discordToken = "";
	static String discordChannel = "";

	// Global variables
	public static String prefix = "/";

	static List<WebElement> publicPosts = new LinkedList<>();
	static List<WebElement> privatePosts = new LinkedList<>();

	public static void main(String[] arg) throws InterruptedException, LoginException {
		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("config.json"));
			System.out.println(jsonObject);
			Object token = jsonObject.get("TOKEN");
			discordToken = token.toString();
			discordToken = discordToken.replaceAll("[\\[\\](){}]", "");
			System.out.println(discordToken);
			Object channel = jsonObject.get("Channel");
			discordChannel = channel.toString();
			discordChannel = discordChannel.replaceAll("[\\[\\](){}]", "");
			System.out.println(discordChannel);


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}


		DiscordBot bot = new DiscordBot(discordToken);
		bot.setChannel(discordChannel);

		PatreonThread testThread = new PatreonThread(webhookUrl, bot, discordChannel);
		testThread.start();
		testThread.join();

		System.out.println("Finished!");
	}
}
