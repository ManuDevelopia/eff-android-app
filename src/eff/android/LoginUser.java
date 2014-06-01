package eff.android;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import eff.android.model.Constants;
import eff.android.model.User;
import eff.android.services.ServiceHandler;
import eff.android.utils.ConnectionDetector;

/**
 * @author Manu
 * 
 */
public class LoginUser extends Activity {

	// Main URL, this would be a class with all the params of the configuration
	private static String url = Constants.URL + "/users?email=";

	// Progress dialog indicator
	private ProgressDialog pDialog;

	// User credentials to get logged
	private EditText email = null;
	private EditText password = null;
	private Button login = null;

	// User to log into the platform
	User user = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Creating the connection detector
		new ConnectionDetector(getApplicationContext());

		// Login screen is shown
		setContentView(R.layout.login);
		email = (EditText) findViewById(R.id.userEmail);
		password = (EditText) findViewById(R.id.userPassword);
		login = (Button) findViewById(R.id.loginButton);

		// Hardcore user data
		email.setText("aelorriaga@innopole.net");
		password.setText("Elorriaga");

		// This is needed to execute the HTTP
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

	}

	// This prevents closing the login activity with back button
	@Override
	public void onBackPressed() {
	}

	public void loginUser(View view) {

		login.setEnabled(false);

		// Lets check if the Internet connection is present
		if (ConnectionDetector.isConnectingToInternet() == false) {
			Toast.makeText(getApplicationContext(),
					"Network connection is not availabe", Toast.LENGTH_SHORT)
					.show();
			login.setEnabled(true);
			return;
		}

		// Lets check if the ExtremeFactories server is up
		if (ConnectionDetector.isConnectedToServer(Constants.URL, 10000) == false) {
			Toast.makeText(getApplicationContext(),
					"ExtremeFactories server is Down!!", Toast.LENGTH_LONG)
					.show();
			login.setEnabled(true);
			return;
		}

		// Showing progress dialog
		pDialog = new ProgressDialog(LoginUser.this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);
		pDialog.show();

		// Lets create the user to be logged, without data, just the token
		user = new User(email.getText().toString(), password.getText()
				.toString());

		// Creating service handler class instance
		ServiceHandler sh = new ServiceHandler();

		// Making a request to url and getting response
		String jsonStr = sh.makeServiceCall(url + email.getText().toString(),
				user.getToken(), ServiceHandler.GET);

		// Dismiss the progress dialog
		if (pDialog.isShowing())
			pDialog.dismiss();

		Log.i("User", "Restr" + jsonStr);
		Log.i("User", "Class" + user.toString());

		if (!jsonStr.startsWith("{\"id\":")) {
			Toast.makeText(getApplicationContext(),
					"Unknwon email/password combination, please try again!",
					Toast.LENGTH_LONG).show();
		} else {
			// Lets add the retrieved data of the user
			try {
				JSONObject jsonObj = new JSONObject(jsonStr);
				user.setId(jsonObj.getString(Constants.USER_ID));
				user.setFirstName(jsonObj.getString(Constants.USER_FNAME));
				user.setLastName(jsonObj.getString(Constants.USER_LNAME));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			Intent in = new Intent(this, CampaingsList.class);
			// TODO: putExtra(JSON in String);
			in.putExtra(Constants.USER_TOKEN, user.getToken());
			in.putExtra(Constants.USER_ID, user.getId());
			startActivity(in);
		}

	}
}

/*
 * to implement remember me checkbox, please check the following pages -
 * CHECKBOX: http://www.mkyong.com/android/android-checkbox-example/ -
 * SHAREDPREFS:
 * https://github.com/sgolivernet/curso-android-src/blob/master/android
 * -preferences-1/src/net/sgoliver/android/preferences1/MainActivity.java
 */
