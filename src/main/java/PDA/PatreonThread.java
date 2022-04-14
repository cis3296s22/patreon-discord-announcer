package PDA;

import ch.qos.logback.classic.Logger;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.*;


public class PatreonThread extends Thread {
	String webhookUrl;
	DiscordBot bot;
	Wait<WebDriver> wait;
	By postCardSelector;
	public boolean ranFunction = false;
	Logger log;

	public PatreonThread(String webhookUrl, DiscordBot bot) {
		this.setName("PatreonThread");
		this.webhookUrl = webhookUrl;
		this.bot = bot;
		this.postCardSelector = By.cssSelector("[data-tag='post-card']");
		this.log = (Logger) LoggerFactory.getLogger(this.getName());
	}

	@Override
	public void run() {
		// Create and initialize the browser
		log.info("Initializing Firefox driver...");
		WebDriver driver = createBrowser();

		// If the browser failed to initialize for whatever reason, stop PDA.
		if (driver == null) {
			log.error("The Firefox driver failed to initialize.  Stopping PDA.");
			System.exit(1);
		}

		log.info("Initialized the Firefox driver!");

		// Initialize our waiting interface
		log.info("Initializing waiting interface...");
		wait = new FluentWait<>(driver)
				.withTimeout(Duration.ofSeconds(5))
				.pollingEvery(Duration.ofMillis(250));
		log.info("Initialized the waiting interface!");

		log.info("Setup complete.  Starting to scan.");
		while (true) {
			Set<Guild> localSet = new HashSet<>(PDA.guildSet);

			for (Guild guild : localSet) {
				System.out.println("GuildSet: " + localSet);
				System.out.println("Current Guild: " + guild);

				ArrayList<String> localUrls = PDA.patreonUrls.get(guild);
				for (int i = 0; i < localUrls.size(); i++) { // using a foreach loop will cause a ConcurrentModificationException since we are iterating through the collection while adding a link to the patreonUrls
					goToLoginPage(driver, guild, localUrls.get(i));

					System.out.println("Finding all posts on the front page...");

					// Wait for any postCard to be visible
					if (this.visibleElementFound(postCardSelector))
						this.sleep(4000);

					List<WebElement> foundPostElements = driver.findElements(postCardSelector);

					for (WebElement currentPostElement : foundPostElements) {
						PostCard currentPostCard = new PostCard(currentPostElement);

						handlePost(guild, currentPostCard);

//						if (currentPostCard.isPrivate()) { // * Private post
//							if (!PDA.privatePosts.get(guild).contains(currentPostCard)) {
//								System.out.println("\n\n" + currentPostCard);
//
//								LinkedList<PostCard> temp = PDA.privatePosts.get(guild);
//								temp.add(currentPostCard);
//								PDA.privatePosts.put(guild, temp);
//								announcePost(currentPostCard, guild);
//							}
//						} else { // * Public post
//							if (!PDA.publicPosts.get(guild).contains(currentPostCard)) {
//								System.out.println("\n\n" + currentPostCard);
//
//								LinkedList<PostCard> temp = PDA.publicPosts.get(guild);
//								temp.add(currentPostCard);
//								PDA.publicPosts.put(guild, temp);
//								announcePost(currentPostCard, guild);
//							}
//						}
					}
				}
			}

			// Wait between 4-5 minutes until the next scan.  Prevents rate limiting & IP blocks
			double sleepTime = 1000;// randNum(240000, 300000);
			System.out.printf("\nWaiting %s until the next Patreon page scan...\n\n", formatTime((int) sleepTime / 1000));
			this.sleep((int) sleepTime);
		}
	}

	private void handlePost(Guild guild, PostCard postCard) {
		HashMap<Guild, LinkedList<PostCard>> hashMap = postCard.isPrivate() ? PDA.privatePosts : PDA.publicPosts;

		if (!hashMap.get(guild).contains(postCard)) {
			System.out.println("\n\n" + postCard);

			LinkedList<PostCard> temp = hashMap.get(guild);
			temp.add(postCard);
			hashMap.put(guild, temp);
			announcePost(postCard, guild);
		}
	}

	private void announcePost(PostCard data, Guild guild) {
//		if (1 == 1) {
//			System.out.println("\n\n" + data);
//			return;
//		}


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

		System.out.println("Waiting for post cards to be found...");
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

				driver.manage().deleteAllCookies();
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

		// For some reason the page isn't loading.  Simply return from the GeeTest function so the program can navigate elsewhere.
		if (!this.visibleElementFound(By.className("geetest_radar_btn")))
			return;

		// Store the GeeTest verification button
		WebElement geeTestVerify = driver.findElement(By.className("geetest_radar_btn"));

		// Click the GeeTest verification button
		geeTestVerify.click();

		int loopAttempts = 0;

		// While the puzzle is visible, attempt to solve it repeatedly
		while (loopAttempts < 4 && this.visibleElementFound(By.className("geetest_canvas_bg"))) {
			loopAttempts++;
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
				this.sleep(randNum(500, 1000));

			WebElement dragButton = driver.findElement(By.className("geetest_slider_button"));
			Actions move = new Actions(driver);

			System.out.println("GeeTest found and simulating MouseKey movement..");

			// Move on top of the button with a seemingly random offset
			move.moveToElement(dragButton, 20 + new Random().nextInt(10), 20 + new Random().nextInt(10)).perform();

			// Wait between 1-2 seconds
//			this.sleep(randNum(500, 1000));

			// Left mouse button down
			move.clickAndHold().perform();

			// Wait between 1-2 seconds
			this.sleep(randNum(500, 1000));

			// Slowly move the slider with varying cursor height adjustments
			int totalDragAmount = 0, currentDragAmount = 1, overShoot = 0;
			boolean toggle = false;

			if (1 == 1) {
				move.moveByOffset(dragAmount + (dragAmount % 2 == 0 ? 2 : -2), 0).perform();
				this.sleep(500);

				if (dragAmount > 90)
					this.sleep(randNum(500, 900));

				move.release().perform();
			} /* else {
//				if (1 == 0) {
//					int[] movementArray = {1, 3, 5, 9, 15};
//
//					for (int moveAmount : movementArray) {
//						move.moveByOffset(moveAmount, 0).perform();
//						if (moveAmount < 9)
//							this.sleep(this.randNum(100, 300));
//						else
//							this.sleep(this.randNum(200, 400));
//					}
//
//					dragAmount -= 33;
//
//					while (dragAmount > 0) {
//						if (dragAmount > 66) {
//							move.moveByOffset(25, 0).perform();
//							dragAmount -= 25;
//						} else if (dragAmount > 44) {
//							move.moveByOffset(22, 0).perform();
//							dragAmount -= 22;
//						} else if (dragAmount >= 11) {
//							move.moveByOffset(11, 0).perform();
//							dragAmount -= 11;
//						} else {
//							move.moveByOffset(dragAmount, 0).perform();
//							dragAmount = 0;
//						}
//						this.sleep(this.randNum(300, 400));
//					}
//
//					move.release().perform();
//				} else {
				// Queue up actions with random additions to the X movement
				while (totalDragAmount < dragAmount + 2) {
					if (currentDragAmount < 22)
						currentDragAmount += new Random().nextInt(2);

					if (currentDragAmount + totalDragAmount >= dragAmount) {
						overShoot = currentDragAmount + totalDragAmount - dragAmount;
						toggle = true;
					}

					move.moveByOffset(currentDragAmount, 0);
					totalDragAmount += currentDragAmount;

					if (toggle)
						break;
				}

				// Start moving the image to the right, intentionally overshooting
				move.perform();

				// Wait a random time between 1-2 seconds to assist simulating human behavior
				this.sleep(randNum(500, 1000));

				// Moving to the left to fix over-shooting
				while (overShoot > 0) {
					move.moveByOffset(-1, 0);
					totalDragAmount -= 1;
					overShoot -= 1;
				}

				// Start moving the image to the left as we intentionally overshot
				move.release().perform();
			} */
//			}

			// Click the "reset" button if it exists
			if (this.visibleElementFound(By.className("geetest_reset_tip_content"))) {
				this.sleep(randNum(500, 1000));

				WebElement resetButton = driver.findElement(By.className("geetest_reset_tip_content"));
				move.moveToElement(resetButton);
				this.sleep(randNum(500, 1000));

				resetButton.click();
			}
		}

		if (loopAttempts >= 5) {
			driver.get("https://www.example.com");
			this.sleep(1500);
			driver.manage().deleteAllCookies();
		}
	}

	private WebDriver createBrowser() {
		try {
			FirefoxOptions options = new FirefoxOptions();
//			options.setHeadless(true);
			options.setLogLevel(FirefoxDriverLogLevel.FATAL);

			// Marionette is required to redirect Firefox's logging
			options.setCapability(FirefoxDriver.Capability.MARIONETTE, "true");

			// Set redirect location.  If OS is Windows based then "NUL:", otherwise "/dev/null"
			String logLocation = SystemUtils.OS_NAME.startsWith("Windows") ? "NUL:" : "/dev/null";

			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, logLocation);

			// Create the driver
			return WebDriverManager.firefoxdriver().capabilities(options).create();
		} catch (Exception e) {
			return null;
		}
	}

	// JUnit testing purposes
	public void testSleep() {
		sleep(1000);
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
		ranFunction = true;
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

	private String formatTime(int timeInSeconds) {
		int seconds = timeInSeconds % 3600 % 60;
		int minutes = (int) Math.floor(timeInSeconds % 3600 / 60);

		if (minutes == 0)
			return seconds + "s";

		return minutes + "m:" + seconds + "s";
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
}