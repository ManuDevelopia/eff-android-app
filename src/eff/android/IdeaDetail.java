package eff.android;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import eff.android.model.Constants;
import eff.android.services.ServiceHandler;

public class IdeaDetail extends ListActivity {

	// User token identification
	private static String token;

	// Loading spiner
	private ProgressDialog pDialog;

	// ID of the campaign & idea to show all the idea details
	private static String campaginId;
	private static String ideaId;

	// Elements to show details of ideas
	TextView shortTextIdea;
	TextView descriptionIdea;

	// JSON with the idea details
	JSONObject jsonObj;

	// data about the idea
	String shortText;
	String description;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> commentList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idea);

		// getting intent data
		Intent in = getIntent();
		token = in.getStringExtra(Constants.USER_TOKEN);

		// Get JSON values from previous intent
		campaginId = in.getStringExtra(Constants.CAMPAING_ID);
		ideaId = in.getStringExtra(Constants.IDEA_ID);

		shortTextIdea = (TextView) findViewById(R.id.short_text_idea);
		descriptionIdea = (TextView) findViewById(R.id.description);

		// Array with all the ideas
		commentList = new ArrayList<HashMap<String, String>>();

		// Calling async task to get json
		new GetIdeas().execute();
	}

	public void voteUp(View view) {
		Log.i("votes", "up");
		Button buttonUp = (Button) findViewById(R.id.voteup);
		buttonUp.setEnabled(false);
	}

	public void voteDown(View view) {
		Log.i("votes", "down");
		Button buttonDown = (Button) findViewById(R.id.votedown);
		buttonDown.setEnabled(false);
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetIdeas extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(IdeaDetail.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(Constants.URL + "/campaigns/"
					+ campaginId + "/ideas/" + ideaId, token,
					ServiceHandler.GET);

			if (jsonStr != null) {
				try {

					// Get the comments
					jsonObj = new JSONObject(jsonStr);
					JSONArray commentsArray = jsonObj.optJSONArray("comments");

					// Get comment author
					for (int i = 0; i < commentsArray.length(); i++) {
						JSONObject commentJson = commentsArray.getJSONObject(i);

						JSONObject author = commentJson
								.getJSONObject(Constants.JSON_TAG_IDEA_AUTHOR);

						// tmp hashmap for single comment
						HashMap<String, String> comment = new HashMap<String, String>();
						comment.put(
								Constants.JSON_TAG_IDEA_COMMENT,
								commentJson
										.getString(Constants.JSON_TAG_IDEA_COMMENT));
						comment.put(Constants.JSON_TAG_IDEA_AUTHOR,
								"by " + author.getString("firstName") + " "
										+ author.getString("lastName"));

						commentList.add(comment);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			// Dismiss the progress dialog
			if (pDialog.isShowing())
				pDialog.dismiss();

			// Displaying all values on the screen from the JSON
			try {

				shortText = jsonObj.getString(Constants.JSON_TAG_SHORT_TEXT);
				shortTextIdea.setText(shortText);

				description = jsonObj.getString(Constants.JSON_TAG_DESCRIPTION);
				if (description != null)
					description = "No description has been provided.";
				descriptionIdea.setText(description);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(IdeaDetail.this, commentList,
					R.layout.comments_item, new String[] {
							Constants.JSON_TAG_IDEA_COMMENT,
							Constants.JSON_TAG_IDEA_AUTHOR }, new int[] {
							R.id.comment, R.id.comment_author });

			setListAdapter(adapter);

		}

	}

}
