package PDA;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.List;


public class PatreonThread extends Thread {

	// String patreonUrl;
	String webhookUrl;
	DiscordBot bot;
	String discordChannel;
	Wait<WebDriver> wait;
	By postCardSelector;

	public PatreonThread(/* String patreonUrl ,*/ String webhookUrl, DiscordBot bot, String discordChannel) {
		// this.patreonUrl = patreonUrl;
		this.webhookUrl = webhookUrl;
		this.bot = bot;
		this.discordChannel = discordChannel;
		this.postCardSelector = By.cssSelector("[data-tag='post-card']");
	}

	@Override
	public void run() {

		JSONParser parser = new JSONParser();
		int drivernum = 0; // add driver type here 0-Chrome, 1-Firefox, 2-Edge

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("config.json"));
			JSONArray checkconfig = (JSONArray) jsonObject.get("drivers");
			JSONObject drivers = (JSONObject) checkconfig.get(drivernum);
			String drivername = drivers.values().toString();
			drivername = drivername.replaceAll("[\\[\\](){}]", "");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}

		System.out.println("Loading the driver...");

		// Create and initialize the browser
		WebDriver driver = null;
		try {
			switch (drivernum) {
				case 0: { // Chrome
					ChromeOptions options = new ChromeOptions();
					options.setPageLoadStrategy(PageLoadStrategy.NORMAL); // NORMAL = driver waits for pages to load and ready state to be 'complete'.
					options.addArguments(/* "--headless", */
							"--ignore-certificate-errors",
							"--no-sandbox",
							"--disable-gpu",
							"--disable-extensions",
							"--disable-crash-reporter",
							"--disable-logging",
							"--disable-dev-shm-usage",
							"--window-size=1600,900",
							"--log-level=3");
					options.setLogLevel(ChromeDriverLogLevel.OFF);
					driver = WebDriverManager.chromedriver().capabilities(options).create();
					break;
				}
				case 1: { // Firefox
					FirefoxOptions options = new FirefoxOptions();
					options.addArguments(/*"--headless"*/);
					driver = WebDriverManager.firefoxdriver().create();
					break;
				}
				case 2: { // Edge
					EdgeOptions options = new EdgeOptions();
					options.addArguments(/* "--headless", */ "--disable-gpu", "--ignore-certificate-errors", "--disable-extensions", "--window-size=1600,900", "--log-level=3");
					driver = WebDriverManager.edgedriver().capabilities(options).create();
					break;
				}
				default: // Incorrect selection
					break;
			}
		} catch (SessionNotCreatedException e) {
			System.out.println("An error occurred while loading the browser.");
			e.printStackTrace();
		} finally {
			if (driver == null)
				System.exit(1);
		}

		// Initialize our waiting interface
		wait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(5))
				.pollingEvery(Duration.ofMillis(250));

		while (true) {
			Set<Guild> localSet = new HashSet<>(PDA.guildSet);

			for (Guild guild : localSet) {
				System.out.println("GuildSet: " + localSet);
				System.out.println("Current Guild: " + guild);

				ArrayList<String> localUrls = PDA.patreonUrls.get(guild);
				for (int i = 0; i < localUrls.size(); i++){ // using a foreach loop will cause a ConcurrentModificationException since we are iterating through the collection while adding a link to the patreonUrls
					goToLoginPage(driver, guild, localUrls.get(i));

//				System.out.printf("Loading patreon page '%s'...", PDA.patreonUrls.get(guild));
//				driver.get(PDA.patreonUrls.get(guild).get(0));
//				waitForPageLoad(driver);

					System.out.println("Finding all posts on the front page...");

					// Get any public posts
					this.sleep(4000);

					List<WebElement> foundPostElements = driver.findElements(postCardSelector);
//				List<PostCard> currentPublicPosts = new LinkedList<>(), currentPrivatePosts = new LinkedList<>();

					for (WebElement currentPostElement : foundPostElements) {
						PostCard currentPostCard = new PostCard(currentPostElement);

						if (currentPostCard.isPrivate()) {

							if (!PDA.privatePosts.get(guild).contains(currentPostCard)) {
								System.out.println("\n\n" + currentPostCard);

								LinkedList<PostCard> temp = PDA.privatePosts.get(guild);
								temp.add(currentPostCard);
								PDA.privatePosts.put(guild, temp);
								announcePost(currentPostCard, guild);
							}

//						currentPrivatePosts.add(currentPostCard);
						} else { // The post isn't private, it must be public
							if (!PDA.publicPosts.get(guild).contains(currentPostCard)) {
								System.out.println("\n\n" + currentPostCard);

								LinkedList<PostCard> temp = PDA.publicPosts.get(guild);
								temp.add(currentPostCard);
								PDA.publicPosts.put(guild, temp);
								announcePost(currentPostCard, guild);
							}


//						currentPublicPosts.add(currentPostCard);
						}
					}

					// Display every post found on the front page
					// TODO: will need to get the parts of the post (like image and whatnot) so we can give it to the discord webhook client
					// bot.setChannel(discordChannel); // will either get channel from user or from config file in the future

					// For every found private post, check to see if we already announced it.
					// If we didn't, add it to the announced posts and then announce it.
//				for (WebElement currentPost : currentPrivatePosts) {
////					if (!PDA.privatePosts.contains(currentPost.getText())) {
////						PDA.privatePosts.add(currentPost.getText());
////						announcePost(currentPost, guild);
////					}
//
////					announcePost(currentPost, guild);
//				}
//
//				for (WebElement currentPost : currentPublicPosts) {
//					if (!PDA.publicPosts.contains(currentPost.getText())) {
//						PDA.publicPosts.add(currentPost.getText());
////						announcePost(currentPost, guild);
//					}
//				}
				}

			}


			// Sleep between 2-3 minutes
			double sleepTime = 1000;// randNum(120000, 180000);
			System.out.printf("\nWaiting %.2f until the next Patreon page scan...\n\n", (sleepTime / 60000));
			this.sleep((int) sleepTime);
		}
	}

	private void announcePost(PostCard data, Guild guild) {

		bot.setTitle((data.isPrivate() ? "Private: " : "Public: ") + data.getTitle(), data.getUrl(), guild);
		bot.setDescription(data.getContent(), guild);
		bot.setFooter(data.getPublishDate(), null, guild);
		bot.setColor(data.isPrivate() ? Color.red : Color.green, guild);
		bot.send(guild);
	}

	private void goToLoginPage(WebDriver driver, Guild guild, String patreonUrl) {
		// Load the login page to pass GeeTest, ensuring we're allowed to see post

		System.out.printf("Loading '%s' for guild '%s'\n", patreonUrl, guild.getName());
		driver.get(patreonUrl);

		// Time has passed and we haven't seen any post cards...
		if (!this.visibleElementFound(postCardSelector)) {
			System.out.println("No postcards found after waiting for 5 second...");

			int loadCount = 0;

			// Keep reload the login page to get the GeeTest.  Sometimes it doesn't appear on the first load.
			while (loadCount++ < 5) {
				System.out.println("Loading login page...");
				driver.get("https://www.patreon.com/login");

				/*
				 * Check for the login page on the 2nd or greater successful page reload
				 *
				 * This is required as sometimes loading the page for the first time
				 * will not show the bot check
				 */
				if (/* loadCount > 1 && */ !driver.getPageSource().contains("New to Patreon?"))
					break;
			}

			if (!driver.getPageSource().contains("New to Patreon?")) {
				System.out.println("Attempting to solve GeeTest CAPTCHA...");

				geeTest(driver);
				driver.get(patreonUrl);
			}
		}
	}

	private void geeTest(WebDriver driver) {

		// Wait until the GeeTest iframe is loaded
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("iframe")));

		// Store the GeeTest iframe
		WebElement iFrame = driver.findElement(By.tagName("iframe"));

		// Switch the web driver context to the iframe
		driver.switchTo().frame(iFrame);

		// Wait until the GeeTest clickable verification button is loaded
		this.visibleElementFound(By.className("geetest_radar_btn"));

		// Check to see if Patreon has blocked our IP entirely
		if (this.visibleElementFound(By.className("captcha__human__title")))
			if (driver.findElement(By.className("captcha__human__title")).getText().contains("You have been blocked")) {
				System.out.println("The current IP has been blocked by Patreon.  Stopping.");
				System.exit(1);
			}

		// Store the GeeTest verification button
		WebElement geeTestVerify = driver.findElement(By.className("geetest_radar_btn"));

		// Click the GeeTest verification button
		geeTestVerify.click();

		// While the puzzle is visible, attempt to solve it repeatedly
		while (this.visibleElementFound(By.className("geetest_canvas_bg"))) {
			this.sleep(1000);

			// Wait until both the original and the puzzle image are present
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("geetest_canvas_fullbg")));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.className("geetest_canvas_bg")));

			// Save both the original and the puzzle image elements
			WebElement originalImageElement = driver.findElement(By.className("geetest_canvas_fullbg"));
			WebElement puzzleImageElement = driver.findElement(By.className("geetest_canvas_bg"));

			// Convert both images to base64 image strings
			String originalImageString = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].toDataURL('image/png').substring(22);", originalImageElement);
			String puzzleImageString = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].toDataURL('image/png').substring(22);", puzzleImageElement);

			byte[] originalImageBytes = DatatypeConverter.parseBase64Binary(originalImageString);
			byte[] puzzleImageBytes = DatatypeConverter.parseBase64Binary(puzzleImageString);

			// Convert the base64 image to an actual image
			BufferedImage originalImage = null, puzzleImage = null;

			// Attempt to convert the base64 encoded image to an image in memory
			try {
				originalImage = ImageIO.read(new ByteArrayInputStream(originalImageBytes));
				puzzleImage = ImageIO.read(new ByteArrayInputStream(puzzleImageBytes));
			} catch (IOException ex) {
				System.out.println("------------------------------------------\n" +
						"An issue occurred while reading the GeeTest puzzle image!\n" +
						"------------------------------------------\n");
				ex.printStackTrace();
				driver.quit();
				System.exit(1);
			}

			// Ensure the images were converted properly
			if (originalImage == null || puzzleImage == null) {
				System.out.println("The original image or the puzzle image were null after being saved.");
				driver.quit();
				System.exit(1);
			}

			// Store WxH of each image
			int originalWidth = originalImage.getWidth();
			int originalHeight = originalImage.getHeight();
//			int puzzleWidth = puzzleImage.getWidth();
//			int puzzleHeight = puzzleImage.getHeight();

			// Ensure image dimensions are the same, otherwise we can't solve this puzzle
//			if (originalWidth != puzzleWidth || originalHeight != puzzleHeight) {
//				System.out.println("The width/height don't match for the images.");
//				System.out.printf("Original: %dx%d\nPuzzle: %dx%d", originalWidth, originalHeight, puzzleWidth, puzzleHeight);
//				System.exit(1);
//			}

			int[][] differenceMatrix = new int[originalWidth][originalHeight];

			// Calculate the differences between the original image and the puzzle image
			for (int y = 0; y < originalHeight; y++) {
				for (int x = 0; x < originalWidth; x++) {
					// Get current RGB values
					int rgbA = originalImage.getRGB(x, y);
					int rgbB = puzzleImage.getRGB(x, y);

					// Something from geeksforgeeks...
					int redA = (rgbA >> 16) & 0xff;
					int greenA = (rgbA >> 8) & 0xff;
					int blueA = (rgbA) & 0xff;
					int redB = (rgbB >> 16) & 0xff;
					int greenB = (rgbB >> 8) & 0xff;
					int blueB = (rgbB) & 0xff;

					// Store the difference values
					differenceMatrix[x][y] += Math.abs(redA - redB);
					differenceMatrix[x][y] += Math.abs(greenA - greenB);
					differenceMatrix[x][y] += Math.abs(blueA - blueB);

					// If the number is less than 130 (a threshold I chose), set it to 0 to signify no change in current pixel.
					if (differenceMatrix[x][y] < 130)
						differenceMatrix[x][y] = 0;

				}
			}

			int dragAmount = 0;

			// Find the first change in the difference matrix by going from top left to bottom right, clearing lines vertically
			for (int x = 0; x < originalWidth && dragAmount == 0; x++)
				for (int y = 0; y < originalHeight; y++)
					if (differenceMatrix[x][y] != 0) {
						dragAmount = x - 6;
						break;
					}

			// Let the page load
			if (this.visibleElementFound(By.className("geetest_slider_button")))
				this.sleep(randNum(1000, 2000));

			WebElement dragButton = driver.findElement(By.className("geetest_slider_button"));
			Actions move = new Actions(driver);

			System.out.println("GeeTest found and simulating MouseKey movement..");

			// Move on top of the button with a seemingly random offset
			move.moveToElement(dragButton, 20 + new Random().nextInt(10), 20 + new Random().nextInt(10)).perform();

			// Wait between 1-2 seconds
			this.sleep(randNum(1000, 2000));

			// Left mouse button down
			move.clickAndHold().perform();

			// Wait between 1-2 seconds
			this.sleep(randNum(500, 1000));

			// Slowly move the slider with varying cursor height adjustments
			int totalDragAmount = 0, currentDragAmount = 1;

			// Queue up actions with random additions to the X movement
			while (totalDragAmount < dragAmount + 2) {
				move.moveByOffset(currentDragAmount, 0);
				totalDragAmount += currentDragAmount;
				currentDragAmount += new Random().nextInt(2);

//				currentDragAmount = (Math.abs(totalDragAmount - dragAmount) < 10 ? 9 : (Math.min(currentDragAmount, 19)));

//				if (Math.abs(totalDragAmount - dragAmount) < 10)
//					currentDragAmount = 9;
//				else if (currentDragAmount > 19)
//					currentDragAmount = 19;
			}

			// Start moving the image to the right, intentionally overshooting
			move.perform();

			// Wait a random time between 1-2 seconds to assist simulating human behavior
			this.sleep(randNum(200, 1000));

			while (dragAmount + this.randNum(-1, 2) < totalDragAmount) {
				move.moveByOffset(-1, 0);
				totalDragAmount -= 1;
			}

			// Start moving the image to the left as we intentionally overshot
			move.release().perform();

			// Wait for the page to load
//			this.sleep(2000);

			// Click the "reset" button if it exists
			if (this.visibleElementFound(By.className("geetest_reset_tip_content"))) {
//				this.sleep(randNum(1500, 2000));
				driver.findElement(By.className("geetest_reset_tip_content")).click();
			}
		}
	}

	/**
	 * Sleeps while handling {@link InterruptedException}
	 *
	 * @param milli Amount in milliseconds to sleep for
	 */
	private void sleep(int milli) {
		try {
			Thread.sleep(milli);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns a number between [a, b)
	 *
	 * @param min Minimum desired number
	 * @param max Maximum desired number
	 * @return A random number within the set [a, b)
	 */
	private int randNum(int min, int max) {
		if (max <= min)
			return 0;

		return new Random().nextInt(max - min) + min;
	}

	/**
	 * Checks for the visibility of an element.  The element must be visible regardless of being loaded.
	 *
	 * @param by desired element to find
	 * @return true if the element exists and is visible, false otherwise
	 */
	private boolean visibleElementFound(By by) {
		try {
			this.wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

//	private void waitForPageLoad(WebDriver driver) {
//		System.out.println("Waiting for the page to finish loading...");
//
//		new WebDriverWait(driver, Duration.ofSeconds(30)).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
//
//		System.out.println("Done!");
//	}
}