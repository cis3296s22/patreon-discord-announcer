import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class PatreonThread extends Thread {

	@Override
	public void run() {
		// Create options to run the driver headlessly
		ChromeOptions options = new ChromeOptions();
		options.addArguments(/* "--headless", */ "--disable-gpu", "--ignore-certificate-errors", "--disable-extensions", "--window-size=1920,1080");

		System.out.println("Loading the driver...");
		// Create and initialize the browser
		WebDriver driver = new ChromeDriver(options);

		// Load the login page to pass Geetest, ensuring we're allowed to see post
		System.out.println("Loading the login page for Geetest");
		driver.get("https://www.patreon.com/login");

		waitForPageLoad(driver);

		if (!driver.getPageSource().contains("New to Patreon?")) {
//			System.out.println("Pass the test on the screen, then press enter in this console to continue...");

			geeTest(driver);

			try {
				System.in.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Loading the webpage...");
		driver.get("https://www.patreon.com/pda_example");
		waitForPageLoad(driver);
//		driver.get("https://www.patreon.com/supermega");

		// Sleep so we can ensure the page loads even if the connection is slow
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Finding all posts on the front page...");
		// Get any public posts
		List<WebElement> publicPosts = driver.findElements(By.cssSelector("[data-tag='post-card']"));

		// Display every post found on the front page
		for (WebElement currentPost : publicPosts)
			System.out.println("\n\n---------- Post ----------\n" + currentPost.getText());

		driver.close();
	}

	private void waitForPageLoad(WebDriver driver) {
		System.out.println("Waiting for the page to finish loading...");

		new WebDriverWait(driver, Duration.ofSeconds(30)).until(
				webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

		System.out.println("Done..!");
	}

	private void geeTest(WebDriver driver) {

	}
}