package PDA;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class PostCard {
	private final String publishDate;
	private final String title;
	private final String content;
	private final boolean isPrivate;

	public PostCard(WebElement postCard) {
		this.isPrivate = !getTagText(postCard, By.cssSelector("[data-tag='locked-rich-text-post']")).equals("N/A");
		this.publishDate = getTagText(postCard, By.cssSelector("[data-tag='post-published-at']"));
		this.title = getTagText(postCard, By.cssSelector("[data-tag='post-title']"));
		this.content = getTagText(postCard, By.cssSelector("[data-tag='post-content']"));
	}

	public String getPublishDate() {
		return this.publishDate;
	}

	public String getTitle() {
		return this.title;
	}

	public String getContent() {
		return this.content;
	}

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
}
