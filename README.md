# Patreon Discord Announcer (PDA)
PDA is an application that utilizes Discord as a notification platform to inform users

the user in the desired Discord channel of any new Patreon posts from designated Patreon boards. The Patreon Discord Announcer software application or PDA, after being properly configured and built, will run in a terminal/console window and immediately load locally stored data of all of the previously announced patreon posts. Doing so is required to avoid announcing posts that have already been announced. The software will output in the terminal how many announcements were previously stored and loaded then load a headless browser to continuously scan the user configured Patreon board. The moment the software finds a new post on the designated board it will output to the designated Discord server through a Discord bot if the user has not configured their own bot.

![This is a screenshot.](pda_pic.png)

## Features
* 

## How to Run
1. **Method: Invite our PDA Bot** - *Recommended, quick and simple*
    1) [Add PDA to your Discord server](https://discord.com/oauth2/authorize?client_id=965002259689783296&scope=bot&permissions=68608)
    2) Use [Bot Commands](#bot-commands) listed below to get updates on your favorite content creators
2. **Method: Use your own bot** - *Not recommended, highly technical*
    1) Download the latest [PDA release](https://github.com/cis3296s22/patreon-discord-announcer/releases)
    2) Extract the downloaded zip in any directory
    3) Inside the directory containing the extracted PDA files, open config.json and replace **TOKEN**'s value **0** with your [Discord bot token](asd)


<!-- 5) If wish to apply the PDA to your own bot and don't have one, follow the instructions in the [Setup Discord Bot](#setup-discord-bot) section.
6) If your personal discord bot has not been added to the server where you want to use the PDA, follow the instructions in the [Add Discord Bot To A Server](#add-discord-bot-to-a-server) section.
7) If you do not have the developer mode on discord enabled, follow the instructions in the [Enable Developer Mode](#enable-developer-mode) section.
8) Right click the channel where you want the bot to initially send messages, at the bottom of the pop-up, click "Copy ID".
9) Inside of the config.json file, paste your discord bot's token value into the "TOKEN" variable(if you do not have this follow the instructions in the "Setup Discord Bot" section).
10) Inside of the config.json file, paste your discord channel ID into the "Channel" variable.
11) Open a terminal/console and navigate to the directory where the PDA binary is stored.
12) Run `java -jar patreon-discord-announcer-x.x-jar-with-dependencies.jar` in the terminal to run PDA.  **Ensure x.x is changed to the appropriate version number**
13) The console shall then display all posts on the page shown, close the Google Chrome instance, then terminate peacefully. -->

## Bot Commands
* Displays this list of commands in your Discord channel, can also be used to test if bot is currently online.  
  > `/help`
* Set the channel id the bot will announce posts in
  > /setchannel 920036233038671953
* Allows the user to select the creator page from patreon by copy and pasting the url after the command.  
  > /addlink https://www.patreon.com/supermega
* Allows the user to remove a channel they no longer wish to recieve notifications from.  
  > /removelink https://www.patreon.com/supermega

## Building
- **System Build Requirements**
    - Java JDK 8 or later
    - Maven 3.8.1 or later
    - Firefox 98 or later
- **Manually (IntelliJ IDEA)**
    1) Open IntelliJ and clone this repository through the program
    2) After the project is cloned and opened, at the top of IntelliJ, go to **View** -> **Tool Windows** -> **Maven**
    3) You can now build the package using Maven by going to **PDA** -> **Lifecycle** -> **package**
- **Manually (with Maven)**
    1) Clone this repository
    2) Enter the project folder in Command Prompt (Windows) or Terminal (macOS/\*nix)
    3) Use Maven to package the project by running `mvn package`
- **Automatically (with Maven)**
    ```
    git clone https://github.com/cis3296s22/patreon-discord-announcer.git
    cd patreon-discord-announcer
    mvn package
    ```

After building with Maven, you can find or run the package by going into the **target** folder.  It will be named **patreon-discord-announcer-x.x-jar-with-dependencies.jar** (replace x.x with the version number)

# Contribute
Follow this project board to know the latest status of the project: [https://github.com/cis3296s22/patreon-discord-announcer/projects/2](https://github.com/cis3296s22/patreon-discord-announcer/projects/2)
