# Patreon Discord Announcer(PDA)
This document proposes a software application that allows the user to add a bot to their Discord server that will notify the user in the desired Discord channel of any new Patreon posts from a designated Patreon board. The Patreon Discord Announcer software application or PDA, after being properly configured and built, will run in a terminal/console window and immediately load locally stored data of all of the previously announced patreon posts. Doing so is required to avoid announcing posts that have already been announced. The software will output in the terminal how many announcements were previously stored and loaded then load a headless browser to continuously scan the user configured Patreon board. The moment the software finds a new post on the designated board it will output to the designated Discord server through a Discord bot if the user has not configured their own bot.

![This is a screenshot.](pda_pic.png)
# Two Ways to Run

### 1. Invite our PDA Bot
*Recommended - Our preconfigured bot makes using PDA quick and simple.

1) Choose or create the Discord server where you want to use PDA Bot.
2) Click this link to invite our Bot https://discord.com/api/oauth2/authorize?client_id=937463083876614216&permissions=8&scope=bot
3) Select the server from Step 1 in the invitation prompt.
4) Use [Bot Commands](#bot-commands) listed below to get updates on your favorite content creators.


### 2. Create/use your own bot
If you are a more technical user, you can use our PDA tool to create and run your own bot locally.

1) Download the latest PDA binary from the Release section on the right on GitHub
2) Make sure you meet all requirements under the [Building From Source](#building-from-source) section.
3) If wish to apply the PDA to your own bot and don't have one, follow the instructions in the [Setup Discord Bot](#setup-discord-bot) section.
4) If your personal discord bot has not been added to the server where you want to use the PDA, follow the instructions in the [Add Discord Bot To A Server](#add-discord-bot-to-a-server) section.
5) If you do not have the developer mode on discord enabled, follow the instructions in the [Enable Developer Mode](#enable-developer-mode) section.
6) Right click the channel where you want the bot to initially send messages, at the bottom of the pop-up, click "Copy ID".
7) Inside of the config.json file, paste your discord bot's token value into the "TOKEN" variable(if you do not have this follow the instructions in the "Setup Discord Bot" section).
8) Inside of the config.json file, paste your discord channel ID into the "Channel" variable.
9) Open a terminal/console and navigate to the directory where the PDA binary is stored.
10) Run `java -jar patreon-discord-announcer-x.x-jar-with-dependencies.jar` in the terminal to run PDA.  **Ensure x.x is changed to the appropriate version number**
11) Pay attention to the output of the console, it may ask you to assist it in passing a bot-check that is being displayed on the web browser.  After doing so, follow the instructions in the terminal.
12) The console shall then display all posts on the page shown, close the Google Chrome instance, then terminate peacefully.

### Setup Discord Bot
1) Go to [this](https://discord.com/developers/applications) link and click "New Application"
2) Give your application a name, then navigate to the "Bot" tab and click "Add Bot" to create your bot.
3) Make sure the "Public Bot" option is ticked and the "Requires OAuth2 Grant" is unticked
4) Save your bot's token by pressing the "Copy" button (if you don't see the "Copy" button, press "Reset Token" and it should pop up).

### Add Discord Bot To A Server
1) Make sure you're logged on to the [Discord website](https://discord.com/).
2) Navigate to the [application page](https://discord.com/developers/applications).
3) Click on your bot's page.
4) Go to the "OAuth2" tab.
5) Tick the "bot" checkbox under "scopes".
6) Tick the permissions required for your bot to function under "Bot Permissions" (Please be aware of the consequences of required your bot to have the "Administrator" permission).
7) Bot owners must have 2FA enabled for certain actions and permissions when added in servers that have Server-Wide 2FA enabled
8) Now the resulting URL can be used to add your bot to a server. Copy and paste the URL into your browser, choose a server to invite the bot to(will only allow the bot to be invited to servers where you have the "Manage Server" permissions). Then click "Authorize".

### Enable Developer Mode
1) Open the discord application.
2) On the bottom left corner of the application, click the cog wheel settings icon.
3) Go to the "Advanced" section and tick on "Developer Mode".


# Bot Commands
/help - Displays this list of commands in your Discord channel.  
/test - Bot will reply with "I AM HERE" if server is running.  
/embed - Bot will show an example of an embed in Discord channel.  
/setchannel - Allows the user to set the channel when command is followed with channel ID (i.e. /setchannel <Channel ID>).  
/setlink - Allows the user to select the creator page from patreon by copy and pasting the link after the command   
  (i.e. /setlink https:​//exampleurl).

# How to Contribute
Follow this project board to know the latest status of the project: [https://github.com/cis3296s22/patreon-discord-announcer/projects/1](https://github.com/cis3296s22/patreon-discord-announcer/projects/1)

# Building From Source
### Requirements
- Java JDK >= 8
- Maven >= 3
- Google Chrome

### Manually (with Maven >= 3)
- Clone this repository
- Enter the project folder
- Use Maven to package the project by running `mvn package`
- Either go into the **target** folder and run **patreon-discord-announcer-x.x-jar-with-dependencies.jar** (replace x.x with the version number) or move the file to your current directory with `mv target/patreon-discord-announcer-*-*.jar .`

### Manually (with IntelliJ IDEA >= 2021.3.x)
- Open IntelliJ and clone this repository
- After the project is cloned and opened, at the top of IntelliJ, go to **View** -> **Tool Windows** -> **Maven**
- You can now build the package using Maven by going to **PDA** -> **Lifecycle** -> **package**

### Automatically (with Maven)
```
git clone https://github.com/cis3296s22/patreon-discord-announcer.git
cd patreon-discord-announcer
mvn package
mv target/patreon-discord-announcer-*-*.jar .
```
