package eff.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import eff.android.model.Constants;
import eff.android.services.ServiceHandler;
import eff.android.utils.ConnectionDetector;

public class CampaignIdeasList extends ListActivity {

	// User token identification
	private static String token;
	private static String userId;
	

	// Loading spiner
	private ProgressDialog pDialog;

	// ID of the campaign to show all the ideas
	private static String campaginId;

	// Campaigns JSONArray
	JSONArray campaigns = null;

	// Hashmap for Ideas
	ArrayList<HashMap<String, String>> ideaList;

	private TextView campaingTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ideas);

		Intent in = getIntent();
		token = in.getStringExtra(Constants.USER_TOKEN);
		userId = in.getStringExtra(Constants.USER_ID);
		campaginId = in.getStringExtra(Constants.CAMPAING_ID);

		Log.i("userId", "CampaignIdeaList " + userId);

		// This is the title of the Campaign
		campaingTitle = (TextView) findViewById(R.id.campaignTitle);
		campaingTitle.setText(in.getStringExtra(Constants.CAMPAING_TITLE));

		// Array with all the ideas
		ideaList = new ArrayList<HashMap<String, String>>();

		ListView ideaListView = getListView();

		// Listview on item click listener
		ideaListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Lets check if the Internet connection is present
				if (ConnectionDetector.isConnectingToInternet() == false) {
					Toast.makeText(getApplicationContext(),
							"Network connection is not availabe",
							Toast.LENGTH_LONG).show();
					return;
				}

				// Lets check if the ExtremeFactories server is up
				if (ConnectionDetector
						.isConnectedToServer(Constants.URL, 10000) == false) {
					Toast.makeText(getApplicationContext(),
							"ExtremeFactories server is Down!!",
							Toast.LENGTH_LONG).show();
					return;
				}

				// getting values from selected ListItem
				String ideaId = ((TextView) view.findViewById(R.id.id))
						.getText().toString();

				// Starting single contact activity
				Intent in = new Intent(getApplicationContext(),
						IdeaDetail.class);
				in.putExtra(Constants.USER_TOKEN, token);
				in.putExtra(Constants.USER_ID, userId);
				in.putExtra(Constants.CAMPAING_ID, campaginId);
				in.putExtra(Constants.IDEA_ID, ideaId);
				startActivity(in);

			}
		});

		// Calling async task to get json
		new GetIdeas().execute();
	}

	// Get back to Main Campaign List
	@Override
	public void onBackPressed() {
		Intent in = new Intent(this, CampaingsList.class);
		// TODO: putExtra(JSON in String);
		in.putExtra(Constants.USER_TOKEN, token);
		in.putExtra(Constants.USER_ID, userId);
		startActivity(in);
	}

	public void postIdea(View view) {
		Intent in = new Intent(getApplicationContext(), IdeaEditorList.class);
		in.putExtra(Constants.CAMPAING_ID, campaginId);
		in.putExtra(Constants.CAMPAING_TITLE, campaingTitle.getText()
				.toString());
		in.putExtra(Constants.USER_TOKEN, token);
		in.putExtra(Constants.USER_ID, userId);
		startActivity(in);

	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetIdeas extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(CampaignIdeasList.this);
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
					+ campaginId + "/ideas", token, ServiceHandler.GET);

			if (jsonStr != null) {
				try {
					JSONArray jsonArray = new JSONArray(jsonStr);

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);

						// Idea ID
						String ideaId = jsonObj
								.getString(Constants.JSON_TAG_ID);

						// Title
						String shortText = jsonObj
								.getString(Constants.JSON_TAG_SHORT_TEXT);

						// Description
						String description = jsonObj
								.getString(Constants.JSON_TAG_DESCRIPTION);
						if (description != null)
							description = "No description has been provided.";

						// Idea posted at date & format
						Date date = new Date(Long.valueOf(jsonObj
								.getString(Constants.JSON_TAG_POSTED_AT)));
						SimpleDateFormat sdf = new SimpleDateFormat(
								"dd/MM/yyyy", Locale.ENGLISH);
						String postedAt = sdf.format(date);

						// tmp hashmap for single campaign
						HashMap<String, String> campaign = new HashMap<String, String>();
						campaign.put(Constants.JSON_TAG_ID, ideaId);
						campaign.put(Constants.JSON_TAG_SHORT_TEXT, shortText);
						campaign.put(Constants.JSON_TAG_DESCRIPTION,
								description);
						campaign.put(Constants.JSON_TAG_POSTED_AT, postedAt);

						ideaList.add(campaign);
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

			/**
			 * Updating parsed JSON data into ListView
			 * */
			ListAdapter adapter = new SimpleAdapter(CampaignIdeasList.this,
					ideaList, R.layout.ideas_item, new String[] {
							Constants.JSON_TAG_ID,
							Constants.JSON_TAG_SHORT_TEXT,
							Constants.JSON_TAG_DESCRIPTION,
							Constants.JSON_TAG_POSTED_AT }, new int[] {
							R.id.id, R.id.name, R.id.createdBy, R.id.duedate });

			setListAdapter(adapter);
		}

	}

}
