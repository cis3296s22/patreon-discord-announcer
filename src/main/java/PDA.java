import javax.security.auth.login.LoginException;

public class PDA {
	// TODO: User settings that will be loaded from a configuration file later on
	static String webDriverExecutable = "chromedriver";
	static String patreonUrl = "https://www.patreon.com/pda_example";
	static String webhookUrl = ""; // https://discord.com/api/webhooks/958181437402644520/Nw6LLM7JGm176hDd6KgtUK3h3FXif-m7fRcnSAvyjrWP7p1lHuIhRJFTZ76RD1sHL0C4
	static String discordToken = System.getenv("TOKEN");
	static String discordChannel = "";

	public static void main(String[] arg) throws InterruptedException, LoginException {
		setWebDriverProperty(webDriverExecutable);

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
