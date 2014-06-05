package com.sbmkorea.plts.activity;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sbmkorea.plts.activity.listener.BarcodeTextWatcher;
import com.sbmkorea.plts.activity.listener.EnterDisablingOnKeyListener;
import com.sbmkorea.plts.data.PartHistoryData;
import com.sbmkorea.plts.task.GetNetworkTask;
import com.sbmkorea.plts.view.BottomHitDetectScrollView;
import com.sbmkorea.plts.view.PartHistoryContentView;

public class PartHistoryActivity extends BaseActivity {
	public static final String TAG = "PartHistoryActivity";
	private final String baseQueryString = "/part_history/?format=json";
	private final int LIMIT_COUNT = 10;

	private EditText searchEditText;
	private Button searchButton;
	
	private LinearLayout recordLayout;
	private int viewCount = 0;
	
	private String searchText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part_history);
		// Show the Up button in the action bar.
		setupActionBar();
		initialize();
		showHistory(null);
	}
	
	private void initialize() {
		searchEditText = (EditText) findViewById(R.id.PartHistoryActivity_Search_EditText);
		searchButton = (Button) findViewById(R.id.PartHistoryActivity_Search_Button);
		recordLayout = (LinearLayout) findViewById(R.id.PartHistoryActivity_History_LinearLayout);
		
		Intent intent = getIntent();
		searchText = intent.getStringExtra("searchText");
		if (searchText == null) {
			searchText = "";
		}
		searchEditText.setOnKeyListener(new EnterDisablingOnKeyListener());
		searchEditText.addTextChangedListener(new PartBarcodeTextWatcher(searchEditText, searchButton));
		Log.e("searchText", "searchText = " + searchText);
		
		BottomHitDetectScrollView scrollView = (BottomHitDetectScrollView) findViewById(R.id.PartHistoryActivity_ScrollView);
		scrollView.setCallback(new BottomHitDetectScrollView.Callback() {
			
			@Override
			public void call() {
				// TODO Auto-generated method stub
				showHistory(null);
			}
		});
	}

	public void showHistory(View view) {
		// To synchronize threads, implement AfterUpload ( step 4,5,6 treated as call-back method )  
		GetNetworkTask.Callback onPostExecuteFunction = new GetNetworkTask.Callback() {

			@Override
			public void call(int id, String result) {
				// TODO Auto-generated method stub
				parseAndSetHistory(result);
			}
		};
		
		// Step 2,3
		String url = GetNetworkTask.BASE_API_URL + getOptionValuesAsQuery();
		url += "&offset=" + viewCount;
		url += "&limit=" + LIMIT_COUNT;
		Log.e("11",url);
		GetNetworkTask getNetworkTask = new GetNetworkTask(this,onPostExecuteFunction);
		getNetworkTask.execute(url);
	}

	public String getOptionValuesAsQuery() { 
		// Check which CHECKBOX has been checked.
				
		String queryString =  baseQueryString;
		if (!searchText.equals("")) {
			Log.e("searchText-getOptionValue",searchText);
			queryString = queryString + "&part__barcode=" + searchText;
		}
		return queryString;
	}
	
	public void parseAndSetHistory(String result) {
		//Log.e("result", result);
		
		// Step 4,5 Parse Date, Set HistoryRow Layout and then set Values onto Layouts.
		try {
			ArrayList<PartHistoryData> historyDataList = parseHistoryData(result);
			addHistoryContentView(historyDataList);
			searchText = "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("viewCount", viewCount + "");
		
		return;
	}
	
	public void startNewPartSearch(View view) {
		Intent intent = new Intent(this, PartHistoryActivity.class);
		
		intent.putExtra("searchText", searchText);
		startActivity(intent);
	}
	
	public ArrayList<PartHistoryData> parseHistoryData(String result) throws JSONException, ParseException {
		ArrayList<PartHistoryData> historyDataList = new ArrayList<PartHistoryData>();
		JSONObject jsonObj = new JSONObject(result);
		JSONArray objects = jsonObj.getJSONArray("objects");
		for (int i = 0; i < objects.length(); i++) {
			// Get reach each objects.
			JSONObject history = objects.getJSONObject(i);
			
			JSONObject prtBarcodeObj = history.getJSONObject("part");
			String prtBarcode = prtBarcodeObj.getString("barcode");
			JSONObject workerObj = history.getJSONObject("worker");
			String worker = workerObj.getString("name");
			
			// Handle Null values
			String rmk = history.getString("remark");
			String regdate = history.getString("reg_date");
			
			PartHistoryData historyData = new PartHistoryData(prtBarcode, regdate, worker, rmk);
			historyDataList.add(historyData);
		}
		return historyDataList;
	}
	
	public void addHistoryContentView(ArrayList<PartHistoryData> historyDataList) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		for (int i = 0; i < historyDataList.size(); i++) {
			PartHistoryContentView hcv = new PartHistoryContentView(this);
			hcv.setRowValues(historyDataList.get(i));
			recordLayout.addView(hcv, params);
			viewCount++;
		}
	}
	
	class PartBarcodeTextWatcher extends BarcodeTextWatcher {

		private EditText target;
		private Button searchButton;
		
		public PartBarcodeTextWatcher(EditText target, Button searchButton) {
			this.target = target;
			this.searchButton = searchButton;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String barcodeAsString = s.toString();
			if(barcodeAsString.length()==BARCODE_LENGTH) {
				if (barcodeAsString.startsWith(PART_ID_PREFIX)) {
					searchText = barcodeAsString;
					searchButton.performClick();
				} else {
					target.setText("");
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
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
