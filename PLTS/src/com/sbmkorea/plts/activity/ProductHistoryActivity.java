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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbmkorea.plts.activity.listener.BarcodeTextWatcher;
import com.sbmkorea.plts.activity.listener.EnterDisablingOnKeyListener;
import com.sbmkorea.plts.data.ProductHistoryData;
import com.sbmkorea.plts.task.GetNetworkTask;
import com.sbmkorea.plts.view.BottomHitDetectScrollView;
import com.sbmkorea.plts.view.ProductHistoryContentView;

public class ProductHistoryActivity extends BaseActivity {
	public static final String TAG = "ProductHistoryActivity";
	private final String baseQueryString = "/manufacturing_history/?format=json&manufacturing_type_name=";
	private final int LIMIT_COUNT = 15;
	
	private CheckBox productionCheckBox;
	private CheckBox repairCheckBox;
	private CheckBox specChangeCheckBox;
	private EditText searchEditText;
	private Button searchButton;
	
	private LinearLayout recordLayout;
	private BottomHitDetectScrollView scrollView;
	private int viewCount = 0;
		
	private String searchText;
	private String part_searchText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_history);
		// Show the Up button in the action bar.
		setupActionBar();
		initialize();
		showHistory();
	}
	
	private void initialize() {
		productionCheckBox = (CheckBox)findViewById(R.id.ProductHistoryActivity_Production_CheckBox);
		repairCheckBox = (CheckBox)findViewById(R.id.ProductHistoryActivity_Repair_CheckBox);
		specChangeCheckBox = (CheckBox)findViewById(R.id.ProductHistoryActivity_SpecChange_CheckBox);
		searchEditText = (EditText) findViewById(R.id.ProductHistoryActivity_Search_EditText);
		searchButton = (Button) findViewById(R.id.ProductHistoryActivity_Search_Button);
		recordLayout = (LinearLayout) findViewById(R.id.ProductHistoryActivity_History_LinearLayout);
		scrollView = (BottomHitDetectScrollView) findViewById(R.id.ProductHistoryActivity_ScrollView);		
		
		Intent intent = getIntent();
		productionCheckBox.setChecked(intent.getBooleanExtra("isProductionChecked", true));
		repairCheckBox.setChecked(intent.getBooleanExtra("isRepairChecked", true));
		specChangeCheckBox.setChecked(intent.getBooleanExtra("isSpecChangeChecked", true));
		
		searchText = intent.getStringExtra("searchText");
		part_searchText = intent.getStringExtra("part_searchText");
		
		if (searchText == null) {
			searchText = "";
		}
		if (part_searchText == null) {
			part_searchText = "";
		}
		searchEditText.setOnKeyListener(new EnterDisablingOnKeyListener());
		searchEditText.addTextChangedListener(new ProductBarcodeTextWatcher(searchEditText, searchButton));
		BottomHitDetectScrollView scrollView = (BottomHitDetectScrollView) findViewById(R.id.ProductHistoryActivity_ScrollView);
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
		Boolean isProduction = productionCheckBox.isChecked();
		Boolean isRepair = repairCheckBox.isChecked();
		Boolean isSpecChange = specChangeCheckBox.isChecked(); 
				
		String queryString =  baseQueryString;
		// TODO: Handling multiple filtering checkBox Values and EditTextValue
		if (isProduction) {
			queryString += "production,";
		}if (isRepair) {
			queryString += "repair,";
		}if (isSpecChange) {
			queryString += "spec_change,";
		}
		if (isProduction || isRepair || isSpecChange) {
			queryString = queryString.substring(0,queryString.length()-1);
		}if (!searchText.equals("")) {
			queryString = queryString + "&product__barcode=" + searchText;
		}if (!part_searchText.equals("")) {
			queryString = queryString + "&part__barcode=" + part_searchText;
		}
		return queryString;
	}
	
	public void parseAndSetHistory(String result) {
		// Step 4,5 Parse Date, Set HistoryRow Layout and then set Values onto Layouts.
		try {
			ArrayList<ProductHistoryData> historyDataList = parseHistoryData(result);
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
	
	public ArrayList<ProductHistoryData> parseHistoryData(String result) throws JSONException, ParseException {
		ArrayList<ProductHistoryData> historyDataList = new ArrayList<ProductHistoryData>();
		JSONObject jsonObj = new JSONObject(result);
		JSONArray objects = jsonObj.getJSONArray("objects");
		for (int i = 0; i < objects.length(); i++) {
			// Get reach each objects.
			JSONObject history = objects.getJSONObject(i);
			
			JSONObject mfgTypeObj = history.getJSONObject("manufacturing_type");
			String mfgType = mfgTypeObj.getString("name");
			JSONObject prdBarcodeObj = history.getJSONObject("product");
			String prdBarcode = prdBarcodeObj.getString("barcode");
			JSONObject prtBarcodeObj = history.getJSONObject("part");
			String prtBarcode = prtBarcodeObj.getString("barcode");
			JSONObject workerObj = history.getJSONObject("worker");
			String worker = workerObj.getString("name");
			
			// Handle Null values
			JSONObject errTypeObj = history.optJSONObject("repair_error_type");
			String errType;
			if (errTypeObj == null) {
				errType = "";			
			} else{
				errType = errTypeObj.optString("name");
			}
			String rmk = history.getString("remark");
			String regdate = history.getString("reg_date");
			
			ProductHistoryData historyData = new ProductHistoryData(mfgType, prdBarcode, prtBarcode, regdate, worker, errType, rmk);
			historyDataList.add(historyData);
		}
		return historyDataList;
	}
	
	public void addHistoryContentView(ArrayList<ProductHistoryData> historyDataList) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		for (int i = 0; i < historyDataList.size(); i++) {
			ProductHistoryContentView hcv = new ProductHistoryContentView(this);
			hcv.setRowValues(historyDataList.get(i));
			recordLayout.addView(hcv, params);
			viewCount++;
			
			hcv.productBarcodeTextView.setOnClickListener(new ProductBarcodeOnClickListener(this));
			hcv.partBarcodeTextView.setOnClickListener(new PartBarcodeOnClickListener(this));
		}
	}
	
	public void startNewProductSearch(View view) {
		Intent intent = new Intent(this, ProductHistoryActivity.class);
		
		intent.putExtra("isProductionChecked", productionCheckBox.isChecked());
		intent.putExtra("isRepairChecked", repairCheckBox.isChecked());
		intent.putExtra("isSpecChangeChecked", specChangeCheckBox.isChecked());
		intent.putExtra("searchText", searchText);
		intent.putExtra("part_searchText", part_searchText);
		startActivity(intent);
		finish();
	}
	
	public void startNewPartSearch(View view) {
		Intent intent = new Intent(this, PartHistoryActivity.class);
		intent.putExtra("searchText", searchText);
		startActivity(intent);
		finish();
	}
	
	class ProductBarcodeOnClickListener implements OnClickListener {
		ProductHistoryActivity activity;
		
		public ProductBarcodeOnClickListener(ProductHistoryActivity activity) {
			this.activity = activity;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			searchText = ((TextView) v).getText().toString(); 
			activity.startNewProductSearch(v);
		}
	}
	
	class PartBarcodeOnClickListener implements OnClickListener {
		ProductHistoryActivity activity;
		
		public PartBarcodeOnClickListener(ProductHistoryActivity activity) {
			this.activity = activity;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			searchText = ((TextView) v).getText().toString(); 
			activity.startNewPartSearch(v);
		}
	}
	
	class ProductBarcodeTextWatcher extends BarcodeTextWatcher {

		private EditText target;
		private Button searchButton;
		
		public ProductBarcodeTextWatcher(EditText target, Button searchButton) {
			this.target = target;
			this.searchButton = searchButton;
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			String barcodeAsString = s.toString();
			if(barcodeAsString.length()==BARCODE_LENGTH) {
				if (barcodeAsString.startsWith(PRODUCT_ID_PREFIX)) {
					part_searchText = null;
					searchText = barcodeAsString;
					searchButton.performClick();
				}else if (barcodeAsString.startsWith(PART_ID_PREFIX)) {
					searchText = null;
					part_searchText = barcodeAsString;
					searchButton.performClick();
				}
				else {
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
