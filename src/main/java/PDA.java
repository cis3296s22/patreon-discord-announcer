public class PDA {
	// Build-configurations that will go into a JSON file later on
	static String webDriverExecutableName = "chromedriver";

	public static void main(String[] arg) throws InterruptedException {
		String osName = System.getProperty("os.name");

		if (osName.contains("Window"))
			webDriverExecutableName += ".exe";

		setWebDrivers(webDriverExecutableName);

		PatreonThread testThread = new PatreonThread();
		testThread.start();
		testThread.join();

		System.out.println("Finished!");
	}

	private static void setWebDrivers(String webDriverExecutableName) {
		// Set the executable path for all possible drivers
		System.setProperty("webdriver.chrome.driver", webDriverExecutableName);
		System.setProperty("webdriver.edge.driver", webDriverExecutableName);
		System.setProperty("webdriver.gecko.driver", webDriverExecutableName);
	}
}
