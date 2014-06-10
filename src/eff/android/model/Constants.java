/**
 * 
 */
package eff.android.model;

/**
 * @author Manu
 * 
 */
public class Constants {

	/**
	 * Server URL
	 */

	public static final String SERVER[] = { "localhost:8080",
			"dmz.safeviewtv.es:8180" };

	public static final String URL = "http://" + SERVER[0]
			+ "/eff-jboss-wicket/rest";

	/**
	 * Tag to identify labels between activities
	 */

	// User 
	public static final String USER_TOKEN = "token";
	public static final String USER_ID = "id";
	public static final String USER_FNAME = "firstName";
	public static final String USER_LNAME = "lastName";
	
	// Campaign
	public static final String CAMPAING_ID = "idCampaign";
	public static final String CAMPAING_TITLE = "nameCampaign";

	public static final String IDEA_ID = "idIdea";
	public static final String IDEA_TITLE = "shortTextIdea";
	public static final String LABEL_IDEA_DESCR = "descrIdea";

	// JSON Node names

	/* Campaigns */
	public static final String JSON_TAG_ID = "id";
	public static final String JSON_TAG_NAME = "name";
	public static final String JSON_TAG_CREATED_BY = "email";
	public static final String JSON_TAG_DUE_DATE = "dueDate";

	/* Ideas */
	public static final String JSON_TAG_SHORT_TEXT = "shortText";
	public static final String JSON_TAG_DESCRIPTION = "description";
	public static final String JSON_TAG_POSTED_AT = "postedAt";

	/* Comments */
	public static final String JSON_TAG_IDEA_COMMENT = "commentText";
	public static final String JSON_TAG_IDEA_AUTHOR = "postedBy";

}
