# Project Name
Put here a short paragraph describing your project. 
Adding an screenshot or a mockup of your application in action would be nice.  

![This is a screenshot.](images.png)
# How to Run
### Requirements
- Java 8 or greater
- Google Chrome 98 or greater
   
1) Download the latest PDA binary from the Release section on the right on GitHub
2) Navigate to [ChromeDriver downloads](https://chromedriver.chromium.org/downloads) and download the release that best matches your version of Google Chrome.  **Ensure the binary you download does not exceed your version of Google Chrome**
3) Put both the PDA binary and the ChromeDriver in the same directory
4) Open a terminal/console and navigate to the directory where the PDA binary is stored.
5) Run `java -jar patreon-discord-announcer-x.x-jar-with-dependencies.jar` in the terminal to run PDA.  **Ensure x.x is changed to the appropriate version number**
6) Pay attention to the output of the console, it may ask you to assist it in passing a bot-check that is being displayed on the web browser.  After doing so, follow the instructions in the terminal.
7) The console shall then display all posts on the page shown, close the Google Chrome instance, then terminate peacefully.

# How to Contribute
Follow this project board to know the latest status of the project: [https://github.com/cis3296s22/patreon-discord-announcer/projects/1](https://github.com/cis3296s22/patreon-discord-announcer/projects/1)

# Building From Source
### Requirements
- Java JDK >= 8

### Manually (with Maven >= 3)
- Clone this repository
- Enter the project folder
- Use Maven to package the project by running `mvn package`
- Either go into the **target** folder and run **patreon-discord-announcer-x.x-jar-with-dependencies.jar** (replace x.x with the version number) or move the file to your current directory with `mv target/patreon-discord-announcer-*.jar .`

### Manually (with IntelliJ IDEA >= 2021.3.x)
- Open IntelliJ and clone this repository
- After the project is cloned and opened, at the top of IntelliJ, go to **View** -> **Tool Windows** -> **Maven**
- You can now build the package using Maven by going to **PDA** -> **Lifecycle** -> **package**

### Automatically (with Maven)
```
git clone https://github.com/cis3296s22/patreon-discord-announcer.git
cd patreon-discord-announcer
mvn package
mv target/patreon-discord-announcer-*.jar .
```
