package PDA;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.security.auth.login.LoginException;


public class DiscordBotTest{

    // DiscordBot db;
    String token;
    String channel = "0";


    @Before
    public void setup() throws LoginException, InterruptedException, IOException, ParseException {

        // when testing the bot, we need correct login information(token) in order to make sure everything works
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("config.json"));
        System.out.println(jsonObject);
        Object token = jsonObject.get("TOKEN");
        this.token = token.toString().replaceAll("[\\[\\](){}]", "");
    }

    @Test
    public void setupDiscordBot() throws InterruptedException, LoginException {

        // instantiate a new discord bot with the token (channel doesn't matter)
        DiscordBot db = new DiscordBot(token, channel);

        // get all the guilds(discord servers) assigned to db that we recorded
        Set<Guild> guilds = db.getAllGuilds();

        // shows that the guilds the bot is assigned to is the same as recorded in our DiscordBot class
        assertEquals("size should be the same: ", db.getJDA().getGuilds().size(), guilds.size());
    }




}