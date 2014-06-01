package eff.android.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import eff.android.model.Idea;

public class JsonUtil {

	public static String toJSON(Idea idea) {
		JSONObject jsonObjMain = new JSONObject();

		try {
			jsonObjMain.put("shortText", idea.getShortText());
			jsonObjMain.put("description", idea.getDescription());
			jsonObjMain.put("postedAt", idea.getPostedAt());

			JSONObject jsonObjSub1 = new JSONObject();
			jsonObjSub1.put("id", idea.getPostedById());
			jsonObjMain.put("postedBy", jsonObjSub1);
			
			Log.e("Json-Util", "idea.getPostedById()" + idea.getPostedById() );
			Log.e("Json-Util", "JSON " + jsonObjMain.toString());

			return jsonObjMain.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("JSON-E", e.toString());
		}

		return null;
	}

}
