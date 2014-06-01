/**
 * 
 */
package eff.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import eff.android.model.Constants;
import eff.android.model.Idea;
import eff.android.services.ServiceHandler;
import eff.android.utils.JsonUtil;

/**
 * @author Manu
 * 
 */

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

public class IdeaEditorList extends Activity {

	// Title of the idea
	private EditText shortTex;
	private EditText decription;
	private Button submit;

	// User token identification
	private static String token;
	private static String userId;

	// ID and Title of the campaign
	private static String campaignId;
	private static TextView campaingTitle;

	private Idea idea;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idea_editor);

		Intent in = getIntent();
		campaignId = in.getStringExtra(Constants.CAMPAING_ID);
		token = in.getStringExtra(Constants.USER_TOKEN);
		userId = in.getStringExtra(Constants.USER_ID);

		shortTex = (EditText) findViewById(R.id.shortText);
		decription = (EditText) findViewById(R.id.description);

		// This is the title of the Campaign
		campaingTitle = (TextView) findViewById(R.id.campaignTitle);
		campaingTitle.setText(in.getStringExtra(Constants.CAMPAING_TITLE));

	}

	@Override
	public void onBackPressed() {
	}

	public void postIdea(View view) {

		// The idea is created after click on post idea
		idea = new Idea(userId);
		idea.setShortText(shortTex.getText().toString());
		idea.setDescription(decription.getText().toString());

		Log.i("EFF-Json", JsonUtil.toJSON(idea));

		ServiceHandler sh = new ServiceHandler();

		sh.makeServiceCall(Constants.URL + "/campaigns/" + campaignId
				+ "/ideas", token, ServiceHandler.POST, JsonUtil.toJSON(idea));

		backToCampaignList(view);
	}

	private void backToCampaignList(View view) {
		// Returns back to Campaign list
		Intent in = new Intent(getApplicationContext(), CampaignIdeasList.class);
		in.putExtra(Constants.CAMPAING_ID, campaignId);
		in.putExtra(Constants.CAMPAING_TITLE, campaingTitle.getText()
				.toString());
		in.putExtra(Constants.USER_TOKEN, token);
		in.putExtra(Constants.USER_ID, userId);
		startActivity(in);
	}

	
	
}
