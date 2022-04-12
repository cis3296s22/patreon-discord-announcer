package PDA;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.LinkedList;

public class PostCard {
	private final String publishDate;
	private final String title;
	private final String url;
	private final String content;
	private final boolean isPrivate;

	public PostCard(WebElement postCard) {
		this.isPrivate = !getTagText(postCard, By.cssSelector("[data-tag='locked-rich-text-post']")).equals("N/A");
		this.publishDate = getTagText(postCard, By.cssSelector("[data-tag='post-published-at']"));
		this.title = getTagText(postCard, By.cssSelector("[data-tag='post-title']"));
		this.content = getTagText(postCard, By.cssSelector("[data-tag='post-content']"));

		String urlContainer = "N/A";

		// If the title exists then set the URL to the title's anchor href link, otherwise it's also N/A
		if (!this.title.equals("N/A")) {
			try {
				urlContainer = postCard.findElement(By.cssSelector("[data-tag='post-title']")).findElement(By.tagName("a")).getAttribute("href");

				// If the post is private then clean the URL of any possible junk that may tarnish it and lead us to a login page
				if (this.isPrivate) {
					int corruptLinkIndex = urlContainer.indexOf("https%3A%2F%2F");

					if (corruptLinkIndex != -1) {
						urlContainer = "https://" + urlContainer.substring(corruptLinkIndex + 14);
						urlContainer = urlContainer.replace("%2Fposts%2F", "/posts/");
					}
				}
			} catch (NoSuchElementException e) { /* Ignore the exception */ }
		}

		this.url = urlContainer;
	}

	/**
	 * Gets the published date and time of the {@link PostCard}
	 *
	 * @return date and time to this object's post
	 */
	public String getPublishDate() {
		return this.publishDate;
	}

	/**
	 * Gets the title of the {@link PostCard}
	 *
	 * @return title to this object's post
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Gets the direct URL/link of the {@link PostCard}
	 *
	 * @return URL to this object's post
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * Gets the content/text of the {@link PostCard}
	 *
	 * @return content to this object's post
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Checks to see if this is a private {@link PostCard}
	 *
	 * @return true if it is private, false otherwise
	 */
	public boolean isPrivate() {
		return this.isPrivate;
	}

	private String getTagText(WebElement postCard, By selector) {
		try {
			return postCard.findElement(selector).getText();
		} catch (NoSuchElementException e) {
			return "N/A";
		}
	}

	@Override
	public boolean equals(Object o) {
		// If the object is being compared with itself then return true
		if (o == this)
			return true;

		// If the object is not a PostCard object then return false
		if (!(o instanceof PostCard))
			return false;

		// If the object and this PostCard have the same URL then return true
		return this.url.equals(((PostCard) o).getUrl());
	}

	@Override
	public String toString() {
		return "Title: " + this.getTitle()
				+ "\nPublish Date: " + this.getPublishDate()
				+ "\nURL: " + this.getUrl()
				+ "\nContent: " + this.getContent()
				+ "\nisPrivate: " + this.isPrivate();
	}
}
