package PDA.apis;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.LoginException;

import static org.junit.jupiter.api.Assertions.*;

class DiscordBotTest{

    DiscordBot db;
    String token = null;
    String channel = null;
    String id = null;

    @BeforeClass
    public static void setUpClass(){
    }

    @AfterClass
    public static void tearDownClass(){

    }

    @Before
    public void setup() throws LoginException, InterruptedException {
        db = new DiscordBot(token, channel);
    }

    @After
    public void tearDown(){
    }

    @Test
    void shouldSetTitle() {
        db.setTitle("Hello", 560673492916240394L);
        assertEquals("Hello", null);
    }

    @Test
    void shouldAddChannel() {
    }

    @Test
    void shouldSetDescription() {
    }

    @Test
    void shouldSetFooter() {
    }

    @Test
    void shouldClearEmbed() {
    }

    @Test
    void shouldSend() {
    }


}