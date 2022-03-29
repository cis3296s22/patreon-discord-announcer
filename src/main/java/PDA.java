import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PDA {
	// TODO: User settings that will be loaded from a configuration file later on
	static String patreonUrl = "https://www.patreon.com/pda_example";
	static String webhookUrl = ""; // https://discord.com/api/webhooks/958181437402644520/Nw6LLM7JGm176hDd6KgtUK3h3FXif-m7fRcnSAvyjrWP7p1lHuIhRJFTZ76RD1sHL0C4
	static String discordToken = "";
	static String discordChannel = "";

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

		PatreonThread testThread = new PatreonThread(patreonUrl, webhookUrl, bot, discordChannel);
		testThread.start();
		testThread.join();

		System.out.println("Finished!");
	}

	private static void setWebDriverProperty(String webDriverExecutableName) {
		// Set the executable path for all possible drivers
		System.setProperty("webdriver.chrome.driver", webDriverExecutableName);
		System.setProperty("webdriver.edge.driver", webDriverExecutableName);
		System.setProperty("webdriver.gecko.driver", webDriverExecutableName);
	}
}
