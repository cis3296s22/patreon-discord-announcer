package PDA;

import ch.qos.logback.classic.Logger;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JSONHelper {
	/**
	 * log holds the reference to a {@link Logger} object to output clean messages to the console
	 */
	private static final Logger log = (Logger) LoggerFactory.getLogger("JSONHelper");

	/**
	 * Creates a new {@link JSONObject} holding the data
	 *
	 * @param filename holds the String value of the json file we want to parse
	 * @return a {@link JSONObject} holding the information from the json file parsed
	 */
	public static JSONObject parseJSONFile(String filename) {
		// Container for the JSON text we will attempt to read.
		String jsonText;

		// Set jsonText to filenames text if the file exists, otherwise return null.
		try {
			jsonText = IOUtils.toString(new FileInputStream(filename), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return null;
		}

		return new JSONObject(jsonText);
	}

	/**
	 * Creates a {@link JSONObject} holding the information from the {@link PostCard} object passed through the arguments
	 *
	 * @param postCard holds the information from a post on a patreon link
	 * @return a {@link JSONObject} holding the information from the postCard
	 */
	public static JSONObject createPostJSONObject(PostCard postCard) {
		JSONObject postDetails = new JSONObject();

		postDetails.put("isPrivate", postCard.isPrivate());
		postDetails.put("publishDate", postCard.getPublishDate());
		postDetails.put("title", postCard.getTitle());
		postDetails.put("content", postCard.getContent());

		return postDetails;
	}

	/**
	 * Will parse through the data saved to posts.json and add it to the posts data saved on the program's memory in HashMaps
	 *
	 * @param bot holds the reference to the single {@link DiscordBot} object we use to talk with discord
	 * @param fileName holds the String value of the json file we want to parse
	 */
	public static void parseSavedData(DiscordBot bot, String fileName) {
		JSONObject savedJson = JSONHelper.parseJSONFile(fileName);

		if (savedJson == null) {
			log.error("An error occurred while reading '" + fileName + "'");
			System.exit(1);
		}

		// Loop through every URL stored
		for (String curPatreonUrl : savedJson.keySet()) {
			JSONObject curWebsite = savedJson.getJSONObject(curPatreonUrl);

			// Loop through every guild ID stored inside this URL
			for (String curGuildId : curWebsite.keySet()) {
				JSONObject guildPosts = curWebsite.getJSONObject(curGuildId);

				for (String curPostUrl : guildPosts.keySet()) {
					JSONObject postDetails = (JSONObject) guildPosts.get(curPostUrl);

					try {
						String publishDate = postDetails.get("publishDate").toString();
						String title = postDetails.get("title").toString();
						String content = postDetails.get("content").toString();
						boolean isPrivate = Boolean.parseBoolean(postDetails.get("isPrivate").toString());

						PostCard postCard = new PostCard(publishDate, title, curPostUrl, content, isPrivate);
						Guild guild = bot.getJDA().getGuildById(curGuildId);

						PDA.addPostCard(postCard, guild);
					} catch (Exception e) {
						log.error("An error occurred while getting post '{}' for guild '{}'", curPostUrl, curGuildId);
					}
				}
			}
		}
	}
}
