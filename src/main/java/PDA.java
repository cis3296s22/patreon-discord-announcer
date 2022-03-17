import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class PDA {
	// Build-configurations that will go into a JSON file later on
	static final String chromeDriverExecutableName = "chromedriver";

	public static void main(String[] arg) throws InterruptedException {
		// Ensure the user configured the program before running
		if (chromeDriverExecutableName.isEmpty()) {
			System.out.println("You have built the program without modifying the 'chromeDriverExecutableName' variable inside of PDA.java.  Exiting...");
			System.exit(1);
		}

		// Set the browser path
		System.setProperty("webdriver.chrome.driver","drivers/" + chromeDriverExecutableName);

		// Create options to run the driver headlessly
		ChromeOptions options = new ChromeOptions();
		options.addArguments(/* "--headless", */ "--disable-gpu", "--ignore-certificate-errors", "--disable-extensions", "--window-size=1920,1080");

		System.out.println("Loading the driver...");
		// Create and initialize the browser
		WebDriver driver = new ChromeDriver(options);

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
			System.out.println("\n\n----- Post -----\n" + currentPost.getText());

		driver.close();
	}
}
