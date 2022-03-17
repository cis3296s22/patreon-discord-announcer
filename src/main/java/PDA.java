import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.List;

public class PDA {
	// Build-configurations that will go into a JSON file later on
	static String chromeDriverExecutableName = "chromedriver";

	public static void main(String[] arg) throws InterruptedException {
		String osName = System.getProperty("os.name");

		if (osName.contains("Window"))
			chromeDriverExecutableName += ".exe";

		// Set the browser path
		System.setProperty("webdriver.chrome.driver", chromeDriverExecutableName);

		// Create options to run the driver headlessly
		ChromeOptions options = new ChromeOptions();
		options.addArguments(/* "--headless", */ "--disable-gpu", "--ignore-certificate-errors", "--disable-extensions", "--window-size=1920,1080");

		System.out.println("Loading the driver...");
		// Create and initialize the browser
		WebDriver driver = new ChromeDriver(options);

		// Load the login page to pass Geetest, ensuring we're allowed to see post
		System.out.println("Loading the login page for Geetest");
		driver.get("https://www.patreon.com/login");

		Thread.sleep(2000);

		if (!driver.getPageSource().contains("New to Patreon?")) {
			System.out.println("Pass the test on the screen, then press enter in this console to continue...");

			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Loading the webpage...");
		driver.get("https://www.patreon.com/pda_example");
//		driver.get("https://www.patreon.com/supermega");

		// Sleep so we can ensure the page loads even if the connection is slow
		Thread.sleep(5000);

		System.out.println("Finding all posts on the front page...");
		// Get any public posts
		List<WebElement> publicPosts = driver.findElements(By.cssSelector("[data-tag='post-card']"));

		// Display every post found on the front page
		for (WebElement currentPost : publicPosts)
			System.out.println("\n\n---------- Post ----------\n" + currentPost.getText());

		driver.close();
	}
}
