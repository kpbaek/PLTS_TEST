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
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.sbmkorea.plts.activity.listener.BarcodeTextWatcher;
import com.sbmkorea.plts.activity.listener.EnterDisablingOnKeyListener;
import com.sbmkorea.plts.activity.listener.SpinnerWatcher;
import com.sbmkorea.plts.data.ShipmentHistoryData;
import com.sbmkorea.plts.task.GetNetworkTask;
import com.sbmkorea.plts.view.BottomHitDetectScrollView;
import com.sbmkorea.plts.view.ShipmentHistoryContentView;

public class ShipmentHistoryActivity extends BaseActivity {
	public static final String TAG = "ShipmentHistoryActivity";
	private final String baseQueryString = "/shipment_history/?format=json";
	private final int LIMIT_COUNT = 15;
	
	private EditText searchEditText;
	private Button searchButton;
	
	private LinearLayout recordLayout;
	private BottomHitDetectScrollView scrollView;
	private int viewCount = 0;
	
	private Spinner EnterpriserSpinner;
	private SpinnerWatcher EnterpriserspinnerWatcher;
	
	private String search_enterprise;
	private String searchText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shipment_history);
		// Show the Up button in the action bar.		
		setupActionBar();
		setupSpinnerWatcher();
		initialize();		
		showHistory();
	}
	
	private void setupSpinnerWatcher() {
		EnterpriserSpinner = (Spinner) findViewById(R.id.ShipmentHistory_Spinner_Enterpriser);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.ShipmentHistoryActivity_Spinner_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		EnterpriserSpinner.setAdapter(adapter);

		EnterpriserspinnerWatcher = new SpinnerWatcher();
		EnterpriserSpinner.setOnItemSelectedListener(EnterpriserspinnerWatcher);
	}	
	
	private void initialize() {
		searchEditText = (EditText) findViewById(R.id.ShipmentHistoryActivity_Search_EditText);
		searchButton = (Button) findViewById(R.id.ShipmentHistoryActivity_Search_Button);
		recordLayout = (LinearLayout) findViewById(R.id.ShipmentHistoryActivity_History_LinearLayout);
		scrollView = (BottomHitDetectScrollView) findViewById(R.id.ShipmentHistoryActivity_ScrollView);
		EnterpriserSpinner = (Spinner) findViewById(R.id.ShipmentHistory_Spinner_Enterpriser);
		
		Intent intent = getIntent();		
		
		searchText = intent.getStringExtra("searchText");
		search_enterprise = intent.getStringExtra("search_enterprise");
		
		if (searchText == null) {
			searchText = "";
		}
		
		if (search_enterprise == null) {
			search_enterprise = "";
		}
		searchEditText.setOnKeyListener(new EnterDisablingOnKeyListener());
		searchEditText.addTextChangedListener(new ShipmentBarcodeTextWatcher(searchEditText, searchButton));
		BottomHitDetectScrollView scrollView = (BottomHitDetectScrollView) findViewById(R.id.ShipmentHistoryActivity_ScrollView);
		scrollView.setCallback(new BottomHitDetectScrollView.Callback() {
			
			@Override
			public void call() {
				// TODO Auto-generated method stub
				showHistory();
			}
		});
	}

	public void showHistory() {
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
		
		Log.e("Url",url+"");
		GetNetworkTask getNetworkTask = new GetNetworkTask(this,onPostExecuteFunction);
		getNetworkTask.execute(url);
	}

	public String getOptionValuesAsQuery() {
		// Check which CHECKBOX has been checked.
				
		String queryString =  baseQueryString;
		
		// TODO: Handling multiple filtering checkBox Values and EditTextValue
		
		if (search_enterprise.equals("")) {
		}
		else			
			queryString += "&enterprise=" + search_enterprise;

			queryString = queryString.substring(0,queryString.length());
			
		if (!searchText.equals("")) {
			queryString = queryString + "&shipment=" + searchText;
		}
		return queryString;
	}
	
	public void parseAndSetHistory(String result) {
		// Step 4,5 Parse Date, Set HistoryRow Layout and then set Values onto Layouts.
		try {
			ArrayList<ShipmentHistoryData> historyDataList = parseHistoryData(result);
			addHistoryContentView(historyDataList);
			if (historyDataList.size() != 0) {
				scrollView.setListeningToScrollChange(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("viewCount", viewCount + "");
		
		return;
	}
	
	public ArrayList<ShipmentHistoryData> parseHistoryData(String result) throws JSONException, ParseException {
		ArrayList<ShipmentHistoryData> historyDataList = new ArrayList<ShipmentHistoryData>();
		JSONObject jsonObj = new JSONObject(result);
		JSONArray objects = jsonObj.getJSONArray("objects");
		for (int i = 0; i < objects.length(); i++) {
			// Get reach each objects.
			JSONObject history = objects.getJSONObject(i);
	
			String enterpriser = history.getString("enterprise");
			JSONObject prdBarcodeObj = history.getJSONObject("product");
			String prdBarcode = prdBarcodeObj.getString("barcode");
			String shipment_Barcode = history.getString("shipment");							
			String regdate = history.getString("reg_date");			
			JSONObject workerObj = history.getJSONObject("worker");
			String worker = workerObj.getString("name");				
			String etc = history.getString("etc");
			
			ShipmentHistoryData historyData = new ShipmentHistoryData(enterpriser, prdBarcode, shipment_Barcode, regdate, worker, etc);
			historyDataList.add(historyData);
		}
		return historyDataList;
	}
	
	public void addHistoryContentView(ArrayList<ShipmentHistoryData> historyDataList) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		for (int i = 0; i < historyDataList.size(); i++) {
			ShipmentHistoryContentView hcv = new ShipmentHistoryContentView(this);
			hcv.setRowValues(historyDataList.get(i));
			recordLayout.addView(hcv, params);
			viewCount++;
			
			hcv.shipmentBarcodeTextView.setOnClickListener(new ShipmentBarcodeOnClickListener(this));
			hcv.productBarcodeTextView.setOnClickListener(new ProductBarcodeOnClickListener(this));
		}
	}
	
	public void startNewShipmentSearch(View view) {
		Intent intent = new Intent(this, ShipmentHistoryActivity.class);
		search_enterprise = EnterpriserSpinner.getSelectedItem().toString();
		Log.i("enterprise=",search_enterprise);
		intent.putExtra("search_enterprise", search_enterprise);
		intent.putExtra("searchText", searchText);		
		startActivity(intent);
	}
		
	public void startNewProductSearch(View view) {
		Intent intent = new Intent(this, ProductHistoryActivity.class);
		intent.putExtra("searchText", searchText);
		startActivity(intent);
	}
	
	class ShipmentBarcodeOnClickListener implements OnClickListener {
		ShipmentHistoryActivity activity;
		
		public ShipmentBarcodeOnClickListener(ShipmentHistoryActivity activity) {
			this.activity = activity;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			searchText = ((TextView) v).getText().toString(); 
			search_enterprise = EnterpriserSpinner.getSelectedItem().toString();
			activity.startNewShipmentSearch(v);
		}
	}

	class ProductBarcodeOnClickListener implements OnClickListener {
		ShipmentHistoryActivity activity;
		
		public ProductBarcodeOnClickListener(ShipmentHistoryActivity activity) {
			this.activity = activity;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			searchText = ((TextView) v).getText().toString(); 
			search_enterprise = EnterpriserSpinner.getSelectedItem().toString();
			activity.startNewProductSearch(v);
		}
	}
	
	class ShipmentBarcodeTextWatcher extends BarcodeTextWatcher {

		private Button searchButton;
		
		public ShipmentBarcodeTextWatcher(EditText target, Button searchButton) {
			this.searchButton = searchButton;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String barcodeAsString = s.toString();
			if(barcodeAsString.length()==SHIPMENT_LENGTH) {
				searchText = barcodeAsString;
				search_enterprise = EnterpriserSpinner.getSelectedItem().toString();				
				searchButton.performClick();
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
