public class PDA {
	// TODO: User settings that will be loaded from a configuration file later on
	static String webDriverExecutable = "chromedriver";
	static String patreonUrl = "https://www.patreon.com/pda_example";

	public static void main(String[] arg) throws InterruptedException {
		setWebDriverProperty(webDriverExecutable);

		PatreonThread testThread = new PatreonThread(patreonUrl);
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
