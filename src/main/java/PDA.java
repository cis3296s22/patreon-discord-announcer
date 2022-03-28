public class PDA {
	// TODO: User settings that will be loaded from a configuration file later on
	static String webDriverExecutable = "chromedriver";
	static String patreonUrl = "https://www.patreon.com/pda_example";
	static String webhookUrl = "https://discord.com/api/webhooks/957725735152402542/Xrk6sY6bRCnCvF437K96XP7AcAw_J6HTC6_7bB2J6_aGYNYn__USzvhMoKsLtNlUMC6Z";

	public static void main(String[] arg) throws InterruptedException {
		setWebDriverProperty(webDriverExecutable);

		PatreonThread testThread = new PatreonThread(patreonUrl, webhookUrl);
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
