/**
 * 
 */
package eff.android.model;

import java.util.Date;

/**
 * @author manu
 * 
 */
public class Idea {

	// ID; of the user who is author of the idea
	// ID: of the campaign to add the idea
	// JSON: to create a new idea

	// {
	// "shortText": "New ideas from REST",
	// "description": null,
	// "postedAt": 1400492707157,
	// "postedBy": {
	// "id": 7
	// }
	// }

	private String shortText = null;
	private String description = null;
	private String postedAt;
	private String postedById;

	/**
	 * @param id: of the user who is author of the idea
	 */
	public Idea(String id) {

		// Id of the user that posted the idea
		this.postedById = id;

		// Idea gets the time
		this.postedAt = Long.valueOf(new Date().getTime()).toString();

	}

	/**
	 * @return the shortText
	 */
	public String getShortText() {
		return shortText;
	}

	/**
	 * @param shortText
	 *            the shortText to set
	 */
	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(String postedAt) {
		this.postedAt = postedAt;
	}

	/**
	 * @return the postedById
	 */
	public String getPostedById() {
		return postedById;
	}

	/**
	 * @param postedById
	 *            the postedById to set
	 */
	public void setPostedById(String postedById) {
		this.postedById = postedById;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Idea [shortText=" + shortText + ", description=" + description
				+ ", postedAt=" + postedAt + "]";
	}

}
