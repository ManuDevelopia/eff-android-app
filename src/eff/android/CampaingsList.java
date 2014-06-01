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

/**
 * @author Manu
 * 
 */

public class CampaingsList extends ListActivity {

	// User token identification
	private String token;
	private String userId;

	// Loading spiner
	private ProgressDialog pDialog;

	// URL to get contacts JSON
	private static String url = Constants.URL + "/campaigns";

	// contacts JSONArray
	JSONArray campaigns = null;

	// Hashmap for ListView
	ArrayList<HashMap<String, String>> campaignList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campaigns);

		Intent in = getIntent();
		token = in.getStringExtra(Constants.USER_TOKEN);
		userId = in.getStringExtra(Constants.USER_ID);
		
		Log.i("userId", "CampaignList " + userId);

		campaignList = new ArrayList<HashMap<String, String>>();

		ListView campaignListView = getListView();

		// Listview on item click listener
		campaignListView.setOnItemClickListener(new OnItemClickListener() {

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
				String idCampaign = ((TextView) view.findViewById(R.id.id))
						.getText().toString();
				String titleCampaign = ((TextView) view.findViewById(R.id.name))
						.getText().toString();

				// Starting idea list activity
				Intent in = new Intent(getApplicationContext(),
						CampaignIdeasList.class);
				in.putExtra(Constants.CAMPAING_ID, idCampaign);
				in.putExtra(Constants.CAMPAING_TITLE, titleCampaign);
				in.putExtra(Constants.USER_TOKEN, token);
				in.putExtra(Constants.USER_ID, userId);
				startActivity(in);

			}
		});

		// Calling async task to get json
		new GetCampaigns().execute();
	}

	// TODO: implement this to exit from the application with twice clicks
	// @Override
	// public void onBackPressed() {
	// }

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetCampaigns extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog(CampaingsList.this);
			pDialog.setMessage("Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(url, token, ServiceHandler.GET);

			Log.i("Campaign", jsonStr);

			if (jsonStr != null) {
				try {
					JSONArray jsonArray = new JSONArray(jsonStr);

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						String id = jsonObj.getString(Constants.JSON_TAG_ID);

						String name = jsonObj
								.getString(Constants.JSON_TAG_NAME);
						String createdBy = "by "
								+ jsonObj.getJSONObject("createdBy").getString(
										"firstName")
								+ " "
								+ jsonObj.getJSONObject("createdBy").getString(
										"lastName");

						Date date = new Date(Long.valueOf(jsonObj
								.getString(Constants.JSON_TAG_DUE_DATE)));
						SimpleDateFormat sdf = new SimpleDateFormat(
								"dd/MM/yyyy", Locale.ENGLISH);
						String dueDate = sdf.format(date);

						// tmp hashmap for single campaign
						HashMap<String, String> campaign = new HashMap<String, String>();
						campaign.put(Constants.JSON_TAG_ID, id);
						campaign.put(Constants.JSON_TAG_NAME, name);
						campaign.put(Constants.JSON_TAG_CREATED_BY, createdBy);
						campaign.put(Constants.JSON_TAG_DUE_DATE, dueDate);

						campaignList.add(campaign);

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
			ListAdapter adapter = new SimpleAdapter(CampaingsList.this,
					campaignList, R.layout.campaigns_item, new String[] {
							Constants.JSON_TAG_ID, Constants.JSON_TAG_NAME,
							Constants.JSON_TAG_CREATED_BY,
							Constants.JSON_TAG_DUE_DATE }, new int[] { R.id.id,
							R.id.name, R.id.createdBy, R.id.duedate });

			setListAdapter(adapter);
		}

	}

}