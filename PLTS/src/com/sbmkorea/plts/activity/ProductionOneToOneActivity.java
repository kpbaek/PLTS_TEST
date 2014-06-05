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
import com.google.gson.JsonObject;
import com.sbmkorea.plts.activity.listener.ProductionOneToOneTextWatcher;
import com.sbmkorea.plts.task.PostNetworkTask;
import com.sbmkorea.plts.view.DynamicTableView;

public class ProductionOneToOneActivity extends BaseActivity {
	public static final String TAG = "ProductionOneToOneActivity";
	protected DynamicTableView table;
	private ProductionOneToOneTextWatcher watcher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_production_one_to_one);
		// Show the Up button in the action bar.
		setupActionBar();
		
		initializeDynamicTable();
		setupBarcodeWatcher();
	}
	
	public void initializeDynamicTable() {
		table = (DynamicTableView) findViewById(R.id.ProductionOneToOneActivity_DynamicTableView);
		table.initialize(setFieldNameList());
		table.addRow();
		table.addEntry();
	}
	
	private void setupBarcodeWatcher() {
		watcher = new ProductionOneToOneTextWatcher(this, table);
		watcher.watch(table.getCurrentEntry());
	}
	
	private ArrayList<String> setFieldNameList() {
		ArrayList<String> fieldNameList = new ArrayList<String>();
		Resources resources = this.getResources();
		fieldNameList.add(resources.getString(R.string.DT_column_id));
		fieldNameList.add(resources.getString(R.string.DT_column_product));
		fieldNameList.add(resources.getString(R.string.DT_column_part));
		
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
		PostNetworkTask postNetworkTask = new PostNetworkTask(this, rowId, onPostExecuteFunction);
		String data = convertDataToJson(rowId);
		postNetworkTask.execute(PostNetworkTask.DEFAULT_TYPE, data);
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
			message = getResources().getString(R.string.ProductionActivities_toastMsg_hasErrors);
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

	protected String convertDataToJson(int rowId) {
		LinearLayout recordsLayout = table.getRecordsLayout();
		LinearLayout row = (LinearLayout) recordsLayout.getChildAt(rowId - 1);
		EditText e;
		
		e = (EditText) row.getChildAt(1);
		String productId = e.getText().toString();
		
		e = (EditText) row.getChildAt(2);
		String partId = e.getText().toString();
		
		Gson gson = new Gson();
		JsonObject data = new JsonObject();
		data.addProperty("manufacturing_type_name", PostNetworkTask.MANUFACTURING_TYPE_PRODUCTION);
		data.addProperty("product_barcode", productId);
		data.addProperty("part_barcode", partId);
		data.addProperty("worker_barcode", USER_BARCODE);
		
		// add null repair_error_type and null remark
		// to use same code in server side.
		data.addProperty("repair_error_type", "");
		data.addProperty("remark", "");
		
		return gson.toJson(data);
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
