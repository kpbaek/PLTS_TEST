package com.sbmkorea.plts.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sbmkorea.plts.task.GetNetworkTask;

public class LoginActivity extends Activity {
	private String queryString = "/worker/?format=json&barcode=";
	private String userBarcode = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setupActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void login(View view) {
		EditText e = (EditText) findViewById(R.id.login_EditText);
		userBarcode = e.getText().toString();
		String url = GetNetworkTask.BASE_API_URL + queryString + userBarcode;
		Log.e("login",url);
		GetNetworkTask.Callback onPostExecuteFunction = new GetNetworkTask.Callback(){
			@Override
			public void call(int id, String result) {
				onPostExecute(result);
			}
		};
		
		GetNetworkTask getNetworkTask = new GetNetworkTask(this, onPostExecuteFunction);
		getNetworkTask.execute(url);
	}
	
	public void onPostExecute(String result) {
		try {
			String userName = getUserName(result);
			setUserView(userName);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}
	
	public void setUserView (String userName) {
		BaseActivity.setUser(userName, userBarcode);
	}
	
	public String getUserName(String result) throws JSONException {
		JSONObject jsonObj = new JSONObject(result);
		JSONArray objects = jsonObj.getJSONArray("objects");
		// When input value is not found in the DB.
		if (objects.length()==0){
			String msg = getResources().getString(R.string.LoginActivity_toastMsg_NullBarcode);
			Toast.makeText(LoginActivity.this, msg,Toast.LENGTH_LONG).show();
			userBarcode = BaseActivity.DEFAULT_USER_BARCODE;
			return BaseActivity.DEFAULT_USER_ID;
		}
		JSONObject loginData = objects.getJSONObject(0);
		String workerName = loginData.getString("name");
		return workerName;
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
