package com.sbmkorea.plts.activity;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sbmkorea.plts.activity.listener.ProductionOneToManyTextWatcher;
import com.sbmkorea.plts.task.PostNetworkTask;
import com.sbmkorea.plts.view.DynamicTableView;

public class ProductionOneToManyActivity extends BaseActivity {
	public static final int BARCODE_LENGTH = 13;
	private DynamicTableView table;
	private ProductionOneToManyTextWatcher watcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_production_one_to_many);
		// Show the Up button in the action bar.
		setupActionBar();
		
		initializeDynamicTable();
		setupBarcodeWatcher();
	}public void initializeDynamicTable() {
		table = (DynamicTableView) findViewById(R.id.ProductionOneToManyActivity_DynamicTableView);
		table.initialize(setFieldNameList());
		table.addRow();
		table.addEntry();
	}
	
	private void setupBarcodeWatcher() {
		watcher = new ProductionOneToManyTextWatcher(this, table);
		watcher.watch(table.getCurrentEntry());
	}
	
	private ArrayList<String> setFieldNameList() {
		ArrayList<String> fieldNameList = new ArrayList<String>();
		Resources resources = this.getResources();
		fieldNameList.add(resources.getString(R.string.DT_column_id));
		fieldNameList.add(resources.getString(R.string.DT_column_product));
		String part = resources.getString(R.string.DT_column_part);
		for(int i = 1; i < 100; i++) {
			fieldNameList.add(part + i);
		}
		return fieldNameList;
	}
	
	public void uploadBarcodeInfo(int rowId) {
		PostNetworkTask.Callback onPostExecuteFunction = new PostNetworkTask.Callback() {
			@Override
			public void call(int id, String result) {
				// TODO Auto-generated method stub
				afterUpload(id, result);
			}
		};
		PostNetworkTask postDataTask = new PostNetworkTask(this, rowId, onPostExecuteFunction);
		String data = convertDataToJson(rowId);
		postDataTask.execute(PostNetworkTask.ONE_TO_MANY_TYPE, data);
	}
	
	private void afterUpload(int rowId, String result) {
		ImageView iv;
		if (result.equals(PostNetworkTask.SUCCESS_STRING)) {
			iv = table.setStatus(rowId, DynamicTableView.Status.SUCCESS);
			iv.setOnClickListener(null);
		}
		else {
			iv = table.setStatus(rowId, DynamicTableView.Status.ERROR);
			iv.setOnClickListener(new ReUploadOnClickListener(rowId));
			// maybe call garbage collection
		}
	}
	
	class ReUploadOnClickListener implements OnClickListener {
		private int rowId;
		
		public ReUploadOnClickListener(int rowId) {
			this.rowId = rowId;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			table.setStatus(rowId, DynamicTableView.Status.PROCESSING);
			uploadBarcodeInfo(rowId);
		}
	}

	private String convertDataToJson(int rowId) {
		LinearLayout recordsLayout = table.getRecordsLayout();
		LinearLayout row = (LinearLayout) recordsLayout.getChildAt(rowId - 1);
		EditText e;
		
		int childCount = row.getChildCount();
		e = (EditText) row.getChildAt(1);
		String productId = e.getText().toString();
		
		ArrayList<String> partIdList = new ArrayList<String>();
		for (int i = 2; i < childCount; i++) {
			e = (EditText) row.getChildAt(i);
			partIdList.add(e.getText().toString());
		}
		
		Gson gson = new Gson();
		JsonElement partIdJE = gson.toJsonTree(partIdList);
		JsonObject data = new JsonObject();
		data.addProperty("manufacturing_type_name", PostNetworkTask.MANUFACTURING_TYPE_PRODUCTION);
		data.addProperty("product_barcode", productId);
		data.add("part_barcode", partIdJE);
		data.addProperty("worker_barcode", USER_BARCODE);
		
		data.addProperty("userId", USER_ID);
		data.addProperty("productId", productId);
		data.add("partId", partIdJE);
		
		return gson.toJson(data);
	}	
/*	
	public void InputProduction(View view){
		String message;
		if (table.hasNoErrors()) {
			//TODO code re-factoring. there is same codeline in convertDataToJson fucntion.
			LinearLayout recordsLayout = table.getRecordsLayout();
			LinearLayout row = (LinearLayout) recordsLayout.getChildAt(0);
			EditText e = (EditText) row.getChildAt(1);
			String productId = e.getText().toString();		    
			if (productId.equals("")){
				message = getResources().getString(R.string.ProductionOneToOneActivity_toastMsg_NullBarcode);
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
				return;
			}
			else if(productId.length() == (BARCODE_LENGTH)){
												
			}
		} else {
			message = getResources().getString(R.string.ProductionOneToManyActivity_toastMsg_hasErrors);
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		}
	}
*/	
	public void finishProduction(View view) {
		String message;
		if (table.hasNoErrors()) {
			//TODO code re-factoring. there is same codeline in convertDataToJson fucntion.
/*			있을 필요가없다 왜냐하면 그냥 종료하면되기때문에
			LinearLayout recordsLayout = table.getRecordsLayout();
			LinearLayout row = (LinearLayout) recordsLayout.getChildAt(0);
			EditText e = (EditText) row.getChildAt(1);
			String productId = e.getText().toString();		    
			if (productId.equals("")){
				message = getResources().getString(R.string.ProductionOneToOneActivity_toastMsg_NullBarcode);
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
				return;
			}
			else*/{
				message = getResources().getString(R.string.ProductionOneToManyActivity_toastMsg_finish);
				Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
				this.finish();
			}
		} else {
			message = getResources().getString(R.string.ProductionOneToManyActivity_toastMsg_hasErrors);
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
