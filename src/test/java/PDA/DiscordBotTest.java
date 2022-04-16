package PDA;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.security.auth.login.LoginException;

/**
 * Testing the implementation of PDA.
 *
 * Responsibilities:
 *
 * 1) Test if the classes in our PDA package correctly implements its functions
 *
 */

public class DiscordBotTest{

    // DiscordBot db;
    String token;


    @Before
    public void setup() throws LoginException, InterruptedException, IOException, ParseException {

        // when testing the bot, we need correct login information(token) in order to make sure everything works as we need a bot with servers in it to test it
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("config.json"));
        System.out.println(jsonObject);
        Object token = jsonObject.get("TOKEN");
        this.token = token.toString().replaceAll("[\\[\\](){}]", "");
    }

    @Test
    public void testDiscordBot() throws InterruptedException, LoginException {

        // instantiate a new discord bot with the token (channel doesn't matter)
        DiscordBot db = new DiscordBot(token);

        // get all the guilds(discord servers) assigned to db that we recorded
        Set<Guild> guilds = db.getAllGuilds();

        // shows that the guilds the bot is assigned to is the same as recorded in our DiscordBot class
        assertEquals("size should be the same: ", db.getJDA().getGuilds().size(),  guilds.size());
    }

    @Test
    public void testEventListener() throws LoginException, InterruptedException {
        // we need to assume that DiscordBot already has guilds in it already

        DiscordBot db = new DiscordBot(token);

        // create fake event
        long responseNum = 10;
        GuildJoinEvent ev = new GuildJoinEvent(db.getJDA(), responseNum, db.getJDA().getGuilds().get(0));
        EventListener listener = new EventListener(db);

        // call EventListener method onGuildJoin and see that it gets to the end of the method by setting commandRan = true
        listener.onGuildJoin(ev);
        assertTrue("running onGuildJoin should set the variable to true after the entire function runs", listener.commandRan);
    }

    @Test
    public void testPatreonThread() throws LoginException, InterruptedException {

        // instantiate discord bot and patreonThread
        DiscordBot db = new DiscordBot(token);
        PatreonThread p = new PatreonThread(db);

        // when running sleep, it will set a boolean to true to make sure that it ran without errors
        p.testSleep();
        assertTrue("running the testSleep() function should see if our sleep function works correctly and sets ranFunction to true", p.ranFunction);
    }

    @Test
    public void testPostcard() throws LoginException, InterruptedException {

        // instantiate discord bot
        DiscordBot db = new DiscordBot(token);

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
        WebDriver driver = WebDriverManager.chromedriver().capabilities(options).create();

        PostCard post = new PostCard(driver.findElement(By.tagName("body")));


        assertTrue("should be public because we aren't technically giving it any post to find", !post.isPrivate());
    }


}